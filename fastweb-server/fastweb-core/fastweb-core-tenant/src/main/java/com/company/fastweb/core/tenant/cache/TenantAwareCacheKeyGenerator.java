package com.company.fastweb.core.tenant.cache;

import com.company.fastweb.core.tenant.context.TenantContextHolder;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 租户感知的缓存Key生成器
 * 在缓存Key中添加租户ID前缀，实现租户数据隔离
 */
public class TenantAwareCacheKeyGenerator implements KeyGenerator {
    
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        String tenantId = TenantContextHolder.getTenantId();
        
        // 添加租户前缀
        if (StringUtils.hasText(tenantId)) {
            sb.append("tenant_").append(tenantId).append(":");
        }
        
        // 添加类名和方法名
        sb.append(target.getClass().getSimpleName()).append(":");
        sb.append(method.getName()).append(":");
        
        // 添加参数
        for (Object param : params) {
            sb.append(param != null ? param.toString() : "null").append(":");
        }
        
        return sb.toString();
    }
}