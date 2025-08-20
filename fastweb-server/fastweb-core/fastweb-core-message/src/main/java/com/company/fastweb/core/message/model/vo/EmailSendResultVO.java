package com.company.fastweb.core.message.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送结果视图对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendResultVO {

    /**
     * 是否发送成功
     */
    private boolean success;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 收件人列表
     */
    private List<String> recipients;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误详情
     */
    private String errorDetail;

    /**
     * 附件数量
     */
    private Integer attachmentCount;

    /**
     * 发送耗时（毫秒）
     */
    private Long duration;

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
     * 邮件服务器响应
     */
    private String serverResponse;

    /**
     * 扩展信息
     */
    private Map<String, Object> extras;

    /**
     * 备注信息
     */
    private String remark;
}