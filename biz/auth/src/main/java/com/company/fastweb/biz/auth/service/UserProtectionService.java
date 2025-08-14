package com.company.fastweb.biz.auth.service;

import com.company.fastweb.core.infra.security.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户信息保护服务
 * 用于保护特殊用户（如超级管理员）的信息不被非法修改
 */
@Slf4j
@Service
public class UserProtectionService {

    /**
     * 检查是否允许修改用户信息
     * @param userId 用户ID
     * @param updateFields 要修改的字段
     * @return 是否允许修改
     */
    public boolean isUserUpdateAllowed(Long userId, Map<String, Object> updateFields) {
        // 检查是否是超级管理员
        if (SecurityConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            log.warn("尝试修改超级管理员信息: {}", updateFields);
            
            // 超级管理员的以下字段不允许修改
            List<String> protectedFields = List.of(
                    "username", // 用户名不允许修改
                    "status",   // 状态不允许修改（不能禁用超级管理员）
                    "roles",    // 角色不允许修改
                    "permissions", // 权限不允许修改
                    "enabled",  // 启用状态不允许修改
                    "accountNonLocked", // 锁定状态不允许修改
                    "accountNonExpired", // 过期状态不允许修改
                    "credentialsNonExpired" // 凭证过期状态不允许修改
            );
            
            // 检查是否包含受保护的字段
            for (String field : protectedFields) {
                if (updateFields.containsKey(field)) {
                    log.error("尝试修改超级管理员受保护字段: {}", field);
                    return false;
                }
            }
            
            // 只允许修改非敏感信息
            log.info("允许修改超级管理员非敏感信息: {}", updateFields);
            return true;
        }
        
        // 非超级管理员可以正常修改
        return true;
    }
    
    /**
     * 检查是否允许修改用户角色
     * @param userId 用户ID
     * @param roleIds 要设置的角色ID列表
     * @return 是否允许修改
     */
    public boolean isRoleUpdateAllowed(Long userId, List<Long> roleIds) {
        // 超级管理员的角色不允许修改
        if (SecurityConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            log.error("尝试修改超级管理员角色: {}", roleIds);
            return false;
        }
        return true;
    }
    
    /**
     * 检查是否允许修改用户权限
     * @param userId 用户ID
     * @param permissionIds 要设置的权限ID列表
     * @return 是否允许修改
     */
    public boolean isPermissionUpdateAllowed(Long userId, List<Long> permissionIds) {
        // 超级管理员的权限不允许修改
        if (SecurityConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            log.error("尝试修改超级管理员权限: {}", permissionIds);
            return false;
        }
        return true;
    }
    
    /**
     * 检查是否允许删除用户
     * @param userId 用户ID
     * @return 是否允许删除
     */
    public boolean isUserDeleteAllowed(Long userId) {
        // 超级管理员不允许删除
        if (SecurityConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            log.error("尝试删除超级管理员");
            return false;
        }
        return true;
    }
    
    /**
     * 检查是否允许禁用用户
     * @param userId 用户ID
     * @return 是否允许禁用
     */
    public boolean isUserDisableAllowed(Long userId) {
        // 超级管理员不允许禁用
        if (SecurityConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            log.error("尝试禁用超级管理员");
            return false;
        }
        return true;
    }
    
    /**
     * 检查是否允许锁定用户
     * @param userId 用户ID
     * @return 是否允许锁定
     */
    public boolean isUserLockAllowed(Long userId) {
        // 超级管理员不允许锁定
        if (SecurityConstants.SUPER_ADMIN_USER_ID.equals(userId)) {
            log.error("尝试锁定超级管理员");
            return false;
        }
        return true;
    }
}