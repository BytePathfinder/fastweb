package com.company.fastweb.core.cache.service.impl;

import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis缓存服务实现
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    // ========== String 操作 ==========

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        return convertValue(value, type);
    }

    @Override
    public <T> T get(String key, Class<T> type, T defaultValue) {
        T value = get(key, type);
        return value != null ? value : defaultValue;
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Boolean expire(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    // ========== Hash 操作 ==========

    @Override
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String hashKey, Class<T> type) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return convertValue(value, type);
    }

    @Override
    public void hSetAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public Map<String, Object> hGetAll(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<String, Object> result = new HashMap<>();
        entries.forEach((k, v) -> result.put(k.toString(), v));
        return result;
    }

    @Override
    public Long hDelete(String key, String... hashKeys) {
        return redisTemplate.opsForHash().delete(key, (Object[]) hashKeys);
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> hKeys(String key) {
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        return keys.stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public List<Object> hValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    @Override
    public Long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    // ========== List 操作 ==========

    @Override
    public Long lLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public Long lRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T lLeftPop(String key, Class<T> type) {
        Object value = redisTemplate.opsForList().leftPop(key);
        return convertValue(value, type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T lRightPop(String key, Class<T> type) {
        Object value = redisTemplate.opsForList().rightPop(key);
        return convertValue(value, type);
    }

    @Override
    public <T> List<T> lRange(String key, long start, long end, Class<T> type) {
        List<Object> values = redisTemplate.opsForList().range(key, start, end);
        if (values == null) {
            return new ArrayList<>();
        }
        return values.stream()
            .map(value -> convertValue(value, type))
            .collect(Collectors.toList());
    }

    @Override
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // ========== Set 操作 ==========

    @Override
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public <T> Set<T> sMembers(String key, Class<T> type) {
        Set<Object> values = redisTemplate.opsForSet().members(key);
        if (values == null) {
            return new HashSet<>();
        }
        return values.stream()
            .map(value -> convertValue(value, type))
            .collect(Collectors.toSet());
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    // ========== ZSet 操作 ==========

    @Override
    public Boolean zAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    @Override
    public <T> Set<T> zRange(String key, long start, long end, Class<T> type) {
        Set<Object> values = redisTemplate.opsForZSet().range(key, start, end);
        if (values == null) {
            return new LinkedHashSet<>();
        }
        return values.stream()
            .map(value -> convertValue(value, type))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    @Override
    public Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    // ========== 通用操作 ==========

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();
        try {
            Properties properties = redisTemplate.getConnectionFactory().getConnection().info();
            properties.forEach((key, value) -> info.put(key.toString(), value));
        } catch (Exception e) {
            log.error("获取Redis信息失败", e);
        }
        return info;
    }

    /**
     * 类型转换
     */
    @SuppressWarnings("unchecked")
    private <T> T convertValue(Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        
        if (type.isInstance(value)) {
            return (T) value;
        }
        
        if (type == String.class) {
            return (T) value.toString();
        }
        
        if (value instanceof String) {
            String strValue = (String) value;
            
            // 基本类型转换
            if (type == Integer.class || type == int.class) {
                return (T) Integer.valueOf(strValue);
            }
            if (type == Long.class || type == long.class) {
                return (T) Long.valueOf(strValue);
            }
            if (type == Double.class || type == double.class) {
                return (T) Double.valueOf(strValue);
            }
            if (type == Boolean.class || type == boolean.class) {
                return (T) Boolean.valueOf(strValue);
            }
            
            // JSON反序列化
            return JsonUtils.parseObject(strValue, type);
        }
        
        // 其他情况尝试JSON转换
        String jsonStr = JsonUtils.toJsonString(value);
        return JsonUtils.parseObject(jsonStr, type);
    }
}