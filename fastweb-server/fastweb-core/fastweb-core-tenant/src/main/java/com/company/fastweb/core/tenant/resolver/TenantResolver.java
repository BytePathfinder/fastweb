package com.company.fastweb.core.tenant.resolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 租户解析器接口
 * 用于从请求中解析租户ID
 */
public interface TenantResolver {
    
    /**
     * 从请求中解析租户ID
     *
     * @param request HTTP请求
     * @return 租户ID，如果无法解析则返回null
     */
    String resolveTenantId(HttpServletRequest request);
}