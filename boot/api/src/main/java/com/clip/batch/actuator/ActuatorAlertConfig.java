package com.clip.batch.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ActuatorAlertConfig {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CpuObservationTasklet cpuObservationTasklet;


    @Scheduled(cron = "0 */5 * * * *")
    public void cpuObservation() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(cpuObservationJob())
                .toJobParameters();
        jobLauncher.run(cpuObservationJob(), jobParameters);
    }


    @Bean
    public Job cpuObservationJob() {
        return new JobBuilder("cpuObservationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(cpuObservationStep())
                .build();
    }

    @Bean
    public Step cpuObservationStep() {
        return new StepBuilder("cpuObservationStep", jobRepository)
                .tasklet(cpuObservationTasklet, transactionManager)
                .build();
    }
}
