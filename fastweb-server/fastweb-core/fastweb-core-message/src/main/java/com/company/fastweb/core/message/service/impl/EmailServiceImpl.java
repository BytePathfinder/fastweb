package com.company.fastweb.core.message.service.impl;

import com.company.fastweb.core.message.model.dto.EmailDTO;
import com.company.fastweb.core.message.model.vo.EmailSendResultVO;
import com.company.fastweb.core.message.properties.FastWebMessageProperties;
import com.company.fastweb.core.message.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * 邮件服务实现类
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final FastWebMessageProperties properties;

    // 邮箱格式验证正则
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // 统计信息
    private final AtomicLong totalSent = new AtomicLong(0);
    private final AtomicLong totalSuccess = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);

    @Override
    public EmailSendResultVO sendSimpleEmail(String to, String subject, String content) {
        return sendSimpleEmail(Collections.singletonList(to), subject, content);
    }

    @Override
    public EmailSendResultVO sendSimpleEmail(List<String> toList, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(getFromAddress());
            message.setTo(toList.toArray(new String[0]));
            message.setSubject(subject);
            message.setText(content);
            message.setSentDate(new Date());

            mailSender.send(message);
            
            totalSent.incrementAndGet();
            totalSuccess.incrementAndGet();
            
            return EmailSendResultVO.builder()
                    .success(true)
                    .messageId(UUID.randomUUID().toString())
                    .recipients(toList)
                    .subject(subject)
                    .sendTime(LocalDateTime.now())
                    .message("邮件发送成功")
                    .build();
        } catch (Exception e) {
            log.error("发送简单邮件失败: to={}, subject={}", toList, subject, e);
            totalSent.incrementAndGet();
            totalFailed.incrementAndGet();
            
            return EmailSendResultVO.builder()
                    .success(false)
                    .recipients(toList)
                    .subject(subject)
                    .sendTime(LocalDateTime.now())
                    .message("邮件发送失败: " + e.getMessage())
                    .errorCode("EMAIL_SEND_ERROR")
                    .build();
        }
    }

    @Override
    public EmailSendResultVO sendHtmlEmail(String to, String subject, String content) {
        return sendHtmlEmail(Collections.singletonList(to), subject, content);
    }

    @Override
    public EmailSendResultVO sendHtmlEmail(List<String> toList, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, properties.getEmail().getEncoding());
            
            helper.setFrom(getFromAddress(), properties.getEmail().getFromName());
            helper.setTo(toList.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(content, true); // true表示HTML格式
            helper.setSentDate(new Date());

            mailSender.send(message);
            
            totalSent.incrementAndGet();
            totalSuccess.incrementAndGet();
            
            return EmailSendResultVO.builder()
                    .success(true)
                    .messageId(UUID.randomUUID().toString())
                    .recipients(toList)
                    .subject(subject)
                    .sendTime(LocalDateTime.now())
                    .message("HTML邮件发送成功")
                    .build();
        } catch (Exception e) {
            log.error("发送HTML邮件失败: to={}, subject={}", toList, subject, e);
            totalSent.incrementAndGet();
            totalFailed.incrementAndGet();
            
            return EmailSendResultVO.builder()
                    .success(false)
                    .recipients(toList)
                    .subject(subject)
                    .sendTime(LocalDateTime.now())
                    .message("HTML邮件发送失败: " + e.getMessage())
                    .errorCode("EMAIL_SEND_ERROR")
                    .build();
        }
    }

    @Override
    public EmailSendResultVO sendEmailWithAttachments(String to, String subject, String content, List<File> attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, properties.getEmail().getEncoding());
            
            helper.setFrom(getFromAddress(), properties.getEmail().getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, properties.getEmail().isHtmlEnabled());
            helper.setSentDate(new Date());

            // 添加附件
            if (!CollectionUtils.isEmpty(attachments)) {
                for (File attachment : attachments) {
                    if (attachment.exists() && attachment.isFile()) {
                        helper.addAttachment(attachment.getName(), attachment);
                    }
                }
            }

            mailSender.send(message);
            
            totalSent.incrementAndGet();
            totalSuccess.incrementAndGet();
            
            return EmailSendResultVO.builder()
                    .success(true)
                    .messageId(UUID.randomUUID().toString())
                    .recipients(Collections.singletonList(to))
                    .subject(subject)
                    .sendTime(LocalDateTime.now())
                    .message("带附件邮件发送成功")
                    .attachmentCount(attachments != null ? attachments.size() : 0)
                    .build();
        } catch (Exception e) {
            log.error("发送带附件邮件失败: to={}, subject={}", to, subject, e);
            totalSent.incrementAndGet();
            totalFailed.incrementAndGet();
            
            return EmailSendResultVO.builder()
                    .success(false)
                    .recipients(Collections.singletonList(to))
                    .subject(subject)
                    .sendTime(LocalDateTime.now())
                    .message("带附件邮件发送失败: " + e.getMessage())
                    .errorCode("EMAIL_SEND_ERROR")
                    .build();
        }
    }

    @Override
    public EmailSendResultVO sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        return sendTemplateEmail(Collections.singletonList(to), subject, templateName, variables);
    }

    @Override
    public EmailSendResultVO sendTemplateEmail(List<String> toList, String subject, String templateName, Map<String, Object> variables) {
        // TODO: 实现模板邮件发送逻辑，可以集成Thymeleaf或FreeMarker
        String content = processTemplate(templateName, variables);
        return sendHtmlEmail(toList, subject, content);
    }

    @Override
    public EmailSendResultVO sendEmail(EmailDTO emailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, properties.getEmail().getEncoding());
            
            helper.setFrom(StringUtils.hasText(emailDTO.getFrom()) ? emailDTO.getFrom() : getFromAddress(), 
                          StringUtils.hasText(emailDTO.getFromName()) ? emailDTO.getFromName() : properties.getEmail().getFromName());
            helper.setTo(emailDTO.getTo().toArray(new String[0]));
            
            if (!CollectionUtils.isEmpty(emailDTO.getCc())) {
                helper.setCc(emailDTO.getCc().toArray(new String[0]));
            }
            if (!CollectionUtils.isEmpty(emailDTO.getBcc())) {
                helper.setBcc(emailDTO.getBcc().toArray(new String[0]));
            }
            
            helper.setSubject(emailDTO.getSubject());
            helper.setText(emailDTO.getContent(), emailDTO.isHtml());
            helper.setSentDate(new Date());
            
            if (StringUtils.hasText(emailDTO.getReplyTo())) {
                helper.setReplyTo(emailDTO.getReplyTo());
            }

            // 添加附件
            if (!CollectionUtils.isEmpty(emailDTO.getAttachments())) {
                for (File attachment : emailDTO.getAttachments()) {
                    if (attachment.exists() && attachment.isFile()) {
                        helper.addAttachment(attachment.getName(), attachment);
                    }
                }
            }

            mailSender.send(message);
            
            totalSent.incrementAndGet();
            totalSuccess.incrementAndGet();
            
            return EmailSendResultVO.builder()
                    .success(true)
                    .messageId(UUID.randomUUID().toString())
                    .recipients(emailDTO.getTo())
                    .subject(emailDTO.getSubject())
                    .sendTime(LocalDateTime.now())
                    .message("邮件发送成功")
                    .attachmentCount(emailDTO.getAttachments() != null ? emailDTO.getAttachments().size() : 0)
                    .build();
        } catch (Exception e) {
            log.error("发送邮件失败: to={}, subject={}", emailDTO.getTo(), emailDTO.getSubject(), e);
            totalSent.incrementAndGet();
            totalFailed.incrementAndGet();
            
            return EmailSendResultVO.builder()
                    .success(false)
                    .recipients(emailDTO.getTo())
                    .subject(emailDTO.getSubject())
                    .sendTime(LocalDateTime.now())
                    .message("邮件发送失败: " + e.getMessage())
                    .errorCode("EMAIL_SEND_ERROR")
                    .build();
        }
    }

    @Async
    @Override
    public CompletableFuture<EmailSendResultVO> sendSimpleEmailAsync(String to, String subject, String content) {
        return CompletableFuture.completedFuture(sendSimpleEmail(to, subject, content));
    }

    @Async
    @Override
    public CompletableFuture<EmailSendResultVO> sendHtmlEmailAsync(String to, String subject, String content) {
        return CompletableFuture.completedFuture(sendHtmlEmail(to, subject, content));
    }

    @Async
    @Override
    public CompletableFuture<EmailSendResultVO> sendTemplateEmailAsync(String to, String subject, String templateName, Map<String, Object> variables) {
        return CompletableFuture.completedFuture(sendTemplateEmail(to, subject, templateName, variables));
    }

    @Async
    @Override
    public CompletableFuture<EmailSendResultVO> sendEmailAsync(EmailDTO emailDTO) {
        return CompletableFuture.completedFuture(sendEmail(emailDTO));
    }

    @Override
    public List<EmailSendResultVO> batchSendEmail(List<EmailDTO> emailList) {
        List<EmailSendResultVO> results = new ArrayList<>();
        for (EmailDTO emailDTO : emailList) {
            results.add(sendEmail(emailDTO));
        }
        return results;
    }

    @Async
    @Override
    public CompletableFuture<List<EmailSendResultVO>> batchSendEmailAsync(List<EmailDTO> emailList) {
        return CompletableFuture.completedFuture(batchSendEmail(emailList));
    }

    @Override
    public boolean validateEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public Map<String, Object> getEmailStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalSent", totalSent.get());
        statistics.put("totalSuccess", totalSuccess.get());
        statistics.put("totalFailed", totalFailed.get());
        statistics.put("successRate", totalSent.get() > 0 ? (double) totalSuccess.get() / totalSent.get() : 0.0);
        statistics.put("lastUpdateTime", LocalDateTime.now());
        return statistics;
    }

    /**
     * 获取发件人地址
     */
    private String getFromAddress() {
        return StringUtils.hasText(properties.getEmail().getFrom()) ? 
               properties.getEmail().getFrom() : "noreply@fastweb.com";
    }

    /**
     * 处理邮件模板
     */
    private String processTemplate(String templateName, Map<String, Object> variables) {
        // TODO: 实现模板处理逻辑，可以集成Thymeleaf或FreeMarker
        // 这里先返回简单的字符串替换
        String template = loadTemplate(templateName);
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                template = template.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        }
        return template;
    }

    /**
     * 加载邮件模板
     */
    private String loadTemplate(String templateName) {
        // TODO: 从文件系统或资源文件加载模板
        return "<html><body><h1>邮件模板: " + templateName + "</h1><p>内容: ${content}</p></body></html>";
    }
}