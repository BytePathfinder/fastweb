package com.company.fastweb.core.security.converter;

import com.company.fastweb.core.security.model.LoginUser;
import com.company.fastweb.core.security.model.dto.LoginDTO;
import com.company.fastweb.core.security.model.dto.UserInfoDTO;
import com.company.fastweb.core.security.model.form.LoginForm;
import com.company.fastweb.core.security.model.vo.LoginVO;
import com.company.fastweb.core.security.model.vo.UserInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 认证对象转换器
 *
 * @author FastWeb
 */
@Mapper(componentModel = "spring")
public interface AuthConverter {

    /**
     * LoginForm -> LoginDTO
     */
    LoginDTO toDTO(LoginForm form);

    /**
     * LoginUser -> LoginDTO
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "captcha", ignore = true)
    @Mapping(target = "captchaKey", ignore = true)
    @Mapping(target = "rememberMe", ignore = true)
    LoginDTO toDTO(LoginUser loginUser);

    /**
     * LoginDTO -> LoginVO
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "captcha", ignore = true)
    @Mapping(target = "captchaKey", ignore = true)
    @Mapping(target = "rememberMe", ignore = true)
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "isFirstLogin", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "loginLocation", ignore = true)
    @Mapping(target = "expiresIn", ignore = true)
    LoginVO toVO(LoginDTO dto);

    /**
     * LoginUser -> UserInfoDTO
     */
    UserInfoDTO toUserInfoDTO(LoginUser loginUser);

    /**
     * UserInfoDTO -> UserInfoVO
     */
    @Mapping(target = "mobile", expression = "java(maskMobile(dto.getMobile()))")
    UserInfoVO toUserInfoVO(UserInfoDTO dto);

    /**
     * 手机号脱敏
     */
    default String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 11) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }
}