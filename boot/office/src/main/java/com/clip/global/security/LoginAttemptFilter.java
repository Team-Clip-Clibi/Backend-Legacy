package com.clip.global.security;

import com.clip.office.admin.exception.NotExistAdminUserException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginAttemptFilter implements Filter {

    @Value("${spring.security.login.max-fail-count}")
    private int maxFailCount;

    private final StringRedisTemplate redisTemplate;


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getRequestURI().equals("/office/admin/login") &&
                request.getMethod().equalsIgnoreCase("POST")) {

            String username = request.getParameter("username");

            if (username != null && !username.isBlank()) {
                String key = "login:fail:" + username;
                String value = redisTemplate.opsForValue().get(key);


                if (value != null && Integer.parseInt(value) >= maxFailCount) {

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
