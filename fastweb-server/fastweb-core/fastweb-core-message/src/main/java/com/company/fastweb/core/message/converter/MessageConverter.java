package com.company.fastweb.core.message.converter;

import com.company.fastweb.core.message.model.dto.EmailDTO;
import com.company.fastweb.core.message.model.dto.NoticeDTO;
import com.company.fastweb.core.message.model.dto.SmsDTO;
import com.company.fastweb.core.message.model.form.EmailSendForm;
import com.company.fastweb.core.message.model.form.NoticeSendForm;
import com.company.fastweb.core.message.model.form.SmsSendForm;
import com.company.fastweb.core.message.model.po.NoticePO;
import com.company.fastweb.core.message.model.vo.EmailSendResultVO;
import com.company.fastweb.core.message.model.vo.NoticeVO;
import com.company.fastweb.core.message.model.vo.SmsSendResultVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 消息模块转换器
 *
 * @author FastWeb
 */
@Mapper(componentModel = "spring")
public interface MessageConverter {

    MessageConverter INSTANCE = Mappers.getMapper(MessageConverter.class);

    // ==================== 邮件相关转换 ====================

    /**
     * 邮件发送表单转DTO
     */
    @Mapping(target = "sendTime", expression = "java(form.getSendTime() != null ? form.getSendTime() : java.time.LocalDateTime.now())")
    @Mapping(target = "retryCount", constant = "0")
    @Mapping(target = "isAsync", source = "async")
    EmailDTO toDTO(EmailSendForm form);

    /**
     * 邮件发送结果转VO
     */
    @Mapping(target = "sendTimeStr", expression = "java(formatDateTime(dto.getSendTime()))")
    @Mapping(target = "costTimeStr", expression = "java(formatCostTime(dto.getCostTime()))")
    EmailSendResultVO toEmailResultVO(EmailDTO dto, boolean success, String messageId, String responseMessage, String errorCode, String errorDetail);

    // ==================== 短信相关转换 ====================

    /**
     * 短信发送表单转DTO
     */
    @Mapping(target = "sendTime", expression = "java(form.getSendTime() != null ? form.getSendTime() : java.time.LocalDateTime.now())")
    @Mapping(target = "retryCount", constant = "0")
    @Mapping(target = "isAsync", source = "async")
    SmsDTO toDTO(SmsSendForm form);

    /**
     * 短信发送结果转VO
     */
    @Mapping(target = "sendTimeStr", expression = "java(formatDateTime(dto.getSendTime()))")
    @Mapping(target = "costTimeStr", expression = "java(formatCostTime(dto.getCostTime()))")
    SmsSendResultVO toSmsResultVO(SmsDTO dto, boolean success, String bizId, String responseMessage, String errorCode, String errorDetail);

    // ==================== 站内信相关转换 ====================

