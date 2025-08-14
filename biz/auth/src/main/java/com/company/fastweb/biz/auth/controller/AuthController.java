package com.company.fastweb.biz.auth.controller;

import com.company.fastweb.biz.auth.dto.LoginRequest;
import com.company.fastweb.biz.auth.dto.UserSessionDTO;
import com.company.fastweb.biz.auth.service.AuthService;
import com.company.fastweb.core.infra.security.jwt.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // 获取设备ID，如果请求头中没有，则生成一个新的
        String deviceId = request.getHeader("Device-ID");
        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = UUID.randomUUID().toString();
        }
        
        // 调用认证服务进行登录
        JwtToken token = authService.login(loginRequest, deviceId);
        
        return ResponseEntity.ok(token);
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtToken> refreshToken(@RequestParam String refreshToken, HttpServletRequest request) {
        // 获取设备ID
        String deviceId = request.getHeader("Device-ID");
        if (deviceId == null || deviceId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // 调用认证服务刷新令牌
        JwtToken token = authService.refreshToken(refreshToken, deviceId);
        
        return ResponseEntity.ok(token);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        // 获取设备ID
        String deviceId = request.getHeader("Device-ID");
        if (deviceId == null || deviceId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // 获取当前用户名
        String username = authService.getCurrentUsername();
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 调用认证服务进行登出
        authService.logout(username, deviceId);
        
        return ResponseEntity.ok().build();
    }

    /**
     * 获取用户会话列表
     */
    @GetMapping("/sessions")
    public ResponseEntity<List<UserSessionDTO>> getUserSessions() {
        // 获取当前用户名
        String username = authService.getCurrentUsername();
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 调用认证服务获取用户会话列表
        List<UserSessionDTO> sessions = authService.getUserSessions(username);
        
        return ResponseEntity.ok(sessions);
    }

    /**
     * 强制下线
     */
    @PostMapping("/sessions/{deviceId}/logout")
    public ResponseEntity<Void> forceLogout(@PathVariable String deviceId) {
        // 获取当前用户名
        String username = authService.getCurrentUsername();
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 调用认证服务强制下线
        authService.forceLogout(username, deviceId);
        
        return ResponseEntity.ok().build();
    }
}