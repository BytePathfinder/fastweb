package com.company.fastweb.core.storage.converter;

import com.company.fastweb.core.storage.model.dto.FileInfoDTO;
import com.company.fastweb.core.storage.model.dto.StorageObjectDTO;
import com.company.fastweb.core.storage.model.form.FileUploadForm;
import com.company.fastweb.core.storage.model.vo.FileInfoVO;
import com.company.fastweb.core.storage.model.vo.FileUploadVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 存储模块对象转换器
 *
 * @author FastWeb
 */
@Mapper(componentModel = "spring")
public interface StorageConverter {

    /**
     * 表单转DTO
     */
    @Mapping(target = "size", expression = "java(form.file().getSize())")
    @Mapping(target = "contentType", expression = "java(form.file().getContentType())")
    @Mapping(target = "etag", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "isDirectory", constant = "false")
    @Mapping(target = "metadata", ignore = true)
    StorageObjectDTO toDTO(FileUploadForm form);

    /**
     * DTO转VO
     */
    @Mapping(target = "uploadTime", expression = "java(System.currentTimeMillis())")
    FileUploadVO toUploadVO(StorageObjectDTO dto);

    /**
     * FileInfoDTO转VO
     */
    @Mapping(target = "bucketName", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "isDirectory", constant = "false")
    FileInfoVO toVO(FileInfoDTO dto);

    /**
     * 批量转换
     */
    List<FileInfoVO> toVOList(List<FileInfoDTO> dtoList);

    /**
     * 创建FileInfoDTO
     */
    default FileInfoDTO createFileInfoDTO(String objectName, String etag, Long size, String lastModified, String contentType) {
        return FileInfoDTO.builder()
                .objectName(objectName)
                .etag(etag)
                .size(size)
                .lastModified(lastModified)
                .contentType(contentType)
                .build();
    }

    /**
     * 创建StorageObjectDTO
     */
    default StorageObjectDTO createStorageObjectDTO(String bucketName, String objectName, MultipartFile file) {
        return StorageObjectDTO.builder()
                .bucketName(bucketName)
                .objectName(objectName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .isDirectory(false)
                .build();
    }
}