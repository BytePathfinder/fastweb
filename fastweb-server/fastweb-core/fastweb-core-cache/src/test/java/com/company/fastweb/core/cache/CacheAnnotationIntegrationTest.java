package com.company.fastweb.core.cache;

import com.company.fastweb.core.cache.samples.SampleCacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheAnnotationIntegrationTest extends BaseCacheIntegrationTest {

    @Autowired
    private SampleCacheService sampleCacheService;

    @Test
    void testCacheableAndCachePutAndEvict() {
        Long id = 1L;
        String first = sampleCacheService.getUserById(id);
        String again = sampleCacheService.getUserById(id);
        Assertions.assertEquals(first, again, "@Cacheable 应命中缓存");

        String updated = sampleCacheService.updateUser(id);
        Assertions.assertNotEquals(first, updated, "@CachePut 应更新缓存");

        String cachedAfterPut = sampleCacheService.getUserById(id);
        Assertions.assertEquals(updated, cachedAfterPut, "@CachePut 后应命中新值");

        sampleCacheService.evictUser(id);
        String afterEvict = sampleCacheService.getUserById(id);
        Assertions.assertNotEquals(updated, afterEvict, "@CacheEvict 后应重新加载");
    }
}