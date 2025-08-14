package com.company.fastweb.biz.user.mapper;

import com.company.fastweb.biz.user.entity.PermissionExpression;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限表达式Mapper
 */
@Mapper
public interface PermissionExpressionMapper {
    
    /**
     * 根据权限ID查询表达式
     */
    PermissionExpression selectByPermissionId(@Param("permissionId") Long permissionId);
    
    /**
     * 根据权限标识查询表达式
     */
    PermissionExpression selectByPermissionCode(@Param("permissionCode") String permissionCode);
    
    /**
     * 查询所有启用的表达式
     */
    List<PermissionExpression> selectAllEnabled();
    
    /**
     * 插入表达式
     */
    int insert(PermissionExpression expression);
    
    /**
     * 根据ID更新表达式
     */
    int updateById(PermissionExpression expression);
    
    /**
     * 根据ID删除表达式
     */
    int deleteById(@Param("id") Long id);
}