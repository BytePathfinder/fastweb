package com.company.fastweb.core.common.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author fastweb
 */
@Getter
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    public BizException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BizException(String message) {
        this("BIZ_ERROR", message);
    }

    public BizException(String message, Throwable cause) {
        this("BIZ_ERROR", message, cause);
    }
}