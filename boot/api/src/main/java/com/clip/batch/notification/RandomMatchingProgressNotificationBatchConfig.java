package com.clip.batch.notification;

import com.clip.batch.notification.service.MatchingProgressNotificationService;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Configuration
public class RandomMatchingProgressNotificationBatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final MatchingProgressNotificationService matchingProgressNotificationService;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;

    @Scheduled(cron = "0 0 19 * * FRI") // 랜덤 매칭 당일(매주 금요일 오후 7시)
    @SchedulerLock(name = "randomMatchingInfo_matchingStart", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runMatchingStartJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_STARTED")
                .addLocalDateTime("targetDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .toJobParameters();
        jobLauncher.run(sendRandomMatchingProgressFcmJob(), params);
    }

    @Scheduled(cron = "0 0 22 * * FRI") // 랜덤 매칭 종료일(매주 금요일 오후 10시)
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
                        and um.matchingStatus = 'CONFIRMED'
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
        return chunk -> {
            List<UserRandomMatching> items = new ArrayList<>(chunk.getItems());
            if (items.isEmpty()) return;
            MessageTemplateType templateType = MessageTemplateType.valueOf(notificationType);
            matchingProgressNotificationService.processRandomProgressNotifications(items, templateType);
        };
    }
}
