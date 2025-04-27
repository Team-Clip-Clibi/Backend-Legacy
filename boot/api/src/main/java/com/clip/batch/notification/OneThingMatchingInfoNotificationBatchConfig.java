package com.clip.batch.notification;

import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationType;
import com.clip.notification.service.NotificationService;
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
import java.util.*;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Configuration
public class OneThingMatchingInfoNotificationBatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final ApplicationEventPublisher sendFCMEventPublisher;
    private final NotificationService notificationService;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;

    @Scheduled(cron = "0 0 17 * * TUE,WED,THU") // 3일 전 알림 스케줄러 (화/수/목)
    @SchedulerLock(name = "oneThingMatchingInfo_threeDaysPrior", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runThreeDaysPriorJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_INFO_OPENED")
                .addLocalDate("targetDate", LocalDate.now().plusDays(3))
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingInfoFcmJob(), params);
    }

    @Scheduled(cron = "0 10 17 * * THU,FRI,SAT") // 1일 전 알림 스케줄러 (목/금/토)
    @SchedulerLock(name = "oneThingMatchingInfo_oneDayPrior", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runOneDayPriorJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_TOMORROW")
                .addLocalDate("targetDate", LocalDate.now().plusDays(1))
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingInfoFcmJob(), params);
    }

    @Scheduled(cron = "0 0 9 * * FRI,SAT,SUN") // 당일 알림 스케줄러 (금/토/일)
    @SchedulerLock(name = "oneThingMatchingInfo_today", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runTodayJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_TODAY")
                .addLocalDate("targetDate", LocalDate.now())
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingInfoFcmJob(), params);
    }

    @Bean
    public Job sendOneThingMatchingInfoFcmJob() {
        return new JobBuilder("sendOneThingMatchingInfoFcmJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(sendFcmOneThingStep())
                .build();
    }

    @Bean
    public Step sendFcmOneThingStep() {
        return new StepBuilder("sendFcmOneThingStep", jobRepository)
                .<UserOneThingMatching, UserOneThingMatching>chunk( CHUNK_SIZE, transactionManager)
                .reader(userOneThingReaderByDate(null))
                .writer(fcmOneThingWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<UserOneThingMatching> userOneThingReaderByDate(
            @Value("#{jobParameters['targetDate']}") LocalDate targetDate) {
        return new JpaPagingItemReaderBuilder<UserOneThingMatching>()
                .name("userOneThingReaderByDate")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        select um from UserOneThingMatching um
                        join fetch um.oneThingMatching om
                        join fetch um.user u
                        where function('DATE', om.meetingTime) = :targetDate
                        and u.fcmToken is not null
                        and u.isAllowNotify = true
                        """)
                .parameterValues(Collections.singletonMap("targetDate", targetDate))
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<UserOneThingMatching> fcmOneThingWriter(
            @Value("#{jobParameters['notificationType']}") String notificationType
    ) {
        return items -> {
            if (items.isEmpty()) return;
            // 1. 모든 알림 객체를 먼저 생성
            List<Notification> notifications = new ArrayList<>();
            List<FcmNotificationEvent.UserFcmData> fcmDataList = new ArrayList<>();

            for (UserOneThingMatching item : items) {
                // FCM 데이터 준비
                String token = item.getUser().getFirebaseToken();
                String dayOfWeek = item.getOneThingMatching().getMeetingTime().getDayOfWeek().name();
                String time = String.valueOf(item.getOneThingMatching().getMeetingTime().getHour());
                String deviceType = item.getUser().getDeviceType().name();

                // MessageParams 객체 생성
                MessageParams.DayOfWeekAndTimeParams messageParams =
                        new MessageParams.DayOfWeekAndTimeParams(dayOfWeek, time);

                // NotificationType을 사용하여 메시지 생성
                MessageTemplateType templateType = MessageTemplateType.valueOf(notificationType);
                String message = templateType.generateMessage(messageParams);

                // 알림 객체 생성
                Notification notification = new Notification(
                        NotificationType.MEETING,
                        false,
                        message,
                        item.getUser()
                );
                notifications.add(notification);

                fcmDataList.add(new FcmNotificationEvent.UserFcmData(
                        item.getOneThingMatching().getId(),
                        deviceType,
                        token,
                        messageParams
                ));
            }

            // 2. 알림을 일괄 저장
            List<Notification> savedNotifications = notificationService.saveNotifications(notifications);

            // 3. 저장된 알림 ID와 FCM 데이터를 매핑
            Map<Long, FcmNotificationEvent.UserFcmData> userDataMap = new HashMap<>();
            IntStream.range(0, savedNotifications.size())
                    .forEach(i -> userDataMap.put(savedNotifications.get(i).getId(), fcmDataList.get(i)));

            // 4. FCM 이벤트 발행
            if (!userDataMap.isEmpty()) {
                sendFCMEventPublisher.publishEvent(new FcmNotificationEvent.GeneralFcmBatchSendEvent(
                        this,
                        MessageTemplateType.valueOf(notificationType),
                        "ONE_THING",
                        userDataMap));
            }
        };
    }

}
