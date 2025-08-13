package com.company.fastweb.core.infra.web.config;

import com.company.fastweb.core.infra.web.exception.GlobalExceptionHandler;
import com.company.fastweb.core.infra.web.interceptor.ApiLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web基础设施自动配置
 *
 * @author fastweb
 */
@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebProperties.class)
@ComponentScan(basePackages = "com.company.fastweb.core.infra.web")
@RequiredArgsConstructor
public class WebAutoConfiguration implements WebMvcConfigurer {

    private final WebProperties webProperties;

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public ApiLogInterceptor apiLogInterceptor() {
        return new ApiLogInterceptor(webProperties);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiLogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**");
    }
}