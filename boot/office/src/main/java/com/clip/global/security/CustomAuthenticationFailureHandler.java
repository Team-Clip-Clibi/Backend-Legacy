package com.clip.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final StringRedisTemplate redisTemplate;

    @Value("${spring.security.login.max-fail-count}")
    private int maxFailCount;

    @Value("${spring.security.login.block-duration-seconds}")
    private long blockDurationSeconds;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        String redisKey = "login:fail:" + username;

        Long failCount = redisTemplate.opsForValue().increment(redisKey);

        if (failCount == null) {
            System.out.println("Redis increment 실패 - failCount가 null입니다.");
            response.sendRedirect("/office/admin/login?error=true");
            return;
        }
        Long ttl = redisTemplate.getExpire(redisKey);
        if (ttl == null || ttl == -1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(blockDurationSeconds));
        }

        System.out.println("현재 실패 횟수: " + failCount);
        System.out.println("현재 TTL: " + redisTemplate.getExpire(redisKey) + "초");

        if (failCount >= maxFailCount) {
            response.sendRedirect("/office/admin/login?locked=true");
        } else {
            response.sendRedirect("/office/admin/login?error=true");
        }
    }
}
