package com.company.fastweb.core.storage.config;

import com.company.fastweb.core.storage.service.StorageService;
import com.company.fastweb.core.storage.service.impl.LocalStorageServiceImpl;
import com.company.fastweb.core.storage.service.impl.MinioStorageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Storage 自动配置集成测试
 * 验证不同配置下的存储服务自动装配
 */
@Testcontainers
class StorageAutoConfigurationIntegrationTest {

    @Container
    static MinIOContainer minioContainer = new MinIOContainer("minio/minio:RELEASE.2024-01-16T16-07-38Z")
            .withUserName("minioadmin")
            .withPassword("minioadmin");

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(StorageAutoConfiguration.class));

    @Test
    @DisplayName("当配置为LOCAL时应该创建LocalStorageService")
    void shouldCreateLocalStorageServiceWhenConfiguredAsLocal() {
        this.contextRunner
                .withPropertyValues(
                        "fastweb.storage.local.path=/tmp/storage",
                        "fastweb.storage.local.url-prefix=/api/storage"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(StorageService.class);
                    assertThat(context).hasSingleBean(LocalStorageServiceImpl.class);
                    assertThat(context).doesNotHaveBean(MinioStorageServiceImpl.class);
                    
                    StorageService storageService = context.getBean(StorageService.class);
                    assertThat(storageService).isInstanceOf(LocalStorageServiceImpl.class);
                });
    }

    @Test
    @DisplayName("当配置为MINIO时应该创建MinIOStorageService")
    void shouldCreateMinIOStorageServiceWhenConfiguredAsMinIO() {
        this.contextRunner
                .withPropertyValues(
                        "fastweb.storage.type=MINIO",
                        "fastweb.storage.minio.endpoint=" + minioContainer.getS3URL(),
                        "fastweb.storage.minio.access-key=" + minioContainer.getUserName(),
                        "fastweb.storage.minio.secret-key=" + minioContainer.getPassword(),
                        "fastweb.storage.minio.default-bucket=test-bucket"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(StorageService.class);
                    assertThat(context).hasSingleBean(MinioStorageServiceImpl.class);
                    assertThat(context).doesNotHaveBean(LocalStorageServiceImpl.class);
                    
                    StorageService storageService = context.getBean(StorageService.class);
                    assertThat(storageService).isInstanceOf(MinioStorageServiceImpl.class);
                });
    }

    @Test
    @DisplayName("当没有配置存储类型时应该默认使用LOCAL")
    void shouldDefaultToLocalStorageWhenNoTypeConfigured() {
        this.contextRunner
                .withPropertyValues(
                        "fastweb.storage.local.path=/tmp/storage",
                        "fastweb.storage.local.url-prefix=/api/storage"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(StorageService.class);
                    assertThat(context).hasSingleBean(LocalStorageServiceImpl.class);
                    assertThat(context).doesNotHaveBean(MinioStorageServiceImpl.class);
                    
                    StorageService storageService = context.getBean(StorageService.class);
                    assertThat(storageService).isInstanceOf(LocalStorageServiceImpl.class);
                });
    }

    @Test
    @DisplayName("当缺少必要配置时应该启动失败")
    void shouldFailWhenRequiredConfigurationIsMissing() {
        // 测试 MinIO 缺少必要配置
        this.contextRunner
                .withPropertyValues(
                        "fastweb.storage.type=MINIO"
                        // 缺少 MinIO 相关配置
                )
                .run(context -> {
                    assertThat(context).hasFailed();
                });
    }

    @Test
    @DisplayName("应该正确绑定配置属性")
    void shouldBindConfigurationPropertiesCorrectly() {
        this.contextRunner
                .withPropertyValues(
                        "fastweb.storage.local.path=/custom/path",
                        "fastweb.storage.local.url-prefix=http://custom.domain/files"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(StorageProperties.class);
                    
                    StorageProperties properties = context.getBean(StorageProperties.class);
                    assertThat(properties.getLocal().getPath()).isEqualTo("/custom/path");
                    assertThat(properties.getLocal().getUrlPrefix()).isEqualTo("http://custom.domain/files");
                });
    }

    @Test
    @DisplayName("应该支持MinIO的完整配置")
    void shouldSupportFullMinIOConfiguration() {
        this.contextRunner
                .withPropertyValues(
                        "fastweb.storage.minio.endpoint=" + minioContainer.getS3URL(),
                        "fastweb.storage.minio.access-key=testkey",
                        "fastweb.storage.minio.secret-key=testsecret",
                        "fastweb.storage.minio.default-bucket=custom-bucket",
                        "fastweb.storage.minio.region=us-east-1"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(StorageProperties.class);
                    
                    StorageProperties properties = context.getBean(StorageProperties.class);
                    assertThat(properties.getMinio().getEndpoint()).isEqualTo(minioContainer.getS3URL());
                    assertThat(properties.getMinio().getAccessKey()).isEqualTo("testkey");
                    assertThat(properties.getMinio().getSecretKey()).isEqualTo("testsecret");
                    assertThat(properties.getMinio().getDefaultBucket()).isEqualTo("custom-bucket");
                    assertThat(properties.getMinio().getRegion()).isEqualTo("us-east-1");
                });
    }

    @Test
    @DisplayName("当用户提供自定义StorageService时应该跳过自动配置")
    void shouldSkipAutoConfigurationWhenCustomStorageServiceProvided() {
        this.contextRunner
                .withPropertyValues(
                        "fastweb.storage.local.path=/tmp/storage",
                        "fastweb.storage.local.url-prefix=/api/storage"
                )
                .withBean("customStorageService", StorageService.class, () -> new CustomStorageService())
                .run(context -> {
                    assertThat(context).hasSingleBean(StorageService.class);
                    assertThat(context).hasBean("customStorageService");
                    assertThat(context).doesNotHaveBean(LocalStorageServiceImpl.class);
                    assertThat(context).doesNotHaveBean(MinioStorageServiceImpl.class);
                    
                    StorageService storageService = context.getBean(StorageService.class);
                    assertThat(storageService).isInstanceOf(CustomStorageService.class);
                });
    }

    /**
     * 自定义存储服务实现，用于测试
     */
    private static class CustomStorageService implements StorageService {
        @Override
        public String uploadFile(String bucketName, String objectName, java.io.InputStream inputStream, String contentType) {
            return "custom://" + bucketName + "/" + objectName;
        }

        @Override
        public String uploadFile(String objectName, java.io.InputStream inputStream, String contentType) {
            return "custom://" + objectName;
        }

        @Override
        public java.io.InputStream downloadFile(String bucketName, String objectName) {
            return null;
        }

        @Override
        public java.io.InputStream downloadFile(String objectName) {
            return null;
        }

        @Override
        public boolean deleteFile(String bucketName, String objectName) {
            return true;
        }

        @Override
        public boolean deleteFile(String objectName) {
            return true;
        }

        @Override
        public java.util.List<String> deleteFiles(String bucketName, java.util.List<String> objectNames) {
            return java.util.Collections.emptyList();
        }

        @Override
        public boolean fileExists(String bucketName, String objectName) {
            return false;
        }

        @Override
        public boolean fileExists(String objectName) {
            return false;
        }

        @Override
        public FileInfo getFileInfo(String bucketName, String objectName) {
            return new FileInfo(objectName, "etag", 0, "2023-01-01", "text/plain");
        }

        @Override
        public FileInfo getFileInfo(String objectName) {
            return new FileInfo(objectName, "etag", 0, "2023-01-01", "text/plain");
        }

        @Override
        public java.util.List<FileInfo> listFiles(String bucketName, String prefix, int maxKeys) {
            return java.util.Collections.emptyList();
        }

        @Override
        public java.util.List<FileInfo> listFiles(String prefix, int maxKeys) {
            return java.util.Collections.emptyList();
        }

        @Override
        public String getPresignedUploadUrl(String bucketName, String objectName, int expiry) {
            return "custom://upload/" + bucketName + "/" + objectName;
        }

        @Override
        public String getPresignedDownloadUrl(String bucketName, String objectName, int expiry) {
            return "custom://download/" + bucketName + "/" + objectName;
        }

        @Override
        public boolean createBucket(String bucketName) {
            return true;
        }

        @Override
        public boolean deleteBucket(String bucketName) {
            return true;
        }

        @Override
        public boolean bucketExists(String bucketName) {
            return true;
        }
    }
}