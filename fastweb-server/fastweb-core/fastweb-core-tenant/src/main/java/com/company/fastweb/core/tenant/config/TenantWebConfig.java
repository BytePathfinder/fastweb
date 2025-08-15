package com.company.fastweb.core.tenant.config;

import com.company.fastweb.core.tenant.interceptor.TenantInterceptor;
import com.company.fastweb.core.tenant.resolver.TenantResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 租户Web配置
 * 注册租户拦截器
 */
@Configuration
public class TenantWebConfig implements WebMvcConfigurer {
    
    private final TenantResolver tenantResolver;
    
    public TenantWebConfig(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor(tenantResolver))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/logout",
                        "/error",
                        "/static/**",
                        "/webjars/**",
                        "/favicon.ico",
                        "/actuator/**"
                );
    }
}