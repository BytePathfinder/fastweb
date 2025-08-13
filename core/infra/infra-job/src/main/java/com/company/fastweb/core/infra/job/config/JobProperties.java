package com.company.fastweb.core.infra.job.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 任务调度配置属性
 *
 * @author fastweb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.infra.job")
public class JobProperties {

    /**
     * 是否启用任务调度基础设施
     */
    private boolean enabled = true;

    /**
     * 线程池配置
     */
    private ThreadPool threadPool = new ThreadPool();

    @Data
    public static class ThreadPool {
        /**
         * 核心线程数
         */
        private int corePoolSize = 5;

        /**
         * 最大线程数
         */
        private int maxPoolSize = 10;

        /**
         * 队列容量
         */
        private int queueCapacity = 50;

        /**
         * 线程名前缀
         */
        private String threadNamePrefix = "job-";

        /**
         * 线程空闲时间（秒）
         */
        private int keepAliveSeconds = 60;
    }
}