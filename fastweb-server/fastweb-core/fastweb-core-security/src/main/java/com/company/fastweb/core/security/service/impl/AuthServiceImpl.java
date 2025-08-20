package com.company.fastweb.core.security.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.company.fastweb.core.exception.BizException;
import com.company.fastweb.core.security.model.LoginUser;
import com.company.fastweb.core.security.model.dto.LoginDTO;
import com.company.fastweb.core.security.model.dto.UserInfoDTO;
import com.company.fastweb.core.security.service.AuthService;
import com.company.fastweb.core.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 认证服务实现类
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginDTO login(LoginDTO loginDTO) {
        // 验证用户凭证
        if (!validateCredentials(loginDTO.getUsername(), loginDTO.getPassword())) {
            throw new BizException("AUTH_FAILED", "用户名或密码错误");
        }

        // 构建登录用户信息（实际项目中应从数据库查询）
        LoginUser loginUser = buildLoginUser(loginDTO);

        // 执行登录
        SecurityUtils.login(loginUser);

        // 返回登录结果
        LoginDTO result = new LoginDTO();
        result.setUserId(loginUser.getUserId());
        result.setUsername(loginUser.getUsername());
        result.setToken(SecurityUtils.getToken());
        result.setLoginTime(loginUser.getLoginTime());
        
        log.info("用户登录成功: userId={}, username={}", loginUser.getUserId(), loginUser.getUsername());
        return result;
    }

    @Override
    public void logout() {
        Long userId = SecurityUtils.getCurrentUserId();
        SecurityUtils.logout();
        log.info("用户登出成功: userId={}", userId);
    }

    @Override
    public UserInfoDTO getCurrentUserInfo() {
        LoginUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("NOT_LOGIN", "用户未登录");
        }

        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setUserId(currentUser.getUserId());
        userInfo.setUsername(currentUser.getUsername());
        userInfo.setNickname(currentUser.getNickname());
        userInfo.setEmail(currentUser.getEmail());
        userInfo.setMobile(currentUser.getMobile());
        userInfo.setAvatar(currentUser.getAvatar());
        userInfo.setDeptId(currentUser.getDeptId());
        userInfo.setDeptName(currentUser.getDeptName());
        userInfo.setRoleIds(currentUser.getRoleIds());
        userInfo.setRoleCodes(currentUser.getRoleCodes());
        userInfo.setPermissions(currentUser.getPermissions());
        userInfo.setTenantId(currentUser.getTenantId());
        userInfo.setLoginTime(currentUser.getLoginTime());
        userInfo.setLoginIp(currentUser.getLoginIp());
        
        return userInfo;
    }

    @Override
    public void refreshToken() {
        if (!SecurityUtils.isLogin()) {
            throw new BizException("NOT_LOGIN", "用户未登录");
        }
        SecurityUtils.refreshToken();
        log.info("令牌刷新成功: userId={}", SecurityUtils.getCurrentUserId());
    }

    @Override
    public void kickout(Long userId) {
        SecurityUtils.kickout(userId);
        log.info("用户被踢下线: userId={}", userId);
    }

    @Override
    public boolean isLogin() {
        return SecurityUtils.isLogin();
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        // 实际项目中应该查询数据库验证用户凭证
        // 这里简单模拟验证逻辑
        if (username == null || password == null) {
            return false;
        }
        
        // 模拟验证：用户名不为空且密码长度大于6
        return username.trim().length() > 0 && password.length() >= 6;
    }

    @Override
    public UserInfoDTO getUserPermissions(Long userId) {
        // 实际项目中应该查询数据库获取用户权限信息
        // 这里返回当前用户信息
        if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId().equals(userId)) {
            return getCurrentUserInfo();
        }
        
        throw new BizException("USER_NOT_FOUND", "用户不存在: " + userId);
    }

    /**
     * 构建登录用户信息
     */
    private LoginUser buildLoginUser(LoginDTO loginDTO) {
        // 实际项目中应该从数据库查询用户完整信息
        return LoginUser.builder()
                .userId(1L) // 模拟用户ID
                .username(loginDTO.getUsername())
                .nickname("测试用户")
                .email("test@example.com")
                .loginTime(LocalDateTime.now())
                .loginIp(loginDTO.getLoginIp())
                .loginDevice(loginDTO.getLoginDevice())
                .tenantId("000000")
                .userType(1)
                .build();
    }
}