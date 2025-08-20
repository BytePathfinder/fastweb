package com.company.fastweb.core.security.converter;

import com.company.fastweb.core.security.model.dto.PermissionInfoDTO;
import com.company.fastweb.core.security.model.vo.PermissionInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 权限对象转换器
 *
 * @author FastWeb
 */
@Mapper(componentModel = "spring")
public interface PermissionConverter {

    /**
     * PermissionInfoDTO -> PermissionInfoVO
     */
    @Mapping(target = "dataScopeDesc", expression = "java(getDataScopeDesc(dto.getDataScope()))")
    @Mapping(target = "permissionResults", ignore = true)
    @Mapping(target = "roleResults", ignore = true)
    @Mapping(target = "cacheInfo", ignore = true)
    PermissionInfoVO toVO(PermissionInfoDTO dto);

    /**
     * 获取数据权限范围描述
     */
    default String getDataScopeDesc(Integer dataScope) {
        if (dataScope == null) {
            return "未设置";
        }
        return switch (dataScope) {
            case 1 -> "全部数据";
            case 2 -> "自定义数据";
            case 3 -> "本部门数据";
            case 4 -> "本部门及以下数据";
            case 5 -> "仅本人数据";
            default -> "未知范围";
        };
    }
}