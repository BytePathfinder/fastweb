package com.company.fastweb.core.config.config;

import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.config.processor.ConfigValueProcessor;
import com.company.fastweb.core.config.service.ConfigService;
import com.company.fastweb.core.config.service.impl.CacheConfigServiceImpl;
import com.company.fastweb.core.config.service.impl.MemoryConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 配置管理自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
public class ConfigAutoConfiguration {

    /**
     * 基于缓存的配置服务
     */
    @Bean
    @ConditionalOnBean(CacheService.class)
    @ConditionalOnMissingBean
    public ConfigService cacheConfigService(CacheService cacheService) {
        log.info("Cache-based Config Service initialized");
        return new CacheConfigServiceImpl(cacheService);
    }

    /**
     * 基于内存的配置服务（fallback）
     */
    @Bean
    @ConditionalOnMissingBean
    public ConfigService memoryConfigService() {
        log.info("Memory-based Config Service initialized");
        return new MemoryConfigServiceImpl();
    }

    /**
     * 配置值处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public ConfigValueProcessor configValueProcessor(ConfigService configService) {
        log.info("Config Value Processor initialized");
        return new ConfigValueProcessor(configService);
    }
}