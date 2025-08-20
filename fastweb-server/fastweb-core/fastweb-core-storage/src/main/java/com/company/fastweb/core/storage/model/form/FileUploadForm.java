package com.company.fastweb.core.storage.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传表单对象
 *
 * @author FastWeb
 */
public record FileUploadForm(
        @NotNull(message = "文件不能为空")
        MultipartFile file,

        String bucketName,

        String objectName
) {
    public FileUploadForm {
        // 对象名称处理
        if (objectName != null) {
            objectName = objectName.trim();
        }
        // 存储桶名称处理
        if (bucketName != null) {
            bucketName = bucketName.trim().toLowerCase();
        }
    }
}