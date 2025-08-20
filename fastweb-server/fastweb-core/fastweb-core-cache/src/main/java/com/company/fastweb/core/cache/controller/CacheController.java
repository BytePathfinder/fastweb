package com.company.fastweb.core.cache.controller;

import com.company.fastweb.core.cache.converter.CacheConverter;
import com.company.fastweb.core.cache.lock.DistributedLockClient;
import com.company.fastweb.core.cache.model.dto.CacheInfoDTO;
import com.company.fastweb.core.cache.model.dto.CacheStatisticsDTO;
import com.company.fastweb.core.cache.model.dto.LockInfoDTO;
import com.company.fastweb.core.cache.model.form.CacheQueryForm;
import com.company.fastweb.core.cache.model.form.CacheSetForm;
import com.company.fastweb.core.cache.model.vo.CacheInfoVO;
import com.company.fastweb.core.cache.model.vo.CacheStatisticsVO;
import com.company.fastweb.core.cache.model.vo.LockInfoVO;
import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.common.response.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 缓存管理控制器
 *
 * @author FastWeb
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/cache")
@RequiredArgsConstructor
@Validated
public class CacheController {

    private final CacheService cacheService;
    private final CacheConverter cacheConverter;
    
    @Autowired(required = false)
    private DistributedLockClient lockClient;

    /**
     * 设置缓存
     */
    @PostMapping("/set")
    public ResponseEntity<ApiResult<Void>> setCache(@Valid @RequestBody CacheSetForm form) {
        if (form.getExpireTime() != null && form.getExpireTime() > 0) {
            cacheService.set(form.getKey(), form.getValue(), form.getExpireTime());
        } else {
            cacheService.set(form.getKey(), form.getValue());
        }
        log.info("设置缓存成功: key={}, expireTime={}", form.getKey(), form.getExpireTime());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 获取缓存
     */
    @GetMapping("/get/{key}")
    public ResponseEntity<ApiResult<CacheInfoVO>> getCache(@PathVariable @NotBlank String key) {
        Object value = cacheService.get(key);
        long expireTime = cacheService.getExpire(key);
        
        CacheInfoDTO dto = CacheInfoDTO.builder()
                .key(key)
                .value(value)
                .remainingTime(expireTime)
                .exists(value != null)
                .cacheType("redis")
                .createTime(LocalDateTime.now())
                .build();
        
        CacheInfoVO vo = cacheConverter.toVO(dto);
        return ResponseEntity.ok(ApiResult.success(vo));
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/delete/{key}")
    public ResponseEntity<ApiResult<Void>> deleteCache(@PathVariable @NotBlank String key) {
        cacheService.delete(key);
        log.info("删除缓存成功: key={}", key);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 检查缓存是否存在
     */
    @GetMapping("/exists/{key}")
    public ResponseEntity<ApiResult<Boolean>> existsCache(@PathVariable @NotBlank String key) {
        boolean exists = cacheService.exists(key);
        return ResponseEntity.ok(ApiResult.success(exists));
    }

    /**
     * 设置过期时间
     */
    @PutMapping("/expire/{key}/{timeout}")
    public ResponseEntity<ApiResult<Void>> expireCache(
            @PathVariable @NotBlank String key,
            @PathVariable Long timeout) {
        cacheService.expire(key, timeout);
        log.info("设置缓存过期时间成功: key={}, timeout={}", key, timeout);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 获取剩余过期时间
     */
    @GetMapping("/ttl/{key}")
    public ResponseEntity<ApiResult<Long>> getTtl(@PathVariable @NotBlank String key) {
        long ttl = cacheService.getExpire(key);
        return ResponseEntity.ok(ApiResult.success(ttl));
    }

    /**
     * 根据模式查询缓存键
     */
    @GetMapping("/keys")
    public ResponseEntity<ApiResult<List<CacheInfoVO>>> getKeys(@Valid CacheQueryForm form) {
        Set<String> keys = cacheService.keys(form.getPattern());
        
        List<CacheInfoVO> result = keys.stream()
                .limit((long) form.getPageSize() * form.getPageNum())
                .skip((long) (form.getPageNum() - 1) * form.getPageSize())
                .map(key -> {
                    CacheInfoDTO dto = CacheInfoDTO.builder()
                            .key(key)
                            .exists(cacheService.exists(key))
                            .remainingTime(cacheService.getExpire(key))
                            .cacheType("redis")
                            .createTime(LocalDateTime.now())
                            .build();
                    
                    if (form.getIncludeValue()) {
                        dto.setValue(cacheService.get(key));
                    }
                    
                    return cacheConverter.toVO(dto);
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResult.success(result));
    }

    /**
     * 批量删除缓存
     */
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResult<Void>> batchDeleteCache(@RequestBody Set<String> keys) {
        cacheService.delete(keys);
        log.info("批量删除缓存成功: keys={}", keys);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 清空指定缓存区域
     */
    @DeleteMapping("/clear/{cacheName}")
    public ResponseEntity<ApiResult<Void>> clearCache(@PathVariable @NotBlank String cacheName) {
        cacheService.clear(cacheName);
        log.info("清空缓存区域成功: cacheName={}", cacheName);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 获取缓存统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResult<CacheStatisticsVO>> getStatistics() {
        // 这里需要扩展CacheService接口来支持统计信息
        CacheStatisticsDTO dto = CacheStatisticsDTO.builder()
                .cacheType("redis")
                .statisticsTime(LocalDateTime.now())
                .build();
        
        CacheStatisticsVO vo = cacheConverter.toStatisticsVO(dto);
        return ResponseEntity.ok(ApiResult.success(vo));
    }

    // ========== 分布式锁相关接口 ==========

    @GetMapping("/lock/info/{lockName}")
    @Operation(summary = "获取锁信息", description = "获取指定锁的详细信息")
    @ConditionalOnBean(DistributedLockClient.class)
    public ResponseEntity<ApiResult<LockInfoVO>> getLockInfo(
            @Parameter(description = "锁名称") @PathVariable String lockName) {
        try {
            if (lockClient == null) {
                return ResponseEntity.ok(ApiResult.error("LOCK_CLIENT_NOT_AVAILABLE", "分布式锁客户端不可用"));
            }
            LockInfoDTO lockInfo = lockClient.getLockInfo(lockName);
            return ResponseEntity.ok(ApiResult.success(cacheConverter.toVO(lockInfo)));
        } catch (Exception e) {
            log.error("获取锁信息失败: {}", lockName, e);
            return ResponseEntity.ok(ApiResult.error("LOCK_INFO_ERROR", "获取锁信息失败: " + e.getMessage()));
        }
    }

    @GetMapping("/lock/status/{lockName}")
    @Operation(summary = "检查锁状态", description = "检查指定锁是否被锁定")
    @ConditionalOnBean(DistributedLockClient.class)
    public ResponseEntity<ApiResult<Boolean>> checkLockStatus(
            @Parameter(description = "锁名称") @PathVariable String lockName) {
        try {
            if (lockClient == null) {
                return ResponseEntity.ok(ApiResult.error("LOCK_CLIENT_NOT_AVAILABLE", "分布式锁客户端不可用"));
            }
            boolean isLocked = lockClient.isLocked(lockName);
            return ResponseEntity.ok(ApiResult.success(isLocked));
        } catch (Exception e) {
            log.error("检查锁状态失败: {}", lockName, e);
            return ResponseEntity.ok(ApiResult.error("LOCK_STATUS_ERROR", "检查锁状态失败: " + e.getMessage()));
        }
    }
}