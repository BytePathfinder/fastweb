package com.company.fastweb.core.tenant.interceptor;

import com.company.fastweb.core.tenant.context.TenantContextHolder;
import com.company.fastweb.core.tenant.resolver.TenantResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 租户拦截器
 * 从请求中提取租户ID并设置到上下文中
 */
public class TenantInterceptor implements HandlerInterceptor {
    
    private final TenantResolver tenantResolver;
    
    public TenantInterceptor(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = tenantResolver.resolveTenantId(request);
        if (tenantId != null) {
            TenantContextHolder.setTenantId(tenantId);
        }
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContextHolder.clear();
    }
}