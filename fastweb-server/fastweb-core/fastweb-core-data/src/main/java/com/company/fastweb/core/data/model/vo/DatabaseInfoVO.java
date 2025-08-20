package com.company.fastweb.core.data.model.vo;

import lombok.Data;

/**
 * 数据库信息视图对象
 *
 * @author FastWeb
 */
@Data
public class DatabaseInfoVO {

    /**
     * 数据库产品名称
     */
    private String databaseProductName;

    /**
     * 数据库产品版本
     */
    private String databaseProductVersion;

    /**
     * 驱动名称
     */
    private String driverName;

    /**
     * 驱动版本
     */
    private String driverVersion;

    /**
     * 数据库连接URL
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 目录名称
     */
    private String catalogName;

    /**
     * 模式名称
     */
    private String schemaName;

    /**
     * 数据库大小
     */
    private String databaseSize;

    /**
     * 表数量
     */
    private Integer tableCount;

    /**
     * 视图数量
     */
    private Integer viewCount;

    /**
     * 索引数量
     */
    private Integer indexCount;

    /**
     * 存储过程数量
     */
    private Integer procedureCount;

    /**
     * 函数数量
     */
    private Integer functionCount;

    /**
     * 触发器数量
     */
    private Integer triggerCount;

    /**
     * 字符集
     */
    private String characterSet;

    /**
     * 排序规则
     */
    private String collation;

    /**
     * 时区
     */
    private String timeZone;

    /**
     * 服务器版本
     */
    private String serverVersion;

    /**
     * 服务器启动时间
     */
    private String serverUptime;

    /**
     * 最大连接数
     */
    private Integer maxConnections;

    /**
     * 当前连接数
     */
    private Integer currentConnections;

    /**
     * 缓冲池大小
     */
    private String bufferPoolSize;

    /**
     * 查询缓存大小
     */
    private String queryCacheSize;

    /**
     * 临时表大小
     */
    private String tmpTableSize;

    /**
     * 最大堆表大小
     */
    private String maxHeapTableSize;

    /**
     * 数据目录
     */
    private String dataDir;

    /**
     * 日志文件大小
     */
    private String logFileSize;

    /**
     * 慢查询日志状态
     */
    private String slowQueryLogStatus;

    /**
     * 慢查询时间阈值
     */
    private String longQueryTime;

    /**
     * 二进制日志状态
     */
    private String binlogStatus;

    /**
     * 事务隔离级别
     */
    private String transactionIsolation;

    /**
     * 自动提交状态
     */
    private String autoCommit;

    /**
     * 存储引擎
     */
    private String defaultStorageEngine;

    /**
     * SQL模式
     */
    private String sqlMode;

    /**
     * 数据库状态
     */
    private String status;

    /**
     * 最后检查时间
     */
    private String lastCheckTime;
}