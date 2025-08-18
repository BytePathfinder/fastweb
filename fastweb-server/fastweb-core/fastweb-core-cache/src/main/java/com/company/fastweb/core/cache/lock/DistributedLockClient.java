package com.company.fastweb.core.cache.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁客户端
 */
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
        if (lock != null && lock.isLocked()) {
            lock.unlock();
        }
    }
}