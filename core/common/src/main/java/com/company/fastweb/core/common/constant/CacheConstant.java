package com.company.fastweb.core.common.constant;

/**
 * 缓存常量
 */
public final class CacheConstant {

    /**
     * 用户缓存
     */
    public static final String USER_CACHE = "user:cache:";

    /**
     * 用户缓存过期时间（分钟）
     */
    public static final long USER_EXPIRE = 30;

    /**
     * 字典缓存
     */
    public static final String DICT_CACHE = "dict:cache:";

    /**
     * 字典缓存过期时间（分钟）
     */
    public static final long DICT_EXPIRE = 60;

    /**
     * 验证码缓存
     */
    public static final String CAPTCHA_CACHE = "captcha:cache:";

    /**
     * 验证码缓存过期时间（分钟）
     */
    public static final long CAPTCHA_EXPIRE = 5;

    /**
     * 限流缓存
     */
    public static final String RATE_LIMIT_CACHE = "rate:limit:";

    /**
     * 限流缓存过期时间（秒）
     */
    public static final long RATE_LIMIT_EXPIRE = 1;

    private CacheConstant() {
        throw new IllegalStateException("Utility class");
    }
}