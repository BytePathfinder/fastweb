package com.company.fastweb.core.cache.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Redis键构建工具类
 *
 * @author FastWeb
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyBuilder {

    private static final String SEPARATOR = ":";
    private static final String WILDCARD = "*";

    /**
     * 构建缓存键
     */
    public static String build(String... parts) {
        if (parts == null || parts.length == 0) {
            throw new IllegalArgumentException("Key parts cannot be null or empty");
        }

        StringBuilder key = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (StringUtils.hasText(parts[i])) {
                if (i > 0) {
                    key.append(SEPARATOR);
                }
                key.append(parts[i]);
            }
        }
        return key.toString();
    }

    /**
     * 构建带前缀的缓存键
     */
    public static String buildWithPrefix(String prefix, String... parts) {
        if (!StringUtils.hasText(prefix)) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }

        String[] newParts = new String[parts.length + 1];
        newParts[0] = prefix;
        System.arraycopy(parts, 0, newParts, 1, parts.length);
        return build(newParts);
    }

    /**
     * 构建用户缓存键
     */
    public static String user(String... parts) {
        return buildWithPrefix("user", parts);
    }

    /**
     * 构建系统配置缓存键
     */
    public static String config(String... parts) {
        return buildWithPrefix("config", parts);
    }

    /**
     * 构建字典缓存键
     */
    public static String dict(String... parts) {
        return buildWithPrefix("dict", parts);
    }

    /**
     * 构建权限缓存键
     */
    public static String permission(String... parts) {
        return buildWithPrefix("permission", parts);
    }

    /**
     * 构建角色缓存键
     */
    public static String role(String... parts) {
        return buildWithPrefix("role", parts);
    }

    /**
     * 构建菜单缓存键
     */
    public static String menu(String... parts) {
        return buildWithPrefix("menu", parts);
    }

    /**
     * 构建部门缓存键
     */
    public static String dept(String... parts) {
        return buildWithPrefix("dept", parts);
    }

    /**
     * 构建验证码缓存键
     */
    public static String captcha(String... parts) {
        return buildWithPrefix("captcha", parts);
    }

    /**
     * 构建限流缓存键
     */
    public static String rateLimit(String... parts) {
        return buildWithPrefix("rate:limit", parts);
    }

    /**
     * 构建分布式锁键
     */
    public static String lock(String... parts) {
        return buildWithPrefix("lock", parts);
    }

    /**
     * 构建消息缓存键
     */
    public static String message(String... parts) {
        return buildWithPrefix("message", parts);
    }

    /**
     * 构建会话缓存键
     */
    public static String session(String... parts) {
        return buildWithPrefix("session", parts);
    }

    /**
     * 构建通配符模式键
     */
    public static String pattern(String prefix, String pattern) {
        if (!StringUtils.hasText(prefix)) {
            return pattern;
        }
        return prefix + SEPARATOR + pattern + WILDCARD;
    }

    /**
     * 获取键的前缀部分
     */
    public static String getPrefix(String key) {
        if (!StringUtils.hasText(key)) {
            return "";
        }
        int index = key.indexOf(SEPARATOR);
        return index > 0 ? key.substring(0, index) : key;
    }

    /**
     * 获取键的后缀部分
     */
    public static String getSuffix(String key) {
        if (!StringUtils.hasText(key)) {
            return "";
        }
        int index = key.lastIndexOf(SEPARATOR);
        return index > 0 ? key.substring(index + 1) : key;
    }
}