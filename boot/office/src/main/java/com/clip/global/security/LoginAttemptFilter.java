package com.clip.global.security;

import com.clip.global.security.util.LoginAttemptManager;
import com.clip.office.admin.exception.NotExistAdminUserException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginAttemptFilter implements Filter {

    private final LoginAttemptManager loginAttemptManager;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getRequestURI().equals("/office/admin/login") &&
                request.getMethod().equalsIgnoreCase("POST")) {

            String username = request.getParameter("username");

            if (username != null && !username.isBlank()) {
                if (loginAttemptManager.isBlocked(username)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.sendRedirect("/office/admin/login?locked=true");
                    return;
                }
            } else {
                log.atError()
                        .setCause(new NotExistAdminUserException())
                        .log();
            }
        }

        chain.doFilter(req, res);
    }
}
