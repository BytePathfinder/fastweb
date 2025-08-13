package com.company.fastweb.core.common.constant;

/**
 * 系统常量
 */
public final class SystemConstant {

    /**
     * 链路追踪ID
     */
    public static final String TRACE_ID = "traceId";

    /**
     * 用户ID
     */
    public static final String USER_ID = "userId";

    /**
     * 用户名
     */
    public static final String USER_NAME = "userName";

    /**
     * 默认页码
     */
    public static final String DEFAULT_PAGE_NUM = "1";

    /**
     * 默认页大小
     */
    public static final String DEFAULT_PAGE_SIZE = "20";

    /**
     * 最大页大小
     */
    public static final int MAX_PAGE_SIZE = 1000;

    /**
     * 默认字符集
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 时间格式
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 时间格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    private SystemConstant() {
        throw new IllegalStateException("Utility class");
    }
}