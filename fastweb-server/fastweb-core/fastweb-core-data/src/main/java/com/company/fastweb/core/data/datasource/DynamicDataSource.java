package com.company.fastweb.core.data.datasource;

import com.company.fastweb.core.data.properties.FastWebDataProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源实现
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 构造函数
     *
     * @param properties 数据源配置属性
     */
    public DynamicDataSource(Map<String, FastWebDataProperties.DataSourceConfig> dataSourceConfigs, String primaryDataSourceName) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource defaultDataSource = null;
        
        for (Map.Entry<String, FastWebDataProperties.DataSourceConfig> entry : dataSourceConfigs.entrySet()) {
            String dsName = entry.getKey();
            FastWebDataProperties.DataSourceConfig config = entry.getValue();
            
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(config.getUrl());
            dataSource.setUsername(config.getUsername());
            dataSource.setPassword(config.getPassword());
            dataSource.setDriverClassName(config.getDriverClassName());
            
            // 应用HikariCP配置
            if (config.getHikari() != null) {
                if (config.getHikari().getMaximumPoolSize() > 0) {
                    dataSource.setMaximumPoolSize(config.getHikari().getMaximumPoolSize());
                }
                if (config.getHikari().getMinimumIdle() > 0) {
                    dataSource.setMinimumIdle(config.getHikari().getMinimumIdle());
                }
                if (config.getHikari().getIdleTimeout() > 0) {
                    dataSource.setIdleTimeout(config.getHikari().getIdleTimeout());
                }
                if (StringUtils.hasText(config.getHikari().getPoolName())) {
                    dataSource.setPoolName(config.getHikari().getPoolName());
                } else {
                    dataSource.setPoolName("HikariPool-" + dsName);
                }
            }
            
            targetDataSources.put(dsName, dataSource);
            
            // 设置默认数据源
            if (dsName.equals(primaryDataSourceName)) {
                defaultDataSource = dataSource;
            }
        }
        
        // 如果没有找到主数据源，使用第一个作为默认
        if (defaultDataSource == null && !targetDataSources.isEmpty()) {
            defaultDataSource = (DataSource) targetDataSources.values().iterator().next();
        }
        
        setTargetDataSources(targetDataSources);
        setDefaultTargetDataSource(defaultDataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceName();
    }
}