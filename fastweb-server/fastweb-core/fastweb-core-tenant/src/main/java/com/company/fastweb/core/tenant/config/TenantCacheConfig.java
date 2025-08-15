package com.company.fastweb.core.tenant.config;

import com.company.fastweb.core.tenant.cache.TenantAwareCacheKeyGenerator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 租户缓存配置
 * 配置租户感知的缓存Key生成器
 */
@Configuration
@EnableCaching
public class TenantCacheConfig {
    
    @Bean("tenantAwareCacheKeyGenerator")
    public TenantAwareCacheKeyGenerator tenantAwareCacheKeyGenerator() {
        return new TenantAwareCacheKeyGenerator();
    }
}