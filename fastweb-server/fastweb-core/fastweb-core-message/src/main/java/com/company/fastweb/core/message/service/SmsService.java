package com.company.fastweb.core.message.service;

import com.company.fastweb.core.message.model.dto.SmsDTO;
import com.company.fastweb.core.message.model.vo.SmsSendResultVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 短信服务接口
 *
 * @author FastWeb
 */
public interface SmsService {

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号码
     * @param code        验证码
     * @return 发送结果
     */
    SmsSendResultVO sendVerifyCode(String phoneNumber, String code);

    /**
     * 发送登录通知短信
     *
     * @param phoneNumber 手机号码
     * @param loginTime   登录时间
     * @param loginIp     登录IP
     * @return 发送结果
     */
    SmsSendResultVO sendLoginNotice(String phoneNumber, String loginTime, String loginIp);

    /**
     * 发送密码重置短信
     *
     * @param phoneNumber 手机号码
     * @param resetCode   重置码
     * @return 发送结果
     */
    SmsSendResultVO sendPasswordReset(String phoneNumber, String resetCode);

    /**
     * 发送系统通知短信
     *
     * @param phoneNumber 手机号码
     * @param message     通知消息
     * @return 发送结果
     */
    SmsSendResultVO sendSystemNotice(String phoneNumber, String message);

    /**
     * 发送模板短信
     *
     * @param phoneNumber  手机号码
     * @param templateCode 模板代码
     * @param params       模板参数
     * @return 发送结果
     */
    SmsSendResultVO sendTemplateSms(String phoneNumber, String templateCode, Map<String, String> params);

    /**
     * 批量发送模板短信
     *
     * @param phoneNumbers 手机号码列表
     * @param templateCode 模板代码
     * @param params       模板参数
     * @return 发送结果
     */
    SmsSendResultVO sendBatchTemplateSms(List<String> phoneNumbers, String templateCode, Map<String, String> params);

    /**
     * 发送短信（完整参数）
     *
     * @param smsDTO 短信DTO
     * @return 发送结果
     */
    SmsSendResultVO sendSms(SmsDTO smsDTO);

    /**
     * 异步发送短信验证码
     *
     * @param phoneNumber 手机号码
     * @param code        验证码
     * @return 异步发送结果
     */
    CompletableFuture<SmsSendResultVO> sendVerifyCodeAsync(String phoneNumber, String code);

    /**
     * 异步发送模板短信
     *
     * @param phoneNumber  手机号码
     * @param templateCode 模板代码
     * @param params       模板参数
     * @return 异步发送结果
     */
    CompletableFuture<SmsSendResultVO> sendTemplateSmsAsync(String phoneNumber, String templateCode, Map<String, String> params);

    /**
     * 异步发送短信（完整参数）
     *
     * @param smsDTO 短信DTO
     * @return 异步发送结果
     */
    CompletableFuture<SmsSendResultVO> sendSmsAsync(SmsDTO smsDTO);

    /**
     * 批量发送短信
     *
     * @param smsList 短信列表
     * @return 发送结果列表
     */
    List<SmsSendResultVO> batchSendSms(List<SmsDTO> smsList);

    /**
     * 异步批量发送短信
     *
     * @param smsList 短信列表
     * @return 异步发送结果
     */
    CompletableFuture<List<SmsSendResultVO>> batchSendSmsAsync(List<SmsDTO> smsList);

    /**
     * 查询短信发送状态
     *
     * @param bizId 业务ID
     * @return 发送状态
     */
    String querySmsStatus(String bizId);

    /**
     * 查询短信发送详情
     *
     * @param bizId 业务ID
     * @return 发送详情
     */
    Map<String, Object> querySmsDetail(String bizId);

    /**
     * 验证手机号码格式
     *
     * @param phoneNumber 手机号码
     * @return 是否有效
     */
    boolean validatePhoneNumber(String phoneNumber);

    /**
     * 获取短信发送统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getSmsStatistics();

    /**
     * 获取短信余额
     *
     * @return 余额信息
     */
    Map<String, Object> getSmsBalance();

    /**
     * 获取支持的短信模板列表
     *
     * @return 模板列表
     */
    List<Map<String, Object>> getSmsTemplates();
}