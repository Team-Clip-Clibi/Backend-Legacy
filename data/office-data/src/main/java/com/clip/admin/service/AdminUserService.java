package com.clip.admin.service;

import com.clip.admin.entity.AdminUser;
import com.clip.admin.exception.NotFoundAdminUserException;
import com.clip.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;


    public AdminUser findOptAdminUser(String username) {
        return adminUserRepository.findByUsername(username)
                .orElseThrow(NotFoundAdminUserException::new);
    }
}
