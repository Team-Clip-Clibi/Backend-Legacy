package com.clip.batch.notification;

import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.matching.entity.UserOneThingMatching;
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
public class OneThingMatchingProgressNotificationBatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final ApplicationEventPublisher sendFCMEventPublisher;
    private final NotificationService notificationService;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;

    @Scheduled(cron = "0 0 12,18,19 * * SAT,SUN") // 원띵 매칭 당일(매주 토요일, 일요일 오후 12시, 6시, 7시)
    @Scheduled(cron = "0 0 19 * * FRI") // 원띵 매칭 당일(매주 월요일 오후 7시)
    @SchedulerLock(name = "oneThingMatchingInfo_matchingStart", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runMatchingStartJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_STARTED")
                .addLocalDateTime("targetDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingProgressFcmJob(), params);
    }

    @Scheduled(cron = "0 0 16,22,23 * * SAT,SUN") // 원띵 매칭 종료알(매주 토요일, 일요일 오후 4시, 10시, 11시)
    @Scheduled(cron = "0 0 23 * * FRI") // 원띵 매칭 종료일(매주 금요일 오후 11시)
    @SchedulerLock(name = "oneThingMatchingInfo_matchingEnd", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runMatchingEndJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_ENDED")
                .addLocalDateTime("targetDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingProgressFcmJob(), params);
    }


    @Bean
    public Job sendOneThingMatchingProgressFcmJob() {
        return new JobBuilder("sendOneThingMatchingProgressFcmJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(sendFcmOneThingProgressStep())
                .build();
    }

    @Bean
    public Step sendFcmOneThingProgressStep() {
        return new StepBuilder("sendFcmOneThingProgressStep", jobRepository)
                .<UserOneThingMatching, UserOneThingMatching>chunk( CHUNK_SIZE, transactionManager)
                .reader(userOneThingReader(null))
                .writer(fcmOneThingProgressWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<UserOneThingMatching> userOneThingReader(
            @Value("#{jobParameters['targetDate']}") LocalDate targetDate) {
        return new JpaPagingItemReaderBuilder<UserOneThingMatching>()
                .name("userOneThingReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        select um from UserOneThingMatching um
                        join fetch um.oneThingMatching om
                        join fetch um.user u
                        where om.meetingTime = :targetDate
                        and u.fcmToken is not null
                        and u.isAllowNotify = true
                        """)
                .parameterValues(Collections.singletonMap("targetDate", targetDate))
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<UserOneThingMatching> fcmOneThingProgressWriter(
            @Value("#{jobParameters['notificationType']}") String notificationType
    ) {
        return items -> {
            if (items.isEmpty()) return;

            // OneThingMatching ID 기준으로 그룹화
            Map<Long, List<UserOneThingMatching>> groupedByMatchingId = new HashMap<>();
            for (UserOneThingMatching item : items) {
                Long matchingId = item.getOneThingMatching().getId();
                if (!groupedByMatchingId.containsKey(matchingId)) {
                    groupedByMatchingId.put(matchingId, new ArrayList<>());
                }
                groupedByMatchingId.get(matchingId).add(item);
            }

            // 모든 알림과 FCM 데이터를 저장할 리스트
            List<Notification> allNotifications = new ArrayList<>();
            List<FcmNotificationEvent.UserFcmData> allFcmDataList = new ArrayList<>();

            // 각 그룹별로 처리
            for (Map.Entry<Long, List<UserOneThingMatching>> entry : groupedByMatchingId.entrySet()) {
                Long matchingId = entry.getKey();
                List<UserOneThingMatching> matchingUsers = entry.getValue();

                // 각 그룹별 알림 객체와 FCM 데이터 준비
                List<Notification> groupNotifications = new ArrayList<>();
                List<FcmNotificationEvent.UserFcmData> groupFcmDataList = new ArrayList<>();

                // 랜덤 닉네임 선택 (MATCHING_STARTED 타입인 경우)
                String randomNickname = null;
                if ("MATCHING_STARTED".equals(notificationType)) {
                    int randomIndex = (int) (Math.random() * matchingUsers.size());
                    randomNickname = matchingUsers.get(randomIndex).getUser().getNickname();
                }

                for (UserOneThingMatching item : matchingUsers) {
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

            // FCM 이벤트 발행 (한 번만)
            if (!userDataMap.isEmpty()) {
                sendFCMEventPublisher.publishEvent(new FcmNotificationEvent.GeneralFcmBatchSendEvent(
                        this,
                        MessageTemplateType.valueOf(notificationType),
                        "ONE_THING",
                        userDataMap
                ));
            }
        };
    }
}
