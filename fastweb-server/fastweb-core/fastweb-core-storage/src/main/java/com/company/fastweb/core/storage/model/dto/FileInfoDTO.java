package com.company.fastweb.core.storage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息传输对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDTO {

    /**
     * 对象名称
     */
    private String objectName;

    /**
     * ETag
     */
    private String etag;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 最后修改时间
     */
    private String lastModified;

    /**
     * 内容类型
     */
    private String contentType;
}