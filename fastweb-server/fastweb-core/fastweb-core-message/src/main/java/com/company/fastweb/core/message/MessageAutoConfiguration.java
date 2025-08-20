package com.company.fastweb.core.message;

import com.company.fastweb.core.message.properties.FastWebMessageProperties;
import com.company.fastweb.core.message.service.EmailService;
import com.company.fastweb.core.message.service.SmsService;
import com.company.fastweb.core.message.service.impl.EmailServiceImpl;
import com.company.fastweb.core.message.service.impl.SmsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 消息模块自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@ComponentScan("com.company.fastweb.core.message")
@EnableConfigurationProperties(FastWebMessageProperties.class)
public class MessageAutoConfiguration {

    /**
     * 邮件服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.message.email", name = "enabled", havingValue = "true", matchIfMissing = true)
    public EmailService emailService(JavaMailSender mailSender, FastWebMessageProperties properties) {
        log.info("初始化邮件服务");
        return new EmailServiceImpl(mailSender, properties);
    }

    /**
     * 短信服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.message.sms", name = "enabled", havingValue = "true")
    public SmsService smsService(FastWebMessageProperties properties) {
        log.info("初始化短信服务");
        return new SmsServiceImpl(properties);
    }
}