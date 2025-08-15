package com.company.fastweb.core.tenant.autoconfigure;

import com.company.fastweb.core.data.properties.FastWebDataProperties;
import com.company.fastweb.core.tenant.cache.TenantAwareCacheKeyGenerator;
import com.company.fastweb.core.tenant.config.TenantCacheConfig;
import com.company.fastweb.core.tenant.config.TenantMybatisPlusConfig;
import com.company.fastweb.core.tenant.config.TenantWebConfig;
import com.company.fastweb.core.tenant.datasource.TenantDataSourceRouter;
import com.company.fastweb.core.tenant.resolver.TenantResolver;
import com.company.fastweb.core.tenant.resolver.impl.DefaultTenantResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 租户自动配置类
 * 整合所有租户相关的配置和组件
 */
@AutoConfiguration
@EnableConfigurationProperties(FastWebDataProperties.class)
@ConditionalOnProperty(prefix = "fastweb.data", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({
    TenantWebConfig.class,
    TenantMybatisPlusConfig.class,
    TenantCacheConfig.class
})
public class TenantAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public TenantResolver tenantResolver() {
        return new DefaultTenantResolver();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public TenantDataSourceRouter tenantDataSourceRouter(FastWebDataProperties properties) {
        return new TenantDataSourceRouter(properties);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public TenantAwareCacheKeyGenerator tenantAwareCacheKeyGenerator() {
        return new TenantAwareCacheKeyGenerator();
    }
}
