package com.company.fastweb.core.message.controller;

import com.company.fastweb.core.message.model.dto.EmailDTO;
import com.company.fastweb.core.message.model.dto.NoticeDTO;
import com.company.fastweb.core.message.model.dto.SmsDTO;
import com.company.fastweb.core.message.model.qo.NoticeQueryQO;
import com.company.fastweb.core.message.model.vo.EmailSendResultVO;
import com.company.fastweb.core.message.model.vo.NoticeVO;
import com.company.fastweb.core.message.model.vo.SmsSendResultVO;
import com.company.fastweb.core.message.service.EmailService;
import com.company.fastweb.core.message.service.NoticeService;
import com.company.fastweb.core.message.service.SmsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 消息管理控制器
 *
 * @author FastWeb
 */
@Slf4j
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final EmailService emailService;
    private final SmsService smsService;
    private final NoticeService noticeService;

    // ==================== 邮件相关接口 ====================

    /**
     * 发送简单文本邮件
     */
    @PostMapping("/email/simple")
    public ResponseEntity<EmailSendResultVO> sendSimpleEmail(
            @RequestParam @NotBlank String to,
            @RequestParam @NotBlank String subject,
            @RequestParam @NotBlank String content) {
        EmailSendResultVO result = emailService.sendSimpleEmail(to, subject, content);
        return ResponseEntity.ok(result);
    }

    /**
     * 发送HTML邮件
     */
    @PostMapping("/email/html")
    public ResponseEntity<EmailSendResultVO> sendHtmlEmail(
            @RequestParam @NotBlank String to,
            @RequestParam @NotBlank String subject,
            @RequestParam @NotBlank String content) {
        EmailSendResultVO result = emailService.sendHtmlEmail(to, subject, content);
        return ResponseEntity.ok(result);
    }

    /**
     * 发送模板邮件
     */
    @PostMapping("/email/template")
    public ResponseEntity<EmailSendResultVO> sendTemplateEmail(
            @RequestParam @NotBlank String to,
            @RequestParam @NotBlank String subject,
            @RequestParam @NotBlank String templateName,
            @RequestBody Map<String, Object> variables) {
        EmailSendResultVO result = emailService.sendTemplateEmail(to, subject, templateName, variables);
        return ResponseEntity.ok(result);
    }

    /**
     * 发送邮件（完整参数）
     */
    @PostMapping("/email/send")
    public ResponseEntity<EmailSendResultVO> sendEmail(@Valid @RequestBody EmailDTO emailDTO) {
        EmailSendResultVO result = emailService.sendEmail(emailDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 异步发送邮件
     */
    @PostMapping("/email/send-async")
    public ResponseEntity<CompletableFuture<EmailSendResultVO>> sendEmailAsync(@Valid @RequestBody EmailDTO emailDTO) {
        CompletableFuture<EmailSendResultVO> result = emailService.sendEmailAsync(emailDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量发送邮件
     */
    @PostMapping("/email/batch-send")
    public ResponseEntity<List<EmailSendResultVO>> batchSendEmail(@Valid @RequestBody @NotEmpty List<EmailDTO> emailList) {
        List<EmailSendResultVO> results = emailService.batchSendEmail(emailList);
        return ResponseEntity.ok(results);
    }

    /**
     * 验证邮箱地址
     */
    @GetMapping("/email/validate")
    public ResponseEntity<Boolean> validateEmail(@RequestParam @NotBlank String email) {
        boolean valid = emailService.validateEmail(email);
        return ResponseEntity.ok(valid);
    }

    /**
     * 获取邮件发送统计
     */
    @GetMapping("/email/statistics")
    public ResponseEntity<Map<String, Object>> getEmailStatistics() {
        Map<String, Object> statistics = emailService.getEmailStatistics();
        return ResponseEntity.ok(statistics);
    }

    // ==================== 短信相关接口 ====================

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms/verify-code")
    public ResponseEntity<SmsSendResultVO> sendVerifyCode(
            @RequestParam @NotBlank String phoneNumber,
            @RequestParam @NotBlank String code) {
        SmsSendResultVO result = smsService.sendVerifyCode(phoneNumber, code);
        return ResponseEntity.ok(result);
    }

    /**
     * 发送登录通知短信
     */
    @PostMapping("/sms/login-notice")
    public ResponseEntity<SmsSendResultVO> sendLoginNotice(
            @RequestParam @NotBlank String phoneNumber,
            @RequestParam @NotBlank String loginTime,
            @RequestParam @NotBlank String loginIp) {
        SmsSendResultVO result = smsService.sendLoginNotice(phoneNumber, loginTime, loginIp);
        return ResponseEntity.ok(result);
    }

    /**
     * 发送模板短信
     */
    @PostMapping("/sms/template")
    public ResponseEntity<SmsSendResultVO> sendTemplateSms(
            @RequestParam @NotBlank String phoneNumber,
            @RequestParam @NotBlank String templateCode,
            @RequestBody Map<String, String> params) {
        SmsSendResultVO result = smsService.sendTemplateSms(phoneNumber, templateCode, params);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量发送模板短信
     */
    @PostMapping("/sms/batch-template")
    public ResponseEntity<SmsSendResultVO> sendBatchTemplateSms(
            @RequestBody @NotEmpty List<String> phoneNumbers,
            @RequestParam @NotBlank String templateCode,
            @RequestBody Map<String, String> params) {
        SmsSendResultVO result = smsService.sendBatchTemplateSms(phoneNumbers, templateCode, params);
        return ResponseEntity.ok(result);
    }

    /**
     * 发送短信（完整参数）
     */
    @PostMapping("/sms/send")
    public ResponseEntity<SmsSendResultVO> sendSms(@Valid @RequestBody SmsDTO smsDTO) {
        SmsSendResultVO result = smsService.sendSms(smsDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 异步发送短信
     */
    @PostMapping("/sms/send-async")
    public ResponseEntity<CompletableFuture<SmsSendResultVO>> sendSmsAsync(@Valid @RequestBody SmsDTO smsDTO) {
        CompletableFuture<SmsSendResultVO> result = smsService.sendSmsAsync(smsDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量发送短信
     */
    @PostMapping("/sms/batch-send")
    public ResponseEntity<List<SmsSendResultVO>> batchSendSms(@Valid @RequestBody @NotEmpty List<SmsDTO> smsList) {
        List<SmsSendResultVO> results = smsService.batchSendSms(smsList);
        return ResponseEntity.ok(results);
    }

    /**
     * 查询短信发送状态
     */
    @GetMapping("/sms/status/{bizId}")
    public ResponseEntity<String> querySmsStatus(@PathVariable @NotBlank String bizId) {
        String status = smsService.querySmsStatus(bizId);
        return ResponseEntity.ok(status);
    }

    /**
     * 查询短信发送详情
     */
    @GetMapping("/sms/detail/{bizId}")
    public ResponseEntity<Map<String, Object>> querySmsDetail(@PathVariable @NotBlank String bizId) {
        Map<String, Object> detail = smsService.querySmsDetail(bizId);
        return ResponseEntity.ok(detail);
    }

    /**
     * 验证手机号码
     */
    @GetMapping("/sms/validate")
    public ResponseEntity<Boolean> validatePhoneNumber(@RequestParam @NotBlank String phoneNumber) {
        boolean valid = smsService.validatePhoneNumber(phoneNumber);
        return ResponseEntity.ok(valid);
    }

    /**
     * 获取短信发送统计
     */
    @GetMapping("/sms/statistics")
    public ResponseEntity<Map<String, Object>> getSmsStatistics() {
        Map<String, Object> statistics = smsService.getSmsStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取短信余额
     */
    @GetMapping("/sms/balance")
    public ResponseEntity<Map<String, Object>> getSmsBalance() {
        Map<String, Object> balance = smsService.getSmsBalance();
        return ResponseEntity.ok(balance);
    }

    /**
     * 获取短信模板列表
     */
    @GetMapping("/sms/templates")
    public ResponseEntity<List<Map<String, Object>>> getSmsTemplates() {
        List<Map<String, Object>> templates = smsService.getSmsTemplates();
        return ResponseEntity.ok(templates);
    }

    // ==================== 站内信相关接口 ====================

    /**
     * 发送站内信
     */
    @PostMapping("/notice/send")
    public ResponseEntity<Long> sendNotice(@Valid @RequestBody NoticeDTO noticeDTO) {
        Long noticeId = noticeService.sendNotice(noticeDTO);
        return ResponseEntity.ok(noticeId);
    }

    /**
     * 发送系统通知
     */
    @PostMapping("/notice/system")
    public ResponseEntity<List<Long>> sendSystemNotice(
            @RequestParam @NotBlank String title,
            @RequestParam @NotBlank String content,
            @RequestBody(required = false) List<Long> userIds) {
        List<Long> noticeIds = noticeService.sendSystemNotice(title, content, userIds);
        return ResponseEntity.ok(noticeIds);
    }

    /**
     * 发送个人消息
     */
    @PostMapping("/notice/personal")
    public ResponseEntity<Long> sendPersonalMessage(
            @RequestParam @NotNull Long fromUserId,
            @RequestParam @NotNull Long toUserId,
            @RequestParam @NotBlank String title,
            @RequestParam @NotBlank String content) {
        Long noticeId = noticeService.sendPersonalMessage(fromUserId, toUserId, title, content);
        return ResponseEntity.ok(noticeId);
    }

    /**
     * 批量发送站内信
     */
    @PostMapping("/notice/batch-send")
    public ResponseEntity<List<Long>> batchSendNotice(@Valid @RequestBody @NotEmpty List<NoticeDTO> noticeList) {
        List<Long> noticeIds = noticeService.batchSendNotice(noticeList);
        return ResponseEntity.ok(noticeIds);
    }

    /**
     * 异步发送站内信
     */
    @PostMapping("/notice/send-async")
    public ResponseEntity<CompletableFuture<Long>> sendNoticeAsync(@Valid @RequestBody NoticeDTO noticeDTO) {
        CompletableFuture<Long> result = noticeService.sendNoticeAsync(noticeDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取用户站内信列表（分页）
     */
    @GetMapping("/notice/user/{userId}")
    public ResponseEntity<Page<NoticeVO>> getUserNoticeList(
            @PathVariable @NotNull Long userId,
            @Valid NoticeQueryQO qo) {
        Page<NoticeVO> page = noticeService.getUserNoticeList(userId, qo);
        return ResponseEntity.ok(page);
    }

    /**
     * 获取用户未读站内信列表
     */
    @GetMapping("/notice/user/{userId}/unread")
    public ResponseEntity<List<NoticeVO>> getUserUnreadNoticeList(
            @PathVariable @NotNull Long userId,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<NoticeVO> notices = noticeService.getUserUnreadNoticeList(userId, limit);
        return ResponseEntity.ok(notices);
    }

    /**
     * 获取站内信详情
     */
    @GetMapping("/notice/{noticeId}/user/{userId}")
    public ResponseEntity<NoticeVO> getNoticeDetail(
            @PathVariable @NotNull Long noticeId,
            @PathVariable @NotNull Long userId) {
        NoticeVO notice = noticeService.getNoticeDetail(noticeId, userId);
        return ResponseEntity.ok(notice);
    }

    /**
     * 标记站内信为已读
     */
    @PutMapping("/notice/{noticeId}/user/{userId}/read")
    public ResponseEntity<Boolean> markAsRead(
            @PathVariable @NotNull Long noticeId,
            @PathVariable @NotNull Long userId) {
        boolean success = noticeService.markAsRead(noticeId, userId);
        return ResponseEntity.ok(success);
    }

    /**
     * 批量标记站内信为已读
     */
    @PutMapping("/notice/user/{userId}/batch-read")
    public ResponseEntity<Integer> batchMarkAsRead(
            @PathVariable @NotNull Long userId,
            @RequestBody @NotEmpty List<Long> noticeIds) {
        int count = noticeService.batchMarkAsRead(noticeIds, userId);
        return ResponseEntity.ok(count);
    }

    /**
     * 标记所有站内信为已读
     */
    @PutMapping("/notice/user/{userId}/read-all")
    public ResponseEntity<Integer> markAllAsRead(@PathVariable @NotNull Long userId) {
        int count = noticeService.markAllAsRead(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * 删除站内信
     */
    @DeleteMapping("/notice/{noticeId}/user/{userId}")
    public ResponseEntity<Boolean> deleteNotice(
            @PathVariable @NotNull Long noticeId,
            @PathVariable @NotNull Long userId) {
        boolean success = noticeService.deleteNotice(noticeId, userId);
        return ResponseEntity.ok(success);
    }

    /**
     * 批量删除站内信
     */
    @DeleteMapping("/notice/user/{userId}/batch-delete")
    public ResponseEntity<Integer> batchDeleteNotice(
            @PathVariable @NotNull Long userId,
            @RequestBody @NotEmpty List<Long> noticeIds) {
        int count = noticeService.batchDeleteNotice(noticeIds, userId);
        return ResponseEntity.ok(count);
    }

    /**
     * 清空用户所有站内信
     */
    @DeleteMapping("/notice/user/{userId}/clear-all")
    public ResponseEntity<Integer> clearAllNotice(@PathVariable @NotNull Long userId) {
        int count = noticeService.clearAllNotice(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * 获取用户未读站内信数量
     */
    @GetMapping("/notice/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable @NotNull Long userId) {
        long count = noticeService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * 获取用户站内信统计信息
     */
    @GetMapping("/notice/user/{userId}/statistics")
    public ResponseEntity<Map<String, Object>> getUserNoticeStatistics(@PathVariable @NotNull Long userId) {
        Map<String, Object> statistics = noticeService.getUserNoticeStatistics(userId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取系统站内信统计信息
     */
    @GetMapping("/notice/system/statistics")
    public ResponseEntity<Map<String, Object>> getSystemNoticeStatistics() {
        Map<String, Object> statistics = noticeService.getSystemNoticeStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 清理过期站内信
     */
    @DeleteMapping("/notice/clean-expired")
    public ResponseEntity<Integer> cleanExpiredNotice() {
        int count = noticeService.cleanExpiredNotice();
        return ResponseEntity.ok(count);
    }

    /**
     * 推送实时消息给用户
     */
    @PostMapping("/notice/user/{userId}/push")
    public ResponseEntity<Boolean> pushRealtimeMessage(
            @PathVariable @NotNull Long userId,
            @RequestBody Object message) {
        boolean success = noticeService.pushRealtimeMessage(userId, message);
        return ResponseEntity.ok(success);
    }

    /**
     * 推送实时消息给多个用户
     */
    @PostMapping("/notice/users/push")
    public ResponseEntity<Integer> pushRealtimeMessageToUsers(
            @RequestBody @NotEmpty List<Long> userIds,
            @RequestBody Object message) {
        int count = noticeService.pushRealtimeMessageToUsers(userIds, message);
        return ResponseEntity.ok(count);
    }

    /**
     * 广播实时消息给所有在线用户
     */
    @PostMapping("/notice/broadcast")
    public ResponseEntity<Integer> broadcastRealtimeMessage(@RequestBody Object message) {
        int count = noticeService.broadcastRealtimeMessage(message);
        return ResponseEntity.ok(count);
    }
}