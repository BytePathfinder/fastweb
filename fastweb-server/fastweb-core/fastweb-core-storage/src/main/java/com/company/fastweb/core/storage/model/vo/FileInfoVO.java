package com.company.fastweb.core.storage.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息视图对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoVO {

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
     * 最后修改时间
     */
    private String lastModified;

    /**
     * 文件访问URL
     */
    private String url;

    /**
     * 是否为目录
     */
    private Boolean isDirectory;
}