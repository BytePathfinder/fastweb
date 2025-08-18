package com.company.fastweb.core.security.expression;

import com.company.fastweb.core.security.model.LoginUser;
import com.company.fastweb.core.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限表达式引擎
 * 基于SpEL实现动态权限控制
 *
 * @author FastWeb
 */
@Slf4j
@Component
public class PermissionExpressionEngine {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final Map<String, Expression> expressionCache = new ConcurrentHashMap<>();

    /**
     * 评估权限表达式
     *
     * @param expression 权限表达式
     * @param context    上下文变量
     * @return 是否有权限
     */
    public boolean evaluate(String expression, Map<String, Object> context) {
        if (expression == null || expression.trim().isEmpty()) {
            return true;
        }

        try {
            // 超级管理员直接通过
            if (SecurityUtils.isSuperAdmin()) {
                return true;
            }

            // 从缓存获取或编译表达式
            Expression expr = expressionCache.computeIfAbsent(expression, 
                key -> parser.parseExpression(key));

            // 创建评估上下文
            StandardEvaluationContext evalContext = createEvaluationContext();
            
            // 设置上下文变量
            if (context != null) {
                context.forEach(evalContext::setVariable);
            }

            // 评估表达式
            Boolean result = expr.getValue(evalContext, Boolean.class);
            return Boolean.TRUE.equals(result);

        } catch (Exception e) {
            log.error("权限表达式评估失败: expression={}, error={}", expression, e.getMessage());
            return false;
        }
    }

    /**
     * 创建评估上下文
     */
    private StandardEvaluationContext createEvaluationContext() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        
        // 设置当前用户信息
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            context.setVariable("currentUser", currentUser);
            context.setVariable("userId", currentUser.getUserId());
            context.setVariable("username", currentUser.getUsername());
            context.setVariable("deptId", currentUser.getDeptId());
            context.setVariable("roleIds", currentUser.getRoleIds());
            context.setVariable("roleCodes", currentUser.getRoleCodes());
            context.setVariable("permissions", currentUser.getPermissions());
            context.setVariable("tenantId", currentUser.getTenantId());
            context.setVariable("level", currentUser.getLevel());
        }

        // 设置时间相关变量
        LocalDateTime now = LocalDateTime.now();
        context.setVariable("now", now);
        context.setVariable("currentTime", now);
        
        // 设置常用方法
        context.setVariable("hasPermission", new HasPermissionFunction());
        context.setVariable("hasRole", new HasRoleFunction());
        context.setVariable("hasAnyRole", new HasAnyRoleFunction());
        
        return context;
    }

    /**
     * 清除表达式缓存
     */
    public void clearCache() {
        expressionCache.clear();
        log.info("权限表达式缓存已清除");
    }

    /**
     * 获取缓存大小
     */
    public int getCacheSize() {
        return expressionCache.size();
    }

    /**
     * 权限检查函数
     */
    public static class HasPermissionFunction {
        public boolean check(String permission) {
            return SecurityUtils.hasPermission(permission);
        }
    }

    /**
     * 角色检查函数
     */
    public static class HasRoleFunction {
        public boolean check(String roleCode) {
            return SecurityUtils.hasRole(roleCode);
        }
    }

    /**
     * 任一角色检查函数
     */
    public static class HasAnyRoleFunction {
        public boolean check(String... roleCodes) {
            return SecurityUtils.hasAnyRole(roleCodes);
        }
    }
}