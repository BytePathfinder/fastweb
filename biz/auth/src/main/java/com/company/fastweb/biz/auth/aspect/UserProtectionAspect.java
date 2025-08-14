package com.company.fastweb.biz.auth.aspect;

import com.company.fastweb.biz.auth.service.UserProtectionService;
import com.company.fastweb.core.infra.security.exception.AdminProtectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户保护切面
 * 用于拦截用户信息修改操作，保护特殊用户（如超级管理员）的信息不被非法修改
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserProtectionAspect {

    private final UserProtectionService userProtectionService;

    /**
     * 拦截用户信息更新方法
     */
    @Around("execution(* com.company.fastweb..*.service..*.updateUser*(..)) && args(userId, updateFields, ..)")
    public Object aroundUserUpdate(ProceedingJoinPoint joinPoint, Long userId, Map<String, Object> updateFields) throws Throwable {
        log.debug("拦截用户信息更新: userId={}, fields={}", userId, updateFields);
        
        // 检查是否允许修改
        if (!userProtectionService.isUserUpdateAllowed(userId, updateFields)) {
            log.warn("用户信息更新被拒绝: userId={}, fields={}", userId, updateFields);
            throw new AdminProtectionException("不允许修改受保护的用户信息");
        }
        
        // 允许修改，继续执行原方法
        return joinPoint.proceed();
    }
    
    /**
     * 拦截用户角色更新方法
     */
    @Around("execution(* com.company.fastweb..*.service..*.updateUserRoles*(..)) && args(userId, roleIds, ..)")
    public Object aroundRoleUpdate(ProceedingJoinPoint joinPoint, Long userId, List<Long> roleIds) throws Throwable {
        log.debug("拦截用户角色更新: userId={}, roleIds={}", userId, roleIds);
        
        // 检查是否允许修改
        if (!userProtectionService.isRoleUpdateAllowed(userId, roleIds)) {
            log.warn("用户角色更新被拒绝: userId={}, roleIds={}", userId, roleIds);
            throw new AdminProtectionException("不允许修改受保护的用户角色");
        }
        
        // 允许修改，继续执行原方法
        return joinPoint.proceed();
    }
    
    /**
     * 拦截用户权限更新方法
     */
    @Around("execution(* com.company.fastweb..*.service..*.updateUserPermissions*(..)) && args(userId, permissionIds, ..)")
    public Object aroundPermissionUpdate(ProceedingJoinPoint joinPoint, Long userId, List<Long> permissionIds) throws Throwable {
        log.debug("拦截用户权限更新: userId={}, permissionIds={}", userId, permissionIds);
        
        // 检查是否允许修改
        if (!userProtectionService.isPermissionUpdateAllowed(userId, permissionIds)) {
            log.warn("用户权限更新被拒绝: userId={}, permissionIds={}", userId, permissionIds);
            throw new AdminProtectionException("不允许修改受保护的用户权限");
        }
        
        // 允许修改，继续执行原方法
        return joinPoint.proceed();
    }
    
    /**
     * 拦截用户删除方法
     */
    @Around("execution(* com.company.fastweb..*.service..*.deleteUser*(..)) && args(userId, ..)")
    public Object aroundUserDelete(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
        log.debug("拦截用户删除: userId={}", userId);
        
        // 检查是否允许删除
        if (!userProtectionService.isUserDeleteAllowed(userId)) {
            log.warn("用户删除被拒绝: userId={}", userId);
            throw new AdminProtectionException("不允许删除受保护的用户");
        }
        
        // 允许删除，继续执行原方法
        return joinPoint.proceed();
    }
    
    /**
     * 拦截用户禁用方法
     */
    @Around("execution(* com.company.fastweb..*.service..*.disableUser*(..)) && args(userId, ..)")
    public Object aroundUserDisable(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
        log.debug("拦截用户禁用: userId={}", userId);
        
        // 检查是否允许禁用
        if (!userProtectionService.isUserDisableAllowed(userId)) {
            log.warn("用户禁用被拒绝: userId={}", userId);
            throw new AdminProtectionException("不允许禁用受保护的用户");
        }
        
        // 允许禁用，继续执行原方法
        return joinPoint.proceed();
    }
    
    /**
     * 拦截用户锁定方法
     */
    @Around("execution(* com.company.fastweb..*.service..*.lockUser*(..)) && args(userId, ..)")
    public Object aroundUserLock(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
        log.debug("拦截用户锁定: userId={}", userId);
        
        // 检查是否允许锁定
        if (!userProtectionService.isUserLockAllowed(userId)) {
            log.warn("用户锁定被拒绝: userId={}", userId);
            throw new AdminProtectionException("不允许锁定受保护的用户");
        }
        
        // 允许锁定，继续执行原方法
        return joinPoint.proceed();
    }
}