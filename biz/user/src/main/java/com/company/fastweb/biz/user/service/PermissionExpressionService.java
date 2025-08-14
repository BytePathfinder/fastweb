package com.company.fastweb.biz.user.service;

import com.company.fastweb.biz.user.entity.PermissionExpression;
import com.company.fastweb.biz.user.repository.PermissionExpressionRepository;
import com.company.fastweb.core.infra.security.expression.PermissionExpressionEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 权限表达式服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionExpressionService {
    
    private final PermissionExpressionRepository repository;
    private final PermissionExpressionEngine expressionEngine;
    
    /**
     * 根据权限标识获取表达式
     * @param permissionCode 权限标识
     * @return 权限表达式
     */
    @Cacheable(value = "permission_expressions", key = "#permissionCode")
    public Optional<PermissionExpression> getByPermissionCode(String permissionCode) {
        return repository.findByPermissionCode(permissionCode);
    }
    
    /**
     * 创建权限表达式
     * @param expression 权限表达式
     * @return 创建的表达式
     */
    @Transactional
    public PermissionExpression createExpression(PermissionExpression expression) {
        // 验证表达式语法
        if (!expressionEngine.validateExpression(expression.getExpressionContent())) {
            throw new IllegalArgumentException("权限表达式语法错误: " + expression.getExpressionContent());
        }
        
        PermissionExpression saved = repository.save(expression);
        
        // 清理相关缓存
        clearExpressionCache();
        
        log.info("创建权限表达式成功: {}", saved.getExpressionName());
        return saved;
    }
    
    /**
     * 更新权限表达式
     * @param expression 权限表达式
     * @return 更新后的表达式
     */
    @Transactional
    public PermissionExpression updateExpression(PermissionExpression expression) {
        // 验证表达式语法
        if (!expressionEngine.validateExpression(expression.getExpressionContent())) {
            throw new IllegalArgumentException("权限表达式语法错误: " + expression.getExpressionContent());
        }
        
        PermissionExpression updated = repository.save(expression);
        
        // 清理相关缓存
        clearExpressionCache();
        
        log.info("更新权限表达式成功: {}", updated.getExpressionName());
        return updated;
    }
    
    /**
     * 删除权限表达式
     * @param id 表达式ID
     */
    @Transactional
    public void deleteExpression(Long id) {
        repository.deleteById(id);
        clearExpressionCache();
        log.info("删除权限表达式成功: {}", id);
    }
    
    /**
     * 获取所有启用的表达式
     * @return 表达式列表
     */
    public List<PermissionExpression> getAllEnabled() {
        return repository.findAllEnabled();
    }
    
    /**
     * 清理表达式缓存
     */
    @CacheEvict(value = "permission_expressions", allEntries = true)
    public void clearExpressionCache() {
        expressionEngine.clearCache();
        log.info("清理权限表达式缓存完成");
    }
}