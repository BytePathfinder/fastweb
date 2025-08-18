package com.company.fastweb.core.data.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础PO类 - 基于MySQL审计字段规范
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 逻辑删除标记（未删除0，已删除null）
     */
    @TableLogic(value = "0", delval = "null")
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 状态（0正常 1停用 2删除）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 排序序号
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id")
    private String tenantId;

    /**
     * 乐观锁版本号
     */
    @Version
    @TableField(value = "version", fill = FieldFill.INSERT)
    private Integer version;
}