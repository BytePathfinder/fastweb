package com.company.fastweb.app.controller;

import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.exception.model.ApiResult;
import com.company.fastweb.core.security.annotation.PreAuthorize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 *
 * @author FastWeb
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final CacheService cacheService;

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ApiResult<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("application", "FastWeb");
        data.put("version", "1.0.0");
        
        log.info("健康检查请求");
        return ApiResult.success("系统运行正常", data);
    }

    /**
     * 测试缓存
     */
    @PostMapping("/cache")
    public ApiResult<String> testCache(@RequestParam String key, @RequestParam String value) {
        cacheService.set(key, value);
        log.info("缓存设置成功: key={}, value={}", key, value);
        return ApiResult.success("缓存设置成功");
    }

    /**
     * 获取缓存
     */
    @GetMapping("/cache/{key}")
    public ApiResult<String> getCache(@PathVariable String key) {
        String value = cacheService.get(key, String.class);
        log.info("缓存获取: key={}, value={}", key, value);
        return ApiResult.success("缓存获取成功", value);
    }

    /**
     * 测试权限注解（无需权限）
     */
    @GetMapping("/public")
    public ApiResult<String> publicEndpoint() {
        return ApiResult.success("公开接口访问成功");
    }

    /**
     * 测试权限注解（需要权限）
     */
    @GetMapping("/protected")
    @PreAuthorize("hasPermission('test:read')")
    public ApiResult<String> protectedEndpoint() {
        return ApiResult.success("受保护接口访问成功");
    }

    /**
     * 测试异常处理
     */
    @GetMapping("/error")
    public ApiResult<String> testError() {
        throw new RuntimeException("这是一个测试异常");
    }

    /**
     * 测试业务异常
     */
    @GetMapping("/biz-error")
    public ApiResult<String> testBizError() {
        throw new com.company.fastweb.core.exception.BizException("这是一个业务异常");
    }

    /**
     * 获取系统信息
     */
    @GetMapping("/info")
    public ApiResult<Map<String, Object>> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("java.version", System.getProperty("java.version"));
        info.put("os.name", System.getProperty("os.name"));
        info.put("user.timezone", System.getProperty("user.timezone"));
        info.put("file.encoding", System.getProperty("file.encoding"));
        
        // 获取缓存信息
        try {
            Map<String, Object> cacheInfo = cacheService.getInfo();
            info.put("cache", cacheInfo);
        } catch (Exception e) {
            info.put("cache", "Redis未连接");
        }
        
        return ApiResult.success("系统信息获取成功", info);
    }
}