package com.clip.office.admin.service;

import com.clip.office.admin.client.AdminAccountClient;
import com.clip.office.admin.client.dto.AdminLoginApiDto;
import com.clip.office.admin.client.dto.TokenProviderApiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAccountApiService {
    private final AdminAccountClient adminAccountClient;

    public TokenProviderApiDto loginAdminAccount(AdminLoginApiDto request) {
        return adminAccountClient.loginAdminAccount(request);
    }
}