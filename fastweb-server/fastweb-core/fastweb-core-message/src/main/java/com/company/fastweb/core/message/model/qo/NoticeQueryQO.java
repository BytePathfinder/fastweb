package com.company.fastweb.core.message.model.qo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 站内信查询参数对象
 *
 * @author FastWeb
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeQueryQO {

    /**
     * 当前页码
     */
    private Long current = 1L;

    /**
     * 每页大小
     */
    private Long size = 10L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 发送者ID
     */
    private Long fromUserId;

    /**
     * 消息类型
     */
    private String noticeType;

    /**
     * 消息类型列表
     */
    private List<String> noticeTypes;

    /**
     * 消息级别
     */
    private String level;

    /**
     * 消息级别列表
     */
    private List<String> levels;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 是否已确认
     */
    private Boolean isConfirmed;

    /**
     * 是否已过期
     */
    private Boolean isExpired;

    /**
     * 标题关键词
     */
    private String titleKeyword;

    /**
     * 内容关键词
     */
    private String contentKeyword;

    /**
     * 业务标识
     */
    private String bizId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 发送开始时间
     */
    private LocalDateTime sendTimeStart;

    /**
     * 发送结束时间
     */
    private LocalDateTime sendTimeEnd;

    /**
     * 创建开始时间
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建结束时间
     */
    private LocalDateTime createTimeEnd;

    /**
     * 排序字段
     */
    private String orderBy = "create_time";

    /**
     * 排序方向（ASC、DESC）
     */
    private String orderDirection = "DESC";

    /**
     * 是否只查询未读消息
     */
    private Boolean unreadOnly;

    /**
     * 是否只查询重要消息
     */
    private Boolean importantOnly;

    /**
     * 是否只查询系统消息
     */
    private Boolean systemOnly;

    /**
     * 是否只查询个人消息
     */
    private Boolean personalOnly;
}