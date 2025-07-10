//package com.clip.global.security.util;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//@Component
//@RequiredArgsConstructor
//public class LoginAttemptManager {
//
//    private final StringRedisTemplate redisTemplate;
//
//    @Value("${spring.security.login.max-fail-count}")
//    private int maxFailCount;
//
//    @Value("${spring.security.login.block-duration-seconds}")
//    private long blockDurationSeconds;
//
//    public boolean isBlockedUserId(String userId) {
//        String failCntInfoKey = getFailCntInfoKey(userId);
//        int failCnt = getFailCnt(failCntInfoKey);
//        return failCnt > maxFailCount;
//    }
//
//    public void increaseFailCount(String userId) {
//        String failCntInfoKey = getFailCntInfoKey(userId);
//        String incrementedFailCount = Integer.toString(getFailCnt(failCntInfoKey) + 1);
//        redisTemplate.opsForValue().set(failCntInfoKey,incrementedFailCount,blockDurationSeconds, TimeUnit.SECONDS);
//    }
//
//    public void deleteFailCount(String username) {
//        redisTemplate.delete(getFailCntInfoKey(username));
//    }
//
//    private String getFailCntInfoKey(String userId) {
//        return "login:fail:" + userId;
//    }
//
//    private int getFailCnt(String failCntInfoKey) {
//        String failCnt = redisTemplate.opsForValue().get(failCntInfoKey);
//        if (failCnt == null) {
//            return 0;
//        }
//        return Integer.parseInt(failCnt);
//    }
//}
