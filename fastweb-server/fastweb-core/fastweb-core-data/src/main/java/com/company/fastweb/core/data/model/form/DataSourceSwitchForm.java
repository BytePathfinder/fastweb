package com.company.fastweb.core.data.model.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 数据源切换表单
 *
 * @author FastWeb
 */
@Data
public class DataSourceSwitchForm {

    /**
     * 数据源标识
     */
    @NotBlank(message = "数据源标识不能为空")
    private String dataSourceKey;

    /**
     * 是否强制切换（忽略健康检查）
     */
    private Boolean forceSwitch = false;

    /**
     * 切换原因
     */
    private String reason;

    /**
     * 备注信息
     */
    private String remark;
}