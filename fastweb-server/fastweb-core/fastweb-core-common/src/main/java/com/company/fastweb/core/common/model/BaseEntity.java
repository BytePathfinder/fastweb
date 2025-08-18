package com.company.fastweb.core.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 *
 * @author FastWeb
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 备注
     */
    private String remark;
}