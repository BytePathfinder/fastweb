package com.company.fastweb.core.data.service.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.company.fastweb.core.data.datasource.DataSourceContextHolder;
import com.company.fastweb.core.data.model.vo.DataSourceInfoVO;
import com.company.fastweb.core.data.service.DataSourceService;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据源服务实现类
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnClass(DynamicRoutingDataSource.class)
public class DataSourceServiceImpl implements DataSourceService {

    private final DynamicRoutingDataSource dynamicRoutingDataSource;
    private final DefaultDataSourceCreator dataSourceCreator;

    @Override
    public List<DataSourceInfoVO> getAllDataSources() {
        Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
        
        return dataSources.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    DataSource dataSource = entry.getValue();
                    
                    DataSourceInfoVO info = new DataSourceInfoVO();
                    info.setKey(key);
                    info.setActive(key.equals(DataSourceContextHolder.getDataSource()));
                    
                    if (dataSource instanceof HikariDataSource hikariDataSource) {
                        info.setUrl(hikariDataSource.getJdbcUrl());
                        info.setUsername(hikariDataSource.getUsername());
                        info.setDriverClassName(hikariDataSource.getDriverClassName());
                        info.setPoolName(hikariDataSource.getPoolName());
                        info.setMaximumPoolSize(hikariDataSource.getMaximumPoolSize());
                        info.setMinimumIdle(hikariDataSource.getMinimumIdle());
                        info.setConnectionTimeout(hikariDataSource.getConnectionTimeout());
                        info.setIdleTimeout(hikariDataSource.getIdleTimeout());
                        info.setMaxLifetime(hikariDataSource.getMaxLifetime());
                        
                        // 获取连接池统计信息
                        HikariPoolMXBean poolMXBean = hikariDataSource.getHikariPoolMXBean();
                        if (poolMXBean != null) {
                            info.setActiveConnections(poolMXBean.getActiveConnections());
                            info.setIdleConnections(poolMXBean.getIdleConnections());
                            info.setTotalConnections(poolMXBean.getTotalConnections());
                            info.setThreadsAwaitingConnection(poolMXBean.getThreadsAwaitingConnection());
                        }
                    }
                    
                    return info;
                })
                .collect(Collectors.toList());
    }

    @Override
    public DataSourceInfoVO getCurrentDataSource() {
        String currentKey = DataSourceContextHolder.getDataSource();
        if (currentKey == null) {
            currentKey = "master"; // 默认数据源
        }
        
        return getAllDataSources().stream()
                .filter(ds -> ds.getKey().equals(currentKey))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void switchDataSource(String dataSourceKey) {
        if (!dynamicRoutingDataSource.getDataSources().containsKey(dataSourceKey)) {
            throw new IllegalArgumentException("数据源不存在: " + dataSourceKey);
        }
        
        DataSourceContextHolder.setDataSource(dataSourceKey);
        log.info("数据源已切换到: {}", dataSourceKey);
    }

    @Override
    public boolean testConnection(String dataSourceKey) {
        DataSource dataSource = dynamicRoutingDataSource.getDataSources().get(dataSourceKey);
        if (dataSource == null) {
            return false;
        }
        
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5); // 5秒超时
        } catch (SQLException e) {
            log.error("测试数据源连接失败: {}", dataSourceKey, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getHealthInfo() {
        Map<String, Object> healthInfo = new HashMap<>();
        Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
        
        for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
            String key = entry.getKey();
            boolean isHealthy = testConnection(key);
            healthInfo.put(key, Map.of(
                    "healthy", isHealthy,
                    "status", isHealthy ? "UP" : "DOWN"
            ));
        }
        
        return healthInfo;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        List<DataSourceInfoVO> dataSources = getAllDataSources();
        
        int totalDataSources = dataSources.size();
        int activeDataSources = (int) dataSources.stream().filter(DataSourceInfoVO::isActive).count();
        int totalConnections = dataSources.stream().mapToInt(ds -> ds.getTotalConnections() != null ? ds.getTotalConnections() : 0).sum();
        int activeConnections = dataSources.stream().mapToInt(ds -> ds.getActiveConnections() != null ? ds.getActiveConnections() : 0).sum();
        int idleConnections = dataSources.stream().mapToInt(ds -> ds.getIdleConnections() != null ? ds.getIdleConnections() : 0).sum();
        
        statistics.put("totalDataSources", totalDataSources);
        statistics.put("activeDataSources", activeDataSources);
        statistics.put("totalConnections", totalConnections);
        statistics.put("activeConnections", activeConnections);
        statistics.put("idleConnections", idleConnections);
        statistics.put("connectionUtilization", totalConnections > 0 ? (double) activeConnections / totalConnections : 0.0);
        
        return statistics;
    }

    @Override
    public void cleanupDataSource(String dataSourceKey) {
        DataSource dataSource = dynamicRoutingDataSource.getDataSources().get(dataSourceKey);
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            hikariDataSource.getHikariPoolMXBean().softEvictConnections();
            log.info("数据源连接池已清理: {}", dataSourceKey);
        }
    }

    @Override
    public void addDataSource(String dataSourceKey, Map<String, Object> properties) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl((String) properties.get("url"));
        dataSourceProperty.setUsername((String) properties.get("username"));
        dataSourceProperty.setPassword((String) properties.get("password"));
        dataSourceProperty.setDriverClassName((String) properties.get("driverClassName"));
        
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        dynamicRoutingDataSource.addDataSource(dataSourceKey, dataSource);
        
        log.info("数据源已添加: {}", dataSourceKey);
    }

    @Override
    public void removeDataSource(String dataSourceKey) {
        if ("master".equals(dataSourceKey)) {
            throw new IllegalArgumentException("不能删除主数据源");
        }
        
        dynamicRoutingDataSource.removeDataSource(dataSourceKey);
        log.info("数据源已移除: {}", dataSourceKey);
    }

    @Override
    public Map<String, Object> getDataSourceConfig(String dataSourceKey) {
        DataSource dataSource = dynamicRoutingDataSource.getDataSources().get(dataSourceKey);
        if (dataSource == null) {
            throw new IllegalArgumentException("数据源不存在: " + dataSourceKey);
        }
        
        Map<String, Object> config = new HashMap<>();
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            config.put("url", hikariDataSource.getJdbcUrl());
            config.put("username", hikariDataSource.getUsername());
            config.put("driverClassName", hikariDataSource.getDriverClassName());
            config.put("maximumPoolSize", hikariDataSource.getMaximumPoolSize());
            config.put("minimumIdle", hikariDataSource.getMinimumIdle());
            config.put("connectionTimeout", hikariDataSource.getConnectionTimeout());
            config.put("idleTimeout", hikariDataSource.getIdleTimeout());
            config.put("maxLifetime", hikariDataSource.getMaxLifetime());
        }
        
        return config;
    }

    @Override
    public void updateDataSourceConfig(String dataSourceKey, Map<String, Object> properties) {
        // 先移除旧的数据源
        removeDataSource(dataSourceKey);
        // 再添加新的数据源
        addDataSource(dataSourceKey, properties);
        
        log.info("数据源配置已更新: {}", dataSourceKey);
    }
}