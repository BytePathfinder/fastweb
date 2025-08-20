package com.company.fastweb.core.storage.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 存储桶创建表单对象
 *
 * @author FastWeb
 */
public record BucketCreateForm(
        @NotBlank(message = "存储桶名称不能为空")
        @Pattern(regexp = "^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$", 
                message = "存储桶名称只能包含小写字母、数字和连字符，长度3-63字符")
        String bucketName
) {
    public BucketCreateForm {
        if (bucketName != null) {
            bucketName = bucketName.trim().toLowerCase();
        }
    }
}