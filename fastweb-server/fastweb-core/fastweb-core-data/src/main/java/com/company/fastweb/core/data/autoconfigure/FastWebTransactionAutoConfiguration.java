package com.company.fastweb.core.data.autoconfigure;

import com.company.fastweb.core.data.datasource.DynamicDataSource;
import com.company.fastweb.core.data.properties.FastWebDataProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * FastWeb 事务管理自动配置类
 */
@AutoConfiguration
@EnableTransactionManagement
@ConditionalOnClass({PlatformTransactionManager.class})
@EnableConfigurationProperties(FastWebDataProperties.class)
@org.springframework.boot.autoconfigure.AutoConfigureAfter({FastWebDataAutoConfiguration.class})
public class FastWebTransactionAutoConfiguration {
    
    private final FastWebDataProperties properties;
    
    public FastWebTransactionAutoConfiguration(FastWebDataProperties properties) {
        this.properties = properties;
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.data", name = "enabled", havingValue = "true", matchIfMissing = true)
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        return transactionManager;
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.data", name = "enabled", havingValue = "true", matchIfMissing = true)
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setTimeout(properties.getTransaction().getDefaultTimeout());
        // 在Spring 6.1+中，回滚规则通过TransactionDefinition设置
        // 默认行为：RuntimeException和Error会触发回滚
        return template;
    }
}