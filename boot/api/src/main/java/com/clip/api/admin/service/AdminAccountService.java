package com.clip.api.admin.service;

import com.clip.admin.entity.AdminUser;
import com.clip.admin.service.AdminService;
import com.clip.api.admin.controller.dto.AdminLoginDto;
import com.clip.api.admin.exception.NotFoundAdminUserException;
import com.clip.auth.service.AdminTokenService;
import com.clip.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminAccountService {
    private final AdminService adminService;
    private final TokenProvider tokenProvider;
    private final AdminTokenService adminTokenService;

    @Transactional
    public TokenProvider.AdminToken loginAdminAccount(AdminLoginDto request) {
        Optional<AdminUser> optAdminUser = adminService.findOptAdminUser(request.getUsername(), request.getPassword());

        if(optAdminUser.isEmpty()){
            throw new NotFoundAdminUserException();
        }

        TokenProvider.AdminToken adminToken = tokenProvider.generateAdminToken(optAdminUser.get().getId(), LocalDateTime.now());
        adminTokenService.updateAdminRefreshToken(optAdminUser.get(), adminToken.refreshToken());
        return adminToken;
    }
}