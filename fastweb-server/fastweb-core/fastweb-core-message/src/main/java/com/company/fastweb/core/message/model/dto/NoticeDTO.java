package com.company.fastweb.core.message.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 站内信传输对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {

    /**
     * 站内信ID
     */
    private Long id;

    /**
     * 发送者ID
     */
    private Long fromUserId;

    /**
     * 发送者名称
     */
    private String fromUserName;

    /**
     * 接收者ID列表
     */
    private List<Long> toUserIds;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型（系统通知、个人消息、公告等）
     */
    private String noticeType;

    /**
     * 消息级别（普通、重要、紧急）
     */
    private String level;

    /**
     * 是否需要确认
     */
    private boolean needConfirm;

    /**
     * 是否实时推送
     */
    private boolean realtimePush;

    /**
     * 发送时间（定时发送）
     */
    private LocalDateTime sendTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 业务标识
     */
    private String bizId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 关联URL
     */
    private String linkUrl;

    /**
     * 附件信息
     */
    private List<Map<String, Object>> attachments;

    /**
     * 扩展属性
     */
    private Map<String, Object> extras;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}