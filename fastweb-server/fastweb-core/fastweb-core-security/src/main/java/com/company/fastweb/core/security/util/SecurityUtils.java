package com.company.fastweb.core.security.util;

import cn.dev33.satoken.stp.StpUtil;
import com.company.fastweb.core.security.model.LoginUser;

/**
 * 安全工具类
 *
 * @author FastWeb
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户ID
     */
    public static Long getCurrentUserId() {
        try {
            return StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前登录用户信息
     */
    public static LoginUser getCurrentUser() {
        try {
            return (LoginUser) StpUtil.getSession().get("loginUser");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        LoginUser loginUser = getCurrentUser();
        return loginUser != null ? loginUser.getUsername() : null;
    }

    /**
     * 获取当前租户ID
     */
    public static String getCurrentTenantId() {
        LoginUser loginUser = getCurrentUser();
        return loginUser != null ? loginUser.getTenantId() : "000000";
    }

    /**
     * 获取当前部门ID
     */
    public static Long getCurrentDeptId() {
        LoginUser loginUser = getCurrentUser();
        return loginUser != null ? loginUser.getDeptId() : null;
    }

    /**
     * 判断是否已登录
     */
    public static boolean isLogin() {
        try {
            return StpUtil.isLogin();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否超级管理员
     */
    public static boolean isSuperAdmin() {
        LoginUser loginUser = getCurrentUser();
        return loginUser != null && loginUser.isSuperAdmin();
    }

    /**
     * 判断是否管理员
     */
    public static boolean isAdmin() {
        LoginUser loginUser = getCurrentUser();
        return loginUser != null && loginUser.isAdmin();
    }

    /**
     * 判断是否拥有权限
     */
    public static boolean hasPermission(String permission) {
        if (isSuperAdmin()) {
            return true;
        }
        LoginUser loginUser = getCurrentUser();
        return loginUser != null && loginUser.hasPermission(permission);
    }

    /**
     * 判断是否拥有角色
     */
    public static boolean hasRole(String roleCode) {
        if (isSuperAdmin()) {
            return true;
        }
        LoginUser loginUser = getCurrentUser();
        return loginUser != null && loginUser.hasRole(roleCode);
    }

    /**
     * 判断是否拥有任一角色
     */
    public static boolean hasAnyRole(String... roleCodes) {
        if (isSuperAdmin()) {
            return true;
        }
        LoginUser loginUser = getCurrentUser();
        return loginUser != null && loginUser.hasAnyRole(roleCodes);
    }

    /**
     * 判断是否拥有所有角色
     */
    public static boolean hasAllRoles(String... roleCodes) {
        if (isSuperAdmin()) {
            return true;
        }
        LoginUser loginUser = getCurrentUser();
        return loginUser != null && loginUser.hasAllRoles(roleCodes);
    }

    /**
     * 登录
     */
    public static void login(LoginUser loginUser) {
        StpUtil.login(loginUser.getUserId());
        StpUtil.getSession().set("loginUser", loginUser);
    }

    /**
     * 登出
     */
    public static void logout() {
        StpUtil.logout();
    }

    /**
     * 踢下线
     */
    public static void kickout(Long userId) {
        StpUtil.kickout(userId);
    }

    /**
     * 获取Token
     */
    public static String getToken() {
        try {
            return StpUtil.getTokenValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 刷新Token
     */
    public static void refreshToken() {
        StpUtil.renewTimeout(StpUtil.getTokenTimeout());
    }
}