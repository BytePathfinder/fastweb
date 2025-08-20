package com.company.fastweb.core.security.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户信息传输对象
 *
 * @author FastWeb
 */
@Data
public class UserInfoDTO {
    
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
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDateTime birthday;
    
    /**
     * 部门ID
     */
    private Long deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 岗位ID集合
     */
    private Set<Long> postIds;
    
    /**
     * 岗位名称集合
     */
    private Set<String> postNames;
    
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
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 用户类型（1-系统用户，2-普通用户）
     */
    private Integer userType;
    
    /**
     * 用户状态（0-正常，1-停用）
     */
    private Integer status;
    
    /**
     * 是否超级管理员
     */
    private Boolean isSuperAdmin;
    
    /**
     * 是否管理员
     */
    private Boolean isAdmin;
    
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    
    /**
     * 登录IP
     */
    private String loginIp;
    
    /**
     * 登录设备
     */
    private String loginDevice;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}