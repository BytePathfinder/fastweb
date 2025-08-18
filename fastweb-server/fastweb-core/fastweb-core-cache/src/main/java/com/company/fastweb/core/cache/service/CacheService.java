package com.company.fastweb.core.cache.service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务接口
 *
 * @author FastWeb
 */
public interface CacheService {

    // ========== String 操作 ==========

    /**
     * 设置缓存
     */
    void set(String key, Object value);

    /**
     * 设置缓存（带过期时间）
     */
    void set(String key, Object value, Duration timeout);

    /**
     * 设置缓存（带过期时间）
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取缓存
     */
    <T> T get(String key, Class<T> type);

    /**
     * 获取缓存（带默认值）
     */
    <T> T get(String key, Class<T> type, T defaultValue);

    /**
     * 删除缓存
     */
    Boolean delete(String key);

    /**
     * 批量删除缓存
     */
    Long delete(Collection<String> keys);

    /**
     * 判断key是否存在
     */
    Boolean hasKey(String key);

    /**
     * 设置过期时间
     */
    Boolean expire(String key, Duration timeout);

    /**
     * 设置过期时间
     */
    Boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * 获取过期时间
     */
    Long getExpire(String key);

    /**
     * 移除过期时间
     */
    Boolean persist(String key);

    // ========== Hash 操作 ==========

    /**
     * Hash设置
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * Hash获取
     */
    <T> T hGet(String key, String hashKey, Class<T> type);

    /**
     * Hash批量设置
     */
    void hSetAll(String key, Map<String, Object> map);

    /**
     * Hash批量获取
     */
    Map<String, Object> hGetAll(String key);

    /**
     * Hash删除
     */
    Long hDelete(String key, String... hashKeys);

    /**
     * Hash判断是否存在
     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * Hash获取所有key
     */
    Set<String> hKeys(String key);

    /**
     * Hash获取所有value
     */
    List<Object> hValues(String key);

    /**
     * Hash大小
     */
    Long hSize(String key);

    // ========== List 操作 ==========

    /**
     * List左推入
     */
    Long lLeftPush(String key, Object value);

    /**
     * List右推入
     */
    Long lRightPush(String key, Object value);

    /**
     * List左弹出
     */
    <T> T lLeftPop(String key, Class<T> type);

    /**
     * List右弹出
     */
    <T> T lRightPop(String key, Class<T> type);

    /**
     * List获取范围
     */
    <T> List<T> lRange(String key, long start, long end, Class<T> type);

    /**
     * List大小
     */
    Long lSize(String key);

    // ========== Set 操作 ==========

    /**
     * Set添加
     */
    Long sAdd(String key, Object... values);

    /**
     * Set移除
     */
    Long sRemove(String key, Object... values);

    /**
     * Set获取所有成员
     */
    <T> Set<T> sMembers(String key, Class<T> type);

    /**
     * Set判断是否存在
     */
    Boolean sIsMember(String key, Object value);

    /**
     * Set大小
     */
    Long sSize(String key);

    // ========== ZSet 操作 ==========

    /**
     * ZSet添加
     */
    Boolean zAdd(String key, Object value, double score);

    /**
     * ZSet移除
     */
    Long zRemove(String key, Object... values);

    /**
     * ZSet获取范围
     */
    <T> Set<T> zRange(String key, long start, long end, Class<T> type);

    /**
     * ZSet获取范围（带分数）
     */
    Set<Object> zRangeWithScores(String key, long start, long end);

    /**
     * ZSet大小
     */
    Long zSize(String key);

    /**
     * ZSet获取分数
     */
    Double zScore(String key, Object value);

    // ========== 通用操作 ==========

    /**
     * 获取匹配的key
     */
    Set<String> keys(String pattern);

    /**
     * 清空所有缓存
     */
    void flushAll();

    /**
     * 获取缓存信息
     */
    Map<String, Object> getInfo();
}