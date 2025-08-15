package com.company.fastweb.core.tenant.context;

/**
 * 租户上下文持有者
 * 用于在线程上下文中存储和获取租户ID
 */
public class TenantContextHolder {
    
    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    
    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    public static void setTenantId(String tenantId) {
        TENANT_ID.set(tenantId);
    }
    
    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    public static String getTenantId() {
        return TENANT_ID.get();
    }
    
    /**
     * 清除租户ID
     */
    public static void clear() {
        TENANT_ID.remove();
    }
}