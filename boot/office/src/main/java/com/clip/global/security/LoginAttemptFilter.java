package com.clip.global.security;

import com.clip.global.security.util.LoginAttemptManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class LoginAttemptFilter extends OncePerRequestFilter {
    private final LoginAttemptManager loginAttemptManager;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("LoginAttemptFilter doFilterInternal");
        if (request.getRequestURI().equals("/office/admin/login")) {
            String requestId = request.getParameter("username");

            if (isExistRequestIdInParameter(requestId)) {
                if (loginAttemptManager.isBlockedUserId(requestId)) {
                    response.sendRedirect("/office/admin/login?locked=true");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private static boolean isExistRequestIdInParameter(String requestId) {
        return !Objects.isNull(requestId) && !requestId.isBlank();
    }
}
