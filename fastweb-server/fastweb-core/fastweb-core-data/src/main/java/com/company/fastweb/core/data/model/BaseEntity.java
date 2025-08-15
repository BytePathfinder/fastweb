package com.company.fastweb.core.data.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类，提供通用字段
 * 借鉴RuoYi的设计理念，提供审计字段和数据安全字段
 */
@Data
public class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 逻辑删除标志（0-正常，1-删除）
     */
    @TableLogic(value = "0", delval = "1")
    @JsonIgnore
    private String delFlag;
    
    /**
     * 乐观锁版本号
     */
    @Version
    @JsonIgnore
    private Integer version;
}