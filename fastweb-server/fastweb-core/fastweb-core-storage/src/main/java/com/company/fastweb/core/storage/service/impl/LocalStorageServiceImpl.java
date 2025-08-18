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
    public String upload(String bucketName, String objectName, InputStream inputStream, String contentType) {
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
    public String upload(String objectName, InputStream inputStream, String contentType) {
        return upload(storageProperties.getDefaultBucket(), objectName, inputStream, contentType);
    }

    @Override
    public InputStream download(String bucketName, String objectName) {
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
    public InputStream download(String objectName) {
        return download(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public boolean delete(String bucketName, String objectName) {
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
    public boolean delete(String objectName) {
        return delete(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public List<String> deleteMultiple(String bucketName, List<String> objectNames) {
        List<String> errors = new ArrayList<>();
        for (String objectName : objectNames) {
            if (!delete(bucketName, objectName)) {
                errors.add("删除失败: " + objectName);
            }
        }
        return errors;
    }

    @Override
    public boolean exists(String bucketName, String objectName) {
        Path filePath = getBucketPath(bucketName).resolve(objectName);
        return Files.exists(filePath);
    }

    @Override
    public boolean exists(String objectName) {
        return exists(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public StorageObject getObjectInfo(String bucketName, String objectName) {
        try {
            Path filePath = getBucketPath(bucketName).resolve(objectName);
            if (!Files.exists(filePath)) {
                throw BizException.of("FILE_NOT_FOUND", "文件不存在: " + objectName);
            }

            return StorageObject.builder()
                .objectName(objectName)
                .bucketName(bucketName)
                .size(Files.size(filePath))
                .contentType(Files.probeContentType(filePath))
                .lastModified(LocalDateTime.ofInstant(
                    Files.getLastModifiedTime(filePath).toInstant(), 
                    ZoneId.systemDefault()))
                .url(getObjectUrl(bucketName, objectName))
                .isDirectory(Files.isDirectory(filePath))
                .build();

        } catch (IOException e) {
            log.error("获取文件信息失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("FILE_INFO_ERROR", "获取文件信息失败: " + e.getMessage());
        }
    }

    @Override
    public StorageObject getObjectInfo(String objectName) {
        return getObjectInfo(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public List<StorageObject> listObjects(String bucketName, String prefix, int maxKeys) {
        List<StorageObject> objects = new ArrayList<>();
        try {
            Path bucketPath = getBucketPath(bucketName);
            if (!Files.exists(bucketPath)) {
                return objects;
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
                            objects.add(StorageObject.builder()
                                .objectName(objectName)
                                .bucketName(bucketName)
                                .size(Files.size(path))
                                .contentType(Files.probeContentType(path))
                                .lastModified(LocalDateTime.ofInstant(
                                    Files.getLastModifiedTime(path).toInstant(), 
                                    ZoneId.systemDefault()))
                                .url(getObjectUrl(bucketName, objectName))
                                .isDirectory(false)
                                .build());
                        } catch (IOException e) {
                            log.warn("获取文件信息失败: {}", path, e);
                        }
                    });
            }
        } catch (IOException e) {
            log.error("列出文件失败: bucket={}, prefix={}", bucketName, prefix, e);
            throw BizException.of("LIST_OBJECTS_ERROR", "列出文件失败: " + e.getMessage());
        }
        return objects;
    }

    @Override
    public List<StorageObject> listObjects(String prefix, int maxKeys) {
        return listObjects(storageProperties.getDefaultBucket(), prefix, maxKeys);
    }

    @Override
    public String generatePresignedUrl(String bucketName, String objectName, int expiry) {
        // 本地存储不支持预签名URL，直接返回访问URL
        return getObjectUrl(bucketName, objectName);
    }

    @Override
    public String generatePresignedUrl(String objectName, int expiry) {
        return generatePresignedUrl(storageProperties.getDefaultBucket(), objectName, expiry);
    }

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