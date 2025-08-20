package com.company.fastweb.core.message.service.impl;

import com.company.fastweb.core.message.model.dto.SmsDTO;
import com.company.fastweb.core.message.model.vo.SmsSendResultVO;
import com.company.fastweb.core.message.properties.FastWebMessageProperties;
import com.company.fastweb.core.message.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * 短信服务实现类
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final FastWebMessageProperties properties;

    // 手机号格式验证正则（中国大陆）
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^1[3-9]\\d{9}$"
    );

    // 统计信息
    private final AtomicLong totalSent = new AtomicLong(0);
    private final AtomicLong totalSuccess = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);

    @Override
    public SmsSendResultVO sendVerifyCode(String phoneNumber, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        return sendTemplateSms(phoneNumber, properties.getSms().getTemplates().getVerifyCode(), params);
    }

    @Override
    public SmsSendResultVO sendLoginNotice(String phoneNumber, String loginTime, String loginIp) {
        Map<String, String> params = new HashMap<>();
        params.put("time", loginTime);
        params.put("ip", loginIp);
        return sendTemplateSms(phoneNumber, properties.getSms().getTemplates().getLoginNotice(), params);
    }

    @Override
    public SmsSendResultVO sendPasswordReset(String phoneNumber, String resetCode) {
        Map<String, String> params = new HashMap<>();
        params.put("code", resetCode);
        return sendTemplateSms(phoneNumber, properties.getSms().getTemplates().getPasswordReset(), params);
    }

    @Override
    public SmsSendResultVO sendSystemNotice(String phoneNumber, String message) {
        Map<String, String> params = new HashMap<>();
        params.put("message", message);
        return sendTemplateSms(phoneNumber, properties.getSms().getTemplates().getSystemNotice(), params);
    }

    @Override
    public SmsSendResultVO sendTemplateSms(String phoneNumber, String templateCode, Map<String, String> params) {
        return sendBatchTemplateSms(Collections.singletonList(phoneNumber), templateCode, params);
    }

    @Override
    public SmsSendResultVO sendBatchTemplateSms(List<String> phoneNumbers, String templateCode, Map<String, String> params) {
        try {
            // 验证手机号
            for (String phoneNumber : phoneNumbers) {
                if (!validatePhoneNumber(phoneNumber)) {
                    return SmsSendResultVO.builder()
                            .success(false)
                            .phoneNumbers(phoneNumbers)
                            .templateCode(templateCode)
                            .sendTime(LocalDateTime.now())
                            .message("手机号格式不正确: " + phoneNumber)
                            .errorCode("INVALID_PHONE_NUMBER")
                            .build();
                }
            }

            // 验证模板代码
            if (!StringUtils.hasText(templateCode)) {
                return SmsSendResultVO.builder()
                        .success(false)
                        .phoneNumbers(phoneNumbers)
                        .templateCode(templateCode)
                        .sendTime(LocalDateTime.now())
                        .message("短信模板代码不能为空")
                        .errorCode("EMPTY_TEMPLATE_CODE")
                        .build();
            }

            // 根据配置的短信服务提供商发送短信
            String provider = properties.getSms().getProvider();
            SmsSendResultVO result;
            
            switch (provider.toLowerCase()) {
                case "aliyun":
                    result = sendByAliyun(phoneNumbers, templateCode, params);
                    break;
                case "tencent":
                    result = sendByTencent(phoneNumbers, templateCode, params);
                    break;
                case "huawei":
                    result = sendByHuawei(phoneNumbers, templateCode, params);
                    break;
                default:
                    result = SmsSendResultVO.builder()
                            .success(false)
                            .phoneNumbers(phoneNumbers)
                            .templateCode(templateCode)
                            .sendTime(LocalDateTime.now())
                            .message("不支持的短信服务提供商: " + provider)
                            .errorCode("UNSUPPORTED_PROVIDER")
                            .build();
            }

            // 更新统计信息
            totalSent.addAndGet(phoneNumbers.size());
            if (result.isSuccess()) {
                totalSuccess.addAndGet(phoneNumbers.size());
            } else {
                totalFailed.addAndGet(phoneNumbers.size());
            }

            return result;
        } catch (Exception e) {
            log.error("发送短信失败: phoneNumbers={}, templateCode={}", phoneNumbers, templateCode, e);
            totalSent.addAndGet(phoneNumbers.size());
            totalFailed.addAndGet(phoneNumbers.size());
            
            return SmsSendResultVO.builder()
                    .success(false)
                    .phoneNumbers(phoneNumbers)
                    .templateCode(templateCode)
                    .sendTime(LocalDateTime.now())
                    .message("短信发送失败: " + e.getMessage())
                    .errorCode("SMS_SEND_ERROR")
                    .build();
        }
    }

    @Override
    public SmsSendResultVO sendSms(SmsDTO smsDTO) {
        return sendBatchTemplateSms(smsDTO.getPhoneNumbers(), smsDTO.getTemplateCode(), smsDTO.getParams());
    }

    @Async
    @Override
    public CompletableFuture<SmsSendResultVO> sendVerifyCodeAsync(String phoneNumber, String code) {
        return CompletableFuture.completedFuture(sendVerifyCode(phoneNumber, code));
    }

    @Async
    @Override
    public CompletableFuture<SmsSendResultVO> sendTemplateSmsAsync(String phoneNumber, String templateCode, Map<String, String> params) {
        return CompletableFuture.completedFuture(sendTemplateSms(phoneNumber, templateCode, params));
    }

    @Async
    @Override
    public CompletableFuture<SmsSendResultVO> sendSmsAsync(SmsDTO smsDTO) {
        return CompletableFuture.completedFuture(sendSms(smsDTO));
    }

    @Override
    public List<SmsSendResultVO> batchSendSms(List<SmsDTO> smsList) {
        List<SmsSendResultVO> results = new ArrayList<>();
        for (SmsDTO smsDTO : smsList) {
            results.add(sendSms(smsDTO));
        }
        return results;
    }

    @Async
    @Override
    public CompletableFuture<List<SmsSendResultVO>> batchSendSmsAsync(List<SmsDTO> smsList) {
        return CompletableFuture.completedFuture(batchSendSms(smsList));
    }

    @Override
    public String querySmsStatus(String bizId) {
        // TODO: 实现短信状态查询逻辑
        log.info("查询短信状态: bizId={}", bizId);
        return "DELIVERED";
    }

    @Override
    public Map<String, Object> querySmsDetail(String bizId) {
        // TODO: 实现短信详情查询逻辑
        Map<String, Object> detail = new HashMap<>();
        detail.put("bizId", bizId);
        detail.put("status", "DELIVERED");
        detail.put("sendTime", LocalDateTime.now());
        detail.put("receiveTime", LocalDateTime.now());
        return detail;
    }

    @Override
    public boolean validatePhoneNumber(String phoneNumber) {
        return StringUtils.hasText(phoneNumber) && PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    @Override
    public Map<String, Object> getSmsStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalSent", totalSent.get());
        statistics.put("totalSuccess", totalSuccess.get());
        statistics.put("totalFailed", totalFailed.get());
        statistics.put("successRate", totalSent.get() > 0 ? (double) totalSuccess.get() / totalSent.get() : 0.0);
        statistics.put("lastUpdateTime", LocalDateTime.now());
        return statistics;
    }

    @Override
    public Map<String, Object> getSmsBalance() {
        // TODO: 实现短信余额查询逻辑
        Map<String, Object> balance = new HashMap<>();
        balance.put("balance", 10000);
        balance.put("unit", "条");
        balance.put("lastUpdateTime", LocalDateTime.now());
        return balance;
    }

    @Override
    public List<Map<String, Object>> getSmsTemplates() {
        // TODO: 实现短信模板列表查询逻辑
        List<Map<String, Object>> templates = new ArrayList<>();
        
        Map<String, Object> template1 = new HashMap<>();
        template1.put("templateCode", properties.getSms().getTemplates().getVerifyCode());
        template1.put("templateName", "验证码模板");
        template1.put("templateContent", "您的验证码是${code}，5分钟内有效。");
        templates.add(template1);
        
        Map<String, Object> template2 = new HashMap<>();
        template2.put("templateCode", properties.getSms().getTemplates().getLoginNotice());
        template2.put("templateName", "登录通知模板");
        template2.put("templateContent", "您于${time}在${ip}登录了系统。");
        templates.add(template2);
        
        return templates;
    }

    /**
     * 通过阿里云发送短信
     */
    private SmsSendResultVO sendByAliyun(List<String> phoneNumbers, String templateCode, Map<String, String> params) {
        // TODO: 集成阿里云短信服务SDK
        log.info("通过阿里云发送短信: phoneNumbers={}, templateCode={}, params={}", phoneNumbers, templateCode, params);
        
        return SmsSendResultVO.builder()
                .success(true)
                .bizId(UUID.randomUUID().toString())
                .phoneNumbers(phoneNumbers)
                .templateCode(templateCode)
                .sendTime(LocalDateTime.now())
                .message("短信发送成功")
                .provider("aliyun")
                .build();
    }

    /**
     * 通过腾讯云发送短信
     */
    private SmsSendResultVO sendByTencent(List<String> phoneNumbers, String templateCode, Map<String, String> params) {
        // TODO: 集成腾讯云短信服务SDK
        log.info("通过腾讯云发送短信: phoneNumbers={}, templateCode={}, params={}", phoneNumbers, templateCode, params);
        
        return SmsSendResultVO.builder()
                .success(true)
                .bizId(UUID.randomUUID().toString())
                .phoneNumbers(phoneNumbers)
                .templateCode(templateCode)
                .sendTime(LocalDateTime.now())
                .message("短信发送成功")
                .provider("tencent")
                .build();
    }

    /**
     * 通过华为云发送短信
     */
    private SmsSendResultVO sendByHuawei(List<String> phoneNumbers, String templateCode, Map<String, String> params) {
        // TODO: 集成华为云短信服务SDK
        log.info("通过华为云发送短信: phoneNumbers={}, templateCode={}, params={}", phoneNumbers, templateCode, params);
        
        return SmsSendResultVO.builder()
                .success(true)
                .bizId(UUID.randomUUID().toString())
                .phoneNumbers(phoneNumbers)
                .templateCode(templateCode)
                .sendTime(LocalDateTime.now())
                .message("短信发送成功")
                .provider("huawei")
                .build();
    }
}