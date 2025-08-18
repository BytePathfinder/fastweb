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
    String upload(String bucketName, String objectName, InputStream inputStream, String contentType);

    /**
     * 上传文件（默认存储桶）
     *
     * @param objectName 对象名称
     * @param inputStream 文件流
     * @param contentType 文件类型
     * @return 文件访问URL
     */
    String upload(String objectName, InputStream inputStream, String contentType);

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 文件流
     */
    InputStream download(String bucketName, String objectName);

    /**
     * 下载文件（默认存储桶）
     *
     * @param objectName 对象名称
     * @return 文件流
     */
    InputStream download(String objectName);

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 是否删除成功
     */
    boolean delete(String bucketName, String objectName);

    /**
     * 删除文件（默认存储桶）
     *
     * @param objectName 对象名称
     * @return 是否删除成功
     */
    boolean delete(String objectName);

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param objectNames 对象名称列表
     * @return 删除结果列表
     */
    List<String> deleteMultiple(String bucketName, List<String> objectNames);

    /**
     * 检查文件是否存在
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 是否存在
     */
    boolean exists(String bucketName, String objectName);

    /**
     * 检查文件是否存在（默认存储桶）
     *
     * @param objectName 对象名称
     * @return 是否存在
     */
    boolean exists(String objectName);

    /**
     * 获取文件信息
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 文件信息
     */
    StorageObject getObjectInfo(String bucketName, String objectName);

    /**
     * 获取文件信息（默认存储桶）
     *
     * @param objectName 对象名称
     * @return 文件信息
     */
    StorageObject getObjectInfo(String objectName);

    /**
     * 列出文件
     *
     * @param bucketName 存储桶名称
     * @param prefix 前缀
     * @param maxKeys 最大数量
     * @return 文件列表
     */
    List<StorageObject> listObjects(String bucketName, String prefix, int maxKeys);

    /**
     * 列出文件（默认存储桶）
     *
     * @param prefix 前缀
     * @param maxKeys 最大数量
     * @return 文件列表
     */
    List<StorageObject> listObjects(String prefix, int maxKeys);

    /**
     * 生成预签名URL
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiry 过期时间（秒）
     * @return 预签名URL
     */
    String generatePresignedUrl(String bucketName, String objectName, int expiry);

    /**
     * 生成预签名URL（默认存储桶）
     *
     * @param objectName 对象名称
     * @param expiry 过期时间（秒）
     * @return 预签名URL
     */
    String generatePresignedUrl(String objectName, int expiry);

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
}