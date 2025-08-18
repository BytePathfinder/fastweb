package com.company.fastweb.core.storage.service.impl;

import com.company.fastweb.core.exception.BizException;
import com.company.fastweb.core.storage.config.StorageProperties;
import com.company.fastweb.core.storage.service.StorageObject;
import com.company.fastweb.core.storage.service.StorageService;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final StorageProperties storageProperties;

    @Override
    public String upload(String bucketName, String objectName, InputStream inputStream, String contentType) {
        try {
            // 确保存储桶存在
            ensureBucketExists(bucketName);

            // 上传文件
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, -1, 10485760) // 10MB part size
                    .contentType(contentType)
                    .build()
            );

            // 返回访问URL
            return getObjectUrl(bucketName, objectName);

        } catch (Exception e) {
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
    public InputStream download(String objectName) {
        return download(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public boolean delete(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            return true;
        } catch (Exception e) {
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
        } catch (Exception e) {
            log.error("批量删除文件失败: bucket={}", bucketName, e);
            errors.add("批量删除失败: " + e.getMessage());
        }
        return errors;
    }

    @Override
    public boolean exists(String bucketName, String objectName) {
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
    public boolean exists(String objectName) {
        return exists(storageProperties.getDefaultBucket(), objectName);
    }

    @Override
    public StorageObject getObjectInfo(String bucketName, String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );

            return StorageObject.builder()
                .objectName(objectName)
                .bucketName(bucketName)
                .size(stat.size())
                .contentType(stat.contentType())
                .etag(stat.etag())
                .lastModified(LocalDateTime.ofInstant(stat.lastModified().toInstant(), ZoneId.systemDefault()))
                .url(getObjectUrl(bucketName, objectName))
                .isDirectory(false)
                .metadata(stat.userMetadata())
                .build();

        } catch (Exception e) {
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
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .maxKeys(maxKeys)
                    .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                objects.add(StorageObject.builder()
                    .objectName(item.objectName())
                    .bucketName(bucketName)
                    .size(item.size())
                    .etag(item.etag())
                    .lastModified(LocalDateTime.ofInstant(item.lastModified().toInstant(), ZoneId.systemDefault()))
                    .url(getObjectUrl(bucketName, item.objectName()))
                    .isDirectory(item.isDir())
                    .build());
            }
        } catch (Exception e) {
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
            log.error("生成预签名URL失败: bucket={}, object={}", bucketName, objectName, e);
            throw BizException.of("PRESIGNED_URL_ERROR", "生成预签名URL失败: " + e.getMessage());
        }
    }

    @Override
    public String generatePresignedUrl(String objectName, int expiry) {
        return generatePresignedUrl(storageProperties.getDefaultBucket(), objectName, expiry);
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
            log.error("创建存储桶失败: {}", bucketName, e);
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
            log.error("删除存储桶失败: {}", bucketName, e);
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
     * 确保存储桶存在
     */
    private void ensureBucketExists(String bucketName) {
        if (!bucketExists(bucketName)) {
            createBucket(bucketName);
        }
    }

    /**
     * 获取对象访问URL
     */
    private String getObjectUrl(String bucketName, String objectName) {
        if (storageProperties.isPublicRead()) {
            return String.format("%s/%s/%s", 
                storageProperties.getEndpoint(), bucketName, objectName);
        } else {
            // 生成临时访问URL
            return generatePresignedUrl(bucketName, objectName, 3600); // 1小时有效期
        }
    }
}