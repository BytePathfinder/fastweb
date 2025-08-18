package com.company.fastweb.core.exception.model;

import com.company.fastweb.core.common.constant.CommonConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 统一响应结果
 *
 * @author FastWeb
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 链路追踪ID
     */
    private String traceId;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 成功响应
     */
    public static <T> ApiResult<T> success() {
        return success(null);
    }

    /**
     * 成功响应
     */
    public static <T> ApiResult<T> success(T data) {
        return success(CommonConstants.SUCCESS_MSG, data);
    }

    /**
     * 成功响应
     */
    public static <T> ApiResult<T> success(String message, T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setCode(CommonConstants.SUCCESS);
        result.setMessage(message);
        result.setData(data);
        result.setTraceId(MDC.get("traceId"));
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 失败响应
     */
    public static <T> ApiResult<T> error(String message) {
        return error(CommonConstants.FAIL, message);
    }

    /**
     * 失败响应
     */
    public static <T> ApiResult<T> error(String code, String message) {
        return error(code, message, null);
    }

    /**
     * 失败响应
     */
    public static <T> ApiResult<T> error(String code, String message, T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        result.setTraceId(MDC.get("traceId"));
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return CommonConstants.SUCCESS.equals(this.code);
    }

    /**
     * 判断是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }
}