package com.company.fastweb.core.cache.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 分布式锁信息视图对象
 *
 * @author FastWeb
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockInfoVO {

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
     * 锁的剩余时间（格式化后的字符串）
     */
    private String remainingTimeFormatted;

    /**
     * 锁的租约时间（格式化后的字符串）
     */
    private String leaseTimeFormatted;

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
     * 锁状态标签（用于前端显示）
     */
    private String statusLabel;

    /**
     * 扩展信息
     */
    private String metadata;
}