package com.company.fastweb.core.data.datasource;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

/**
 * 数据源上下文持有者，用于多数据源切换
 */
public class DataSourceContextHolder {
    
    private static final ThreadLocal<String> CONTEXT_HOLDER = new NamedThreadLocal<>("Dynamic DataSource Context");
    
    /**
     * 设置数据源
     *
     * @param dataSourceName 数据源名称
     */
    public static void setDataSourceName(String dataSourceName) {
        if (StringUtils.hasText(dataSourceName)) {
            CONTEXT_HOLDER.set(dataSourceName);
        }
    }
    
    /**
     * 获取当前数据源
     *
     * @return 数据源名称
     */
    public static String getDataSourceName() {
        return CONTEXT_HOLDER.get();
    }
    
    /**
     * 清除数据源
     */
    public static void clearDataSourceName() {
        CONTEXT_HOLDER.remove();
    }
}