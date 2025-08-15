package com.company.fastweb.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础测试控制器
 */
@RestController
@RequestMapping("/api/test")
public class TenantTestController {

    /**
     * 基础健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("message", "应用运行正常");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 基础信息
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> result = new HashMap<>();
        result.put("application", "FastWeb");
        result.put("version", "1.0.0");
        result.put("description", "FastWeb应用框架");
        return result;
    }
}
