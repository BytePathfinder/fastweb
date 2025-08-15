package com.company.fastweb.core.data.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.fastweb.core.data.properties.FastWebDataProperties;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 分页工具类
 * 借鉴RuoYi的分页处理设计，提供统一的分页参数处理
 */
public class PageUtils {
    
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    
    /**
     * 构建分页对象
     */
    public static <T> Page<T> buildPage(Integer pageNum, Integer pageSize) {
        return new Page<>(Objects.requireNonNullElse(pageNum, 1), 
                         Objects.requireNonNullElse(pageSize, 10));
    }
    
    /**
     * 构建分页对象（带排序）
     */
    public static <T> Page<T> buildPage(Integer pageNum, Integer pageSize, String orderByColumn, String isAsc) {
        Page<T> page = buildPage(pageNum, pageSize);
        
        if (StringUtils.hasText(orderByColumn)) {
            if (ASC.equalsIgnoreCase(isAsc)) {
                page.setAsc(orderByColumn);
            } else if (DESC.equalsIgnoreCase(isAsc)) {
                page.setDesc(orderByColumn);
            }
        }
        
        return page;
    }
    
    /**
     * 构建分页对象（带安全排序）
     * 防止SQL注入
     */
    public static <T> Page<T> buildSafePage(Integer pageNum, Integer pageSize, String orderByColumn, String isAsc) {
        Page<T> page = buildPage(pageNum, pageSize);
        
        if (StringUtils.hasText(orderByColumn)) {
            // 安全检查：只允许字母、数字、下划线
            String safeColumn = orderByColumn.replaceAll("[^a-zA-Z0-9_]", "");
            if (!safeColumn.isEmpty()) {
                if (ASC.equalsIgnoreCase(isAsc)) {
                    page.setAsc(safeColumn);
                } else if (DESC.equalsIgnoreCase(isAsc)) {
                    page.setDesc(safeColumn);
                }
            }
        }
        
        return page;
    }
    
    /**
     * 转换分页结果
     */
    public static <T> IPage<T> convertPage(IPage<?> source, java.util.function.Function<Object, T> converter) {
        return source.convert(converter);
    }
}