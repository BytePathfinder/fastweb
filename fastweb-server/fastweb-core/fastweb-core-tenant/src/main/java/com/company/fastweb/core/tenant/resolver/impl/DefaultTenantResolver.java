package com.company.fastweb.core.tenant.resolver.impl;

import com.company.fastweb.core.tenant.resolver.TenantResolver;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 默认租户解析器实现
 * 从请求头、请求参数或Cookie中获取租户ID
 */
@Component
public class DefaultTenantResolver implements TenantResolver {
    
    private static final String TENANT_ID_HEADER = "X-Tenant-ID";
    private static final String TENANT_ID_PARAM = "tenantId";
    private static final String TENANT_ID_COOKIE = "tenant_id";
    
    @Override
    public String resolveTenantId(HttpServletRequest request) {
        // 1. 尝试从请求头获取
        String tenantId = request.getHeader(TENANT_ID_HEADER);
        
        // 2. 尝试从请求参数获取
        if (!StringUtils.hasText(tenantId)) {
            tenantId = request.getParameter(TENANT_ID_PARAM);
        }
        
        // 3. 尝试从Cookie获取
        if (!StringUtils.hasText(tenantId) && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (TENANT_ID_COOKIE.equals(cookie.getName())) {
                    tenantId = cookie.getValue();
                    break;
                }
            }
        }
        
        return StringUtils.hasText(tenantId) ? tenantId : null;
    }
}