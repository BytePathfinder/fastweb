package com.company.fastweb.core.data.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * SQL执行结果视图对象
 *
 * @author FastWeb
 */
@Data
public class SqlExecuteResultVO {

    /**
     * 执行是否成功
     */
    private boolean success;

    /**
     * 执行结果消息
     */
    private String message;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 影响行数
     */
    private Integer rowCount;

    /**
     * 查询结果数据（SELECT语句）
     */
    private List<Map<String, Object>> data;

    /**
     * 列信息
     */
    private List<Map<String, Object>> columns;

    /**
     * SQL语句类型
     */
    private String sqlType;

    /**
     * 执行的SQL语句
     */
    private String executedSql;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误详情
     */
    private String errorDetail;

    /**
     * 警告信息
     */
    private List<String> warnings;

    /**
     * 执行计划
     */
    private List<Map<String, Object>> executionPlan;

    /**
     * 统计信息
     */
    private Map<String, Object> statistics;

    /**
     * 是否有更多结果
     */
    private boolean hasMoreResults;

    /**
     * 总记录数（分页查询时）
     */
    private Long totalCount;

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 执行用户
     */
    private String executeUser;

    /**
     * 执行数据源
     */
    private String dataSource;

    /**
     * 事务ID
     */
    private String transactionId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 备注信息
     */
    private String remark;
}