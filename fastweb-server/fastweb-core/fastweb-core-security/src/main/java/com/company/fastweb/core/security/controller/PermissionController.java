package com.company.fastweb.core.security.controller;

import com.company.fastweb.core.common.model.ApiResult;
import com.company.fastweb.core.security.converter.PermissionConverter;
import com.company.fastweb.core.security.model.dto.PermissionCheckDTO;
import com.company.fastweb.core.security.model.form.PermissionCheckForm;
import com.company.fastweb.core.security.model.vo.PermissionInfoVO;
import com.company.fastweb.core.security.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理控制器
 *
 * @author FastWeb
 */
@Tag(name = "权限管理", description = "权限检查、权限信息查询等权限相关接口")
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionConverter permissionConverter;

    @Operation(summary = "检查权限", description = "检查当前用户是否拥有指定权限")
    @PostMapping("/check")
    public ResponseEntity<ApiResult<Boolean>> checkPermission(@Valid @RequestBody PermissionCheckForm form) {
        PermissionCheckDTO dto = permissionConverter.toDTO(form);
        boolean hasPermission = permissionService.checkPermission(dto);
        return ResponseEntity.ok(ApiResult.success(hasPermission));
    }

    @Operation(summary = "批量检查权限", description = "批量检查当前用户是否拥有多个权限")
    @PostMapping("/check/batch")
    public ResponseEntity<ApiResult<Map<String, Boolean>>> checkPermissions(
            @RequestBody List<String> permissions) {
        Map<String, Boolean> result = permissionService.checkPermissions(permissions);
        return ResponseEntity.ok(ApiResult.success(result));
    }

    @Operation(summary = "获取当前用户权限信息", description = "获取当前登录用户的所有权限信息")
    @GetMapping("/current")
    public ResponseEntity<ApiResult<PermissionInfoVO>> getCurrentUserPermissions() {
        PermissionInfoVO permissionInfo = permissionService.getCurrentUserPermissions();
        return ResponseEntity.ok(ApiResult.success(permissionInfo));
    }

    @Operation(summary = "检查角色", description = "检查当前用户是否拥有指定角色")
    @GetMapping("/role/{roleCode}")
    public ResponseEntity<ApiResult<Boolean>> checkRole(
            @Parameter(description = "角色编码") @PathVariable String roleCode) {
        boolean hasRole = permissionService.checkRole(roleCode);
        return ResponseEntity.ok(ApiResult.success(hasRole));
    }

    @Operation(summary = "检查任一角色", description = "检查当前用户是否拥有任一指定角色")
    @PostMapping("/role/any")
    public ResponseEntity<ApiResult<Boolean>> checkAnyRole(@RequestBody List<String> roleCodes) {
        boolean hasAnyRole = permissionService.checkAnyRole(roleCodes.toArray(new String[0]));
        return ResponseEntity.ok(ApiResult.success(hasAnyRole));
    }

    @Operation(summary = "清除权限缓存", description = "清除权限表达式缓存")
    @PostMapping("/cache/clear")
    public ResponseEntity<ApiResult<Void>> clearPermissionCache() {
        permissionService.clearPermissionCache();
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @Operation(summary = "获取权限缓存信息", description = "获取权限表达式缓存统计信息")
    @GetMapping("/cache/info")
    public ResponseEntity<ApiResult<Map<String, Object>>> getPermissionCacheInfo() {
        Map<String, Object> cacheInfo = permissionService.getPermissionCacheInfo();
        return ResponseEntity.ok(ApiResult.success(cacheInfo));
    }
}