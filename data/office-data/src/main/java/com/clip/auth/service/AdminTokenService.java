package com.clip.auth.service;

import com.clip.admin.entity.AdminUser;
import com.clip.auth.entity.AdminToken;
import com.clip.auth.repository.AdminTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminTokenService {
    private final AdminTokenRepository adminTokenRepository;

    public AdminToken save(AdminToken adminToken) {
        return adminTokenRepository.save(adminToken);
    }

    public void updateAdminRefreshToken(AdminUser adminUser, String refreshToken) {
        adminTokenRepository.updateAdminRefreshToken(adminUser, refreshToken);
    }
}
