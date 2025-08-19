package com.company.fastweb.core.storage.service.impl;

import com.company.fastweb.core.exception.BizException;
import com.company.fastweb.core.storage.config.StorageProperties;
import com.company.fastweb.core.storage.exception.StorageException;
import com.company.fastweb.core.storage.service.StorageObject;
import com.company.fastweb.core.storage.service.StorageService;
import com.company.fastweb.core.storage.util.StorageRetryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
@RequiredArgsConstructor
public class LocalStorageServiceImpl implements StorageService {

    private final StorageProperties storageProperties;

    @Override
    public String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) {
        return StorageRetryUtil.executeWithRetry(() -> {
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
                log.error("文件上传失败: bucket={}, object={}, error={}", bucketName, objectName, e.getMessage(), e);
                throw StorageException.fileUploadError(bucketName, objectName, e);
            }
        }, String.format("uploadFile(bucket=%s, object=%s)", bucketName, objectName));
    }

    @Override
    public String uploadFile(String objectName, InputStream inputStream, String contentType) {
        return uploadFile(storageProperties.getDefaultBucket(), objectName, inputStream, contentType);
    }

    @Override
    public InputStream downloadFile(String bucketName, String objectName) {
        return StorageRetryUtil.executeWithRetry(() -> {
            try {
                Path filePath = getBucketPath(bucketName).resolve(objectName);
                if (!Files.exists(filePath)) {
                    log.warn("文件不存在: bucket={}, object={}", bucketName, objectName);
                    throw StorageException.fileNotFound(bucketName, objectName);
                }
                log.debug("文件下载开始: bucket={}, object={}", bucketName, objectName);
                return Files.newInputStream(filePath);
            } catch (IOException e) {
                log.error("文件下载失败: bucket={}, object={}, error={}", bucketName, objectName, e.getMessage(), e);
                throw StorageException.fileDownloadError(bucketName, objectName, e);
            }
        }, String.format("downloadFile(bucket=%s, object=%s)", bucketName, objectName));
    }

    @Override
    public InputStream downloadFile(String objectName) {
        return downloadFile(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public boolean deleteFile(String bucketName, String objectName) {
        try {
            return StorageRetryUtil.executeWithRetry(() -> {
                try {
                    Path filePath = getBucketPath(bucketName).resolve(objectName);
                    boolean deleted = Files.deleteIfExists(filePath);
                    if (deleted) {
                        log.info("文件删除成功: bucket={}, object={}", bucketName, objectName);
                    } else {
                        log.debug("文件不存在，无需删除: bucket={}, object={}", bucketName, objectName);
                    }
                    return deleted;
                } catch (IOException e) {
                    log.error("文件删除失败: bucket={}, object={}, error={}", bucketName, objectName, e.getMessage(), e);
                    throw StorageException.fileUploadError(bucketName, objectName, e);
                }
            }, String.format("deleteFile(bucket=%s, object=%s)", bucketName, objectName));
        } catch (StorageException e) {
            log.warn("文件删除操作失败: bucket={}, object={}, error={}", bucketName, objectName, e.getMessage());
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
        return StorageRetryUtil.executeWithRetry(() -> {
            try {
                Path filePath = getBucketPath(bucketName).resolve(objectName);
                if (!Files.exists(filePath)) {
                    log.debug("文件不存在: bucket={}, object={}", bucketName, objectName);
                    return null;
                }

                String contentType = Files.probeContentType(filePath);
                long size = Files.size(filePath);
                String lastModified = Files.getLastModifiedTime(filePath).toString();
                // ETag is not readily available for local files, so we'll use an empty string.
                String etag = "";

                log.debug("获取文件信息成功: bucket={}, object={}, size={}", bucketName, objectName, size);
                return new FileInfo(objectName, etag, size, lastModified, contentType);

            } catch (IOException e) {
                log.error("获取文件信息失败: bucket={}, object={}, error={}", bucketName, objectName, e.getMessage(), e);
                throw StorageException.fileInfoError(bucketName, objectName, e);
            }
        }, String.format("getFileInfo(bucket=%s, object=%s)", bucketName, objectName));
    }

    @Override
    public FileInfo getFileInfo(String objectName) {
        return getFileInfo(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public List<FileInfo> listFiles(String bucketName, String prefix, int maxKeys) {
        return StorageRetryUtil.executeWithRetry(() -> {
            List<FileInfo> files = new ArrayList<>();
            try {
                Path bucketPath = getBucketPath(bucketName);
                if (!Files.exists(bucketPath)) {
                    log.debug("存储桶不存在: bucket={}", bucketName);
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
                                log.warn("获取文件信息失败: path={}, error={}", path, e.getMessage());
                            }
                        });
                }
                log.debug("列出文件成功: bucket={}, prefix={}, count={}", bucketName, prefix, files.size());
            } catch (IOException e) {
                log.error("列出文件失败: bucket={}, prefix={}, error={}", bucketName, prefix, e.getMessage(), e);
                throw StorageException.listObjectsError(bucketName, prefix, e);
            }
            return files;
        }, String.format("listFiles(bucket=%s, prefix=%s, maxKeys=%d)", bucketName, prefix, maxKeys));
    }

    @Override
    public List<FileInfo> listFiles(String prefix, int maxKeys) {
        return listFiles(storageProperties.getDefaultBucket(), prefix, maxKeys);
    }

    @Override
    public String getPresignedUploadUrl(String bucketName, String objectName, int expiry) {
        try {
            // 本地存储不支持预签名URL，返回直接上传URL
            String url = String.format("/api/storage/upload?bucket=%s&object=%s", bucketName, objectName);
            log.debug("生成预签名上传URL: bucket={}, object={}, url={}", bucketName, objectName, url);
            return url;
        } catch (Exception e) {
            log.error("生成预签名上传URL失败: bucket={}, object={}, error={}", bucketName, objectName, e.getMessage(), e);
            throw StorageException.presignedUrlError(bucketName, objectName, e);
        }
    }

    @Override
    public String getPresignedDownloadUrl(String bucketName, String objectName, int expiry) {
        try {
            // 验证文件是否存在
            if (!fileExists(bucketName, objectName)) {
                throw StorageException.fileNotFound(bucketName, objectName);
            }
            
            // 本地存储不支持预签名URL，返回直接下载URL
            String url = String.format("/api/storage/download?bucket=%s&object=%s", bucketName, objectName);
            log.debug("生成预签名下载URL: bucket={}, object={}, url={}", bucketName, objectName, url);
            return url;
        } catch (StorageException e) {
            throw e;
        } catch (Exception e) {
            log.error("生成预签名下载URL失败: bucket={}, object={}, error={}", bucketName, objectName, e.getMessage(), e);
            throw StorageException.presignedUrlError(bucketName, objectName, e);
        }
    }

    // 删除以下无用方法
    // @Override
    // public String generatePresignedUrl(String objectName, int expiry) {
    //     return generatePresignedUrl(storageProperties.getDefaultBucket(), objectName, expiry);
    // }

    @Override
    public boolean createBucket(String bucketName) {
        return StorageRetryUtil.executeWithRetry(() -> {
            try {
                Path bucketPath = getBucketPath(bucketName);
                if (Files.exists(bucketPath)) {
                    log.debug("存储桶已存在: bucket={}", bucketName);
                    return false;
                }
                
                Files.createDirectories(bucketPath);
                log.info("创建存储桶成功: bucket={}, path={}", bucketName, bucketPath);
                return true;
            } catch (IOException e) {
                log.error("创建存储桶失败: bucket={}, error={}", bucketName, e.getMessage(), e);
                throw StorageException.bucketOperationError(bucketName, "create", e);
            }
        }, String.format("createBucket(bucket=%s)", bucketName));
    }

    @Override
    public boolean deleteBucket(String bucketName) {
        return StorageRetryUtil.executeWithRetry(() -> {
            try {
                Path bucketPath = getBucketPath(bucketName);
                if (!Files.exists(bucketPath)) {
                    log.debug("存储桶不存在，无需删除: bucket={}", bucketName);
                    return false;
                }
                
                // 删除目录及其所有内容
                Files.walkFileTree(bucketPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                log.info("删除存储桶成功: bucket={}, path={}", bucketName, bucketPath);
                return true;
            } catch (IOException e) {
                log.error("删除存储桶失败: bucket={}, error={}", bucketName, e.getMessage(), e);
                throw StorageException.bucketOperationError(bucketName, "delete", e);
            }
        }, String.format("deleteBucket(bucket=%s)", bucketName));
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            Path bucketPath = getBucketPath(bucketName);
            boolean exists = Files.exists(bucketPath) && Files.isDirectory(bucketPath);
            log.debug("检查存储桶存在性: bucket={}, exists={}", bucketName, exists);
            return exists;
        } catch (Exception e) {
            log.error("检查存储桶存在性失败: bucket={}, error={}", bucketName, e.getMessage(), e);
            throw StorageException.bucketOperationError(bucketName, "exists", e);
        }
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