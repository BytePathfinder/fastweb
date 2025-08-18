package com.company.fastweb.core.monitor.service;

import java.util.Map;

/**
 * 监控服务接口
 *
 * @author FastWeb
 */
public interface MonitorService {

    /**
     * 获取系统信息
     *
     * @return 系统信息
     */
    Map<String, Object> getSystemInfo();

    /**
     * 获取JVM信息
     *
     * @return JVM信息
     */
    Map<String, Object> getJvmInfo();

    /**
     * 获取内存信息
     *
     * @return 内存信息
     */
    Map<String, Object> getMemoryInfo();

    /**
     * 获取CPU信息
     *
     * @return CPU信息
     */
    Map<String, Object> getCpuInfo();

    /**
     * 获取磁盘信息
     *
     * @return 磁盘信息
     */
    Map<String, Object> getDiskInfo();

    /**
     * 获取网络信息
     *
     * @return 网络信息
     */
    Map<String, Object> getNetworkInfo();

    /**
     * 获取数据库连接信息
     *
     * @return 数据库连接信息
     */
    Map<String, Object> getDatabaseInfo();

    /**
     * 获取缓存信息
     *
     * @return 缓存信息
     */
    Map<String, Object> getCacheInfo();

    /**
     * 获取应用信息
     *
     * @return 应用信息
     */
    Map<String, Object> getApplicationInfo();

    /**
     * 获取健康状态
     *
     * @return 健康状态
     */
    Map<String, Object> getHealthStatus();

    /**
     * 获取性能指标
     *
     * @return 性能指标
     */
    Map<String, Object> getMetrics();

    /**
     * 记录自定义指标
     *
     * @param name 指标名称
     * @param value 指标值
     * @param tags 标签
     */
    void recordMetric(String name, double value, String... tags);

    /**
     * 增加计数器
     *
     * @param name 计数器名称
     * @param tags 标签
     */
    void incrementCounter(String name, String... tags);

    /**
     * 记录计时器
     *
     * @param name 计时器名称
     * @param duration 持续时间（毫秒）
     * @param tags 标签
     */
    void recordTimer(String name, long duration, String... tags);

    /**
     * 设置仪表盘值
     *
     * @param name 仪表盘名称
     * @param value 值
     * @param tags 标签
     */
    void setGauge(String name, double value, String... tags);
}