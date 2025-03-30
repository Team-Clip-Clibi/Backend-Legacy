package com.clip.global.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

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
                System.out.println("필터에서 username 파라미터를 가져올 수 없습니다.");
            }
        }

        chain.doFilter(req, res);
    }
}
