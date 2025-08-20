package com.company.fastweb.core.security.model.vo;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * 权限信息响应视图对象
 *
 * @author FastWeb
 */
@Data
public class PermissionInfoVO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 角色名称集合
     */
    private Set<String> roleNames;
    
    /**
     * 权限标识集合
     */
    private Set<String> permissions;
    
    /**
     * 菜单权限集合
     */
    private Set<String> menuPermissions;
    
    /**
     * 按钮权限集合
     */
    private Set<String> buttonPermissions;
    
    /**
     * 数据权限范围描述
     */
    private String dataScopeDesc;
    
    /**
     * 是否超级管理员
     */
    private Boolean isSuperAdmin;
    
    /**
     * 是否管理员
     */
    private Boolean isAdmin;
    
    /**
     * 权限检查结果
     */
    private Map<String, Boolean> permissionResults;
    
    /**
     * 角色检查结果
     */
    private Map<String, Boolean> roleResults;
    
    /**
     * 权限缓存信息
     */
    private Map<String, Object> cacheInfo;
}