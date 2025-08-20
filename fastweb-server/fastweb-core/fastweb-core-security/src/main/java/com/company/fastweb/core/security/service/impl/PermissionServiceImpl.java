package com.company.fastweb.core.security.service.impl;

import com.company.fastweb.core.exception.BizException;
import com.company.fastweb.core.security.expression.PermissionExpressionEngine;
import com.company.fastweb.core.security.model.LoginUser;
import com.company.fastweb.core.security.model.dto.PermissionInfoDTO;
import com.company.fastweb.core.security.model.dto.UserInfoDTO;
import com.company.fastweb.core.security.service.PermissionService;
import com.company.fastweb.core.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionExpressionEngine permissionExpressionEngine;

    @Override
    public boolean hasPermission(String permission) {
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        // 超级管理员拥有所有权限
        if (currentUser.isSuperAdmin()) {
            return true;
        }
        
        Set<String> userPermissions = currentUser.getPermissions();
        return userPermissions != null && userPermissions.contains(permission);
    }

    @Override
    public Map<String, Boolean> hasPermissions(List<String> permissions) {
        return permissions.stream()
                .collect(Collectors.toMap(
                        permission -> permission,
                        this::hasPermission
                ));
    }

    @Override
    public PermissionInfoDTO getCurrentUserPermissions() {
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("NOT_LOGIN", "用户未登录");
        }

        PermissionInfoDTO permissionInfo = new PermissionInfoDTO();
        permissionInfo.setUserId(currentUser.getUserId());
        permissionInfo.setUsername(currentUser.getUsername());
        permissionInfo.setRoleIds(currentUser.getRoleIds());
        permissionInfo.setRoleCodes(currentUser.getRoleCodes());
        permissionInfo.setPermissions(currentUser.getPermissions());
        permissionInfo.setIsSuperAdmin(currentUser.isSuperAdmin());
        permissionInfo.setIsAdmin(currentUser.isAdmin());
        permissionInfo.setTenantId(currentUser.getTenantId());
        
        return permissionInfo;
    }

    @Override
    public boolean hasRole(String roleCode) {
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        // 超级管理员拥有所有角色
        if (currentUser.isSuperAdmin()) {
            return true;
        }
        
        Set<String> userRoles = currentUser.getRoleCodes();
        return userRoles != null && userRoles.contains(roleCode);
    }

    @Override
    public boolean hasAnyRole(List<String> roleCodes) {
        if (roleCodes == null || roleCodes.isEmpty()) {
            return false;
        }
        
        return roleCodes.stream().anyMatch(this::hasRole);
    }

    @Override
    public void clearPermissionCache(Long userId) {
        // 实际项目中应该清除Redis中的权限缓存
        // 这里简单记录日志
        log.info("清除用户权限缓存: userId={}", userId);
        
        // 如果是当前用户，强制重新登录
        if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId().equals(userId)) {
            SecurityUtils.logout();
            log.info("当前用户权限缓存已清除，需要重新登录: userId={}", userId);
        }
    }

    @Override
    public Map<String, Object> getPermissionCacheInfo(Long userId) {
        // 实际项目中应该从Redis获取缓存信息
        // 这里返回模拟数据
        return Map.of(
                "userId", userId,
                "cacheExists", SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId().equals(userId),
                "cacheTime", System.currentTimeMillis(),
                "ttl", 7200 // 2小时
        );
    }

    @Override
    public boolean evaluateExpression(String expression, Map<String, Object> context) {
        try {
            return permissionExpressionEngine.evaluate(expression, context);
        } catch (Exception e) {
            log.error("权限表达式评估失败: expression={}, context={}", expression, context, e);
            return false;
        }
    }

    @Override
    public boolean evaluateExpression(String expression) {
        return evaluateExpression(expression, Map.of());
    }
}