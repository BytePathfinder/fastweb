package com.company.fastweb.core.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 缓存配置属性
 */
@Data
@ConfigurationProperties(prefix = "fastweb.cache")
public class FastWebCacheProperties {

    /**
     * 是否启用缓存
     */
    private boolean enabled = true;

    /**
     * 缓存配置
     */
    private Map<String, CacheConfig> configs;

    @Data
    public static class CacheConfig {
        /**
         * 缓存名称
         */
        private String name;

        /**
         * 过期时间（秒）
         */
        private long expireTime = 3600;

        /**
         * 本地缓存配置
         */
        private LocalCacheConfig local;

        /**
         * Redis缓存配置
         */
        private RedisCacheConfig redis;
    }

    @Data
    public static class LocalCacheConfig {
        /**
         * 是否启用本地缓存
         */
        private boolean enabled = true;

        /**
         * 初始容量
         */
        private int initialCapacity = 100;

        /**
         * 最大容量
         */
        private int maximumSize = 1000;
    }

    @Data
    public static class RedisCacheConfig {
        /**
         * 是否启用Redis缓存
         */
        private boolean enabled = true;

        /**
         * 缓存Key前缀
         */
        private String keyPrefix;
    }
}