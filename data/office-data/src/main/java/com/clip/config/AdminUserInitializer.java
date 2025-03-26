package com.clip.config;

import com.clip.admin.entity.AdminUser;
import com.clip.admin.entity.UserRole;
import com.clip.admin.repository.AdminUserRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (isDatabaseEmpty()) {
            AdminUser adminUser = AdminUser.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            adminUserRepository.save(adminUser);
        }
    }

    private boolean isDatabaseEmpty() {
        return adminUserRepository.count() == 0;
    }
}