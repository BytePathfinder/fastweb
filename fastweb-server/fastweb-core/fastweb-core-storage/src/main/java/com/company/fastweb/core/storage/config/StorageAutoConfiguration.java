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

import java.time.Duration;

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
     * MinIO客户端配置
     */
    @Bean
    @ConditionalOnClass(MinioClient.class)
    @ConditionalOnProperty(name = "fastweb.storage.type", havingValue = "minio", matchIfMissing = true)
    @ConditionalOnMissingBean
    public MinioClient minioClient(StorageProperties properties) {
        MinioClient client = MinioClient.builder()
            .endpoint(properties.getEndpoint())
            .credentials(properties.getAccessKey(), properties.getSecretKey())
            .build();
        
        // 设置超时时间
        client.setTimeout(
            Duration.ofMillis(properties.getConnectTimeout()),
            Duration.ofMillis(properties.getWriteTimeout()),
            Duration.ofMillis(properties.getReadTimeout())
        );
        
        log.info("MinIO client configured: endpoint={}", properties.getEndpoint());
        return client;
    }

    /**
     * MinIO存储服务
     */
    @Bean
    @ConditionalOnClass(MinioClient.class)
    @ConditionalOnProperty(name = "fastweb.storage.type", havingValue = "minio", matchIfMissing = true)
    @ConditionalOnMissingBean
    public StorageService minioStorageService(MinioClient minioClient, StorageProperties properties) {
        log.info("MinIO Storage Service initialized");
        return new MinioStorageServiceImpl(minioClient, properties);
    }

    /**
     * 本地存储服务
     */
    @Bean
    @ConditionalOnProperty(name = "fastweb.storage.type", havingValue = "local")
    @ConditionalOnMissingBean
    public StorageService localStorageService(StorageProperties properties) {
        log.info("Local Storage Service initialized");
        return new LocalStorageServiceImpl(properties);
    }
}