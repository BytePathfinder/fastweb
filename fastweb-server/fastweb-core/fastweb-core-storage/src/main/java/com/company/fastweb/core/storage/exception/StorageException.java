package com.company.fastweb.core.storage.exception;

import com.company.fastweb.core.exception.BizException;

/**
 * 存储服务异常类
 * 提供统一的存储相关异常处理
 *
 * @author FastWeb
 */
public class StorageException extends BizException {

    /**
     * 文件上传失败
     */
    public static final String FILE_UPLOAD_ERROR = "FILE_UPLOAD_ERROR";

    /**
     * 文件下载失败
     */
    public static final String FILE_DOWNLOAD_ERROR = "FILE_DOWNLOAD_ERROR";

    /**
     * 文件删除失败
     */
    public static final String FILE_DELETE_ERROR = "FILE_DELETE_ERROR";

    /**
     * 文件不存在
     */
    public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";

    /**
     * 获取文件信息失败
     */
    public static final String FILE_INFO_ERROR = "FILE_INFO_ERROR";

    /**
     * 列出文件失败
     */
    public static final String LIST_OBJECTS_ERROR = "LIST_OBJECTS_ERROR";

    /**
     * 存储桶操作失败
     */
    public static final String BUCKET_OPERATION_ERROR = "BUCKET_OPERATION_ERROR";

    /**
     * 预签名URL生成失败
     */
    public static final String PRESIGNED_URL_ERROR = "PRESIGNED_URL_ERROR";

    /**
     * 存储服务连接失败
     */
    public static final String STORAGE_CONNECTION_ERROR = "STORAGE_CONNECTION_ERROR";

    /**
     * 存储配置错误
     */
    public static final String STORAGE_CONFIG_ERROR = "STORAGE_CONFIG_ERROR";

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String code, String message) {
        super(code, message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * 创建文件上传异常
     */
    public static StorageException fileUploadError(String bucketName, String objectName, Throwable cause) {
        return new StorageException(FILE_UPLOAD_ERROR, 
            String.format("文件上传失败: bucket=%s, object=%s, error=%s", bucketName, objectName, cause.getMessage()), 
            cause);
    }

    /**
     * 创建文件下载异常
     */
    public static StorageException fileDownloadError(String bucketName, String objectName, Throwable cause) {
        return new StorageException(FILE_DOWNLOAD_ERROR, 
            String.format("文件下载失败: bucket=%s, object=%s, error=%s", bucketName, objectName, cause.getMessage()), 
            cause);
    }

    /**
     * 创建文件删除异常
     */
    public static StorageException fileDeleteError(String bucketName, String objectName, Throwable cause) {
        return new StorageException(FILE_DELETE_ERROR, 
            String.format("文件删除失败: bucket=%s, object=%s, error=%s", bucketName, objectName, cause.getMessage()), 
            cause);
    }

    /**
     * 创建文件不存在异常
     */
    public static StorageException fileNotFound(String bucketName, String objectName) {
        return new StorageException(FILE_NOT_FOUND, 
            String.format("文件不存在: bucket=%s, object=%s", bucketName, objectName));
    }

    /**
     * 创建文件信息获取异常
     */
    public static StorageException fileInfoError(String bucketName, String objectName, Throwable cause) {
        return new StorageException(FILE_INFO_ERROR, 
            String.format("获取文件信息失败: bucket=%s, object=%s, error=%s", bucketName, objectName, cause.getMessage()), 
            cause);
    }

    /**
     * 创建列出文件异常
     */
    public static StorageException listObjectsError(String bucketName, String prefix, Throwable cause) {
        return new StorageException(LIST_OBJECTS_ERROR, 
            String.format("列出文件失败: bucket=%s, prefix=%s, error=%s", bucketName, prefix, cause.getMessage()), 
            cause);
    }

    /**
     * 创建存储桶操作异常
     */
    public static StorageException bucketOperationError(String bucketName, String operation, Throwable cause) {
        return new StorageException(BUCKET_OPERATION_ERROR, 
            String.format("存储桶操作失败: bucket=%s, operation=%s, error=%s", bucketName, operation, cause.getMessage()), 
            cause);
    }

    /**
     * 创建预签名URL异常
     */
    public static StorageException presignedUrlError(String bucketName, String objectName, Throwable cause) {
        return new StorageException(PRESIGNED_URL_ERROR, 
            String.format("预签名URL生成失败: bucket=%s, object=%s, error=%s", bucketName, objectName, cause.getMessage()), 
            cause);
    }

    /**
     * 创建存储连接异常
     */
    public static StorageException connectionError(String endpoint, Throwable cause) {
        return new StorageException(STORAGE_CONNECTION_ERROR, 
            String.format("存储服务连接失败: endpoint=%s, error=%s", endpoint, cause.getMessage()), 
            cause);
    }

    /**
     * 创建存储配置异常
     */
    public static StorageException configError(String message) {
        return new StorageException(STORAGE_CONFIG_ERROR, "存储配置错误: " + message);
    }
}