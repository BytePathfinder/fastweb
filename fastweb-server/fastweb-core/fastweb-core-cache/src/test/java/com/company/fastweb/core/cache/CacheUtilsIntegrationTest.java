package com.company.fastweb.core.cache;

import com.company.fastweb.core.cache.util.CacheUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheUtilsIntegrationTest extends BaseCacheIntegrationTest {

    @Autowired
    private CacheUtils cacheUtils;

    @Test
    void testPutGetEvictClear() {
        String cacheName = "user";
        String key = "k1";

        cacheUtils.put(cacheName, key, "v1");
        String v = cacheUtils.get(cacheName, key);
        Assertions.assertEquals("v1", v);

        cacheUtils.evict(cacheName, key);
        String v2 = cacheUtils.get(cacheName, key);
        Assertions.assertNull(v2);

        cacheUtils.put(cacheName, key, "v3");
        cacheUtils.clear(cacheName);
        String v3 = cacheUtils.get(cacheName, key);
        Assertions.assertNull(v3);
    }
}