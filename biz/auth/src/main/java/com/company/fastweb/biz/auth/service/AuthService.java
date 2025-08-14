package com.company.fastweb.biz.auth.service;

import com.company.fastweb.core.infra.security.jwt.JwtToken;
import com.company.fastweb.biz.auth.dto.LoginRequest;
import com.company.fastweb.biz.auth.dto.UserSessionDTO;

import java.util.List;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @param deviceId 设备ID
     * @return JWT令牌
     */
    JwtToken login(LoginRequest loginRequest, String deviceId);
    
    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @param deviceId 设备ID
     * @return 新的JWT令牌
     */
    JwtToken refreshToken(String refreshToken, String deviceId);
    
    /**
     * 用户登出
     * @param username 用户名
     * @param deviceId 设备ID
     */
    void logout(String username, String deviceId);
    
    /**
     * 强制下线
     * @param username 用户名
     * @param deviceId 设备ID
     */
    void forceLogout(String username, String deviceId);
    
    /**
     * 获取用户会话列表
     * @param username 用户名
     * @return 会话列表
     */
    List<UserSessionDTO> getUserSessions(String username);
    
    /**
     * 更新用户权限
     * @param username 用户名
     */
    void updateUserPermissions(String username);
    
    /**
     * 获取当前登录用户名
     * @return 用户名
     */
    String getCurrentUsername();
}