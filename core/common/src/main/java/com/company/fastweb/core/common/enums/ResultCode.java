package com.company.fastweb.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应结果枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("200", "操作成功"),
    BAD_REQUEST("400", "请求参数错误"),
    UNAUTHORIZED("401", "未认证"),
    FORBIDDEN("403", "无权限"),
    NOT_FOUND("404", "资源不存在"),
    METHOD_NOT_ALLOWED("405", "方法不允许"),
    CONFLICT("409", "资源冲突"),
    TOO_MANY_REQUESTS("429", "请求过于频繁"),
    INTERNAL_SERVER_ERROR("500", "系统内部错误"),
    SERVICE_UNAVAILABLE("503", "服务不可用"),

    // 业务错误码
    BUSINESS_ERROR("1000", "业务处理失败"),
    DATA_NOT_FOUND("1001", "数据不存在"),
    DATA_ALREADY_EXISTS("1002", "数据已存在"),
    PARAM_ERROR("1003", "参数校验失败"),
    OPERATION_NOT_ALLOWED("1004", "操作不允许"),

    // 用户相关错误码
    USER_NOT_FOUND("2001", "用户不存在"),
    USER_DISABLED("2002", "用户已被禁用"),
    USER_PASSWORD_ERROR("2003", "密码错误"),
    USER_LOGIN_EXPIRED("2004", "登录已过期"),

    // 文件相关错误码
    FILE_NOT_FOUND("3001", "文件不存在"),
    FILE_UPLOAD_ERROR("3002", "文件上传失败"),
    FILE_TYPE_ERROR("3003", "文件类型不支持"),
    FILE_SIZE_EXCEEDED("3004", "文件大小超出限制");

    private final String code;
    private final String message;
}