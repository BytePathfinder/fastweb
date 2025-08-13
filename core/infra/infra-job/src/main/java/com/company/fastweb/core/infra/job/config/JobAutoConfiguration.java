package com.company.fastweb.core.infra.job.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 任务调度基础设施自动配置
 *
 * @author fastweb
 */
@AutoConfiguration
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(JobProperties.class)
@ComponentScan(basePackages = "com.company.fastweb.core.infra.job")
public class JobAutoConfiguration {
}