package com.company.fastweb.core.starter.data;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 数据启动器自动配置
 *
 * @author fastweb
 */
@AutoConfiguration
@ComponentScan(basePackages = "com.company.fastweb.core.infra.data")
public class DataStarterAutoConfiguration {
}