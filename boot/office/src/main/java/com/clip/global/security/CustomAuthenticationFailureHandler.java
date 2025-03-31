package com.clip.global.security;

import com.clip.global.security.util.LoginAttemptManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final LoginAttemptManager loginAttemptManager;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

        loginAttemptManager.recordFail(username);

        if (loginAttemptManager.isBlocked(username)) {
            response.sendRedirect("/office/admin/login?locked=true");
        } else {
            response.sendRedirect("/office/admin/login?error=true");
        }
    }
}
