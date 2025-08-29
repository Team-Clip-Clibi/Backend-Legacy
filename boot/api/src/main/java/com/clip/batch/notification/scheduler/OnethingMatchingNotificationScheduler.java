package com.clip.batch.notification.scheduler;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OnethingMatchingNotificationScheduler {
    private final JobLauncher jobLauncher;
    @Qualifier("sendOneThingMatchingInfoFcmJob")
    private final Job sendOneThingMatchingInfoFcmJob;
    private final JobExplorer jobExplorer;

    @Scheduled(cron = "*/10 * * * * *") // 1일 전 알림 스케줄러 (금/토)
//    @Scheduled(cron = "0 0 19 * * FRI,SAT") // 1일 전 알림 스케줄러 (금/토)
    @SchedulerLock(name = "oneThingMatchingInfo_oneDayPrior", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runOneDayPriorJob() throws Exception {
        JobParameters params = new JobParametersBuilder(jobExplorer)
                .addString("messageTemplateType", "MATCHING_TOMORROW")
                .addString("batchKey", UUID.randomUUID().toString())
                .addLocalDate("targetDate", LocalDate.now().plusDays(1))
                .getNextJobParameters(sendOneThingMatchingInfoFcmJob)
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingInfoFcmJob, params);
    }

    @Scheduled(cron = "*/10 * * * * *") // 당일 알림 스케줄러 (토/일)
//    @Scheduled(cron = "0 0 9 * * SAT,SUN") // 당일 알림 스케줄러 (토/일)
    @SchedulerLock(name = "oneThingMatchingInfo_today", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void runTodayJob() throws Exception {
        JobParameters params = new JobParametersBuilder(jobExplorer)
                .addString("messageTemplateType", "MATCHING_TODAY")
                .addString("batchKey", UUID.randomUUID().toString())
                .addLocalDate("targetDate", LocalDate.now())
                .getNextJobParameters(sendOneThingMatchingInfoFcmJob)
                .toJobParameters();
        jobLauncher.run(sendOneThingMatchingInfoFcmJob, params);
    }
}
