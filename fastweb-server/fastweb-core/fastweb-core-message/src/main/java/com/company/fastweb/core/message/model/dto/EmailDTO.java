package com.company.fastweb.core.message.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 邮件传输对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

    /**
     * 发件人邮箱
     */
    private String from;

    /**
     * 发件人名称
     */
    private String fromName;

    /**
     * 收件人列表
     */
    private List<String> to;

    /**
     * 抄送列表
     */
    private List<String> cc;

    /**
     * 密送列表
     */
    private List<String> bcc;

    /**
     * 回复邮箱
     */
    private String replyTo;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 是否HTML格式
     */
    private boolean html;

    /**
     * 字符编码
     */
    private String encoding;

    /**
     * 附件列表
     */
    private List<File> attachments;

    /**
     * 邮件模板名称
     */
    private String templateName;

    /**
     * 模板变量
     */
    private Map<String, Object> templateVariables;

    /**
     * 邮件优先级（1-5，1最高）
     */
    private Integer priority;

    /**
     * 是否异步发送
     */
    private boolean async;

    /**
     * 发送时间（定时发送）
     */
    private LocalDateTime sendTime;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 业务标识
     */
    private String bizId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 扩展属性
     */
    private Map<String, Object> extras;

    /**
     * 备注信息
     */
    private String remark;
}