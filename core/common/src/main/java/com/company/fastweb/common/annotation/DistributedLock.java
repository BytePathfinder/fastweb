package com.company.fastweb.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 锁的key前缀
     */
    String prefix() default "lock:";

    /**
     * 锁的key，支持SpEL表达式
     */
    String key();

    /**
     * 等待时间（秒）
     */
    long waitTime() default 5;

    /**
     * 过期时间（秒）
     */
    long expireTime() default 30;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否自动续期
     */
    boolean autoRenew() default true;
}