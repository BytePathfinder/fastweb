package com.company.fastweb.core.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存配置属性
 *
 * @author FastWeb
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class CacheProperties {
    
    private String host = "localhost";
    private int port = 6379;
    private String password;
    private int database = 0;
    private int timeout = 3000;
    private int connectTimeout = 3000;
    private Pool pool = new Pool();
    private int defaultTtl = 30; // 默认缓存过期时间（分钟）
    
    @Data
    public static class Pool {
        private int maxActive = 8;
        private int maxIdle = 8;
        private int minIdle = 0;
        private long idleTimeout = 60000;
    }
}