package com.company.fastweb.core.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解
 * 借鉴RuoYi的数据权限设计，支持部门、用户、自定义等权限控制
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
    
    /**
     * 部门表的别名
     */
    String deptAlias() default "";
    
    /**
     * 用户表的别名
     */
    String userAlias() default "";
    
    /**
     * 权限字符（用于权限检查）
     */
    String permission() default "";
    
    /**
     * 数据权限类型
     */
    DataScopeType type() default DataScopeType.ALL;
    
    enum DataScopeType {
        /** 全部数据权限 */
        ALL,
        /** 自定义数据权限 */
        CUSTOM,
        /** 部门数据权限 */
        DEPT,
        /** 部门及以下数据权限 */
        DEPT_AND_CHILD,
        /** 仅本人数据权限 */
        SELF
    }
}