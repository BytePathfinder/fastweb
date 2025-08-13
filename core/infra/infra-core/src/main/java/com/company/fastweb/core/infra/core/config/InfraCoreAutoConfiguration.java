package com.company.fastweb.core.infra.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 基础设施核心自动配置
 *
 * @author fastweb
 */
@AutoConfiguration
@EnableConfigurationProperties(InfraCoreProperties.class)
@ComponentScan(basePackages = "com.company.fastweb.core.infra.core")
public class InfraCoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}