package com.company.fastweb.core.common.utils;

import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public final class StringUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 去除字符串首尾空格
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 判断是否为邮箱格式
     */
    public static boolean isEmail(String email) {
        return !isEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 判断是否为手机号格式
     */
    public static boolean isPhone(String phone) {
        return !isEmpty(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 手机号脱敏
     */
    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() != 11) {
            return phone;
        }
        return mask(phone, 3, 7);
    }

    /**
     * 字符串脱敏
     */
    public static String mask(String str, int start, int end) {
        if (isEmpty(str)) {
            return str;
        }
        int length = str.length();
        if (start < 0 || end > length || start >= end) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str, 0, start);
        for (int i = start; i < end; i++) {
            sb.append('*');
        }
        sb.append(str.substring(end));
        return sb.toString();
    }

    /**
     * 身份证号脱敏
     */
    public static String maskIdCard(String idCard) {
        if (isEmpty(idCard) || idCard.length() < 15) {
            return idCard;
        }
        return mask(idCard, 6, 14);
    }

    /**
     * 邮箱脱敏
     */
    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf('@');
        String prefix = email.substring(0, atIndex);
        String suffix = email.substring(atIndex);

        if (prefix.length() <= 1) {
            return "*" + suffix;
        } else if (prefix.length() == 2) {
            return prefix.charAt(0) + "*" + suffix;
        } else {
            return prefix.charAt(0) + "***" + prefix.charAt(prefix.length() - 1) + suffix;
        }
    }
}