package com.company.fastweb.core.security.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 登录表单
 *
 * @author FastWeb
 */
public record LoginForm(
        
        @NotBlank(message = "用户名不能为空")
        @Size(min = 2, max = 30, message = "用户名长度必须在2-30之间")
        String username,
        
        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
        String password,
        
        String captcha,
        
        String captchaKey,
        
        Boolean rememberMe,
        
        String loginDevice,
        
        String loginIp
) {
    public LoginForm {
        if (username != null) {
            username = username.trim();
        }
        if (rememberMe == null) {
            rememberMe = false;
        }
    }
}