package com.company.fastweb.core.common.model;

import com.company.fastweb.core.common.constant.CommonConstants;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询基类
 *
 * @author FastWeb
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = CommonConstants.DEFAULT_PAGE_SIZE;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向：asc/desc
     */
    private String orderDirection = "desc";

    /**
     * 获取偏移量
     */
    public Integer getOffset() {
        return (current - 1) * size;
    }

    /**
     * 设置分页参数
     */
    public void setPageParams(Integer current, Integer size) {
        this.current = current != null && current > 0 ? current : 1;
        this.size = size != null && size > 0 ? 
            Math.min(size, CommonConstants.MAX_PAGE_SIZE) : CommonConstants.DEFAULT_PAGE_SIZE;
    }

    /**
     * 获取排序SQL片段
     */
    public String getOrderBySql() {
        if (orderBy == null || orderBy.trim().isEmpty()) {
            return "";
        }
        return orderBy + " " + ("asc".equalsIgnoreCase(orderDirection) ? "ASC" : "DESC");
    }
}