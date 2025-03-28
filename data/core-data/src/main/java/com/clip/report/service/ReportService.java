package com.clip.report.service;

import com.clip.report.entity.Report;
import com.clip.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public Report save(Report report) {
        return reportRepository.save(report);
    }
}
