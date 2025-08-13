package com.company.fastweb.core.starter.web;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Web启动器自动配置
 *
 * @author fastweb
 */
@AutoConfiguration
@ComponentScan(basePackages = "com.company.fastweb.core.infra.web")
public class WebStarterAutoConfiguration {
}