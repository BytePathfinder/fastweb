package com.company.fastweb.core.storage.config;

import com.company.fastweb.core.storage.service.StorageService;
import com.company.fastweb.core.storage.service.impl.MinioStorageServiceImpl;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 存储自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    /**
     * MinIO客户端配置
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.storage.minio", name = "endpoint")
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
     * 存储服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(MinioClient.class)
    public StorageService storageService(MinioClient minioClient) {
        log.info("FastWeb Storage Service (MinIO) initialized");
        return new MinioStorageServiceImpl(minioClient);
    }
}