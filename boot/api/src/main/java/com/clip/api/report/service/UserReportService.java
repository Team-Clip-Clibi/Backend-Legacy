package com.clip.api.report.service;

import com.clip.api.report.controller.dto.ReportDto;
import com.clip.api.report.mapper.ReportMapper;
import com.clip.report.service.ReportService;
import com.clip.user.entity.User;
import com.clip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserReportService {
    private final ReportService reportService;
    private final ReportMapper reportMapper;
    private final UserService userService;

    @Transactional
    public void saveUserReport(long userId, ReportDto reportDto) {
        User user = userService.findUser(userId);
        reportService.save(reportMapper.toReport(user, reportDto));
    }
}
