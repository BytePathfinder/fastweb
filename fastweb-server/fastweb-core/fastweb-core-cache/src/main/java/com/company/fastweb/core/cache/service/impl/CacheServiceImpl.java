package com.company.fastweb.core.cache.service.impl;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.cache.model.dto.CacheStatisticsDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

/**
 * 缓存服务实现，优先使用 Redis；当 RedisTemplate 不存在时降级为本地 Map（仅用于开发测试）
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "fastweb.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CacheServiceImpl implements CacheService {

    private final ApplicationContext applicationContext;
    private final Map<String, Object> localCache = new HashMap<>();
    private Object redisTemplate; // 动态获取 RedisTemplate

    /**
     * 获取 RedisTemplate 实例，如果不存在则返回 null
     */
    private Object getRedisTemplate() {
        if (redisTemplate == null) {
            try {
                // 尝试通过 ApplicationContext 获取 RedisTemplate Bean
                redisTemplate = applicationContext.getBean("redisTemplate");
                log.debug("RedisTemplate found and initialized");
            } catch (Exception e) {
                // RedisTemplate 不存在，继续使用本地缓存
                log.debug("RedisTemplate not available, using local cache");
                redisTemplate = new Object(); // 标记为已尝试
            }
        }
        return redisTemplate instanceof org.springframework.data.redis.core.RedisTemplate ? redisTemplate : null;
    }

    @Override
    public void set(String key, Object value) {
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                // 使用反射调用 RedisTemplate 的方法
                Object ops = template.getClass().getMethod("opsForValue").invoke(template);
                ops.getClass().getMethod("set", Object.class, Object.class).invoke(ops, key, value);
            } catch (Exception e) {
                log.warn("Redis set operation failed, falling back to local cache", e);
                localCache.put(key, value);
            }
        } else {
            localCache.put(key, value);
        }
    }

    @Override
    public void set(String key, Object value, long timeout) {
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                Object ops = template.getClass().getMethod("opsForValue").invoke(template);
                ops.getClass().getMethod("set", Object.class, Object.class, Duration.class)
                    .invoke(ops, key, value, Duration.ofSeconds(timeout));
            } catch (Exception e) {
                log.warn("Redis set with timeout operation failed, falling back to local cache", e);
                localCache.put(key, value);
            }
        } else {
            localCache.put(key, value);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value;
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                Object ops = template.getClass().getMethod("opsForValue").invoke(template);
                value = ops.getClass().getMethod("get", Object.class).invoke(ops, key);
            } catch (Exception e) {
                log.warn("Redis get operation failed, falling back to local cache", e);
                value = localCache.get(key);
            }
        } else {
            value = localCache.get(key);
        }
        
        if (value == null) return null;
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        if (value instanceof String && clazz == String.class) {
            return clazz.cast(value);
        }
        return null;
    }

    @Override
    public String get(String key) {
        Object value;
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                Object ops = template.getClass().getMethod("opsForValue").invoke(template);
                value = ops.getClass().getMethod("get", Object.class).invoke(ops, key);
            } catch (Exception e) {
                log.warn("Redis get operation failed, falling back to local cache", e);
                value = localCache.get(key);
            }
        } else {
            value = localCache.get(key);
        }
        return value != null ? String.valueOf(value) : null;
    }

    @Override
    public void delete(String key) {
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                template.getClass().getMethod("delete", Object.class).invoke(template, key);
            } catch (Exception e) {
                log.warn("Redis delete operation failed, falling back to local cache", e);
                localCache.remove(key);
            }
        } else {
            localCache.remove(key);
        }
    }

    @Override
    public boolean exists(String key) {
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                Boolean has = (Boolean) template.getClass().getMethod("hasKey", Object.class).invoke(template, key);
                return has != null && has;
            } catch (Exception e) {
                log.warn("Redis exists operation failed, falling back to local cache", e);
                return localCache.containsKey(key);
            }
        } else {
            return localCache.containsKey(key);
        }
    }

    @Override
    public void expire(String key, long timeout) {
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                template.getClass().getMethod("expire", Object.class, Duration.class)
                    .invoke(template, key, Duration.ofSeconds(timeout));
            } catch (Exception e) {
                log.warn("Redis expire operation failed", e);
            }
        }
        // 本地Map不支持过期，忽略
    }

    @Override
    public long getExpire(String key) {
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                Long ttl = (Long) template.getClass().getMethod("getExpire", Object.class).invoke(template, key);
                return ttl == null ? -2 : ttl;
            } catch (Exception e) {
                log.warn("Redis getExpire operation failed", e);
                return exists(key) ? -1 : -2;
            }
        } else {
            return exists(key) ? -1 : -2;
        }
    }

    @Override
    public void clear(String cacheName) {
        // 简化处理：对于 Redis，建议按前缀清理；这里为了示例仅清理本地Map
        localCache.clear();
    }

    @Override
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();
        Object template = getRedisTemplate();
        info.put("backend", template != null ? "redis" : "local");
        if (template != null) {
            try {
                Object connectionFactory = template.getClass().getMethod("getConnectionFactory").invoke(template);
                Object connection = connectionFactory.getClass().getMethod("getConnection").invoke(connectionFactory);
                Long dbSize = (Long) connection.getClass().getMethod("dbSize").invoke(connection);
                info.put("redis.dbsize", dbSize);
            } catch (Exception e) {
                log.warn("获取Redis信息失败", e);
            }
        } else {
            info.put("local.size", localCache.size());
        }
        return info;
    }

    @Override
    public Set<String> keys(String pattern) {
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                Set<String> keys = (Set<String>) template.getClass().getMethod("keys", Object.class).invoke(template, pattern);
                return keys != null ? keys : new HashSet<>();
            } catch (Exception e) {
                log.warn("Redis keys operation failed, falling back to local cache", e);
                return localCache.keySet().stream()
                    .filter(key -> matchPattern(key, pattern))
                    .collect(Collectors.toSet());
            }
        } else {
            return localCache.keySet().stream()
                .filter(key -> matchPattern(key, pattern))
                .collect(Collectors.toSet());
        }
    }

    @Override
    public void delete(Set<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return;
        }
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                template.getClass().getMethod("delete", Collection.class).invoke(template, keys);
            } catch (Exception e) {
                log.warn("Redis batch delete operation failed, falling back to local cache", e);
                keys.forEach(localCache::remove);
            }
        } else {
            keys.forEach(localCache::remove);
        }
    }

    @Override
    public boolean hasKey(String key) {
        return exists(key);
    }

    /**
     * 简单的通配符匹配，支持 * 通配符
     */
    private boolean matchPattern(String key, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return false;
        }
        if ("*".equals(pattern)) {
            return true;
        }
        if (!pattern.contains("*")) {
            return key.equals(pattern);
        }
        
        String regex = pattern.replace("*", ".*");
        return key.matches(regex);
    }

    @Override
    public CacheStatisticsDTO getStatistics() {
        Object template = getRedisTemplate();
        if (template != null) {
            // Redis统计信息
            try {
                org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate = 
                    (org.springframework.data.redis.core.RedisTemplate<String, Object>) template;
                
                // 获取Redis信息（这里简化处理，实际可以通过Redis INFO命令获取更详细信息）
                Set<String> keys = redisTemplate.keys("*");
                long totalKeys = keys != null ? keys.size() : 0;
                
                return CacheStatisticsDTO.builder()
                        .cacheType("redis")
                        .totalKeys(totalKeys)
                        .usedMemory(0L) // 需要通过Redis INFO命令获取
                        .maxMemory(0L)
                        .memoryUsageRate(0.0)
                        .hitCount(0L)
                        .missCount(0L)
                        .hitRate(0.0)
                        .connectionCount(1)
                        .uptime(0L)
                        .statisticsTime(LocalDateTime.now())
                        .build();
            } catch (Exception e) {
                log.warn("获取Redis统计信息失败", e);
            }
        }
        
        // 本地缓存统计信息
        return CacheStatisticsDTO.builder()
                .cacheType("local")
                .totalKeys((long) localCache.size())
                .usedMemory(0L)
                .maxMemory(0L)
                .memoryUsageRate(0.0)
                .hitCount(0L)
                .missCount(0L)
                .hitRate(0.0)
                .connectionCount(0)
                .uptime(0L)
                .statisticsTime(LocalDateTime.now())
                .build();
    }

    @Override
    public long size() {
        Object template = getRedisTemplate();
        if (template != null) {
            try {
                org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate = 
                    (org.springframework.data.redis.core.RedisTemplate<String, Object>) template;
                Set<String> keys = redisTemplate.keys("*");
                return keys != null ? keys.size() : 0;
            } catch (Exception e) {
                log.warn("获取Redis缓存大小失败", e);
                return 0;
            }
        }
        return localCache.size();
    }

    @Override
    public long size(String pattern) {
        Set<String> keys = keys(pattern);
        return keys.size();
    }
}