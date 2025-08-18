package com.company.fastweb.core.data.datasource;

/**
 * 数据源上下文持有者
 * 用于在线程上下文中存储和获取数据源名称
 * 
 * @author FastWeb
 */
public class DataSourceContextHolder {
    
    private static final ThreadLocal<String> DATA_SOURCE_NAME = new ThreadLocal<>();
    
    /**
     * 设置数据源名称
     *
     * @param dataSourceName 数据源名称
     */
    public static void setDataSourceName(String dataSourceName) {
        DATA_SOURCE_NAME.set(dataSourceName);
    }
    
    /**
     * 获取数据源名称
     *
     * @return 数据源名称
     */
    public static String getDataSourceName() {
        return DATA_SOURCE_NAME.get();
    }
    
    /**
     * 清除数据源名称
     */
    public static void clear() {
        DATA_SOURCE_NAME.remove();
    }
}