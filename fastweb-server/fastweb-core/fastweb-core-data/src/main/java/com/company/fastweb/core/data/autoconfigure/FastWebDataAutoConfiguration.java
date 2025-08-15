package com.company.fastweb.core.data.autoconfigure;

import com.company.fastweb.core.data.datasource.DataSourceContextHolder;
import com.company.fastweb.core.data.datasource.DynamicDataSource;
import com.company.fastweb.core.data.datasource.aspect.DSAspect;
import com.company.fastweb.core.data.properties.FastWebDataProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * FastWeb 数据源自动配置类
 */
@AutoConfiguration
@EnableConfigurationProperties(FastWebDataProperties.class)
@ConditionalOnClass(DataSource.class)
@org.springframework.boot.autoconfigure.AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class FastWebDataAutoConfiguration {
    
    private final FastWebDataProperties properties;
    
    public FastWebDataAutoConfiguration(FastWebDataProperties properties) {
        this.properties = properties;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DataSourceContextHolder dataSourceContextHolder() {
        return new DataSourceContextHolder();
    }
    
    @Bean
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.data", name = "enabled", havingValue = "true", matchIfMissing = true)
    public DynamicDataSource dynamicDataSource() {
        return new DynamicDataSource(properties.getDatasource(), properties.getPrimary());
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.data", name = "enabled", havingValue = "true", matchIfMissing = true)
    public DSAspect dsAspect() {
        return new DSAspect();
    }
}