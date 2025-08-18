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
     * 存储类型：minio, local, s3
     */
    private String type = "minio";

    /**
     * 服务端点
     */
    private String endpoint = "http://localhost:9000";

    /**
     * 访问密钥
     */
    private String accessKey = "minioadmin";

    /**
     * 秘密密钥
     */
    private String secretKey = "minioadmin";

    /**
     * 默认存储桶
     */
    private String defaultBucket = "fastweb";

    /**
     * 是否公开读取
     */
    private boolean publicRead = true;

    /**
     * 连接超时时间（毫秒）
     */
    private long connectTimeout = 10000;

    /**
     * 写入超时时间（毫秒）
     */
    private long writeTimeout = 60000;

    /**
     * 读取超时时间（毫秒）
     */
    private long readTimeout = 10000;

    /**
     * 本地存储配置
     */
    private Local local = new Local();

    /**
     * 本地存储配置
     */
    @Data
    public static class Local {
        /**
         * 本地存储路径
         */
        private String path = "./storage";

        /**
         * 访问路径前缀
         */
        private String urlPrefix = "/files";
    }
}