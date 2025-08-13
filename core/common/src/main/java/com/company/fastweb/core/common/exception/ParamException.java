package com.company.fastweb.core.common.exception;

import com.company.fastweb.core.common.enums.ResultCode;

/**
 * 参数异常
 */
public class ParamException extends BizException {

    public ParamException(String message) {
        super(ResultCode.PARAM_ERROR.getCode(), message);
    }

    public ParamException(String message, Throwable cause) {
        super(ResultCode.PARAM_ERROR.getCode(), message, cause);
    }
}