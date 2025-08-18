package com.company.fastweb.core.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author FastWeb
 */
@Getter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误消息
     */
    private final String message;

    public BizException(String message) {
        super(message);
        this.code = "BIZ_ERROR";
        this.message = message;
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.code = "BIZ_ERROR";
        this.message = message;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 创建业务异常
     */
    public static BizException of(String message) {
        return new BizException(message);
    }

    /**
     * 创建业务异常
     */
    public static BizException of(String code, String message) {
        return new BizException(code, message);
    }

    /**
     * 创建业务异常
     */
    public static BizException of(String message, Throwable cause) {
        return new BizException(message, cause);
    }

    /**
     * 创建业务异常
     */
    public static BizException of(String code, String message, Throwable cause) {
        return new BizException(code, message, cause);
    }
}