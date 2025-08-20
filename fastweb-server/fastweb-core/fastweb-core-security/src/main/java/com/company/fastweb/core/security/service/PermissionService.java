package com.company.fastweb.core.security.service;

import com.company.fastweb.core.security.model.dto.PermissionCheckDTO;
import com.company.fastweb.core.security.model.vo.PermissionInfoVO;

import java.util.List;
import java.util.Map;

/**
 * 权限服务接口
 *
 * @author FastWeb
 */
public interface PermissionService {

    /**
     * 检查权限
     *
     * @param permissionCheckDTO 权限检查参数
     * @return 是否有权限
     */
    boolean checkPermission(PermissionCheckDTO permissionCheckDTO);

    /**
     * 批量检查权限
     *
     * @param permissions 权限列表
     * @return 权限检查结果
     */
    Map<String, Boolean> checkPermissions(List<String> permissions);

    /**
     * 获取当前用户权限信息
     *
     * @return 权限信息
     */
    PermissionInfoVO getCurrentUserPermissions();

    /**
     * 检查角色
     *
     * @param roleCode 角色编码
     * @return 是否有角色
     */
    boolean checkRole(String roleCode);

    /**
     * 检查任一角色
     *
     * @param roleCodes 角色编码数组
     * @return 是否有任一角色
     */
    boolean checkAnyRole(String... roleCodes);

    /**
     * 检查所有角色
     *
     * @param roleCodes 角色编码数组
     * @return 是否有所有角色
     */
    boolean checkAllRoles(String... roleCodes);

    /**
     * 清除权限缓存
     */
    void clearPermissionCache();

    /**
     * 获取权限缓存信息
     *
     * @return 缓存信息
     */
    Map<String, Object> getPermissionCacheInfo();

    /**
     * 评估权限表达式
     *
     * @param expression 权限表达式
     * @param context    上下文变量
     * @return 是否有权限
     */
    boolean evaluateExpression(String expression, Map<String, Object> context);
}