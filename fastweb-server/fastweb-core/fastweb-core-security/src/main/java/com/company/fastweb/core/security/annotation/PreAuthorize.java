package com.company.fastweb.core.security.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * 支持SpEL表达式
 *
 * @author FastWeb
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {

    /**
     * 权限表达式
     * 支持SpEL语法，例如：
     * - hasPermission('user:edit')
     * - hasRole('admin')
     * - #currentUser.deptId == #target.deptId
     */
    String value();

    /**
     * 权限描述
     */
    String description() default "";
}