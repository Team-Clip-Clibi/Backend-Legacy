package com.clip.batch.actuator;

import com.clip.batch.actuator.feign.DiscordFeign;
import com.clip.batch.actuator.feign.dto.DiscordMSGRequestDto;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.RequiredSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Component
@RequiredArgsConstructor
public class CpuObservationTasklet implements Tasklet {
    private final MeterRegistry meterRegistry;
    private final DiscordFeign discordFeign;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        StringBuilder sb = new StringBuilder();

        double systemCpuUsage = getValue("system.cpu.usage");
        double processCpuUsage = getValue("process.cpu.usage");
        double usedMemory = getValue("jvm.memory.used");
        double maxMemory = getValue("jvm.memory.max");
        double hikariActive = getValue("hikaricp.connections.active");
        double hikariTotal = getValue("hikaricp.connections");

        String metricsLog = String.format(
                "========== [Metrics] ========== \n systemCpu=%.2f%%, processCpu=%.2f%%, usedMem=%.2fMB, maxMem=%.2fMB, hikariActive=%.0f, hikariTotal=%.0f",
                systemCpuUsage * 100,
                processCpuUsage * 100,
                usedMemory / (1024 * 1024),
                maxMemory / (1024 * 1024),
                hikariActive,
                hikariTotal
        );
        log.info(metricsLog);

        if (systemCpuUsage > 0.3){
            sb.append(metricsLog).append("\n\n");
            sb.append(printTop5ProcessesOrderByCpuUsage());
            sb.append(printTop5ProcessesOrderByMemoryUsage());

            sendDiscordMessage(sb.toString());
        }

        return RepeatStatus.FINISHED;
    }

    private String printTop5ProcessesOrderByCpuUsage() {
        StringBuilder sb = new StringBuilder();
        String os = System.getProperty("os.name").toLowerCase();
        String command;

        if (os.contains("mac")) {
            command = "ps aux | sort -nrk 3 | head -n 10";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            command = "ps -eo pid,comm,%cpu,%mem --sort=-%cpu | head -n 11";
        } else {
            String msg = "Unsupported OS for process listing: " + os;
            log.info(msg);
            return msg + "\n";
        }

        Process process = null;
        try {
            process = new ProcessBuilder("bash", "-c", command)
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                sb.append("========== [Top 10 Processes by CPU] ==========\n");
                while ((line = reader.readLine()) != null) {
                    String truncated = line.substring(0, Math.min(line.length(), 200));
                    log.info(truncated);
                    sb.append(truncated).append("\n");
                }
            }
            process.waitFor();
        } catch (Exception e) {
            log.error("Failed to get top processes", e);
            sb.append("Failed to get top processes\n");
        } finally {
            if (process != null) process.destroy();
        }
        sb.append("\n");
        return sb.toString();
    }

    private String printTop5ProcessesOrderByMemoryUsage() {
        StringBuilder sb = new StringBuilder();
        String os = System.getProperty("os.name").toLowerCase();
        String command;

        if (os.contains("mac")) {
            command = "ps aux | sort -nrk 4 | head -n 10";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            command = "ps -eo pid,comm,%cpu,%mem --sort=-%mem | head -n 11";
        } else {
            String msg = "Unsupported OS for process listing: " + os;
            log.info(msg);
            return msg + "\n";
        }

        Process process = null;
        try {
            process = new ProcessBuilder("bash", "-c", command)
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                sb.append("========== [Top 10 Processes by Memory] ==========\n");
                while ((line = reader.readLine()) != null) {
                    String truncated = line.substring(0, Math.min(line.length(), 200));
                    log.info(truncated);
                    sb.append(truncated).append("\n");
                }
            }
            process.waitFor();
        } catch (Exception e) {
            log.error("Failed to get top processes by memory", e);
            sb.append("Failed to get top processes by memory\n");
        } finally {
            if (process != null) process.destroy();
        }
        sb.append("\n");
        return sb.toString();
    }

    private void sendDiscordMessage(String fullMsg) {
        int sizeLimit = 1900;
        for (int i = 0; i < fullMsg.length(); i += sizeLimit) {
            int end = Math.min(fullMsg.length(), i + sizeLimit);
            String part = fullMsg.substring(i, end);
            discordFeign.sendMessage(new DiscordMSGRequestDto(part));
        }
    }

    private double getValue(String metricName) {
        RequiredSearch search = meterRegistry.get(metricName);
        return search.gauge().value();
    }
}
