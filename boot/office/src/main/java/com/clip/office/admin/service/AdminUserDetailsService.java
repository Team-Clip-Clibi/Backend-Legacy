package com.clip.office.admin.service;

import com.clip.admin.entity.AdminUser;
import com.clip.admin.service.AdminUserService;
import com.clip.office.admin.exception.NotFoundAdminUserException;
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
        AdminUser admin = adminUserService.findOptAdminUser(username)
                .orElseThrow(NotFoundAdminUserException::new);

        return User.builder() //security.core.userdetails.User
                .username(admin.getUsername())
                .password(admin.getPassword())
                .build();
    }
}
