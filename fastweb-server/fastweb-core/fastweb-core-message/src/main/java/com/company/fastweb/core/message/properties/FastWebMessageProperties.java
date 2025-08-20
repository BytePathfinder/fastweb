package com.company.fastweb.core.message.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 消息模块配置属性
 *
 * @author FastWeb
 */
@Data
@ConfigurationProperties(prefix = "fastweb.message")
public class FastWebMessageProperties {

    /**
     * 邮件配置
     */
    private Email email = new Email();

    /**
     * 短信配置
     */
    private Sms sms = new Sms();

    /**
     * 站内信配置
     */
    private Notice notice = new Notice();

    /**
     * 邮件配置
     */
    @Data
    public static class Email {
        /**
         * 是否启用邮件服务
         */
        private boolean enabled = true;

        /**
         * 发件人邮箱
         */
        private String from;

        /**
         * 发件人名称
         */
        private String fromName = "FastWeb";

        /**
         * 邮件模板路径
         */
        private String templatePath = "classpath:/templates/email/";

        /**
         * 默认字符编码
         */
        private String encoding = "UTF-8";

        /**
         * 是否启用HTML格式
         */
        private boolean htmlEnabled = true;

        /**
         * 邮件发送超时时间（毫秒）
         */
        private long timeout = 30000;

        /**
         * 最大重试次数
         */
        private int maxRetryCount = 3;

        /**
         * 重试间隔时间（毫秒）
         */
        private long retryInterval = 5000;

        /**
         * 是否异步发送
         */
        private boolean async = true;

        /**
         * 异步线程池大小
         */
        private int asyncPoolSize = 10;
    }

    /**
     * 短信配置
     */
    @Data
    public static class Sms {
        /**
         * 是否启用短信服务
         */
        private boolean enabled = false;

        /**
         * 短信服务提供商（aliyun、tencent、huawei等）
         */
        private String provider = "aliyun";

        /**
         * AccessKey ID
         */
        private String accessKeyId;

        /**
         * AccessKey Secret
         */
        private String accessKeySecret;

        /**
         * 短信签名
         */
        private String signName;

        /**
         * 地域节点
         */
        private String regionId = "cn-hangzhou";

        /**
         * 请求超时时间（毫秒）
         */
        private long timeout = 10000;

        /**
         * 最大重试次数
         */
        private int maxRetryCount = 3;

        /**
         * 重试间隔时间（毫秒）
         */
        private long retryInterval = 3000;

        /**
         * 是否异步发送
         */
        private boolean async = true;

        /**
         * 异步线程池大小
         */
        private int asyncPoolSize = 5;

        /**
         * 短信模板配置
         */
        private Templates templates = new Templates();

        /**
         * 短信模板配置
         */
        @Data
        public static class Templates {
            /**
             * 验证码模板
             */
            private String verifyCode;

            /**
             * 登录通知模板
             */
            private String loginNotice;

            /**
             * 密码重置模板
             */
            private String passwordReset;

            /**
             * 系统通知模板
             */
            private String systemNotice;
        }
    }

    /**
     * 站内信配置
     */
    @Data
    public static class Notice {
        /**
         * 是否启用站内信服务
         */
        private boolean enabled = true;

        /**
         * 消息保留天数
         */
        private int retentionDays = 30;

        /**
         * 单用户最大未读消息数
         */
        private int maxUnreadCount = 1000;

        /**
         * 是否启用实时推送
         */
        private boolean realtimePushEnabled = true;

        /**
         * WebSocket推送路径
         */
        private String websocketPath = "/ws/notice";

        /**
         * 批量发送大小
         */
        private int batchSize = 100;
    }
}