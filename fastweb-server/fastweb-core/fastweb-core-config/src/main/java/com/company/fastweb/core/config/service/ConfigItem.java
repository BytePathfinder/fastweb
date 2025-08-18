package com.company.fastweb.core.config.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 配置项
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigItem {

    /**
     * 配置键
     */
    private String key;

    /**
     * 配置值
     */
    private String value;

    /**
     * 配置描述
     */
    private String description;

    /**
     * 配置分组
     */
    private String group;

    /**
     * 配置类型
     */
    private String type;

    /**
     * 是否加密
     */
    private Boolean encrypted;

    /**
     * 是否只读
     */
    private Boolean readonly;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 更新者
     */
    private String updater;
}