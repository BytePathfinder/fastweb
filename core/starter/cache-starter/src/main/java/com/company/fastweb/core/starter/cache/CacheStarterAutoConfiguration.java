package com.company.fastweb.core.starter.cache;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 缓存启动器自动配置
 *
 * @author fastweb
 */
@AutoConfiguration
@ComponentScan(basePackages = "com.company.fastweb.core.infra.cache")
public class CacheStarterAutoConfiguration {
}