package com.company.fastweb.core.cache.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 缓存统计信息传输对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheStatisticsDTO {

    /**
     * 缓存类型（redis/local）
     */
    private String cacheType;

    /**
     * 总键数量
     */
    private Long totalKeys;

    /**
     * 已使用内存（字节）
     */
    private Long usedMemory;

    /**
     * 最大内存（字节）
     */
    private Long maxMemory;

    /**
     * 内存使用率
     */
    private Double memoryUsageRate;

    /**
     * 命中次数
     */
    private Long hitCount;

    /**
     * 未命中次数
     */
    private Long missCount;

    /**
     * 命中率
     */
    private Double hitRate;

    /**
     * 连接数
     */
    private Integer connectionCount;

    /**
     * 运行时间（秒）
     */
    private Long uptime;

    /**
     * 统计时间
     */
    private LocalDateTime statisticsTime;

    /**
     * 扩展统计信息
     */
    private Map<String, Object> additionalInfo;
}