package com.clip.batch.notification;

import com.clip.batch.notification.service.MatchingProgressNotificationService;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Configuration
public class OneThingMatchingProgressNotificationBatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final MatchingProgressNotificationService matchingProgressNotificationService;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;

    @Scheduled(cron = "0 0 19 * * SAT,SUN") // 원띵 매칭 당일(매주 토요일, 일요일 오후 7시)
    @SchedulerLock(name = "oneThingMatchingInfo_matchingStart", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runMatchingStartJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("notificationType", "MATCHING_STARTED")
                .addLocalDateTime("targetDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingProgressFcmJob(), params);
    }

    @Scheduled(cron = "0 0 22 * * SAT,SUN") // 원띵 매칭 종료일(매주 토요일, 일요일 오후 10시)
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
    public ItemWriter<UserOneThingMatching> fcmOneThingProgressWriter(
            @Value("#{jobParameters['notificationType']}") String notificationType
    ) {
        return chunk -> {
            List<UserOneThingMatching> items = new ArrayList<>(chunk.getItems());
            if (items.isEmpty()) return;
            MessageTemplateType templateType = MessageTemplateType.valueOf(notificationType);
            matchingProgressNotificationService.processOneThingProgressNotifications(items, templateType);
        };
    }
}
