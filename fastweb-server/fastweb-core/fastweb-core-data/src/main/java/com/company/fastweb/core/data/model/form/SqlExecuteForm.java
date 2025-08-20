package com.company.fastweb.core.data.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * SQL执行表单
 *
 * @author FastWeb
 */
@Data
public class SqlExecuteForm {

    /**
     * SQL语句
     */
    @NotBlank(message = "SQL语句不能为空")
    @Size(max = 10000, message = "SQL语句长度不能超过10000字符")
    private String sql;

    /**
     * SQL参数
     */
    private Map<String, Object> parameters;

    /**
     * 是否返回执行计划
     */
    private Boolean includeExecutionPlan = false;

    /**
     * 查询超时时间（秒）
     */
    private Integer timeoutSeconds = 30;

    /**
     * 最大返回行数（SELECT语句）
     */
    private Integer maxRows = 1000;

    /**
     * 是否格式化结果
     */
    private Boolean formatResult = true;

    /**
     * 执行模式（EXECUTE: 直接执行, EXPLAIN: 仅解释执行计划）
     */
    private String executeMode = "EXECUTE";

    /**
     * 数据源标识（可选，不指定则使用当前数据源）
     */
    private String dataSourceKey;

    /**
     * 备注信息
     */
    private String remark;
}