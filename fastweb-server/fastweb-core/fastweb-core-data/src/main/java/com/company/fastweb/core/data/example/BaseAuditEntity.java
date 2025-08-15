package com.company.fastweb.core.data.example;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.fastweb.core.data.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 示例：基础审计实体类
 * 继承统一的BaseEntity，自动获得审计字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_audit_example")
public class BaseAuditEntity extends BaseEntity {
    
    private Long id;
    private String name;
    private String description;
    
    // 继承BaseEntity后自动获得：createBy, createTime, updateBy, updateTime, remark, delFlag, version
}