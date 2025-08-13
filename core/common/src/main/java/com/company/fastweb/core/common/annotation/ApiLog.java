package com.company.fastweb.core.common.annotation;

import java.lang.annotation.*;

/**
 * API日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {

    /**
     * 操作描述
     */
    String value() default "";

    /**
     * 是否记录参数
     */
    boolean logParams() default true;

    /**
     * 是否记录返回值
     */
    boolean logResult() default true;

    /**
     * 最大参数长度
     */
    int maxParamLength() default 2000;
}