package com.company.fastweb.core.data.util;

import java.util.Collection;

import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * MyBatis-Plus工具类
 *
 * @author FastWeb
 */
public class MyBatisPlusUtils {

    /**
     * 构建分页对象
     */
    public static <T> Page<T> buildPage(long current, long size) {
        return new Page<>(current, size);
    }

    /**
     * 构建分页对象（带排序）
     */
    public static <T> Page<T> buildPage(long current, long size, String orderBy) {
        Page<T> page = new Page<>(current, size);
        if (StringUtils.isNotBlank(orderBy)) {
            // 简化排序处理，直接设置orderBy
            page.setOptimizeCountSql(true);
        }
        return page;
    }

    /**
     * 模糊查询条件
     */
    public static <T> LambdaQueryWrapper<T> likeIfNotBlank(LambdaQueryWrapper<T> wrapper, 
                                                          SFunction<T, ?> column, 
                                                          String value) {
        if (StringUtils.isNotBlank(value)) {
            wrapper.like(column, value);
        }
        return wrapper;
    }

    /**
     * 等于查询条件
     */
    public static <T, R> LambdaQueryWrapper<T> eqIfNotNull(LambdaQueryWrapper<T> wrapper, 
                                                          SFunction<T, R> column, 
                                                          R value) {
        if (value != null) {
            wrapper.eq(column, value);
        }
        return wrapper;
    }

    /**
     * 范围查询条件
     */
    public static <T> LambdaQueryWrapper<T> betweenIfNotNull(LambdaQueryWrapper<T> wrapper, 
                                                            SFunction<T, ?> column, 
                                                            Object start, 
                                                            Object end) {
        if (start != null && end != null) {
            wrapper.between(column, start, end);
        } else if (start != null) {
            wrapper.ge(column, start);
        } else if (end != null) {
            wrapper.le(column, end);
        }
        return wrapper;
    }

    /**
     * 在列表中查询条件
     */
    public static <T, R> LambdaQueryWrapper<T> inIfNotEmpty(LambdaQueryWrapper<T> wrapper, 
                                                           SFunction<T, R> column, 
                                                           Collection<R> values) {
        if (!CollectionUtils.isEmpty(values)) {
            wrapper.in(column, values);
        }
        return wrapper;
    }

    /**
     * 构建查询条件包装器
     */
    public static <T> LambdaQueryWrapper<T> buildWrapper() {
        return new LambdaQueryWrapper<>();
    }

    /**
     * 构建查询条件包装器（带实体）
     */
    public static <T> QueryWrapper<T> buildWrapper(T entity) {
        return new QueryWrapper<>(entity);
    }

    /**
     * 检查分页结果是否为空
     */
    public static <T> boolean isEmpty(IPage<T> page) {
        return page == null || CollectionUtils.isEmpty(page.getRecords());
    }

    /**
     * 检查分页结果是否非空
     */
    public static <T> boolean isNotEmpty(IPage<T> page) {
        return !isEmpty(page);
    }
}