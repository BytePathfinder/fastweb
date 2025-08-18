package com.company.fastweb.core.config.config;

import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.config.processor.ConfigValueProcessor;
import com.company.fastweb.core.config.service.ConfigService;
import com.company.fastweb.core.config.service.impl.CacheConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 配置管理自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(CacheService.class)
public class ConfigAutoConfiguration {

    /**
     * 配置服务
     */
    @Bean
    @ConditionalOnMissingBean
    public ConfigService configService(CacheService cacheService) {
        log.info("FastWeb Config Service initialized");
        return new CacheConfigServiceImpl(cacheService);
    }

    /**
     * 配置值处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public ConfigValueProcessor configValueProcessor(ConfigService configService) {
        log.info("FastWeb Config Value Processor initialized");
        return new ConfigValueProcessor(configService);
    }
}