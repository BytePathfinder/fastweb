package com.company.fastweb.core.storage.controller;

import com.company.fastweb.core.storage.converter.StorageConverter;
import com.company.fastweb.core.storage.model.dto.FileInfoDTO;
import com.company.fastweb.core.storage.model.dto.StorageObjectDTO;
import com.company.fastweb.core.storage.model.form.BucketCreateForm;
import com.company.fastweb.core.storage.model.form.FileUploadForm;
import com.company.fastweb.core.storage.model.vo.FileInfoVO;
import com.company.fastweb.core.storage.model.vo.FileUploadVO;
import com.company.fastweb.core.storage.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 存储服务控制器
 *
 * @author FastWeb
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;
    private final StorageConverter storageConverter;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<FileUploadVO> uploadFile(@Valid @ModelAttribute FileUploadForm form) {
        try {
            StorageObjectDTO dto = storageConverter.toDTO(form);
            String url = storageService.uploadFile(
                    form.bucketName(),
                    form.objectName() != null ? form.objectName() : form.file().getOriginalFilename(),
                    form.file().getInputStream(),
                    form.file().getContentType()
            );
            
            dto.setUrl(url);
            FileUploadVO vo = storageConverter.toUploadVO(dto);
            return ResponseEntity.ok(vo);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/files")
    public ResponseEntity<Boolean> deleteFile(
            @RequestParam String bucketName,
            @RequestParam String objectName) {
        boolean result = storageService.deleteFile(bucketName, objectName);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量删除文件
     */
    @DeleteMapping("/files/batch")
    public ResponseEntity<List<String>> deleteFiles(
            @RequestParam String bucketName,
            @RequestBody List<String> objectNames) {
        List<String> result = storageService.deleteFiles(bucketName, objectNames);
        return ResponseEntity.ok(result);
    }

    /**
     * 检查文件是否存在
     */
    @GetMapping("/files/exists")
    public ResponseEntity<Boolean> fileExists(
            @RequestParam String bucketName,
            @RequestParam String objectName) {
        boolean exists = storageService.fileExists(bucketName, objectName);
        return ResponseEntity.ok(exists);
    }

    /**
     * 获取文件信息
     */
    @GetMapping("/files/info")
    public ResponseEntity<FileInfoVO> getFileInfo(
            @RequestParam String bucketName,
            @RequestParam String objectName) {
        FileInfoDTO dto = storageService.getFileInfo(bucketName, objectName);
        FileInfoVO vo = storageConverter.toVO(dto);
        vo.setBucketName(bucketName);
        return ResponseEntity.ok(vo);
    }

    /**
     * 列出文件
     */
    @GetMapping("/files")
    public ResponseEntity<List<FileInfoVO>> listFiles(
            @RequestParam String bucketName,
            @RequestParam(required = false) String prefix,
            @RequestParam(defaultValue = "100") int maxKeys) {
        List<FileInfoDTO> dtoList = storageService.listFiles(bucketName, prefix, maxKeys);
        List<FileInfoVO> voList = storageConverter.toVOList(dtoList);
        // 设置bucketName
        voList.forEach(vo -> vo.setBucketName(bucketName));
        return ResponseEntity.ok(voList);
    }

    /**
     * 获取预签名上传URL
     */
    @PostMapping("/presigned-upload-url")
    public ResponseEntity<String> getPresignedUploadUrl(
            @RequestParam String bucketName,
            @RequestParam String objectName,
            @RequestParam(defaultValue = "3600") int expiry) {
        String url = storageService.getPresignedUploadUrl(bucketName, objectName, expiry);
        return ResponseEntity.ok(url);
    }

    /**
     * 获取预签名下载URL
     */
    @GetMapping("/presigned-download-url")
    public ResponseEntity<String> getPresignedDownloadUrl(
            @RequestParam String bucketName,
            @RequestParam String objectName,
            @RequestParam(defaultValue = "3600") int expiry) {
        String url = storageService.getPresignedDownloadUrl(bucketName, objectName, expiry);
        return ResponseEntity.ok(url);
    }

    /**
     * 创建存储桶
     */
    @PostMapping("/buckets")
    public ResponseEntity<Boolean> createBucket(@Valid @RequestBody BucketCreateForm form) {
        boolean result = storageService.createBucket(form.bucketName());
        return ResponseEntity.ok(result);
    }

    /**
     * 删除存储桶
     */
    @DeleteMapping("/buckets/{bucketName}")
    public ResponseEntity<Boolean> deleteBucket(@PathVariable String bucketName) {
        boolean result = storageService.deleteBucket(bucketName);
        return ResponseEntity.ok(result);
    }

    /**
     * 检查存储桶是否存在
     */
    @GetMapping("/buckets/{bucketName}/exists")
    public ResponseEntity<Boolean> bucketExists(@PathVariable String bucketName) {
        boolean exists = storageService.bucketExists(bucketName);
        return ResponseEntity.ok(exists);
    }
}