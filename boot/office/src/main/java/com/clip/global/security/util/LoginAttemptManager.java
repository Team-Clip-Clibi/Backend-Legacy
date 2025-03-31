package com.clip.global.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class LoginAttemptManager {

    private final StringRedisTemplate redisTemplate;

    @Value("${spring.security.login.max-fail-count}")
    private int maxFailCount;

    @Value("${spring.security.login.block-duration-seconds}")

    private long blockDurationSeconds;

    public String getRedisKey(String username) {
        return "login:fail:" + username;
    }

    public boolean isBlocked(String username) {
        String key = getRedisKey(username);
        String value = redisTemplate.opsForValue().get(key);
        return value != null && Integer.parseInt(value) >= maxFailCount;
    }

    public void recordFail(String username) {
        String key = getRedisKey(username);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && redisTemplate.getExpire(key) == -1) {
            redisTemplate.expire(key, Duration.ofSeconds(blockDurationSeconds));
        }
    }

    public void resetFailCount(String username) {
        redisTemplate.delete(getRedisKey(username));
    }
}
