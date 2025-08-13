package com.company.fastweb.common.base;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础表单对象
 */
@Data
public abstract class BaseForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    @Min(value = 1, message = "页码最小为1")
    private final Integer pageNum = 1;

    /**
     * 页大小
     */
    @Min(value = 1, message = "页大小最小为1")
    @Max(value = 1000, message = "页大小最大为1000")
    private final Integer pageSize = 20;
    /**
     * 排序方式（asc/desc）
     */
    private final String orderType = "asc";
    /**
     * 排序字段
     */
    private String orderBy;
}