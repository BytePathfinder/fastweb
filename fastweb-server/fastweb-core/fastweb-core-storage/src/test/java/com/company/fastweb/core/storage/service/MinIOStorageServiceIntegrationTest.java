package com.company.fastweb.core.storage.service;

import com.company.fastweb.core.storage.service.impl.MinioStorageServiceImpl;
import io.minio.MinioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MinIO 存储服务集成测试
 * 使用 Testcontainers 启动真实的 MinIO 容器进行测试
 */
@Testcontainers
@SpringBootTest
class MinIOStorageServiceIntegrationTest {

    @Container
    static MinIOContainer minioContainer = new MinIOContainer("minio/minio:RELEASE.2024-01-16T16-07-38Z")
            .withUserName("minioadmin")
            .withPassword("minioadmin");

    private MinioStorageServiceImpl storageService;
    private MinioClient minioClient;

    @BeforeEach
    void setUp() {
        // 创建 MinioClient
        minioClient = MinioClient.builder()
                .endpoint(minioContainer.getS3URL())
                .credentials(minioContainer.getUserName(), minioContainer.getPassword())
                .build();

        // 创建存储服务实例
        storageService = new MinioStorageServiceImpl(minioClient);
    }

    @Test
    @DisplayName("应该成功上传文件")
    void shouldUploadFileSuccessfully() throws Exception {
        // Given
        String fileName = "test-file.txt";
        String content = "Hello, MinIO!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        long contentLength = content.getBytes(StandardCharsets.UTF_8).length;

        // When
        String fileUrl = storageService.uploadFile(fileName, inputStream, "text/plain");

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains(fileName);
        assertThat(fileUrl).startsWith("http");
    }

    @Test
    @DisplayName("应该成功下载文件")
    void shouldDownloadFileSuccessfully() throws Exception {
        // Given - 先上传一个文件
        String fileName = "download-test.txt";
        String content = "Download test content";
        InputStream uploadStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        long contentLength = content.getBytes(StandardCharsets.UTF_8).length;
        
        storageService.uploadFile(fileName, uploadStream, "text/plain");

        // When
        InputStream downloadStream = storageService.downloadFile(fileName);

        // Then
        assertThat(downloadStream).isNotNull();
        String downloadedContent = new String(downloadStream.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(downloadedContent).isEqualTo(content);
    }

    @Test
    @DisplayName("应该成功删除文件")
    void shouldDeleteFileSuccessfully() throws Exception {
        // Given - 先上传一个文件
        String fileName = "delete-test.txt";
        String content = "Delete test content";
        InputStream uploadStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        long contentLength = content.getBytes(StandardCharsets.UTF_8).length;
        
        storageService.uploadFile(fileName, uploadStream, "text/plain");

        // When
        boolean deleted = storageService.deleteFile(fileName);

        // Then
        assertThat(deleted).isTrue();
        
        // 验证文件确实被删除了
        assertThrows(Exception.class, () -> {
            storageService.downloadFile(fileName);
        });
    }

    @Test
    @DisplayName("应该正确检查文件是否存在")
    void shouldCheckFileExistsCorrectly() throws Exception {
        // Given
        String existingFileName = "exists-test.txt";
        String nonExistingFileName = "non-exists-test.txt";
        
        // 上传一个文件
        String content = "Exists test content";
        InputStream uploadStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        long contentLength = content.getBytes(StandardCharsets.UTF_8).length;
        storageService.uploadFile(existingFileName, uploadStream, "text/plain");

        // When & Then
        assertThat(storageService.fileExists(existingFileName)).isTrue();
        assertThat(storageService.fileExists(nonExistingFileName)).isFalse();
    }

    @Test
    @DisplayName("应该正确获取文件信息")
    void shouldGetFileInfoCorrectly() throws Exception {
        // Given
        String fileName = "info-test.txt";
        String content = "Info test content with specific length";
        InputStream uploadStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        long expectedSize = content.getBytes(StandardCharsets.UTF_8).length;
        
        storageService.uploadFile(fileName, uploadStream, "text/plain");

        // When
        StorageService.FileInfo fileInfo = storageService.getFileInfo(fileName);

        // Then
        assertThat(fileInfo).isNotNull();
        assertThat(fileInfo.getSize()).isEqualTo(expectedSize);
    }
}