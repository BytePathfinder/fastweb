package com.company.fastweb.core.cache.controller;

import com.company.fastweb.core.cache.converter.CacheConverter;
import com.company.fastweb.core.cache.lock.DistributedLockClient;
import com.company.fastweb.core.cache.model.dto.LockInfoDTO;
import com.company.fastweb.core.cache.model.form.LockOperationForm;
import com.company.fastweb.core.cache.model.vo.LockInfoVO;
import com.company.fastweb.core.common.model.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁管理控制器
 *
 * @author FastWeb
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/lock")
@RequiredArgsConstructor
@ConditionalOnBean(DistributedLockClient.class)
@Tag(name = "分布式锁管理", description = "分布式锁的获取、释放、查询等操作")
public class LockController {

    private final DistributedLockClient lockClient;
    private final CacheConverter cacheConverter;

    @PostMapping("/acquire")
    @Operation(summary = "获取锁", description = "获取分布式锁")
    public ResponseEntity<ApiResult<LockInfoVO>> acquireLock(
            @Valid @RequestBody LockOperationForm form) {
        try {
            RLock lock;
            boolean acquired = false;
            
            if ("tryLock".equals(form.getOperation())) {
                // 尝试获取锁
                acquired = lockClient.tryLock(
                    form.getLockName(),
                    form.getWaitTime() != null ? form.getWaitTime() : 0L,
                    form.getLeaseTime() != null ? form.getLeaseTime() : 30000L,
                    form.getTimeUnit() != null ? form.getTimeUnit() : TimeUnit.MILLISECONDS
                );
            } else if ("lock".equals(form.getOperation())) {
                // 阻塞获取锁
                if (form.getLeaseTime() != null) {
                    lock = lockClient.lock(
                        form.getLockName(),
                        form.getLeaseTime(),
                        form.getTimeUnit() != null ? form.getTimeUnit() : TimeUnit.MILLISECONDS
                    );
                } else {
                    lock = lockClient.lock(form.getLockName());
                }
                acquired = true;
            }
            
            LockInfoDTO lockInfo = lockClient.getLockInfo(form.getLockName());
            lockInfo.setStatus(acquired ? "locked" : "failed");
            
            return ResponseEntity.ok(ApiResult.success(cacheConverter.toVO(lockInfo)));
        } catch (Exception e) {
            log.error("获取锁失败: {}", form.getLockName(), e);
            return ResponseEntity.ok(ApiResult.error("LOCK_ACQUIRE_ERROR", "获取锁失败: " + e.getMessage()));
        }
    }

    @PostMapping("/release")
    @Operation(summary = "释放锁", description = "释放分布式锁")
    public ResponseEntity<ApiResult<Boolean>> releaseLock(
            @Valid @RequestBody LockOperationForm form) {
        try {
            if (Boolean.TRUE.equals(form.getForceUnlock())) {
                // 强制解锁
                boolean result = lockClient.forceUnlock(form.getLockName());
                return ResponseEntity.ok(ApiResult.success(result));
            } else {
                // 正常解锁
                lockClient.unlock(form.getLockName());
                return ResponseEntity.ok(ApiResult.success(true));
            }
        } catch (Exception e) {
            log.error("释放锁失败: {}", form.getLockName(), e);
            return ResponseEntity.ok(ApiResult.error("LOCK_RELEASE_ERROR", "释放锁失败: " + e.getMessage()));
        }
    }

    @GetMapping("/info/{lockName}")
    @Operation(summary = "获取锁信息", description = "获取指定锁的详细信息")
    public ResponseEntity<ApiResult<LockInfoVO>> getLockInfo(
            @Parameter(description = "锁名称") @PathVariable String lockName) {
        try {
            LockInfoDTO lockInfo = lockClient.getLockInfo(lockName);
            return ResponseEntity.ok(ApiResult.success(cacheConverter.toVO(lockInfo)));
        } catch (Exception e) {
            log.error("获取锁信息失败: {}", lockName, e);
            return ResponseEntity.ok(ApiResult.error("LOCK_INFO_ERROR", "获取锁信息失败: " + e.getMessage()));
        }
    }

    @GetMapping("/status/{lockName}")
    @Operation(summary = "检查锁状态", description = "检查指定锁是否被锁定")
    public ResponseEntity<ApiResult<Boolean>> checkLockStatus(
            @Parameter(description = "锁名称") @PathVariable String lockName) {
        try {
            boolean isLocked = lockClient.isLocked(lockName);
            return ResponseEntity.ok(ApiResult.success(isLocked));
        } catch (Exception e) {
            log.error("检查锁状态失败: {}", lockName, e);
            return ResponseEntity.ok(ApiResult.error("LOCK_STATUS_ERROR", "检查锁状态失败: " + e.getMessage()));
        }
    }

    @PostMapping("/force-unlock/{lockName}")
    @Operation(summary = "强制解锁", description = "强制释放指定的锁")
    public ResponseEntity<ApiResult<Boolean>> forceUnlock(
            @Parameter(description = "锁名称") @PathVariable String lockName) {
        try {
            boolean result = lockClient.forceUnlock(lockName);
            return ResponseEntity.ok(ApiResult.success(result));
        } catch (Exception e) {
            log.error("强制解锁失败: {}", lockName, e);
            return ResponseEntity.ok(ApiResult.error("FORCE_UNLOCK_ERROR", "强制解锁失败: " + e.getMessage()));
        }
    }
}