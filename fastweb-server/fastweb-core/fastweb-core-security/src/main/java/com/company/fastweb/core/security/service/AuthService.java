package com.company.fastweb.core.security.service;

import com.company.fastweb.core.security.model.dto.LoginDTO;
import com.company.fastweb.core.security.model.dto.UserInfoDTO;

/**
 * 认证服务接口
 *
 * @author FastWeb
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginDTO login(LoginDTO loginDTO);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    UserInfoDTO getCurrentUserInfo();

    /**
     * 刷新令牌
     */
    void refreshToken();

    /**
     * 踢下线
     *
     * @param userId 用户ID
     */
    void kickout(Long userId);

    /**
     * 检查登录状态
     *
     * @return 是否已登录
     */
    boolean isLogin();

    /**
     * 验证用户凭证
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否验证成功
     */
    boolean validateCredentials(String username, String password);

    /**
     * 获取用户权限信息
     *
     * @param userId 用户ID
     * @return 用户权限信息
     */
    UserInfoDTO getUserPermissions(Long userId);
}