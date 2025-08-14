package com.company.fastweb.biz.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.company.fastweb.core.common.entity.BaseEntity;

/**
 * 权限表达式实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionExpression extends BaseEntity {
    
    /**
     * 权限ID
     */
    private Long permissionId;
    
    /**
     * 表达式名称
     */
    private String expressionName;
    
    /**
     * 表达式内容
     */
    private String expressionContent;
    
    /**
     * 表达式类型：spel, groovy, javascript
     */
    private String expressionType;
    
    /**
     * 变量定义(JSON格式)
     */
    private String variables;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;
    
    /**
     * 租户ID
     */
    private Long tenantId;
}