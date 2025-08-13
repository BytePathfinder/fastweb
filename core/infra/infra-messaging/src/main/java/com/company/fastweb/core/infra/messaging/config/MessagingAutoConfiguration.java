package com.company.fastweb.core.infra.messaging.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * 消息基础设施自动配置
 *
 * @author fastweb
 */
@AutoConfiguration
@EnableConfigurationProperties(MessagingProperties.class)
@ComponentScan(basePackages = "com.company.fastweb.core.infra.messaging")
public class MessagingAutoConfiguration {
}
