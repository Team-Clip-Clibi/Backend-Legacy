package com.clip.global.security.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class DistributeLockService {
    private final RedissonClient redissonClient;

    public void executeWithLock(String lockId, Runnable task) {
        RLock rLock = redissonClient.getLock(lockId);
        boolean isLocked = false;
        try {
            isLocked = rLock.tryLock(5L, 3L, TimeUnit.SECONDS);
            if (isLocked) {
                task.run();
            }else {
                log.info("wait time over");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            if (isLocked) {
                try {
                    rLock.unlock();
                } catch (IllegalMonitorStateException e) {
                    log.info("Redisson Lock Already UnLock");
                }
            }
        }
    }
}
