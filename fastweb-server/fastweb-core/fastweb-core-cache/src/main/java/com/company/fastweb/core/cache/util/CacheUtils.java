package com.company.fastweb.core.cache.util;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * 缓存工具类
 */
@Component
@RequiredArgsConstructor
public class CacheUtils {

    private final CacheManager cacheManager;

    public Optional<Cache> getCache(String name) {
        return Optional.ofNullable(cacheManager.getCache(name));
    }

    public <T> T get(String cacheName, String key) {
        return (T) getCache(cacheName).map(cache -> cache.get(key)).map(Cache.ValueWrapper::get).orElse(null);
    }

    public void put(String cacheName, String key, Object value) {
        getCache(cacheName).ifPresent(cache -> cache.put(key, value));
    }

    public void evict(String cacheName, String key) {
        getCache(cacheName).ifPresent(cache -> cache.evict(key));
    }

    public void clear(String cacheName) {
        getCache(cacheName).ifPresent(Cache::clear);
    }
}