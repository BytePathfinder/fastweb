package com.company.fastweb.core.common.constants;

/**
 * 通用常量
 *
 * @author fastweb
 */
public class CommonConstants {

    /**
     * 成功状态码
     */
    public static final String SUCCESS_CODE = "SUCCESS";

    /**
     * 失败状态码
     */
    public static final String FAIL_CODE = "FAIL";

    /**
     * 系统错误状态码
     */
    public static final String SYSTEM_ERROR_CODE = "SYSTEM_ERROR";

    /**
     * 参数校验错误状态码
     */
    public static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";

    /**
     * 未授权状态码
     */
    public static final String UNAUTHORIZED_CODE = "UNAUTHORIZED";

    /**
     * 禁止访问状态码
     */
    public static final String FORBIDDEN_CODE = "FORBIDDEN";

    /**
     * 资源不存在状态码
     */
    public static final String NOT_FOUND_CODE = "NOT_FOUND";

    private CommonConstants() {
        // 工具类，禁止实例化
    }
}