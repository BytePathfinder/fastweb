package com.company.fastweb.core.cache.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁信息传输对象
 *
 * @author FastWeb
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockInfoDTO {

    /**
     * 锁名称
     */
    private String lockName;

    /**
     * 锁持有者
     */
    private String holder;

    /**
     * 是否已锁定
     */
    private Boolean locked;

    /**
     * 是否被当前线程持有
     */
    private Boolean heldByCurrentThread;

    /**
     * 锁的剩余时间（毫秒）
     */
    private Long remainingTime;

    /**
     * 锁的租约时间（毫秒）
     */
    private Long leaseTime;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 锁创建时间
     */
    private LocalDateTime createTime;

    /**
     * 锁过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 重入次数
     */
    private Integer holdCount;

    /**
     * 锁状态描述
     */
    private String status;

    /**
     * 扩展信息
     */
    private String metadata;
}