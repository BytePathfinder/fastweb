package com.company.fastweb.core.storage.config;

import com.company.fastweb.core.storage.service.StorageService;
import com.company.fastweb.core.storage.service.impl.LocalStorageServiceImpl;
import com.company.fastweb.core.storage.service.impl.MinioStorageServiceImpl;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 存储自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    /**
     * MinIO 存储配置类
     */
    @Configuration
    @ConditionalOnClass(MinioClient.class)
    @ConditionalOnProperty(prefix = "fastweb.storage.minio", name = "endpoint")
    public static class MinioStorageConfiguration {

        /**
         * MinIO客户端配置
         */
        @Bean
        @ConditionalOnMissingBean
        public MinioClient minioClient(StorageProperties properties) {
            StorageProperties.MinioProperties minio = properties.getMinio();
            
            MinioClient.Builder builder = MinioClient.builder()
                .endpoint(minio.getEndpoint())
                .credentials(minio.getAccessKey(), minio.getSecretKey());
            
            if (minio.getRegion() != null && !minio.getRegion().isEmpty()) {
                builder.region(minio.getRegion());
            }
            
            log.info("MinIO client configured: endpoint={}", minio.getEndpoint());
            return builder.build();
        }

        /**
         * MinIO 存储服务
         */
        @Bean
        @ConditionalOnMissingBean
        public StorageService minioStorageService(MinioClient minioClient) {
            log.info("FastWeb Storage Service (MinIO) initialized");
            return new MinioStorageServiceImpl(minioClient);
        }
    }

    /**
     * 本地存储配置类
     */
    @Configuration
    @ConditionalOnProperty(prefix = "fastweb.storage.local", name = "path")
    @ConditionalOnMissingBean(StorageService.class)
    public static class LocalStorageConfiguration {

        /**
         * 本地存储服务
         */
        @Bean
        @ConditionalOnMissingBean
        public StorageService localStorageService(StorageProperties properties) {
            log.info("FastWeb Storage Service (Local) initialized: path={}", 
                    properties.getLocal().getPath());
            return new LocalStorageServiceImpl(properties);
        }
    }

    /**
     * 默认本地存储配置（当没有任何特定配置时）
     */
    @Configuration
    @ConditionalOnMissingBean(StorageService.class)
    public static class DefaultStorageConfiguration {

        /**
         * 默认本地存储服务
         */
        @Bean
        public StorageService defaultStorageService(StorageProperties properties) {
            log.info("FastWeb Storage Service (Default Local) initialized: path={}", 
                    properties.getLocal().getPath());
            return new LocalStorageServiceImpl(properties);
        }
    }
}