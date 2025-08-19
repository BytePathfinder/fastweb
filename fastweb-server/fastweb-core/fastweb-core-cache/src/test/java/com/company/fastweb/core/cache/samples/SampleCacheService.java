package com.company.fastweb.core.cache.samples;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SampleCacheService {

    @Cacheable(cacheNames = "user", key = "#id")
    public String getUserById(Long id) {
        return UUID.randomUUID().toString();
    }

    @CachePut(cacheNames = "user", key = "#id")
    public String updateUser(Long id) {
        return UUID.randomUUID().toString();
    }

    @CacheEvict(cacheNames = "user", key = "#id")
    public void evictUser(Long id) {
    }
}