    /**
     * 站内信发送表单转DTO
     */
    @Mapping(target = "sendTime", expression = "java(form.getSendTime() != null ? form.getSendTime() : java.time.LocalDateTime.now())")
    @Mapping(target = "noticeId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    NoticeDTO toDTO(NoticeSendForm form);

    /**
     * 站内信DTO转PO
     */
    @Mapping(target = "id", source = "noticeId")
    @Mapping(target = "receiverIds", expression = "java(joinList(dto.getReceiverIds()))")
    @Mapping(target = "attachments", expression = "java(mapToJson(dto.getAttachments()))")
    @Mapping(target = "extendedProperties", expression = "java(mapToJson(dto.getExtendedProperties()))")
    @Mapping(target = "createTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateTime", expression = "java(java.time.LocalDateTime.now())")
    NoticePO toPO(NoticeDTO dto);

    /**
     * 站内信PO转VO
     */
    @Mapping(target = "noticeId", source = "id")
    @Mapping(target = "receiverIds", expression = "java(splitString(po.getReceiverIds()))")
    @Mapping(target = "attachments", expression = "java(jsonToMap(po.getAttachments()))")
    @Mapping(target = "extendedProperties", expression = "java(jsonToMap(po.getExtendedProperties()))")
    @Mapping(target = "sendTimeStr", expression = "java(formatDateTime(po.getSendTime()))")
    @Mapping(target = "expireTimeStr", expression = "java(formatDateTime(po.getExpireTime()))")
    @Mapping(target = "readTimeStr", expression = "java(formatDateTime(po.getReadTime()))")
    @Mapping(target = "confirmTimeStr", expression = "java(formatDateTime(po.getConfirmTime()))")
    @Mapping(target = "createTimeStr", expression = "java(formatDateTime(po.getCreateTime()))")
    @Mapping(target = "updateTimeStr", expression = "java(formatDateTime(po.getUpdateTime()))")
    NoticeVO toVO(NoticePO po);

    /**
     * 站内信PO列表转VO列表
     */
    List<NoticeVO> toVOList(List<NoticePO> poList);

    // ==================== 辅助方法 ====================

    /**
     * 格式化日期时间
     */
    default String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 格式化耗时
     */
    default String formatCostTime(Long costTime) {
        if (costTime == null) {
            return null;
        }
        if (costTime < 1000) {
            return costTime + "ms";
        } else if (costTime < 60000) {
            return String.format("%.2fs", costTime / 1000.0);
        } else {
            long minutes = costTime / 60000;
            long seconds = (costTime % 60000) / 1000;
            return String.format("%dm%ds", minutes, seconds);
        }
    }

    /**
     * 格式化文件大小
     */
    default String formatFileSize(Long size) {
        if (size == null || size <= 0) {
            return "0B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double fileSize = size.doubleValue();
        
        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }
        
        if (unitIndex == 0) {
            return String.format("%.0f%s", fileSize, units[unitIndex]);
        } else {
            return String.format("%.2f%s", fileSize, units[unitIndex]);
        }
    }

    /**
     * 格式化邮件优先级
     */
    default String formatEmailPriority(Integer priority) {
        if (priority == null) {
            return "普通";
        }
        switch (priority) {
            case 1:
                return "最高";
            case 2:
                return "高";
            case 3:
                return "普通";
            case 4:
                return "低";
            case 5:
                return "最低";
            default:
                return "普通";
        }
    }

    /**
     * 格式化短信类型
     */
    default String formatSmsType(String smsType) {
        if (smsType == null) {
            return "未知";
        }
        switch (smsType.toUpperCase()) {
            case "VERIFY_CODE":
                return "验证码";
            case "LOGIN_NOTICE":
                return "登录通知";
            case "PASSWORD_RESET":
                return "密码重置";
            case "SYSTEM_NOTICE":
                return "系统通知";
            case "MARKETING":
                return "营销短信";
            case "TEMPLATE":
                return "模板短信";
            default:
                return "其他";
        }
    }

    /**
     * 格式化站内信类型
     */
    default String formatNoticeType(String noticeType) {
        if (noticeType == null) {
            return "未知";
        }
        switch (noticeType.toUpperCase()) {
            case "SYSTEM":
                return "系统通知";
            case "PERSONAL":
                return "个人消息";
            case "ANNOUNCEMENT":
                return "公告";
            case "REMINDER":
                return "提醒";
            case "WARNING":
                return "警告";
            default:
                return "其他";
        }
    }

    /**
     * 格式化站内信级别
     */
    default String formatNoticeLevel(String level) {
        if (level == null) {
            return "普通";
        }
        switch (level.toUpperCase()) {
            case "LOW":
                return "低";
            case "NORMAL":
                return "普通";
            case "HIGH":
                return "高";
            case "URGENT":
                return "紧急";
            case "CRITICAL":
                return "严重";
            default:
                return "普通";
        }
    }

    /**
     * 格式化发送状态
     */
    default String formatSendStatus(String status) {
        if (status == null) {
            return "未知";
        }
        switch (status.toUpperCase()) {
            case "PENDING":
                return "待发送";
            case "SENDING":
                return "发送中";
            case "SUCCESS":
                return "发送成功";
            case "FAILED":
                return "发送失败";
            case "CANCELLED":
                return "已取消";
            default:
                return "未知";
        }
    }

    /**
     * 脱敏手机号
     */
    default String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return phoneNumber;
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(phoneNumber.length() - 4);
    }

    /**
     * 脱敏邮箱地址
     */
    default String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return "*" + username.substring(username.length() - 1) + "@" + domain;
        } else {
            return username.substring(0, 2) + "****" + username.substring(username.length() - 1) + "@" + domain;
        }
    }

    /**
     * 列表转字符串（逗号分隔）
     */
    default String joinList(List<?> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream()
                .map(Object::toString)
                .reduce((a, b) -> a + "," + b)
                .orElse(null);
    }

    /**
     * 字符串转列表（逗号分隔）
     */
    default List<String> splitString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return List.of();
        }
        return List.of(str.split(","));
    }

    /**
     * Map转JSON字符串
     */
    default String mapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        // 这里应该使用JSON库进行转换，简化处理
        return map.toString();
    }

    /**
     * JSON字符串转Map
     */
    default Map<String, Object> jsonToMap(String json) {
        if (json == null || json.trim().isEmpty()) {
            return Map.of();
        }
        // 这里应该使用JSON库进行转换，简化处理
        return Map.of();
    }

    /**
     * 获取邮件服务提供商
     */
    default String getEmailProvider(String email) {
        if (email == null || !email.contains("@")) {
            return "未知";
        }
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        switch (domain) {
            case "qq.com":
                return "QQ邮箱";
            case "163.com":
                return "网易邮箱";
            case "126.com":
                return "网易邮箱";
            case "gmail.com":
                return "Gmail";
            case "outlook.com":
            case "hotmail.com":
                return "Outlook";
            case "sina.com":
            case "sina.cn":
                return "新浪邮箱";
            case "sohu.com":
                return "搜狐邮箱";
            case "aliyun.com":
                return "阿里云邮箱";
            default:
                return "其他";
        }
    }

    /**
     * 获取短信服务提供商名称
     */
    default String getSmsProviderName(String provider) {
        if (provider == null) {
            return "未知";
        }
        switch (provider.toUpperCase()) {
            case "ALIYUN":
                return "阿里云";
            case "TENCENT":
                return "腾讯云";
            case "HUAWEI":
                return "华为云";
            case "BAIDU":
                return "百度云";
            case "JDCLOUD":
                return "京东云";
            case "UCLOUD":
                return "UCloud";
            case "QINIU":
                return "七牛云";
            default:
                return provider;
        }
    }

    /**
     * 验证邮箱格式
     */
    default boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * 验证手机号格式
     */
    default boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        // 简单的中国手机号验证
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phoneNumber.matches(phoneRegex);
    }
}