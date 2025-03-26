package com.clip.admin.service;

import com.clip.admin.entity.AdminUser;
import com.clip.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminUserRepository adminUserRepository;


    public Optional<AdminUser> findOptAdminUser(String username, String password) {
        return adminUserRepository.findByUsername(username);
    }
}
