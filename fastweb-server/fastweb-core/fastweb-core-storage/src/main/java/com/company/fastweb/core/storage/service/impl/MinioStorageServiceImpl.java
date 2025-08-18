package com.company.fastweb.core.storage.service.impl;

import com.company.fastweb.core.exception.BizException;
import com.company.fastweb.core.storage.service.StorageService;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * MinIO存储服务实现
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements StorageService {

    private final MinioClient minioClient;

    @Value("${fastweb.storage.minio.default-bucket:fastweb}")
    private String defaultBucket;

    @Value("${fastweb.storage.minio.endpoint:}")
    private String endpoint;

    @Override
    public String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) {
        try {
            // 确保存储桶存在
            if (!bucketExists(bucketName)) {
                createBucket(bucketName);
            }

            // 上传文件
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, -1, 10485760) // 10MB
                    .contentType(contentType)
                    .build()
            );

            log.info("文件上传成功: bucket={}, object={}", bucketName, objectName);
            return getFileUrl(bucketName, objectName);

        } catch (Exception e) {
            log.error("文件上传失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("FILE_UPLOAD_ERROR", "文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadFile(String objectName, InputStream inputStream, String contentType) {
        return uploadFile(defaultBucket, objectName, inputStream, contentType);
    }

    @Override
    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            log.error("文件下载失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("FILE_DOWNLOAD_ERROR", "文件下载失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream downloadFile(String objectName) {
        return downloadFile(defaultBucket, objectName);
    }

    @Override
    public boolean deleteFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            log.info("文件删除成功: bucket={}, object={}", bucketName, objectName);
            return true;
        } catch (Exception e) {
            log.error("文件删除失败: bucket={}, object={}", bucketName, objectName, e);
            return false;
        }
    }

    @Override
    public boolean deleteFile(String objectName) {
        return deleteFile(defaultBucket, objectName);
    }

    @Override
    public List<String> deleteFiles(String bucketName, List<String> objectNames) {
        List<String> errors = new ArrayList<>();
        try {
            List<DeleteObject> objects = objectNames.stream()
                .map(DeleteObject::new)
                .collect(Collectors.toList());

            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                    .bucket(bucketName)
                    .objects(objects)
                    .build()
            );

            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                errors.add(error.objectName() + ": " + error.message());
            }

            log.info("批量删除文件完成: bucket={}, total={}, errors={}", bucketName, objectNames.size(), errors.size());
        } catch (Exception e) {
            log.error("批量删除文件失败: bucket={}", bucketName, e);
            errors.add("批量删除失败: " + e.getMessage());
        }
        return errors;
    }

    @Override
    public boolean fileExists(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean fileExists(String objectName) {
        return fileExists(defaultBucket, objectName);
    }

    @Override
    public FileInfo getFileInfo(String bucketName, String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );

            return new FileInfo(
                objectName,
                stat.etag(),
                stat.size(),
                stat.lastModified().toString(),
                stat.contentType()
            );
        } catch (Exception e) {
            log.error("获取文件信息失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("FILE_INFO_ERROR", "获取文件信息失败: " + e.getMessage());
        }
    }

    @Override
    public FileInfo getFileInfo(String objectName) {
        return getFileInfo(defaultBucket, objectName);
    }

    @Override
    public List<FileInfo> listFiles(String bucketName, String prefix, int maxKeys) {
        List<FileInfo> files = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .maxKeys(maxKeys)
                    .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                files.add(new FileInfo(
                    item.objectName(),
                    item.etag(),
                    item.size(),
                    item.lastModified().toString(),
                    null // ListObjects不返回contentType
                ));
            }
        } catch (Exception e) {
            log.error("列出文件失败: bucket={}, prefix={}", bucketName, prefix, e);
            throw BizException.of("LIST_FILES_ERROR", "列出文件失败: " + e.getMessage());
        }
        return files;
    }

    @Override
    public List<FileInfo> listFiles(String prefix, int maxKeys) {
        return listFiles(defaultBucket, prefix, maxKeys);
    }

    @Override
    public String getPresignedUploadUrl(String bucketName, String objectName, int expiry) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expiry, TimeUnit.SECONDS)
                    .build()
            );
        } catch (Exception e) {
            log.error("获取预签名上传URL失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("PRESIGNED_URL_ERROR", "获取预签名URL失败: " + e.getMessage());
        }
    }

    @Override
    public String getPresignedDownloadUrl(String bucketName, String objectName, int expiry) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expiry, TimeUnit.SECONDS)
                    .build()
            );
        } catch (Exception e) {
            log.error("获取预签名下载URL失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("PRESIGNED_URL_ERROR", "获取预签名URL失败: " + e.getMessage());
        }
    }

    @Override
    public boolean createBucket(String bucketName) {
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
                );
                log.info("存储桶创建成功: {}", bucketName);
            }
            return true;
        } catch (Exception e) {
            log.error("存储桶创建失败: {}", bucketName, e);
            return false;
        }
    }

    @Override
    public boolean deleteBucket(String bucketName) {
        try {
            minioClient.removeBucket(
                RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
            log.info("存储桶删除成功: {}", bucketName);
            return true;
        } catch (Exception e) {
            log.error("存储桶删除失败: {}", bucketName, e);
            return false;
        }
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
        } catch (Exception e) {
            log.error("检查存储桶是否存在失败: {}", bucketName, e);
            return false;
        }
    }

    /**
     * 获取文件访问URL
     */
    private String getFileUrl(String bucketName, String objectName) {
        if (endpoint != null && !endpoint.isEmpty()) {
            return endpoint + "/" + bucketName + "/" + objectName;
        }
        return "/" + bucketName + "/" + objectName;
    }
}