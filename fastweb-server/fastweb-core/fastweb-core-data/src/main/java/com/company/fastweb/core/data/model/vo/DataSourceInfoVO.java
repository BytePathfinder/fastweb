package com.company.fastweb.core.data.model.vo;

import lombok.Data;

/**
 * 数据源信息视图对象
 *
 * @author FastWeb
 */
@Data
public class DataSourceInfoVO {

    /**
     * 数据源标识
     */
    private String key;

    /**
     * 数据库连接URL
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 驱动类名
     */
    private String driverClassName;

    /**
     * 连接池名称
     */
    private String poolName;

    /**
     * 是否为当前激活的数据源
     */
    private boolean active;

    /**
     * 最大连接池大小
     */
    private Integer maximumPoolSize;

    /**
     * 最小空闲连接数
     */
    private Integer minimumIdle;

    /**
     * 连接超时时间（毫秒）
     */
    private Long connectionTimeout;

    /**
     * 空闲超时时间（毫秒）
     */
    private Long idleTimeout;

    /**
     * 连接最大生命周期（毫秒）
     */
    private Long maxLifetime;

    /**
     * 当前活跃连接数
     */
    private Integer activeConnections;

    /**
     * 当前空闲连接数
     */
    private Integer idleConnections;

    /**
     * 总连接数
     */
    private Integer totalConnections;

    /**
     * 等待连接的线程数
     */
    private Integer threadsAwaitingConnection;

    /**
     * 连接池健康状态
     */
    private String healthStatus;

    /**
     * 最后测试时间
     */
    private String lastTestTime;

    /**
     * 数据源类型
     */
    private String dataSourceType;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 备注信息
     */
    private String remark;
}