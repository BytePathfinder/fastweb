package com.company.fastweb.biz.auth.service.impl;

import com.company.fastweb.biz.user.entity.Permission;
import com.company.fastweb.biz.user.entity.Role;
import com.company.fastweb.biz.user.repository.UserRepository;
import com.company.fastweb.core.infra.security.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户详情服务实现
 * 从数据库加载用户信息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("加载用户详情: {}", username);
        
        // 检查是否是超级管理员
        if (SecurityConstants.SUPER_ADMIN_USERNAME.equals(username)) {
            // 超级管理员拥有所有权限，且权限不能被修改
            log.debug("加载超级管理员权限");
            return createSuperAdminUserDetails();
        }
        
        // 从数据库加载用户信息
        com.company.fastweb.biz.user.entity.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        
        // 检查用户状态
        if (!user.getEnabled()) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }
        
        // 转换为Spring Security的UserDetails
        return createUserDetailsFromEntity(user);
    }
    
    /**
     * 创建超级管理员用户详情
     * 超级管理员的权限是硬编码的，不能从数据库加载，确保不会被篡改
     */
    private UserDetails createSuperAdminUserDetails() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 添加超级管理员角色
        authorities.add(new SimpleGrantedAuthority(SecurityConstants.SUPER_ADMIN_ROLE));
        // 添加其他所有角色
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // 可以添加更多角色...
        
        return new User(
                SecurityConstants.SUPER_ADMIN_USERNAME,
                // 注意：实际项目中应该使用更安全的密码存储方式
                "{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG",
                true, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
    
    /**
     * 从数据库实体创建用户详情
     */
    private UserDetails createUserDetailsFromEntity(com.company.fastweb.biz.user.entity.User user) {
        // 收集用户权限
        Set<String> authorities = new HashSet<>();
        
        // 添加角色权限
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                // 添加角色
                authorities.add("ROLE_" + role.getCode().toUpperCase());
                
                // 添加角色下的权限
                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        authorities.add(permission.getCode());
                    }
                }
            }
        }
        
        // 添加用户直接权限
        if (user.getPermissions() != null) {
            for (Permission permission : user.getPermissions()) {
                authorities.add(permission.getCode());
            }
        }
        
        // 转换为GrantedAuthority
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                grantedAuthorities
        );
    }
    
    /**
     * 创建用户详情（兼容旧方法）
     */
    private UserDetails createUserDetails(String username, String password, List<String> authorities) {
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        return new User(username, password, grantedAuthorities);
    }
}