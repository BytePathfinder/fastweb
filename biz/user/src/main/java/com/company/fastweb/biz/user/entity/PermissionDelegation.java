package com.company.fastweb.biz.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.company.fastweb.core.common.entity.BaseEntity;
import java.time.LocalDateTime;

/**
 * 权限委托实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionDelegation extends BaseEntity {
    
    /**
     * 委托人ID
     */
    private Long delegatorId;
    
    /**
     * 被委托人ID
     */
    private Long delegateeId;
    
    /**
     * 委托权限ID列表(JSON格式)
     */
    private String permissionIds;
    
    /**
     * 委托类型：temporary-临时，permanent-永久
     */
    private String delegationType;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 委托原因
     */
    private String reason;
    
    /**
     * 状态：active-生效，revoked-撤销，expired-过期
     */
    private String status;
    
    /**
     * 租户ID
     */
    private Long tenantId;
}