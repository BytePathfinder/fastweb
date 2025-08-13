package com.company.fastweb.core.infra.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存配置属性
 *
 * @author fastweb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.infra.cache")
public class CacheProperties {

    /**
     * 是否启用缓存基础设施
     */
    private boolean enabled = true;

    /**
     * 默认缓存过期时间（秒）
     */
    private long defaultExpiration = 3600;

    /**
     * 缓存键前缀
     */
    private String keyPrefix = "fastweb:";

    /**
     * 分布式锁配置
     */
    private Lock lock = new Lock();

    @Data
    public static class Lock {
        /**
         * 锁的默认过期时间（秒）
         */
        private long defaultExpiration = 30;

        /**
         * 锁的重试间隔（毫秒）
         */
        private long retryInterval = 100;

        /**
         * 锁的最大重试次数
         */
        private int maxRetryTimes = 3;
    }
}