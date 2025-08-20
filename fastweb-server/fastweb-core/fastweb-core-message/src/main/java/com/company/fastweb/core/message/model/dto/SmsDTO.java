package com.company.fastweb.core.message.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 短信传输对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsDTO {

    /**
     * 手机号码列表
     */
    private List<String> phoneNumbers;

    /**
     * 短信模板代码
     */
    private String templateCode;

    /**
     * 模板参数
     */
    private Map<String, String> params;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信内容（非模板短信）
     */
    private String content;

    /**
     * 短信类型（验证码、通知、营销等）
     */
    private String smsType;

    /**
     * 服务提供商（aliyun、tencent、huawei等）
     */
    private String provider;

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
     * 请求超时时间（毫秒）
     */
    private Long timeout;

    /**
     * 扩展属性
     */
    private Map<String, Object> extras;

    /**
     * 备注信息
     */
    private String remark;
}