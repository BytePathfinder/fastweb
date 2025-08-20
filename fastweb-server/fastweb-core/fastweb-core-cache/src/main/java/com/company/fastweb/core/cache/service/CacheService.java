package com.company.fastweb.core.cache.service;

import java.util.Map;
import java.util.Set;
import com.company.fastweb.core.cache.model.dto.CacheStatisticsDTO;

/**
 * 缓存服务接口
 * 提供统一的缓存操作接口，支持 Redis 和本地缓存
 * 
 * @author FastWeb
 */
public interface CacheService {

    /**
     * 设置缓存
     * 
     * @param key   缓存键
     * @param value 缓存值
     */
    void set(String key, Object value);

    /**
     * 设置缓存（带过期时间）
     * 
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间（秒）
     */
    void set(String key, Object value, long timeout);

    /**
     * 获取缓存
     * 
     * @param key   缓存键
     * @param clazz 值类型
     * @return 缓存值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取缓存（字符串类型）
     * 
     * @param key 缓存键
     * @return 缓存值
     */
    String get(String key);

    /**
     * 删除缓存
     * 
     * @param key 缓存键
     */
    void delete(String key);

    /**
     * 检查缓存是否存在
     * 
     * @param key 缓存键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 设置过期时间
     * 
     * @param key     缓存键
     * @param timeout 过期时间（秒）
     */
    void expire(String key, long timeout);

    /**
     * 获取剩余过期时间
     * 
     * @param key 缓存键
     * @return 剩余过期时间（秒），-1表示永不过期，-2表示不存在
     */
    long getExpire(String key);

    /**
     * 清空指定缓存区域
     * 
     * @param cacheName 缓存区域名称
     */
    void clear(String cacheName);

    /**
     * 获取缓存信息
     * 
     * @return 缓存统计信息
     */
    Map<String, Object> getInfo();

    /**
     * 根据模式匹配获取键集合
     * 
     * @param pattern 匹配模式（支持通配符 *）
     * @return 匹配的键集合
     */
    Set<String> keys(String pattern);

    /**
     * 批量删除缓存
     * 
     * @param keys 缓存键集合
     */
    void delete(Set<String> keys);

    /**
     * 检查缓存键是否存在（别名方法）
     * 
     * @param key 缓存键
     * @return 是否存在
     */
    boolean hasKey(String key);

    /**
     * 获取缓存统计信息
     * 
     * @return 缓存统计信息
     */
    CacheStatisticsDTO getStatistics();

    /**
     * 获取缓存大小（键的数量）
     * 
     * @return 缓存大小
     */
    long size();

    /**
     * 获取缓存大小（指定模式）
     * 
     * @param pattern 匹配模式
     * @return 匹配的键数量
     */
    long size(String pattern);
}