package com.company.fastweb.core.cache.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import com.company.fastweb.core.cache.model.dto.LockInfoDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

/**
 * Redisson 分布式锁客户端
 */
@Slf4j
@Component
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnBean(RedissonClient.class)
public class DistributedLockClient {

    private final RedissonClient redissonClient;

    public DistributedLockClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RLock lock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        lock.lock();
        return lock;
    }

    public RLock lock(String lockName, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockName);
        lock.lock(leaseTime, unit);
        return lock;
    }

    public boolean tryLock(String lockName, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockName);
        return lock.tryLock(waitTime, leaseTime, unit);
    }

    public void unlock(String lockName) {
        redissonClient.getLock(lockName).unlock();
    }

    public void unlock(RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 获取锁信息
     */
    public LockInfoDTO getLockInfo(String lockName) {
        try {
            RLock lock = redissonClient.getLock(lockName);
            
            return LockInfoDTO.builder()
                    .lockName(lockName)
                    .locked(lock.isLocked())
                    .heldByCurrentThread(lock.isHeldByCurrentThread())
                    .remainingTime(lock.remainTimeToLive())
                    .holdCount(lock.getHoldCount())
                    .status(lock.isLocked() ? "locked" : "unlocked")
                    .createTime(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("获取锁信息失败: {}", lockName, e);
            return LockInfoDTO.builder()
                    .lockName(lockName)
                    .locked(false)
                    .status("error")
                    .createTime(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 强制解锁
     */
    public boolean forceUnlock(String lockName) {
        try {
            RLock lock = redissonClient.getLock(lockName);
            lock.forceUnlock();
            return true;
        } catch (Exception e) {
            log.error("强制解锁失败: {}", lockName, e);
            return false;
        }
    }

    /**
     * 检查锁是否存在
     */
    public boolean isLocked(String lockName) {
        try {
            RLock lock = redissonClient.getLock(lockName);
            return lock.isLocked();
        } catch (Exception e) {
            log.error("检查锁状态失败: {}", lockName, e);
            return false;
        }
    }
}