package com.company.fastweb.core.message.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 站内信视图对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVO {

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
     * 发送者头像
     */
    private String fromUserAvatar;

    /**
     * 接收者ID
     */
    private Long toUserId;

    /**
     * 接收者名称
     */
    private String toUserName;

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
     * 消息类型名称
     */
    private String noticeTypeName;

    /**
     * 消息级别（普通、重要、紧急）
     */
    private String level;

    /**
     * 消息级别名称
     */
    private String levelName;

    /**
     * 是否已读
     */
    private boolean isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 是否需要确认
     */
    private boolean needConfirm;

    /**
     * 是否已确认
     */
    private boolean isConfirmed;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 是否已过期
     */
    private boolean isExpired;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 扩展属性
     */
    private Map<String, Object> extras;

    /**
     * 备注信息
     */
    private String remark;
}