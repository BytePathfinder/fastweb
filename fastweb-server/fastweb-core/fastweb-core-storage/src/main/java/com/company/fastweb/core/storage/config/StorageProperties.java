package com.company.fastweb.core.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 存储配置属性
 *
 * @author FastWeb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.storage")
public class StorageProperties {

    /**
     * 本地存储配置
     */
    private LocalProperties local = new LocalProperties();

    /**
     * MinIO配置
     */
    private MinioProperties minio = new MinioProperties();

    /**
     * 默认存储桶
     */
    private String defaultBucket = "fastweb";

    @Data
    public static class MinioProperties {
        /**
         * MinIO服务端点
         */
        private String endpoint;

        /**
         * 访问密钥
         */
        private String accessKey;

        /**
         * 秘密密钥
         */
        private String secretKey;

        /**
         * 区域
         */
        private String region;

        /**
         * 默认存储桶
         */
        private String defaultBucket = "fastweb";

        /**
         * 是否启用HTTPS
         */
        private boolean secure = false;

        /**
         * 连接超时时间（毫秒）
         */
        private int connectTimeout = 10000;

        /**
         * 写超时时间（毫秒）
         */
        private int writeTimeout = 60000;

        /**
         * 读超时时间（毫秒）
         */
        private int readTimeout = 10000;
    }

    @Data
    public static class LocalProperties {
        /**
         * 本地存储路径
         */
        private String path = "./storage";

        /**
         * URL前缀
         */
        private String urlPrefix = "/api/storage";
    }
}