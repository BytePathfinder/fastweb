package com.company.fastweb.core.tenant.config;

import com.company.fastweb.core.data.properties.FastWebDataProperties;
import com.company.fastweb.core.tenant.mybatis.EnhancedTenantLineHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 租户MyBatis-Plus配置
 * 提供租户处理器Bean，由data模块的配置使用
 */
@Configuration
@ConditionalOnClass(name = "com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler")
@ConditionalOnProperty(prefix = "fastweb.data.mybatis-plus.tenant", name = "enabled", havingValue = "true")
public class TenantMybatisPlusConfig {
    
    /**
     * 提供增强版租户处理器
     * 这个Bean会被data模块的MyBatis-Plus配置使用
     */
    @Bean("fastWebTenantLineHandler")
    @ConditionalOnMissingBean(name = "fastWebTenantLineHandler")
    public EnhancedTenantLineHandler enhancedTenantLineHandler(FastWebDataProperties properties) {
        return new EnhancedTenantLineHandler(properties);
    }
}
