package com.company.fastweb.core.exception;

import lombok.Getter;

/**
 * 系统异常
 *
 * @author FastWeb
 */
@Getter
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误消息
     */
    private final String message;

    public SystemException(String message) {
        super(message);
        this.code = "SYSTEM_ERROR";
        this.message = message;
    }

    public SystemException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
        this.code = "SYSTEM_ERROR";
        this.message = message;
    }

    public SystemException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 创建系统异常
     */
    public static SystemException of(String message) {
        return new SystemException(message);
    }

    /**
     * 创建系统异常
     */
    public static SystemException of(String code, String message) {
        return new SystemException(code, message);
    }

    /**
     * 创建系统异常
     */
    public static SystemException of(String message, Throwable cause) {
        return new SystemException(message, cause);
    }

    /**
     * 创建系统异常
     */
    public static SystemException of(String code, String message, Throwable cause) {
        return new SystemException(code, message, cause);
    }
}