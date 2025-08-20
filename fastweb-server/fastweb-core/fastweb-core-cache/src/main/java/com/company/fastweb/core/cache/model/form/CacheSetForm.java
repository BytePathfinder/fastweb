package com.company.fastweb.core.cache.model.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

/**
 * 缓存设置表单对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheSetForm {

    /**
     * 缓存键
     */
    @NotBlank(message = "缓存键不能为空")
    private String key;

    /**
     * 缓存值
     */
    @NotNull(message = "缓存值不能为空")
    private Object value;

    /**
     * 过期时间（秒），-1表示永不过期
     */
    @Min(value = -1, message = "过期时间不能小于-1")
    private Long expireTime;

    /**
     * 缓存类型（redis/local）
     */
    private String cacheType = "redis";
}