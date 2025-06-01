package com.clip.batch.notification;

import com.clip.batch.notification.service.MatchingNotificationService;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.matching.entity.UserOneThingMatching;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Configuration
public class OneThingMatchingInfoNotificationBatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final MatchingNotificationService matchingNotificationService;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;

    @Scheduled(cron = "0 0 19 * * FRI,SAT") // 1일 전 알림 스케줄러 (금/토)
    @SchedulerLock(name = "oneThingMatchingInfo_oneDayPrior", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runOneDayPriorJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_TOMORROW")
                .addLocalDate("targetDate", LocalDate.now().plusDays(1))
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingInfoFcmJob(), params);
    }

    @Scheduled(cron = "0 0 9 * * SAT,SUN") // 당일 알림 스케줄러 (토/일)
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
                        and um.matchingStatus = 'CONFIRMED'
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
        return chunk -> {
            List<UserOneThingMatching> items = new ArrayList<>(chunk.getItems());
            if (items.isEmpty()) return;
            MessageTemplateType templateType = MessageTemplateType.valueOf(notificationType);
            matchingNotificationService.processOneThingMatchingNotifications(items, templateType);
        };
    }

}
