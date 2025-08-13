package com.company.fastweb.core.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 日期工具类
 */
public final class DateUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 格式化日期时间
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return null;
        }
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    /**
     * 格式化日期
     */
    public static String formatDate(LocalDate date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * 格式化时间
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return null;
        }
        return TIME_FORMATTER.format(dateTime);
    }

    /**
     * 解析日期时间字符串
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (Objects.isNull(dateTimeStr)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
    }

    /**
     * 解析日期字符串
     */
    public static LocalDate parseDate(String dateStr) {
        if (Objects.isNull(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    /**
     * 获取当前日期时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     */
    public static LocalDate today() {
        return LocalDate.now();
    }
}