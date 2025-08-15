package com.company.fastweb.core.data.datasource.annotation;

import java.lang.annotation.*;

/**
 * 数据源切换注解
 * 
 * 可以标注在方法或类上，用于指定使用的数据源
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {
    
    /**
     * 数据源名称
     * 
     * @return 数据源名称
     */
    String value() default "";
}