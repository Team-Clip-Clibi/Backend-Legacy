package com.clip.batch.matching;

import com.clip.batch.matching.service.DeleteExpiredOrderService;
import com.clip.batch.matching.service.UpdateCapacityService;
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
    private final UpdateCapacityService updateCapacityService;
    private final DeleteExpiredOrderService deleteExpiredOrderService;
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
                .start(updateCapacityStep())
                .next(deleteExpiredOrderStep())
                .build();
    }

    // 수량 복구
    @Bean
    public Step updateCapacityStep() {
        return new StepBuilder("updateCapacityStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    updateCapacityService.updateAvailableCapacity();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    // 주문서 삭제
    @Bean
    public Step deleteExpiredOrderStep() {
        return new StepBuilder("deleteExpiredOrderStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    deleteExpiredOrderService.deleteExpiredOrder();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


}
