package com.company.fastweb.core.data.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * FastWeb 数据访问配置属性
 * 
 * @author FastWeb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.data")
public class FastWebDataProperties {

    /**
     * 主数据源名称
     */
    private String primary = "master";

    /**
     * MyBatis-Plus 配置
     */
    private MyBatisPlusProperties mybatisPlus = new MyBatisPlusProperties();

    /**
     * 数据源监控配置
     */
    private MonitorProperties monitor = new MonitorProperties();

    /**
     * MyBatis-Plus 配置
     */
    @Data
    public static class MyBatisPlusProperties {
        
        /**
         * 多租户配置
         */
        private TenantProperties tenant = new TenantProperties();
        
        /**
         * 分页配置
         */
        private PageProperties page = new PageProperties();
    }

    /**
     * 租户配置
     */
    @Data
    public static class TenantProperties {
        
        /**
         * 是否启用多租户
         */
        private boolean enabled = true;
        
        /**
         * 租户模式
         */
        private TenantMode tenantMode = TenantMode.COLUMN;
        
        /**
         * 租户ID列名
         */
        private String tenantIdColumn = "tenant_id";
        
        /**
         * 忽略的表名列表
         */
        private List<String> ignoreTables = List.of();
    }

    /**
     * 分页配置
     */
    @Data
    public static class PageProperties {
        
        /**
         * 默认页大小
         */
        private long defaultSize = 10;
        
        /**
         * 最大页大小
         */
        private long maxSize = 500;
    }

    /**
     * 监控配置
     */
    @Data
    public static class MonitorProperties {
        
        /**
         * 是否启用监控
         */
        private boolean enabled = true;
        
        /**
         * 慢查询阈值
         */
        private Duration slowQueryThreshold = Duration.ofSeconds(3);
    }

    /**
     * 租户模式枚举
     */
    public enum TenantMode {
        /**
         * 字段模式 - 通过字段区分租户
         */
        COLUMN,
        
        /**
         * 数据库模式 - 通过不同数据库区分租户
         */
        DATABASE,
        
        /**
         * 数据表模式 - 通过不同表区分租户
         */
        TABLE
    }
}