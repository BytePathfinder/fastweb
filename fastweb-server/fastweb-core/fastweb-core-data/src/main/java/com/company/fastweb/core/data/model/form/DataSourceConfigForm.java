package com.company.fastweb.core.data.model.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据源配置表单
 *
 * @author FastWeb
 */
@Data
public class DataSourceConfigForm {

    /**
     * 数据源标识
     */
    @NotBlank(message = "数据源标识不能为空")
    private String dataSourceKey;

    /**
     * 数据库连接URL
     */
    @NotBlank(message = "数据库连接URL不能为空")
    private String url;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 驱动类名
     */
    @NotBlank(message = "驱动类名不能为空")
    private String driverClassName;

    /**
     * 连接池名称
     */
    private String poolName;

    /**
     * 最大连接池大小
     */
    @NotNull(message = "最大连接池大小不能为空")
    @Min(value = 1, message = "最大连接池大小不能小于1")
    @Max(value = 100, message = "最大连接池大小不能大于100")
    private Integer maximumPoolSize = 10;

    /**
     * 最小空闲连接数
     */
    @NotNull(message = "最小空闲连接数不能为空")
    @Min(value = 0, message = "最小空闲连接数不能小于0")
    private Integer minimumIdle = 5;

    /**
     * 连接超时时间（毫秒）
     */
    @NotNull(message = "连接超时时间不能为空")
    @Min(value = 1000, message = "连接超时时间不能小于1000毫秒")
    @Max(value = 300000, message = "连接超时时间不能大于300000毫秒")
    private Long connectionTimeout = 30000L;

    /**
     * 空闲超时时间（毫秒）
     */
    @NotNull(message = "空闲超时时间不能为空")
    @Min(value = 60000, message = "空闲超时时间不能小于60000毫秒")
    private Long idleTimeout = 600000L;

    /**
     * 连接最大生命周期（毫秒）
     */
    @NotNull(message = "连接最大生命周期不能为空")
    @Min(value = 300000, message = "连接最大生命周期不能小于300000毫秒")
    private Long maxLifetime = 1800000L;

    /**
     * 连接测试查询语句
     */
    private String connectionTestQuery = "SELECT 1";

    /**
     * 是否自动提交
     */
    private Boolean autoCommit = true;

    /**
     * 是否只读
     */
    private Boolean readOnly = false;

    /**
     * 事务隔离级别
     */
    private String transactionIsolation;

    /**
     * 连接初始化SQL
     */
    private String connectionInitSql;

    /**
     * 数据源类型
     */
    private String dataSourceType = "HikariCP";

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 备注信息
     */
    private String remark;
}