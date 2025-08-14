package com.company.fastweb.biz.user.controller;

import com.company.fastweb.biz.user.entity.PermissionExpression;
import com.company.fastweb.biz.user.service.PermissionExpressionService;
import com.company.fastweb.core.infra.security.expression.PermissionExpressionEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限表达式管理控制器
 */
@RestController
@RequestMapping("/api/permission-expressions")
@RequiredArgsConstructor
public class PermissionExpressionController {
    
    private final PermissionExpressionService expressionService;
    private final PermissionExpressionEngine expressionEngine;
    
    /**
     * 获取所有启用的权限表达式
     */
    @GetMapping
    public List<PermissionExpression> getAllEnabled() {
        return expressionService.getAllEnabled();
    }
    
    /**
     * 根据权限标识获取表达式
     */
    @GetMapping("/{permissionCode}")
    public PermissionExpression getByPermissionCode(@PathVariable String permissionCode) {
        return expressionService.getByPermissionCode(permissionCode)
                .orElse(null);
    }
    
    /**
     * 创建权限表达式
     */
    @PostMapping
    public PermissionExpression create(@RequestBody PermissionExpression expression) {
        return expressionService.createExpression(expression);
    }
    
    /**
     * 更新权限表达式
     */
    @PutMapping("/{id}")
    public PermissionExpression update(@PathVariable Long id, @RequestBody PermissionExpression expression) {
        expression.setId(id);
        return expressionService.updateExpression(expression);
    }
    
    /**
     * 删除权限表达式
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        expressionService.deleteExpression(id);
    }
    
    /**
     * 验证表达式语法
     */
    @PostMapping("/validate")
    public Map<String, Object> validateExpression(@RequestBody Map<String, String> request) {
        String expression = request.get("expression");
        boolean valid = expressionEngine.validateExpression(expression);
        
        Map<String, Object> result = new HashMap<>();
        result.put("valid", valid);
        result.put("expression", expression);
        
        return result;
    }
    
    /**
     * 测试表达式执行
     */
    @PostMapping("/test")
    public Map<String, Object> testExpression(@RequestBody Map<String, Object> request) {
        String expression = (String) request.get("expression");
        Map<String, Object> context = (Map<String, Object>) request.get("context");
        
        if (context == null) {
            context = new HashMap<>();
        }
        
        boolean result = expressionEngine.evaluate(expression, context);
        
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        response.put("expression", expression);
        response.put("context", context);
        
        return response;
    }
    
    /**
     * 清理表达式缓存
     */
    @PostMapping("/clear-cache")
    public Map<String, String> clearCache() {
        expressionService.clearExpressionCache();
        Map<String, String> result = new HashMap<>();
        result.put("message", "权限表达式缓存已清理");
        return result;
    }
}