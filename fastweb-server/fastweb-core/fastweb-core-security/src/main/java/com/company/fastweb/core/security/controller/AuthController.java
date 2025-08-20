package com.company.fastweb.core.security.controller;

import com.company.fastweb.core.common.model.ApiResult;
import com.company.fastweb.core.security.converter.AuthConverter;
import com.company.fastweb.core.security.model.dto.LoginDTO;
import com.company.fastweb.core.security.model.dto.UserInfoDTO;
import com.company.fastweb.core.security.model.form.LoginForm;
import com.company.fastweb.core.security.model.vo.LoginVO;
import com.company.fastweb.core.security.model.vo.UserInfoVO;
import com.company.fastweb.core.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author FastWeb
 */
@Tag(name = "认证管理", description = "用户登录、登出、获取用户信息等认证相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthConverter authConverter;

    @Operation(summary = "用户登录", description = "用户名密码登录，返回访问令牌")
    @PostMapping("/login")
    public ResponseEntity<ApiResult<LoginVO>> login(@Valid @RequestBody LoginForm form) {
        LoginDTO loginDTO = authConverter.toDTO(form);
        LoginDTO result = authService.login(loginDTO);
        return ResponseEntity.ok(ApiResult.success(authConverter.toVO(result)));
    }

    @Operation(summary = "用户登出", description = "退出登录，清除用户会话")
    @PostMapping("/logout")
    public ResponseEntity<ApiResult<Void>> logout() {
        authService.logout();
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/userinfo")
    public ResponseEntity<ApiResult<UserInfoVO>> getUserInfo() {
        UserInfoDTO userInfo = authService.getCurrentUserInfo();
        return ResponseEntity.ok(ApiResult.success(authConverter.toVO(userInfo)));
    }

    @Operation(summary = "刷新令牌", description = "刷新访问令牌的有效期")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResult<Void>> refreshToken() {
        authService.refreshToken();
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @Operation(summary = "踢下线", description = "强制指定用户下线")
    @PostMapping("/kickout/{userId}")
    public ResponseEntity<ApiResult<Void>> kickout(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        authService.kickout(userId);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @Operation(summary = "检查登录状态", description = "检查当前用户是否已登录")
    @GetMapping("/status")
    public ResponseEntity<ApiResult<Boolean>> checkLoginStatus() {
        boolean isLogin = authService.isLogin();
        return ResponseEntity.ok(ApiResult.success(isLogin));
    }
}