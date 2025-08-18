package com.company.fastweb.core.security.config;

import com.company.fastweb.core.security.aspect.PreAuthorizeAspect;
import com.company.fastweb.core.security.expression.PermissionExpressionEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 安全模块自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@EnableAspectJAutoProxy
@ConditionalOnClass(name = "cn.dev33.satoken.stp.StpUtil")
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PermissionExpressionEngine permissionExpressionEngine() {
        log.info("FastWeb Permission Expression Engine initialized");
        return new PermissionExpressionEngine();
    }

    @Bean
    @ConditionalOnMissingBean
    public PreAuthorizeAspect preAuthorizeAspect(PermissionExpressionEngine expressionEngine) {
        log.info("FastWeb PreAuthorize Aspect initialized");
        return new PreAuthorizeAspect(expressionEngine);
    }
}