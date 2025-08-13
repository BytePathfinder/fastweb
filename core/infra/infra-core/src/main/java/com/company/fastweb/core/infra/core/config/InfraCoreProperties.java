package com.company.fastweb.core.infra.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础设施核心配置属性
 *
 * @author fastweb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.infra.core")
public class InfraCoreProperties {

    /**
     * 是否启用基础设施核心功能
     */
    private boolean enabled = true;

    /**
     * 应用名称
     */
    private String applicationName = "fastweb";

    /**
     * 环境配置
     */
    private Environment environment = new Environment();

    @Data
    public static class Environment {
        /**
         * 环境名称
         */
        private String name = "dev";

        /**
         * 是否为生产环境
         */
        private boolean production = false;
    }
}