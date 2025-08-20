package com.company.fastweb.core.cache.model.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * 缓存查询表单对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheQueryForm {

    /**
     * 查询模式（支持通配符 *）
     */
    private String pattern = "*";

    /**
     * 缓存类型（redis/local/all）
     */
    private String cacheType = "all";

    /**
     * 页码
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    @Min(value = 1, message = "页大小不能小于1")
    @Max(value = 1000, message = "页大小不能超过1000")
    private Integer pageSize = 20;

    /**
     * 是否包含值内容
     */
    private Boolean includeValue = false;
}