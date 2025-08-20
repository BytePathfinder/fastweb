package com.company.fastweb.core.message.service;

import com.company.fastweb.core.message.model.dto.EmailDTO;
import com.company.fastweb.core.message.model.vo.EmailSendResultVO;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 邮件服务接口
 *
 * @author FastWeb
 */
public interface EmailService {

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @return 发送结果
     */
    EmailSendResultVO sendSimpleEmail(String to, String subject, String content);

    /**
     * 发送简单文本邮件（多收件人）
     *
     * @param toList  收件人列表
     * @param subject 主题
     * @param content 内容
     * @return 发送结果
     */
    EmailSendResultVO sendSimpleEmail(List<String> toList, String subject, String content);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content HTML内容
     * @return 发送结果
     */
    EmailSendResultVO sendHtmlEmail(String to, String subject, String content);

    /**
     * 发送HTML邮件（多收件人）
     *
     * @param toList  收件人列表
     * @param subject 主题
     * @param content HTML内容
     * @return 发送结果
     */
    EmailSendResultVO sendHtmlEmail(List<String> toList, String subject, String content);

    /**
     * 发送带附件的邮件
     *
     * @param to          收件人
     * @param subject     主题
     * @param content     内容
     * @param attachments 附件列表
     * @return 发送结果
     */
    EmailSendResultVO sendEmailWithAttachments(String to, String subject, String content, List<File> attachments);

    /**
     * 发送模板邮件
     *
     * @param to           收件人
     * @param subject      主题
     * @param templateName 模板名称
     * @param variables    模板变量
     * @return 发送结果
     */
    EmailSendResultVO sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables);

    /**
     * 发送模板邮件（多收件人）
     *
     * @param toList       收件人列表
     * @param subject      主题
     * @param templateName 模板名称
     * @param variables    模板变量
     * @return 发送结果
     */
    EmailSendResultVO sendTemplateEmail(List<String> toList, String subject, String templateName, Map<String, Object> variables);

    /**
     * 发送邮件（完整参数）
     *
     * @param emailDTO 邮件DTO
     * @return 发送结果
     */
    EmailSendResultVO sendEmail(EmailDTO emailDTO);

    /**
     * 异步发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @return 异步发送结果
     */
    CompletableFuture<EmailSendResultVO> sendSimpleEmailAsync(String to, String subject, String content);

    /**
     * 异步发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content HTML内容
     * @return 异步发送结果
     */
    CompletableFuture<EmailSendResultVO> sendHtmlEmailAsync(String to, String subject, String content);

    /**
     * 异步发送模板邮件
     *
     * @param to           收件人
     * @param subject      主题
     * @param templateName 模板名称
     * @param variables    模板变量
     * @return 异步发送结果
     */
    CompletableFuture<EmailSendResultVO> sendTemplateEmailAsync(String to, String subject, String templateName, Map<String, Object> variables);

    /**
     * 异步发送邮件（完整参数）
     *
     * @param emailDTO 邮件DTO
     * @return 异步发送结果
     */
    CompletableFuture<EmailSendResultVO> sendEmailAsync(EmailDTO emailDTO);

    /**
     * 批量发送邮件
     *
     * @param emailList 邮件列表
     * @return 发送结果列表
     */
    List<EmailSendResultVO> batchSendEmail(List<EmailDTO> emailList);

    /**
     * 异步批量发送邮件
     *
     * @param emailList 邮件列表
     * @return 异步发送结果
     */
    CompletableFuture<List<EmailSendResultVO>> batchSendEmailAsync(List<EmailDTO> emailList);

    /**
     * 验证邮箱地址格式
     *
     * @param email 邮箱地址
     * @return 是否有效
     */
    boolean validateEmail(String email);

    /**
     * 获取邮件发送统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getEmailStatistics();
}