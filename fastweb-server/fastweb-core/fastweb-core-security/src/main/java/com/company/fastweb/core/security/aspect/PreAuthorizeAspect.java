package com.company.fastweb.core.security.aspect;

import com.company.fastweb.core.exception.BizException;
import com.company.fastweb.core.security.annotation.PreAuthorize;
import com.company.fastweb.core.security.expression.PermissionExpressionEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限校验切面
 *
 * @author FastWeb
 */
@Slf4j
@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class PreAuthorizeAspect {

    private final PermissionExpressionEngine expressionEngine;

    @Before("@annotation(preAuthorize)")
    public void checkPermission(JoinPoint joinPoint, PreAuthorize preAuthorize) {
        String expression = preAuthorize.value();
        if (expression == null || expression.trim().isEmpty()) {
            return;
        }

        // 构建上下文变量
        Map<String, Object> context = buildContext(joinPoint);

        // 评估权限表达式
        boolean hasPermission = expressionEngine.evaluate(expression, context);
        
        if (!hasPermission) {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            log.warn("权限校验失败: {}#{}, expression={}", className, methodName, expression);
            throw BizException.of("PERMISSION_DENIED", "权限不足，无法访问该资源");
        }
    }

    /**
     * 构建上下文变量
     */
    private Map<String, Object> buildContext(JoinPoint joinPoint) {
        Map<String, Object> context = new HashMap<>();
        
        // 获取方法参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        
        // 将方法参数添加到上下文
        for (int i = 0; i < parameters.length && i < args.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            
            // 使用参数名作为变量名
            String paramName = parameter.getName();
            context.put(paramName, arg);
            
            // 特殊处理一些常用参数名
            if ("id".equals(paramName) || "targetId".equals(paramName)) {
                context.put("targetId", arg);
            }
            if ("target".equals(paramName) || "entity".equals(paramName)) {
                context.put("target", arg);
            }
        }
        
        return context;
    }
}