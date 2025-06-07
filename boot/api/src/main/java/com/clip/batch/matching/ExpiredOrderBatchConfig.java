package com.clip.batch.matching;

import com.clip.batch.matching.service.HandleExpiredOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class ExpiredOrderBatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final HandleExpiredOrderService handleExpiredOrderService;
    private final PlatformTransactionManager transactionManager;

    @Scheduled(cron = "0 */1 * * * *")
    public void runExpiredOrderJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(expiredOrderJob(), params);
    }

    @Bean
    public Job expiredOrderJob() {
        return new JobBuilder("expiredOrderJob", jobRepository)
                .start(deleteAndRestoreStep())
                .build();
    }

    @Bean
    public Step deleteAndRestoreStep() {
        return new StepBuilder("deleteAndRestoreStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    handleExpiredOrderService.deleteExpiredOrdersAndUpdateCapacity();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
