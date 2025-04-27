package com.clip.batch.notification;

import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationType;
import com.clip.notification.service.NotificationService;
import com.clip.user.entity.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Configuration
public class RandomMatchingProgressNotificationBatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final ApplicationEventPublisher sendFCMEventPublisher;
    private final NotificationService notificationService;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;

    @Scheduled(cron = "0 0 19 * * MON") // 랜덤 매칭 당일(매주 월요일 오후 7시)
    @SchedulerLock(name = "randomMatchingInfo_matchingStart", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runMatchingStartJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_STARTED")
                .addLocalDateTime("targetDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .toJobParameters();
        jobLauncher.run(sendRandomMatchingProgressFcmJob(), params);
    }

    @Scheduled(cron = "0 0 23 * * MON") // 랜덤 매칭 종료일(매주 월요일 오후 11시)
    @SchedulerLock(name = "randomMatchingInfo_matchingEnd", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runMatchingEndJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_ENDED")
                .addLocalDateTime("targetDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .toJobParameters();
        jobLauncher.run(sendRandomMatchingProgressFcmJob(), params);
    }

    @Bean
    public Job sendRandomMatchingProgressFcmJob() {
        return new JobBuilder("sendRandomMatchingProgressFcmJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(sendFcmRandomProgressStep())
                .build();
    }

    @Bean
    public Step sendFcmRandomProgressStep() {
        return new StepBuilder("sendFcmRandomProgressStep", jobRepository)
                .<UserRandomMatching, UserRandomMatching>chunk( CHUNK_SIZE, transactionManager)
                .reader(userRandomReader(null))
                .writer(fcmRandomProgressWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<UserRandomMatching> userRandomReader(
            @Value("#{jobParameters['targetDateTime']}") LocalDate targetDateTime) {
        return new JpaPagingItemReaderBuilder<UserRandomMatching>()
                .name("userRandomReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        select um from UserRandomMatching um
                        join fetch um.randomMatching rm
                        join fetch um.user u
                        where rm.meetingTime = :targetDateTime
                        and u.fcmToken is not null
                        and u.isAllowNotify = true
                        """)
                .parameterValues(Collections.singletonMap("targetDateTime", targetDateTime))
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<UserRandomMatching> fcmRandomProgressWriter(
            @Value("#{jobParameters['notificationType']}") String notificationType
    ) {
        return items -> {
            if (items.isEmpty()) return;

            // RandomMatching ID 기준으로 그룹화
            Map<Long, List<UserRandomMatching>> groupedByMatchingId = new HashMap<>();
            for (UserRandomMatching item : items) {
                Long matchingId = item.getRandomMatching().getId();
                if (!groupedByMatchingId.containsKey(matchingId)) {
                    groupedByMatchingId.put(matchingId, new ArrayList<>());
                }
                groupedByMatchingId.get(matchingId).add(item);
            }

            // 모든 알림과 FCM 데이터를 저장할 리스트
            List<Notification> allNotifications = new ArrayList<>();
            List<FcmNotificationEvent.UserFcmData> allFcmDataList = new ArrayList<>();

            // 각 그룹별로 처리
            for (Map.Entry<Long, List<UserRandomMatching>> entry : groupedByMatchingId.entrySet()) {
                Long matchingId = entry.getKey();
                List<UserRandomMatching> matchingUsers = entry.getValue();

                // 알림 객체와 FCM 데이터 준비
                // 각 그룹별 알림 객체와 FCM 데이터 준비
                List<Notification> groupNotifications = new ArrayList<>();
                List<FcmNotificationEvent.UserFcmData> groupFcmDataList = new ArrayList<>();

                // 각 사용자별 알림 생성
                String randomNickname = null;
                if ("MATCHING_STARTED".equals(notificationType)) {
                    // 랜덤으로 한 명의 사용자 선택하여 닉네임 추출
                    int randomIndex = (int) (Math.random() * matchingUsers.size());
                    randomNickname = matchingUsers.get(randomIndex).getUser().getNickname();
                }

                for (UserRandomMatching item : matchingUsers) {
                    User user = item.getUser();
                    String token = user.getFirebaseToken();
                    String nickname = "MATCHING_STARTED".equals(notificationType)
                            ? randomNickname  // MATCHING_STARTED일 경우 랜덤 선택된 닉네임 사용
                            : user.getNickname();  // 그 외의 경우 각 사용자의 닉네임 사용
                    String deviceType = user.getDeviceType().name();

                    // 메시지 생성
                    MessageParams.NicknameParams messageParams = new MessageParams.NicknameParams(nickname);
                    MessageTemplateType templateType = MessageTemplateType.valueOf(notificationType);
                    String message = templateType.generateMessage(messageParams);

                    // 알림 객체 생성
                    groupNotifications.add(new Notification(
                            NotificationType.MEETING,
                            false,
                            message,
                            user
                    ));

                    groupFcmDataList.add(new FcmNotificationEvent.UserFcmData(
                            matchingId,
                            deviceType,
                            token,
                            messageParams
                    ));
                }

                // 그룹별 데이터를 전체 리스트에 추가
                allNotifications.addAll(groupNotifications);
                allFcmDataList.addAll(groupFcmDataList);
            }
            // 모든 알림 일괄 저장
            List<Notification> savedNotifications = notificationService.saveNotifications(allNotifications);

            // 알림 ID와 FCM 데이터 매핑
            Map<Long, FcmNotificationEvent.UserFcmData> userDataMap = new HashMap<>();
            IntStream.range(0, savedNotifications.size())
                    .forEach(i -> userDataMap.put(savedNotifications.get(i).getId(), allFcmDataList.get(i)));

            // FCM 이벤트 발행 (한 번만)x
            if (!userDataMap.isEmpty()) {
                sendFCMEventPublisher.publishEvent(new FcmNotificationEvent.GeneralFcmBatchSendEvent(
                        this,
                        MessageTemplateType.valueOf(notificationType),
                        "RANDOM",
                        userDataMap
                ));
            }
        };
    }
}
