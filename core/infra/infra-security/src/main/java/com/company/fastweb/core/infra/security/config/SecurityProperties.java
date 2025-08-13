package com.company.fastweb.core.infra.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 安全配置属性
 *
 * @author fastweb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.infra.security")
public class SecurityProperties {

    /**
     * 是否启用安全基础设施
     */
    private boolean enabled = true;

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * 密码配置
     */
    private Password password = new Password();

    @Data
    public static class Jwt {
        /**
         * JWT密钥
         */
        private String secret = "fastweb-jwt-secret-key-2024";

        /**
         * JWT过期时间（秒）
         */
        private long expiration = 86400; // 24小时

        /**
         * JWT刷新时间（秒）
         */
        private long refreshExpiration = 604800; // 7天

        /**
         * JWT头部前缀
         */
        private String tokenPrefix = "Bearer ";

        /**
         * JWT头部名称
         */
        private String headerName = "Authorization";
    }

    @Data
    public static class Password {
        /**
         * 密码最小长度
         */
        private int minLength = 6;

        /**
         * 密码最大长度
         */
        private int maxLength = 20;

        /**
         * 是否需要包含数字
         */
        private boolean requireDigit = true;

        /**
         * 是否需要包含字母
         */
        private boolean requireLetter = true;

        /**
         * 是否需要包含特殊字符
         */
        private boolean requireSpecialChar = false;
    }
}