package com.company.fastweb.core.security.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

/**
 * 权限检查表单
 *
 * @author FastWeb
 */
public record PermissionCheckForm(
        
        @NotBlank(message = "权限标识不能为空")
        String permission,
        
        @NotEmpty(message = "权限标识列表不能为空")
        List<String> permissions,
        
        @NotBlank(message = "角色编码不能为空")
        String roleCode,
        
        @NotEmpty(message = "角色编码列表不能为空")
        List<String> roleCodes,
        
        @NotBlank(message = "权限表达式不能为空")
        String expression,
        
        Map<String, Object> context,
        
        Long userId
) {
    public PermissionCheckForm {
        if (permission != null) {
            permission = permission.trim();
        }
        if (roleCode != null) {
            roleCode = roleCode.trim();
        }
        if (expression != null) {
            expression = expression.trim();
        }
    }
}