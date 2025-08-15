package com.company.fastweb.core.data.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 分页工具类
 * 基于MyBatis-Plus的分页处理，提供统一的分页参数处理
 */
public class PageUtils {
    
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final Pattern SAFE_COLUMN_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 500;
    
    /**
     * 构建分页对象
     */
    public static <T> Page<T> buildPage(Integer pageNum, Integer pageSize) {
        int page = Objects.requireNonNullElse(pageNum, 1);
        int size = Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE);
        size = Math.min(size, MAX_PAGE_SIZE);
        return new Page<>(Math.max(page, 1), Math.max(size, 1));
    }
    
    /**
     * 构建分页对象（带排序）
     */
    public static <T> Page<T> buildPage(Integer pageNum, Integer pageSize, String orderByColumn, String isAsc) {
        Page<T> page = buildPage(pageNum, pageSize);
        
        if (StringUtils.hasText(orderByColumn)) {
            addOrder(page, orderByColumn, isAsc);
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
            if (!safeColumn.isEmpty() && SAFE_COLUMN_PATTERN.matcher(safeColumn).matches()) {
                addOrder(page, safeColumn, isAsc);
            }
        }
        
        return page;
    }
    
    /**
     * 添加排序
     */
    private static <T> void addOrder(Page<T> page, String column, String direction) {
        if (ASC.equalsIgnoreCase(direction)) {
            page.addOrder(OrderItem.asc(camelToUnderline(column)));
        } else if (DESC.equalsIgnoreCase(direction)) {
            page.addOrder(OrderItem.desc(camelToUnderline(column)));
        }
    }
    
    /**
     * 驼峰命名转下划线命名
     */
    private static String camelToUnderline(String param) {
        if (param == null || param.trim().isEmpty()) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 转换分页结果
     */
    public static <T, R> IPage<R> convertPage(IPage<T> source, java.util.function.Function<T, R> converter) {
        return source.convert(converter);
    }
}