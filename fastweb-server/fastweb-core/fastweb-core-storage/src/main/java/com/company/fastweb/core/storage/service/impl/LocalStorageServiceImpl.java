package com.company.fastweb.core.storage.service.impl;

import com.company.fastweb.core.exception.BizException;
import com.company.fastweb.core.storage.config.StorageProperties;
import com.company.fastweb.core.storage.service.StorageObject;
import com.company.fastweb.core.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 本地存储服务实现
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocalStorageServiceImpl implements StorageService {

    private final StorageProperties storageProperties;

    @Override
    public String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) {
        try {
            Path bucketPath = getBucketPath(bucketName);
            Path filePath = bucketPath.resolve(objectName);
            
            // 创建目录
            Files.createDirectories(filePath.getParent());
            
            // 写入文件
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("文件上传成功: bucket={}, object={}", bucketName, objectName);
            return getObjectUrl(bucketName, objectName);
            
        } catch (IOException e) {
            log.error("文件上传失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("FILE_UPLOAD_ERROR", "文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadFile(String objectName, InputStream inputStream, String contentType) {
        return uploadFile(storageProperties.getDefaultBucket(), objectName, inputStream, contentType);
    }

    @Override
    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            Path filePath = getBucketPath(bucketName).resolve(objectName);
            if (!Files.exists(filePath)) {
                throw BizException.of("FILE_NOT_FOUND", "文件不存在: " + objectName);
            }
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error("文件下载失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("FILE_DOWNLOAD_ERROR", "文件下载失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream downloadFile(String objectName) {
        return downloadFile(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public boolean deleteFile(String bucketName, String objectName) {
        try {
            Path filePath = getBucketPath(bucketName).resolve(objectName);
            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                log.info("文件删除成功: bucket={}, object={}", bucketName, objectName);
            }
            return deleted;
        } catch (IOException e) {
            log.error("文件删除失败: bucket={}, object={}", bucketName, objectName, e);
            return false;
        }
    }

    @Override
    public boolean deleteFile(String objectName) {
        return deleteFile(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public List<String> deleteFiles(String bucketName, List<String> objectNames) {
        List<String> errors = new ArrayList<>();
        for (String objectName : objectNames) {
            if (!deleteFile(bucketName, objectName)) {
                errors.add("删除失败: " + objectName);
            }
        }
        return errors;
    }

    @Override
    public boolean fileExists(String bucketName, String objectName) {
        Path filePath = getBucketPath(bucketName).resolve(objectName);
        return Files.exists(filePath);
    }

    @Override
    public boolean fileExists(String objectName) {
        return fileExists(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public FileInfo getFileInfo(String bucketName, String objectName) {
        try {
            Path filePath = getBucketPath(bucketName).resolve(objectName);
            if (!Files.exists(filePath)) {
                throw BizException.of("FILE_NOT_FOUND", "文件不存在: " + objectName);
            }

            String contentType = Files.probeContentType(filePath);
            long size = Files.size(filePath);
            String lastModified = Files.getLastModifiedTime(filePath).toString();
            // ETag is not readily available for local files, so we'll use an empty string.
            String etag = "";

            return new FileInfo(objectName, etag, size, lastModified, contentType);

        } catch (IOException e) {
            log.error("获取文件信息失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("FILE_INFO_ERROR", "获取文件信息失败: " + e.getMessage());
        }
    }

    @Override
    public FileInfo getFileInfo(String objectName) {
        return getFileInfo(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public List<FileInfo> listFiles(String bucketName, String prefix, int maxKeys) {
        List<FileInfo> files = new ArrayList<>();
        try {
            Path bucketPath = getBucketPath(bucketName);
            if (!Files.exists(bucketPath)) {
                return files;
            }

            try (Stream<Path> paths = Files.walk(bucketPath)) {
                paths.filter(Files::isRegularFile)
                    .filter(path -> {
                        String relativePath = bucketPath.relativize(path).toString().replace("\\", "/");
                        return prefix == null || relativePath.startsWith(prefix);
                    })
                    .limit(maxKeys)
                    .forEach(path -> {
                        try {
                            String objectName = bucketPath.relativize(path).toString().replace("\\", "/");
                            String contentType = Files.probeContentType(path);
                            long size = Files.size(path);
                            String lastModified = Files.getLastModifiedTime(path).toString();
                            
                            files.add(new FileInfo(objectName, "", size, lastModified, contentType));
                        } catch (IOException e) {
                            log.warn("获取文件信息失败: {}", path, e);
                        }
                    });
            }
        } catch (IOException e) {
            log.error("列出文件失败: bucket={}, prefix={}", bucketName, prefix, e);
            throw BizException.of("LIST_OBJECTS_ERROR", "列出文件失败: " + e.getMessage());
        }
        return files;
    }

    @Override
    public List<FileInfo> listFiles(String prefix, int maxKeys) {
        return listFiles(storageProperties.getDefaultBucket(), prefix, maxKeys);
    }

    @Override
    public String getPresignedUploadUrl(String bucketName, String objectName, int expiry) {
        // 本地存储不支持预签名，直接返回访问URL
        return getObjectUrl(bucketName, objectName);
    }

    @Override
    public String getPresignedDownloadUrl(String bucketName, String objectName, int expiry) {
        // 本地存储不支持预签名，直接返回访问URL
        return getObjectUrl(bucketName, objectName);
    }

    // 删除以下无用方法
    // @Override
    // public String generatePresignedUrl(String objectName, int expiry) {
    //     return generatePresignedUrl(storageProperties.getDefaultBucket(), objectName, expiry);
    // }

    @Override
    public boolean createBucket(String bucketName) {
        try {
            Path bucketPath = getBucketPath(bucketName);
            Files.createDirectories(bucketPath);
            log.info("存储桶创建成功: {}", bucketName);
            return true;
        } catch (IOException e) {
            log.error("创建存储桶失败: {}", bucketName, e);
            return false;
        }
    }

    @Override
    public boolean deleteBucket(String bucketName) {
        try {
            Path bucketPath = getBucketPath(bucketName);
            if (Files.exists(bucketPath)) {
                // 删除目录及其所有内容
                try (Stream<Path> paths = Files.walk(bucketPath)) {
                    paths.sorted((a, b) -> b.compareTo(a)) // 先删除文件，再删除目录
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                log.warn("删除文件失败: {}", path, e);
                            }
                        });
                }
                log.info("存储桶删除成功: {}", bucketName);
                return true;
            }
            return false;
        } catch (IOException e) {
            log.error("删除存储桶失败: {}", bucketName, e);
            return false;
        }
    }

    @Override
    public boolean bucketExists(String bucketName) {
        Path bucketPath = getBucketPath(bucketName);
        return Files.exists(bucketPath) && Files.isDirectory(bucketPath);
    }

    /**
     * 获取存储桶路径
     */
    private Path getBucketPath(String bucketName) {
        return Paths.get(storageProperties.getLocal().getPath(), bucketName);
    }

    /**
     * 获取对象访问URL
     */
    private String getObjectUrl(String bucketName, String objectName) {
        return storageProperties.getLocal().getUrlPrefix() + "/" + bucketName + "/" + objectName;
    }
}