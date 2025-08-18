package com.company.fastweb.core.storage.service;

import java.io.InputStream;
import java.util.List;

/**
 * 存储服务接口
 *
 * @author FastWeb
 */
public interface StorageService {

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param inputStream 文件流
     * @param contentType 文件类型
     * @return 文件访问URL
     */
    String uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType);

    /**
     * 上传文件（默认存储桶）
     *
     * @param objectName 对象名称
     * @param inputStream 文件流
     * @param contentType 文件类型
     * @return 文件访问URL
     */
    String uploadFile(String objectName, InputStream inputStream, String contentType);

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 文件流
     */
    InputStream downloadFile(String bucketName, String objectName);

    /**
     * 下载文件（默认存储桶）
     *
     * @param objectName 对象名称
     * @return 文件流
     */
    InputStream downloadFile(String objectName);

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 是否删除成功
     */
    boolean deleteFile(String bucketName, String objectName);

    /**
     * 删除文件（默认存储桶）
     *
     * @param objectName 对象名称
     * @return 是否删除成功
     */
    boolean deleteFile(String objectName);

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param objectNames 对象名称列表
     * @return 删除结果
     */
    List<String> deleteFiles(String bucketName, List<String> objectNames);

    /**
     * 检查文件是否存在
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 是否存在
     */
    boolean fileExists(String bucketName, String objectName);

    /**
     * 检查文件是否存在（默认存储桶）
     *
     * @param objectName 对象名称
     * @return 是否存在
     */
    boolean fileExists(String objectName);

    /**
     * 获取文件信息
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 文件信息
     */
    FileInfo getFileInfo(String bucketName, String objectName);

    /**
     * 获取文件信息（默认存储桶）
     *
     * @param objectName 对象名称
     * @return 文件信息
     */
    FileInfo getFileInfo(String objectName);

    /**
     * 列出文件
     *
     * @param bucketName 存储桶名称
     * @param prefix 前缀
     * @param maxKeys 最大数量
     * @return 文件列表
     */
    List<FileInfo> listFiles(String bucketName, String prefix, int maxKeys);

    /**
     * 列出文件（默认存储桶）
     *
     * @param prefix 前缀
     * @param maxKeys 最大数量
     * @return 文件列表
     */
    List<FileInfo> listFiles(String prefix, int maxKeys);

    /**
     * 获取预签名上传URL
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiry 过期时间（秒）
     * @return 预签名URL
     */
    String getPresignedUploadUrl(String bucketName, String objectName, int expiry);

    /**
     * 获取预签名下载URL
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiry 过期时间（秒）
     * @return 预签名URL
     */
    String getPresignedDownloadUrl(String bucketName, String objectName, int expiry);

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @return 是否创建成功
     */
    boolean createBucket(String bucketName);

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return 是否删除成功
     */
    boolean deleteBucket(String bucketName);

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return 是否存在
     */
    boolean bucketExists(String bucketName);

    /**
     * 文件信息
     */
    class FileInfo {
        private String objectName;
        private String etag;
        private long size;
        private String lastModified;
        private String contentType;

        // 构造函数
        public FileInfo() {}

        public FileInfo(String objectName, String etag, long size, String lastModified, String contentType) {
            this.objectName = objectName;
            this.etag = etag;
            this.size = size;
            this.lastModified = lastModified;
            this.contentType = contentType;
        }

        // Getter和Setter
        public String getObjectName() { return objectName; }
        public void setObjectName(String objectName) { this.objectName = objectName; }

        public String getEtag() { return etag; }
        public void setEtag(String etag) { this.etag = etag; }

        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }

        public String getLastModified() { return lastModified; }
        public void setLastModified(String lastModified) { this.lastModified = lastModified; }

        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
    }
}