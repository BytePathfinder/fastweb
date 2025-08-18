package com.company.fastweb.core.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 登录用户信息
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 岗位ID列表
     */
    private List<Long> postIds;

    /**
     * 角色ID列表
     */
    private List<Long> roleIds;

    /**
     * 角色编码列表
     */
    private List<String> roleCodes;

    /**
     * 权限标识列表
     */
    private Set<String> permissions;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 用户类型：1-系统用户，2-会员用户
     */
    private Integer userType;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录设备
     */
    private String loginDevice;

    /**
     * 用户级别（用于权限控制）
     */
    private Integer level;

    /**
     * 是否超级管理员
     */
    public boolean isSuperAdmin() {
        return roleCodes != null && roleCodes.contains("super_admin");
    }

    /**
     * 是否管理员
     */
    public boolean isAdmin() {
        return roleCodes != null && (roleCodes.contains("super_admin") || roleCodes.contains("admin"));
    }

    /**
     * 是否拥有权限
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }

    /**
     * 是否拥有角色
     */
    public boolean hasRole(String roleCode) {
        return roleCodes != null && roleCodes.contains(roleCode);
    }

    /**
     * 是否拥有任一角色
     */
    public boolean hasAnyRole(String... roleCodes) {
        if (this.roleCodes == null || roleCodes == null) {
            return false;
        }
        for (String roleCode : roleCodes) {
            if (this.roleCodes.contains(roleCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否拥有所有角色
     */
    public boolean hasAllRoles(String... roleCodes) {
        if (this.roleCodes == null || roleCodes == null) {
            return false;
        }
        for (String roleCode : roleCodes) {
            if (!this.roleCodes.contains(roleCode)) {
                return false;
            }
        }
        return true;
    }
}