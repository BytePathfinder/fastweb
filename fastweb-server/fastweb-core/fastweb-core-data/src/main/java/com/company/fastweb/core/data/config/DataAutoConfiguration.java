package com.company.fastweb.core.data.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.company.fastweb.core.data.properties.FastWebDataProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据访问自动配置
 * <p>
 * 功能特性：
 * - MyBatis-Plus 自动配置
 * - 多租户支持（可选）
 * - 审计字段自动填充
 * - 分页插件
 * - 乐观锁插件
 * - 逻辑删除插件
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(name = "com.baomidou.mybatisplus.core.mapper.BaseMapper")
@ComponentScan(basePackages = "com.company.fastweb.core.data")
@EnableConfigurationProperties(FastWebDataProperties.class)
@Import({MyBatisPlusConfig.class})
public class DataAutoConfiguration {

    public DataAutoConfiguration() {
        log.info("FastWeb Data Module initialized");
    }


}