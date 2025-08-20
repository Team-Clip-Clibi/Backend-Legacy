package com.clip.batch.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.RequiredSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CpuObservationTasklet implements Tasklet {
    private final MeterRegistry meterRegistry;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        double systemCpuUsage = getValue("system.cpu.usage");           // 시스템 CPU 사용률
        double processCpuUsage = getValue("process.cpu.usage");         // 현재 JVM 프로세스 CPU 사용률
        double usedMemory = getValue("jvm.memory.used");                // 사용중인 메모리
        double maxMemory = getValue("jvm.memory.max");                  // 전체 메모리
        double hikariActive = getValue("hikaricp.connections.active");  // HikariCP 사용중 커넥션
        double hikariTotal = getValue("hikaricp.connections");          // HikariCP 전체 커넥션 풀

        System.out.printf(
                "[Metrics] systemCpu=%.2f, processCpu=%.2f, usedMem=%.2fMB, maxMem=%.2fMB, hikariActive=%.0f, hikariTotal=%.0f%n",
                systemCpuUsage * 100,
                processCpuUsage * 100,
                usedMemory / (1024 * 1024),
                maxMemory / (1024 * 1024),
                hikariActive,
                hikariTotal
        );

        return RepeatStatus.FINISHED;
    }

    private double getValue(String metricName) {
        RequiredSearch search = meterRegistry.get(metricName);
        if (search.gauge() != null) {
            log.info("asdf");
            return search.gauge().value();
        }
        log.info("asaaaaaaaaaaa");
        return Double.NaN;
    }
}
