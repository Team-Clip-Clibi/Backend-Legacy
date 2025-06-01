package com.clip.batch.notification;

import com.clip.batch.notification.service.MatchingNotificationService;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.matching.entity.UserRandomMatching;
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
public class RandomMatchingInfoNotificationBatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final MatchingNotificationService matchingNotificationService;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;

    @Scheduled(cron = "0 0 19 * * THU") // 1일 전 알림 스케줄러 (목)
    @SchedulerLock(name = "randomMatchingInfo_oneDayPrior", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runOneDayPriorJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_TOMORROW")
                .addLocalDate("targetDate", LocalDate.now().plusDays(1))
                .toJobParameters();
        jobLauncher.run(sendRandomMatchingInfoFcmJob(), params);
    }

    @Scheduled(cron = "0 0 9 * * FRI") // 당일 알림 스케줄러 (금)
    @SchedulerLock(name = "randomMatchingInfo_today", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runTodayJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_TODAY")
                .addLocalDate("targetDate", LocalDate.now())
                .toJobParameters();
        jobLauncher.run(sendRandomMatchingInfoFcmJob(), params);
    }

    @Bean
    public Job sendRandomMatchingInfoFcmJob() {
        return new JobBuilder("sendRandomMatchingInfoFcmJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(sendFcmRandomStep())
                .build();
    }

    @Bean
    public Step sendFcmRandomStep() {
        return new StepBuilder("sendFcmRandomStep", jobRepository)
                .<UserRandomMatching, UserRandomMatching>chunk( CHUNK_SIZE, transactionManager)
                .reader(userRandomReaderByDate(null))
                .writer(fcmRandomWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<UserRandomMatching> userRandomReaderByDate(
            @Value("#{jobParameters['targetDate']}") LocalDate targetDate) {
        return new JpaPagingItemReaderBuilder<UserRandomMatching>()
                .name("userRandomReaderByDate")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        select um from UserRandomMatching um
                        join fetch um.randomMatching rm
                        join fetch um.user u
                        where function('DATE', rm.meetingTime) = :targetDate
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
    public ItemWriter<UserRandomMatching> fcmRandomWriter(
            @Value("#{jobParameters['notificationType']}") String notificationType
    ) {
        return chunk -> {
            List<UserRandomMatching> items = new ArrayList<>(chunk.getItems());
            if (items.isEmpty()) return;
            MessageTemplateType templateType = MessageTemplateType.valueOf(notificationType);
            matchingNotificationService.processRandomMatchingNotifications(items, templateType);
        };
    }
}
