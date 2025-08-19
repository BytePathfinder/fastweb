package com.company.fastweb.core.storage.controller;

import com.company.fastweb.core.storage.config.StorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地存储文件访问控制器
 * 仅在本地存储模式下启用，提供文件的HTTP访问功能
 */
@Slf4j
@RestController
@RequestMapping("${fastweb.storage.local.url-prefix:/api/storage}")
@RequiredArgsConstructor
@ConditionalOnProperty(
    prefix = "fastweb.storage", 
    name = "type", 
    havingValue = "local", 
    matchIfMissing = true
)
public class LocalStorageController {

    private final StorageProperties storageProperties;

    /**
     * 访问本地存储文件
     * @param bucket 存储桶名称
     * @param object 文件对象路径（支持多级目录）
     * @return 文件内容响应
     */
    @GetMapping("/{bucket}/**")
    public ResponseEntity<byte[]> getLocalFile(@PathVariable("bucket") String bucket,
                                               HttpServletRequest request) {
        try {
            // 从请求路径中提取 object 路径
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            String servletPath = request.getServletPath();
            
            // 移除上下文路径和servlet路径，获取实际的映射路径
            String mappingPath = requestURI;
            if (contextPath != null && !contextPath.isEmpty()) {
                mappingPath = mappingPath.substring(contextPath.length());
            }
            
            // 提取 object 路径（去掉 /api/storage/{bucket}/ 前缀）
            String bucketPath = "/" + bucket + "/";
            int bucketIndex = mappingPath.indexOf(bucketPath);
            String object = "";
            if (bucketIndex >= 0) {
                object = mappingPath.substring(bucketIndex + bucketPath.length());
            }
            
            // 参数校验
            if (!StringUtils.hasText(bucket) || !StringUtils.hasText(object)) {
                log.warn("无效的文件访问参数: bucket={}, object={}", bucket, object);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // 构建文件路径
            Path basePath = Paths.get(storageProperties.getLocal().getPath());
            Path filePath = basePath.resolve(bucket).resolve(object).normalize();

            // 路径安全检查，防止目录穿越攻击
            if (!filePath.startsWith(basePath)) {
                log.warn("非法的文件访问路径: {}", filePath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // 文件存在性检查
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                log.debug("文件不存在: {}", filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 读取文件内容
            byte[] fileBytes = Files.readAllBytes(filePath);
            
            // 检测文件MIME类型
            String contentType = Files.probeContentType(filePath);
            if (!StringUtils.hasText(contentType)) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            log.debug("成功访问本地文件: bucket={}, object={}, size={}", bucket, object, fileBytes.length);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length))
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600") // 缓存1小时
                    .body(fileBytes);

        } catch (IOException e) {
            log.error("读取本地文件失败: bucket={}, requestURI={}", bucket, request.getRequestURI(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}