package com.company.fastweb.core.security.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录响应视图对象
 *
 * @author FastWeb
 */
@Data
public class LoginVO {
    
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
     * 头像
     */
    private String avatar;
    
    /**
     * 访问令牌
     */
    private String token;
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
    
    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";
    
    /**
     * 令牌过期时间（秒）
     */
    private Long expiresIn;
    
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
     * 登录地点
     */
    private String loginLocation;
    
    /**
     * 是否首次登录
     */
    private Boolean isFirstLogin;
    
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 上次登录IP
     */
    private String lastLoginIp;
}