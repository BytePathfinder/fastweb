package com.company.fastweb.core.monitor.config;

import com.company.fastweb.core.monitor.service.MonitorService;
import com.company.fastweb.core.monitor.service.impl.DefaultMonitorServiceImpl;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * 监控自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass({MeterRegistry.class, HealthEndpoint.class})
public class MonitorAutoConfiguration {

    /**
     * 监控服务
     */
    @Bean
    @ConditionalOnMissingBean
    public MonitorService monitorService(MeterRegistry meterRegistry,
                                       HealthEndpoint healthEndpoint,
                                       MetricsEndpoint metricsEndpoint,
                                       DataSource dataSource) {
        log.info("FastWeb Monitor Service initialized");
        return new DefaultMonitorServiceImpl(meterRegistry, healthEndpoint, metricsEndpoint, dataSource);
    }
}