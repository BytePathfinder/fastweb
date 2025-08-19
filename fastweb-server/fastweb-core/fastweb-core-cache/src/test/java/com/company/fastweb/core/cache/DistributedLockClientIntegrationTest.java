package com.company.fastweb.core.cache;

import com.company.fastweb.core.cache.lock.DistributedLockClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class DistributedLockClientIntegrationTest extends BaseCacheIntegrationTest {

    @Autowired(required = false)
    private DistributedLockClient lockClient;

    @Test
    void testLockUnlock() {
        if (lockClient == null) {
            // Redisson 未启用则跳过
            return;
        }
        String key = "lock:test:1";
        lockClient.lock(key);
        Assertions.assertTrue(lockClient.isLocked(key));
        lockClient.unlock(key);
        Assertions.assertFalse(lockClient.isLocked(key));
    }

    @Test
    void testTryLockWithLeaseTime() throws InterruptedException {
        if (lockClient == null) {
            return;
        }
        String key = "lock:test:2";
        boolean ok = lockClient.tryLock(key, 100, 2, TimeUnit.SECONDS);
        Assertions.assertTrue(ok);
        Assertions.assertTrue(lockClient.isLocked(key));
        // 等待租约过期
        TimeUnit.SECONDS.sleep(3);
        Assertions.assertFalse(lockClient.isLocked(key));
    }
}