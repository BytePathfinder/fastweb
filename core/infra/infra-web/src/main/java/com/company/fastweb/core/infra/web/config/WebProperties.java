package com.company.fastweb.core.infra.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web配置属性
 *
 * @author fastweb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.infra.web")
public class WebProperties {

    /**
     * 是否启用Web基础设施
     */
    private boolean enabled = true;

    /**
     * API日志配置
     */
    private ApiLog apiLog = new ApiLog();

    /**
     * CORS配置
     */
    private Cors cors = new Cors();

    @Data
    public static class ApiLog {
        /**
         * 是否启用API日志
         */
        private boolean enabled = true;

        /**
         * 是否记录请求体
         */
        private boolean logRequestBody = true;

        /**
         * 是否记录响应体
         */
        private boolean logResponseBody = true;

        /**
         * 最大日志长度
         */
        private int maxLogLength = 1000;
    }

    @Data
    public static class Cors {
        /**
         * 是否启用CORS
         */
        private boolean enabled = true;

        /**
         * 允许的源
         */
        private String[] allowedOrigins = {"*"};

        /**
         * 允许的方法
         */
        private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

        /**
         * 允许的头部
         */
        private String[] allowedHeaders = {"*"};

        /**
         * 是否允许凭证
         */
        private boolean allowCredentials = true;
    }
}