package com.company.fastweb.biz.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户会话DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDTO {
    
    /**
     * 设备ID
     */
    private String deviceId;
    
    /**
     * 登录时间（毫秒时间戳）
     */
    private Long loginTime;
    
    /**
     * 最后活动时间（毫秒时间戳）
     */
    private Long lastActiveTime;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 设备类型
     */
    private String deviceType;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 浏览器
     */
    private String browser;
    
    /**
     * 是否当前会话
     */
    private Boolean current;
}