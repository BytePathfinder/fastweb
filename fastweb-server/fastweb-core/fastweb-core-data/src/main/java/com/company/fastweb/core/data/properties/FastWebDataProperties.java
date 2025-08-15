package com.company.fastweb.core.data.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * FastWeb 数据访问配置属性
 */
@Data
@ConfigurationProperties(prefix = "fastweb.data")
public class FastWebDataProperties {
    
    /**
     * 是否启用数据访问功能
     */
    private boolean enabled = true;
    
    /**
     * 数据源配置
     */
    private Map<String, DataSourceConfig> datasource = new HashMap<>();
    
    /**
     * 主数据源名称
     */
    private String primary = "master";
    
    /**
     * MyBatis-Plus 配置
     */
    private MyBatisPlusConfig mybatisPlus = new MyBatisPlusConfig();
    
    /**
     * 事务配置
     */
    private TransactionConfig transaction = new TransactionConfig();
    
    /**
     * 审计配置
     */
    private AuditConfig audit = new AuditConfig();
    
    /**
     * 数据源配置
     */
    @Data
    public static class DataSourceConfig {
        /**
         * 数据库连接URL
         */
        private String url;
        
        /**
         * 数据库用户名
         */
        private String username;
        
        /**
         * 数据库密码
         */
        private String password;
        
        /**
         * 数据库驱动类名
         */
        private String driverClassName = "com.mysql.cj.jdbc.Driver";
        
        /**
         * HikariCP连接池配置
         */
        private HikariConfig hikari = new HikariConfig();
    }
    
    /**
     * MyBatis-Plus配置
     */
    @Data
    public static class MyBatisPlusConfig {
        /**
         * 分页配置
         */
        private PaginationConfig pagination = new PaginationConfig();
        
        /**
         * 多租户配置
         */
        private TenantConfig tenant = new TenantConfig();
        
        /**
         * Mapper XML文件位置
         */
        private String mapperLocations = "classpath*:/mapper/**/*.xml";
        
        /**
         * 实体类型别名包
         */
        private String typeAliasesPackage;
        
        /**
         * 分页配置
         */
        @Data
        public static class PaginationConfig {
            /**
             * 是否启用分页
             */
            private boolean enabled = true;
            
            /**
             * 最大分页限制
             */
            private Long maxLimit = 1000L;
        }
        
        /**
         * 多租户配置
         */
        @Data
        public static class TenantConfig {
            /**
             * 是否启用多租户
             */
            private boolean enabled = false;
            
            /**
             * 租户ID列名
             */
            private String tenantIdColumn = "tenant_id";
        }
    }
    
    /**
     * 事务配置
     */
    @Data
    public static class TransactionConfig {
        /**
         * 默认事务超时时间（秒）
         */
        private int defaultTimeout = 30;
        
        /**
         * 是否对所有异常都进行回滚
         */
        private boolean rollbackForAnyException = false;
    }
    
    /**
     * 审计配置
     */
    @Data
    public static class AuditConfig {
        /**
         * 是否启用审计
         */
        private boolean enabled = true;
        
        /**
         * 创建人字段名
         */
        private String createByField = "createBy";
        
        /**
         * 创建时间字段名
         */
        private String createTimeField = "createTime";
        
        /**
         * 最后修改人字段名
         */
        private String updateByField = "updateBy";
        
        /**
         * 最后修改时间字段名
         */
        private String updateTimeField = "updateTime";
    }
}