package com.company.fastweb.core.cache.model.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁操作表单
 *
 * @author FastWeb
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockOperationForm {

    /**
     * 锁名称
     */
    @NotBlank(message = "锁名称不能为空")
    private String lockName;

    /**
     * 等待时间（毫秒）
     */
    @Min(value = 0, message = "等待时间不能为负数")
    private Long waitTime;

    /**
     * 租约时间（毫秒）
     */
    @Min(value = 1, message = "租约时间必须大于0")
    private Long leaseTime;

    /**
     * 时间单位
     */
    @NotNull(message = "时间单位不能为空")
    private TimeUnit timeUnit;

    /**
     * 操作类型
     */
    @NotBlank(message = "操作类型不能为空")
    private String operation; // lock, tryLock, unlock

    /**
     * 是否强制解锁
     */
    private Boolean forceUnlock;

    /**
     * 备注信息
     */
    private String remark;
}