package com.clip.office.admin.controller;

import com.clip.office.admin.client.dto.AdminLoginApiDto;
import com.clip.office.admin.client.dto.TokenProviderApiDto;
import com.clip.office.admin.service.AdminAccountApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/office")
public class AdminAccountApiController {

    private final AdminAccountApiService adminAccountApiService;

    @PostMapping("/login")
    public TokenProviderApiDto loginAdminAccount(@RequestBody AdminLoginApiDto request) {
        return adminAccountApiService.loginAdminAccount(request);
    }
}
