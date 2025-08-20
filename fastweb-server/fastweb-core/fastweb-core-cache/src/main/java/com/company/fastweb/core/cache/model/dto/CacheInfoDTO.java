package com.company.fastweb.core.cache.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 缓存信息传输对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheInfoDTO {

    /**
     * 缓存键
     */
    private String key;

    /**
     * 缓存值
     */
    private Object value;

    /**
     * 过期时间（秒）
     */
    private Long expireTime;

    /**
     * 剩余过期时间（秒）
     */
    private Long remainingTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 缓存类型（redis/local）
     */
    private String cacheType;

    /**
     * 缓存大小（字节）
     */
    private Long size;

    /**
     * 扩展属性
     */
    private Map<String, Object> metadata;
}