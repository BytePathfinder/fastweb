package com.company.fastweb.core.storage.service;

import com.company.fastweb.core.storage.config.StorageProperties;
import com.company.fastweb.core.storage.service.impl.LocalStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 本地存储服务集成测试
 * 使用临时目录进行文件操作测试
 */
class LocalStorageServiceIntegrationTest {

    @TempDir
    Path tempDir;

    private LocalStorageServiceImpl storageService;
    private StorageProperties storageProperties;

    @BeforeEach
    void setUp() {
        // 配置本地存储信息
        storageProperties = new StorageProperties();
        StorageProperties.LocalProperties localProperties = new StorageProperties.LocalProperties();
        localProperties.setPath(tempDir.toString());
        localProperties.setUrlPrefix("/api/storage");
        storageProperties.setLocal(localProperties);

        // 创建存储服务实例
        storageService = new LocalStorageServiceImpl(storageProperties);
    }

    @Test
    @DisplayName("应该成功上传文件")
    void shouldUploadFileSuccessfully() throws Exception {
        // Given
        String fileName = "test-file.txt";
        String content = "Hello, Local Storage!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        long contentLength = content.getBytes(StandardCharsets.UTF_8).length;

        // When
        String fileUrl = storageService.uploadFile(fileName, inputStream, "text/plain");

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains(fileName);
        assertThat(fileUrl).startsWith("/api/storage");
        
        // 验证文件确实被保存到磁盘
        Path filePath = tempDir.resolve("fastweb").resolve(fileName);
        assertThat(Files.exists(filePath)).isTrue();
        String savedContent = Files.readString(filePath, StandardCharsets.UTF_8);
        assertThat(savedContent).isEqualTo(content);
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
        
        // 验证文件存在
        Path filePath = tempDir.resolve("fastweb").resolve(fileName);
        assertThat(Files.exists(filePath)).isTrue();

        // When
        boolean deleted = storageService.deleteFile(fileName);

        // Then
        assertThat(deleted).isTrue();
        assertThat(Files.exists(filePath)).isFalse();
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
        String content = "File info test content with specific length";
        InputStream uploadStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        long expectedSize = content.getBytes(StandardCharsets.UTF_8).length;
        
        storageService.uploadFile(fileName, uploadStream, "text/plain");

        // When
        StorageService.FileInfo fileInfo = storageService.getFileInfo(fileName);

        // Then
        assertThat(fileInfo).isNotNull();
        assertThat(fileInfo.getSize()).isEqualTo(expectedSize);
        assertThat(fileInfo.getContentType()).isEqualTo("text/plain");
        assertThat(fileInfo.getLastModified()).isNotNull();
    }

    @Test
    @DisplayName("应该处理子目录文件上传")
    void shouldHandleSubdirectoryUpload() throws Exception {
        // Given
        String fileName = "subdir/nested/test-file.txt";
        String content = "Subdirectory test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        long contentLength = content.getBytes(StandardCharsets.UTF_8).length;

        // When
        String fileUrl = storageService.uploadFile(fileName, inputStream, "text/plain");

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains(fileName);
        
        // 验证文件和目录结构被正确创建
        Path filePath = tempDir.resolve("fastweb").resolve(fileName);
        assertThat(Files.exists(filePath)).isTrue();
        assertThat(Files.isDirectory(filePath.getParent())).isTrue();
        
        String savedContent = Files.readString(filePath, StandardCharsets.UTF_8);
        assertThat(savedContent).isEqualTo(content);
    }

    @Test
    @DisplayName("删除不存在的文件应该返回false")
    void shouldReturnFalseWhenDeletingNonExistentFile() {
        // Given
        String nonExistentFileName = "non-existent-file.txt";

        // When
        boolean deleted = storageService.deleteFile(nonExistentFileName);

        // Then
        assertThat(deleted).isFalse();
    }

    @Test
    @DisplayName("获取不存在文件的信息应该返回null")
    void shouldReturnNullForNonExistentFileInfo() {
        // Given
        String nonExistentFileName = "non-existent-file.txt";

        // When
        StorageService.FileInfo fileInfo = storageService.getFileInfo(nonExistentFileName);

        // Then
        assertThat(fileInfo).isNull();
    }
}