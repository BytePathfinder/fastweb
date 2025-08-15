package com.company.fastweb.core.data.autoconfigure;

import com.company.fastweb.core.data.properties.FastWebDataProperties;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * FastWeb 数据库迁移自动配置类
 */
@AutoConfiguration
@ConditionalOnClass(Flyway.class)
@ConditionalOnProperty(prefix = "fastweb.data", name = "migration.enabled", havingValue = "true")
@EnableConfigurationProperties(FastWebDataProperties.class)
@org.springframework.boot.autoconfigure.AutoConfigureAfter(FastWebDataAutoConfiguration.class)
public class FastWebMigrationAutoConfiguration {

    private final FastWebDataProperties properties;

    public FastWebMigrationAutoConfiguration(FastWebDataProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();

        // 自动执行迁移
        flyway.migrate();

        return flyway;
    }
}