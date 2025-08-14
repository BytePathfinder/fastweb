package com.company.fastweb.biz.auth.service.impl;

import com.company.fastweb.biz.auth.dto.LoginRequest;
import com.company.fastweb.biz.auth.dto.UserSessionDTO;
import com.company.fastweb.biz.auth.service.AuthService;
import com.company.fastweb.core.infra.security.jwt.JwtToken;
import com.company.fastweb.core.infra.security.jwt.JwtUtils;
import com.company.fastweb.core.infra.security.session.RedisTokenStore;
import com.company.fastweb.core.infra.security.session.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final RedisTokenStore tokenStore;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_PERMISSION_KEY = "fastweb:user:permissions:";

    @Override
    public JwtToken login(LoginRequest loginRequest, String deviceId) {
        // 验证用户名和密码
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        
        // 设置安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成JWT令牌
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtToken token = jwtUtils.generateToken(userDetails, deviceId);
        
        // 存储令牌
        tokenStore.storeToken(userDetails.getUsername(), deviceId, token);
        
        return token;
    }

    @Override
    public JwtToken refreshToken(String refreshToken, String deviceId) {
        try {
            // 验证刷新令牌
            String username = jwtUtils.getUsernameFromToken(refreshToken);
            String tokenDeviceId = jwtUtils.getDeviceIdFromToken(refreshToken);
            
            // 检查设备ID是否匹配
            if (!deviceId.equals(tokenDeviceId)) {
                throw new IllegalArgumentException("设备ID不匹配");
            }
            
            // 加载用户详情
            UserDetails userDetails = loadUserByUsername(username);
            
            // 生成新的JWT令牌
            JwtToken token = jwtUtils.generateToken(userDetails, deviceId);
            
            // 存储令牌
            tokenStore.storeToken(username, deviceId, token);
            
            return token;
        } catch (Exception e) {
            log.error("刷新令牌失败", e);
            throw new IllegalArgumentException("无效的刷新令牌");
        }
    }

    @Override
    public void logout(String username, String deviceId) {
        // 删除令牌
        tokenStore.removeToken(username, deviceId);
    }

    @Override
    public void forceLogout(String username, String deviceId) {
        // 强制下线
        tokenStore.forceLogout(username, deviceId);
    }

    @Override
    public List<UserSessionDTO> getUserSessions(String username) {
        // 获取用户所有设备的令牌
        Map<String, JwtToken> tokens = tokenStore.getAllTokens(username);
        
        // 获取当前设备ID
        String currentDeviceId = getCurrentDeviceId();
        
        // 转换为DTO
        return tokens.entrySet().stream()
                .map(entry -> {
                    String deviceId = entry.getKey();
                    
                    // 获取会话信息
                    SessionInfo sessionInfo = getSessionInfo(username, deviceId);
                    
                    return UserSessionDTO.builder()
                            .deviceId(deviceId)
                            .loginTime(sessionInfo != null ? sessionInfo.getLoginTime() : null)
                            .lastActiveTime(sessionInfo != null ? sessionInfo.getLastActiveTime() : null)
                            .ipAddress(sessionInfo != null ? sessionInfo.getIpAddress() : null)
                            .userAgent(sessionInfo != null ? sessionInfo.getUserAgent() : null)
                            .deviceType(sessionInfo != null ? sessionInfo.getDeviceType() : null)
                            .os(sessionInfo != null ? sessionInfo.getOs() : null)
                            .browser(sessionInfo != null ? sessionInfo.getBrowser() : null)
                            .current(deviceId.equals(currentDeviceId))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserPermissions(String username) {
        // 从数据库加载用户权限
        List<String> permissions = loadUserPermissionsFromDatabase(username);
        
        // 存储到Redis
        String key = USER_PERMISSION_KEY + username;
        redisTemplate.delete(key);
        if (!permissions.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, permissions.toArray());
        }
    }

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
    
    /**
     * 获取当前设备ID
     */
    private String getCurrentDeviceId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String deviceId = request.getHeader("Device-ID");
                return deviceId != null ? deviceId : "unknown";
            }
        } catch (Exception e) {
            log.warn("获取设备ID失败", e);
        }
        return "unknown";
    }
    
    /**
     * 获取会话信息
     */
    private SessionInfo getSessionInfo(String username, String deviceId) {
        try {
            String sessionKey = "fastweb:session:" + username + ":" + deviceId;
            Object sessionData = redisTemplate.opsForValue().get(sessionKey);
            if (sessionData instanceof SessionInfo) {
                return (SessionInfo) sessionData;
            }
        } catch (Exception e) {
            log.warn("获取会话信息失败", e);
        }
        return null;
    }
    
    /**
     * 从数据库加载用户权限
     */
    private List<String> loadUserPermissionsFromDatabase(String username) {
        try {
            // TODO: 实现从数据库加载用户权限的逻辑
            // 这里应该调用用户权限查询服务
            log.debug("从数据库加载用户权限: {}", username);
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("从数据库加载用户权限失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 加载用户详情
     */
    private UserDetails loadUserByUsername(String username) {
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            log.error("加载用户详情失败: {}", username, e);
            throw new IllegalArgumentException("用户不存在: " + username);
        }
    }
}