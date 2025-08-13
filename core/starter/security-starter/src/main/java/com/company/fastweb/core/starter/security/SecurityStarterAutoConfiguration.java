package com.company.fastweb.core.starter.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 安全启动器自动配置
 *
 * @author fastweb
 */
@AutoConfiguration
@ComponentScan(basePackages = "com.company.fastweb.core.infra.security")
public class SecurityStarterAutoConfiguration {
}