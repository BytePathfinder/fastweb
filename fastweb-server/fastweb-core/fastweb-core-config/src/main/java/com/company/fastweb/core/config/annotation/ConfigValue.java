package com.company.fastweb.core.config.annotation;

import java.lang.annotation.*;

/**
 * 配置值注解
 * 用于自动注入配置值到字段或方法参数
 *
 * @author FastWeb
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigValue {

    /**
     * 配置键
     */
    String key();

    /**
     * 默认值
     */
    String defaultValue() default "";

    /**
     * 是否必需
     */
    boolean required() default false;

    /**
     * 是否自动刷新
     */
    boolean autoRefresh() default false;

    /**
     * 描述
     */
    String description() default "";
}