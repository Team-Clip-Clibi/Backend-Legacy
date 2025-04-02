package com.clip.office.admin.service;

import com.clip.admin.entity.AdminUser;
import com.clip.admin.exception.NotFoundAdminUserException;
import com.clip.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserService adminUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin;
        try {
            admin = adminUserService.findAdminUser(username);
        } catch (NotFoundAdminUserException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .build();
    }
}
