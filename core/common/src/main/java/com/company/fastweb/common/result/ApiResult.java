package com.company.fastweb.common.result;

import com.company.fastweb.common.enums.ResultCode;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
public class ApiResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final T data;
    private final String traceId;

    public ApiResult(String code, String message, T data, String traceId) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.traceId = traceId;
    }

    public static <T> ApiResult<T> success() {
        return success(null);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),
                data,
                null
        );
    }

    public static <T> ApiResult<T> error(String message) {
        return error(ResultCode.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    public static <T> ApiResult<T> error(String code, String message) {
        return new ApiResult<>(code, message, null, null);
    }

    public static <T> ApiResult<T> error(ResultCode resultCode) {
        return new ApiResult<>(
                resultCode.getCode(),
                resultCode.getMessage(),
                null,
                null
        );
    }

    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }
}