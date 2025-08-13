package com.company.fastweb.core.infra.messaging.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 消息配置属性
 *
 * @author fastweb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.infra.messaging")
public class MessagingProperties {

    /**
     * 是否启用消息基础设施
     */
    private boolean enabled = true;

    /**
     * 异步处理配置
     */
    private Async async = new Async();

    @Data
    public static class Async {
        /**
         * 核心线程数
         */
        private int corePoolSize = 5;

        /**
         * 最大线程数
         */
        private int maxPoolSize = 20;

        /**
         * 队列容量
         */
        private int queueCapacity = 100;

        /**
         * 线程名前缀
         */
        private String threadNamePrefix = "messaging-";
    }
}