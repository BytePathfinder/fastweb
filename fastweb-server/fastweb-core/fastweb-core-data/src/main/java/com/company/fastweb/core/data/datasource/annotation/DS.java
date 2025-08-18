package com.company.fastweb.core.data.datasource.annotation;

import java.lang.annotation.*;

/**
 * 数据源切换注解
 * 可用于方法或类上，标识需要切换的数据源
 * 
 * 支持 SpEL 表达式（例如："#tenantId"）以在运行时动态计算数据源名称。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {
    /**
     * 数据源名称或SpEL表达式（例如："master" 或 "#tenantId"）
     */
    String value();
}