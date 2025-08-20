package com.company.fastweb.core.data.service;

import com.company.fastweb.core.data.model.vo.DataSourceInfoVO;

import java.util.List;
import java.util.Map;

/**
 * 数据源服务接口
 *
 * @author FastWeb
 */
public interface DataSourceService {

    /**
     * 获取所有数据源信息
     *
     * @return 数据源信息列表
     */
    List<DataSourceInfoVO> getAllDataSources();

    /**
     * 获取当前数据源信息
     *
     * @return 当前数据源信息
     */
    DataSourceInfoVO getCurrentDataSource();

    /**
     * 切换数据源
     *
     * @param dataSourceKey 数据源键
     */
    void switchDataSource(String dataSourceKey);

    /**
     * 测试数据源连接
     *
     * @param dataSourceKey 数据源键
     * @return 是否连接成功
     */
    boolean testConnection(String dataSourceKey);

    /**
     * 获取数据源健康状态
     *
     * @return 健康状态信息
     */
    Map<String, Object> getHealthInfo();

    /**
     * 获取数据源统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 清理数据源连接池
     *
     * @param dataSourceKey 数据源键
     */
    void cleanupDataSource(String dataSourceKey);

    /**
     * 添加数据源
     *
     * @param dataSourceKey 数据源键
     * @param properties 数据源配置
     */
    void addDataSource(String dataSourceKey, Map<String, Object> properties);

    /**
     * 移除数据源
     *
     * @param dataSourceKey 数据源键
     */
    void removeDataSource(String dataSourceKey);

    /**
     * 获取数据源配置
     *
     * @param dataSourceKey 数据源键
     * @return 数据源配置
     */
    Map<String, Object> getDataSourceConfig(String dataSourceKey);

    /**
     * 更新数据源配置
     *
     * @param dataSourceKey 数据源键
     * @param properties 新的配置
     */
    void updateDataSourceConfig(String dataSourceKey, Map<String, Object> properties);
}