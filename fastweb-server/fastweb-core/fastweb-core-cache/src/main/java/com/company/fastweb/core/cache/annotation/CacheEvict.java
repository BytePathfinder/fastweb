package com.company.fastweb.core.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存清除注解
 *
 * @author FastWeb
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvict {

    /**
     * 缓存名称
     */
    String value() default "";

    /**
     * 缓存key，支持SpEL表达式
     */
    String key() default "";

    /**
     * 是否删除所有缓存
     */
    boolean allEntries() default false;

    /**
     * 是否在方法执行前删除缓存
     */
    boolean beforeInvocation() default false;

    /**
     * 条件表达式，支持SpEL
     */
    String condition() default "";
}