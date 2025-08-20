package com.company.fastweb.core.security.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录传输对象
 *
 * @author FastWeb
 */
@Data
public class LoginDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 访问令牌
     */
    private String token;
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
    
    /**
     * 令牌过期时间
     */
    private LocalDateTime tokenExpireTime;
    
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
     * 浏览器类型
     */
    private String browser;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 验证码
     */
    private String captcha;
    
    /**
     * 验证码键
     */
    private String captchaKey;
    
    /**
     * 记住我
     */
    private Boolean rememberMe;
}