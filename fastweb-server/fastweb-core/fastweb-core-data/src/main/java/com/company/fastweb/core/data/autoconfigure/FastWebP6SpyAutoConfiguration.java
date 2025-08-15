package com.company.fastweb.core.data.autoconfigure;

import com.company.fastweb.core.data.properties.FastWebDataProperties;
import com.p6spy.engine.spy.P6DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * P6Spy SQL监控自动配置
 * 
 * 功能特性：
 * 1. 完整的SQL语句输出（包含参数值）
 * 2. 执行时间监控（可配置慢查询阈值）
 * 3. 美观的格式化输出
 * 4. 按数据源分别统计
 * 5. 可配置的日志级别和输出位置
 */
@Slf4j
@Configuration
@AutoConfiguration
@ConditionalOnClass(P6DataSource.class)
@EnableConfigurationProperties(FastWebDataProperties.class)
@ConditionalOnProperty(prefix = "fastweb.data.p6spy", name = "enabled", havingValue = "true")
public class FastWebP6SpyAutoConfiguration {

    public FastWebP6SpyAutoConfiguration() {
        log.info("FastWeb P6Spy SQL监控已启用");
    }

    /**
     * 创建P6Spy包装的数据源
     */
    @Bean
    public P6DataSource p6DataSource(DataSource dataSource) {
        log.debug("包装数据源为P6Spy代理: {}", dataSource.getClass().getSimpleName());
        return new P6DataSource(dataSource);
    }
}