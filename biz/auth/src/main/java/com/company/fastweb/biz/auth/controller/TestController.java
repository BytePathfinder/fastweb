package com.company.fastweb.biz.auth.controller;

import com.company.fastweb.core.infra.security.session.RedisTokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于测试认证和权限控制功能
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final RedisTokenStore tokenStore;

    /**
     * 公开接口，无需认证
     */
    @GetMapping("/public")
    public Map<String, Object> publicEndpoint() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个公开接口，无需认证");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 需要认证的接口
     */
    @GetMapping("/authenticated")
    public Map<String, Object> authenticatedEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个需要认证的接口");
        result.put("username", auth.getName());
        result.put("authorities", auth.getAuthorities());
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 需要管理员权限的接口
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Map<String, Object> adminEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个需要管理员权限的接口");
        result.put("username", auth.getName());
        result.put("authorities", auth.getAuthorities());
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 需要超级管理员权限的接口
     */
    @GetMapping("/super-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Map<String, Object> superAdminEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个需要超级管理员权限的接口");
        result.put("username", auth.getName());
        result.put("authorities", auth.getAuthorities());
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 需要特定权限的接口
     */
    @GetMapping("/user-management")
    @PreAuthorize("hasAuthority('SYSTEM:USER:LIST')")
    public Map<String, Object> userManagementEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个需要用户管理权限的接口");
        result.put("username", auth.getName());
        result.put("authorities", auth.getAuthorities());
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 获取当前用户会话信息
     */
    @GetMapping("/session")
    public Map<String, Object> getSessionInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Map<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("authorities", auth.getAuthorities());
        
        // 获取Redis中的会话信息
        try {
            var sessions = tokenStore.getUserSessions(username);
            result.put("sessionCount", sessions.size());
            result.put("sessions", sessions);
        } catch (Exception e) {
            result.put("sessionError", e.getMessage());
        }
        
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}