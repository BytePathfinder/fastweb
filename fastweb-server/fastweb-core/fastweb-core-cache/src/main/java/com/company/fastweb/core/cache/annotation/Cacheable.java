package com.company.fastweb.core.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存注解
 *
 * @author FastWeb
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {

    /**
     * 缓存名称
     */
    String value() default "";

    /**
     * 缓存key，支持SpEL表达式
     */
    String key() default "";

    /**
     * 缓存过期时间（秒）
     */
    long expire() default 3600;

    /**
     * 条件表达式，支持SpEL
     */
    String condition() default "";

    /**
     * 排除条件表达式，支持SpEL
     */
    String unless() default "";

    /**
     * 是否允许空值缓存
     */
    boolean allowNullValues() default false;
}