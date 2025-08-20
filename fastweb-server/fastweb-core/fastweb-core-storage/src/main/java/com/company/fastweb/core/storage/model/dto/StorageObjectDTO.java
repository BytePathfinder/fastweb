package com.company.fastweb.core.storage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 存储对象传输对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageObjectDTO {

    /**
     * 对象名称
     */
    private String objectName;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * ETag
     */
    private String etag;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModified;

    /**
     * 访问URL
     */
    private String url;

    /**
     * 是否为目录
     */
    private Boolean isDirectory;

    /**
     * 元数据
     */
    private Map<String, String> metadata;
}