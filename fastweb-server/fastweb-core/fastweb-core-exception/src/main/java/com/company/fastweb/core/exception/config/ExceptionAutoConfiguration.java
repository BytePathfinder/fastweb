package com.company.fastweb.core.exception.config;

import com.company.fastweb.core.exception.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 异常处理自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ExceptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        log.info("FastWeb Exception Handler initialized");
        return new GlobalExceptionHandler();
    }
}