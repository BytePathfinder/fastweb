package com.company.fastweb.common.exception;

import com.company.fastweb.common.enums.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BizException extends RuntimeException {

    private final String code;

    public BizException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}