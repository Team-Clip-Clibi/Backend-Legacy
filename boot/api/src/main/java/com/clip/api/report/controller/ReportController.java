package com.clip.api.report.controller;

import com.clip.api.docs.report.ReportDocs;
import com.clip.api.report.controller.dto.ReportDto;
import com.clip.api.report.service.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController implements ReportDocs {

    private final UserReportService userReportService;

    @Override
    public void createRandomCommunity(ReportDto reportDto, UserDetails userDetails) {
        userReportService.saveUserReport(Long.parseLong(userDetails.getUsername()), reportDto);
    }
}
