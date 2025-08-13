package com.company.fastweb.core.infra.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据访问配置属性
 *
 * @author fastweb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.infra.data")
public class DataProperties {

    /**
     * 是否启用数据访问基础设施
     */
    private boolean enabled = true;

    /**
     * 分页配置
     */
    private Pagination pagination = new Pagination();

    /**
     * 审计配置
     */
    private Audit audit = new Audit();

    @Data
    public static class Pagination {
        /**
         * 默认页大小
         */
        private long defaultPageSize = 20;

        /**
         * 最大页大小
         */
        private long maxPageSize = 1000;
    }

    @Data
    public static class Audit {
        /**
         * 是否启用审计
         */
        private boolean enabled = true;

        /**
         * 创建时间字段名
         */
        private String createTimeField = "createTime";

        /**
         * 更新时间字段名
         */
        private String updateTimeField = "updateTime";

        /**
         * 创建人字段名
         */
        private String createByField = "createBy";

        /**
         * 更新人字段名
         */
        private String updateByField = "updateBy";
    }
}