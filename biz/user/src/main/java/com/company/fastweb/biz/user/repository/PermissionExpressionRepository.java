package com.company.fastweb.biz.user.repository;

import com.company.fastweb.biz.user.entity.PermissionExpression;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限表达式Repository
 */
@Repository
public interface PermissionExpressionRepository {
    
    /**
     * 根据权限ID查找表达式
     * @param permissionId 权限ID
     * @return 权限表达式
     */
    Optional<PermissionExpression> findByPermissionId(Long permissionId);
    
    /**
     * 根据权限标识查找表达式
     * @param permissionCode 权限标识
     * @return 权限表达式
     */
    Optional<PermissionExpression> findByPermissionCode(String permissionCode);
    
    /**
     * 查找所有启用的表达式
     * @return 表达式列表
     */
    List<PermissionExpression> findAllEnabled();
    
    /**
     * 保存权限表达式
     * @param expression 权限表达式
     * @return 保存后的实体
     */
    PermissionExpression save(PermissionExpression expression);
    
    /**
     * 删除权限表达式
     * @param id 表达式ID
     */
    void deleteById(Long id);
}