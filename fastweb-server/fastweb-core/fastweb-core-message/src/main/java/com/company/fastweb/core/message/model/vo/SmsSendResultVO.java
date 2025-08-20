package com.company.fastweb.core.message.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 短信发送结果视图对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendResultVO {

    /**
     * 是否发送成功
     */
    private boolean success;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 手机号码列表
     */
    private List<String> phoneNumbers;

    /**
     * 短信模板代码
     */
    private String templateCode;

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
     * 服务提供商
     */
    private String provider;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 发送状态（SENDING、SUCCESS、FAILED等）
     */
    private String status;

    /**
     * 发送耗时（毫秒）
     */
    private Long duration;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 消费金额（分）
     */
    private Long cost;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 服务商响应
     */
    private String providerResponse;

    /**
     * 扩展信息
     */
    private Map<String, Object> extras;

    /**
     * 备注信息
     */
    private String remark;
}