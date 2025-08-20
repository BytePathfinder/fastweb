package com.company.fastweb.core.security.model.dto;

import lombok.Data;

import java.util.Set;

/**
 * 权限信息传输对象
 *
 * @author FastWeb
 */
@Data
public class PermissionInfoDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 角色ID集合
     */
    private Set<Long> roleIds;
    
    /**
     * 角色编码集合
     */
    private Set<String> roleCodes;
    
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
     * 数据权限范围（1-全部数据，2-自定义数据，3-本部门数据，4-本部门及以下数据，5-仅本人数据）
     */
    private Integer dataScope;
    
    /**
     * 数据权限部门ID集合
     */
    private Set<Long> dataScopeDeptIds;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 是否超级管理员
     */
    private Boolean isSuperAdmin;
    
    /**
     * 是否管理员
     */
    private Boolean isAdmin;
    
    /**
     * 权限缓存时间（秒）
     */
    private Long cacheTime;
    
    /**
     * 权限缓存TTL（秒）
     */
    private Long cacheTtl;
}