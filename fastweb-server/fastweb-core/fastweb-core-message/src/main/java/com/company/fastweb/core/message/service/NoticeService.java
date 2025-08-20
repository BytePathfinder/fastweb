package com.company.fastweb.core.message.service;

import com.company.fastweb.core.message.model.dto.NoticeDTO;
import com.company.fastweb.core.message.model.qo.NoticeQueryQO;
import com.company.fastweb.core.message.model.vo.NoticeVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 站内信服务接口
 *
 * @author FastWeb
 */
public interface NoticeService {

    /**
     * 发送站内信
     *
     * @param noticeDTO 站内信DTO
     * @return 站内信ID
     */
    Long sendNotice(NoticeDTO noticeDTO);

    /**
     * 发送系统通知
     *
     * @param title   标题
     * @param content 内容
     * @param userIds 用户ID列表（为空则发送给所有用户）
     * @return 发送结果
     */
    List<Long> sendSystemNotice(String title, String content, List<Long> userIds);

    /**
     * 发送个人消息
     *
     * @param fromUserId 发送者ID
     * @param toUserId   接收者ID
     * @param title      标题
     * @param content    内容
     * @return 站内信ID
     */
    Long sendPersonalMessage(Long fromUserId, Long toUserId, String title, String content);

    /**
     * 批量发送站内信
     *
     * @param noticeList 站内信列表
     * @return 发送结果
     */
    List<Long> batchSendNotice(List<NoticeDTO> noticeList);

    /**
     * 异步发送站内信
     *
     * @param noticeDTO 站内信DTO
     * @return 异步发送结果
     */
    CompletableFuture<Long> sendNoticeAsync(NoticeDTO noticeDTO);

    /**
     * 异步批量发送站内信
     *
     * @param noticeList 站内信列表
     * @return 异步发送结果
     */
    CompletableFuture<List<Long>> batchSendNoticeAsync(List<NoticeDTO> noticeList);

    /**
     * 获取用户站内信列表（分页）
     *
     * @param userId 用户ID
     * @param qo     查询条件
     * @return 站内信分页列表
     */
    Page<NoticeVO> getUserNoticeList(Long userId, NoticeQueryQO qo);

    /**
     * 获取用户未读站内信列表
     *
     * @param userId 用户ID
     * @param limit  限制数量
     * @return 未读站内信列表
     */
    List<NoticeVO> getUserUnreadNoticeList(Long userId, Integer limit);

    /**
     * 获取站内信详情
     *
     * @param noticeId 站内信ID
     * @param userId   用户ID
     * @return 站内信详情
     */
    NoticeVO getNoticeDetail(Long noticeId, Long userId);

    /**
     * 标记站内信为已读
     *
     * @param noticeId 站内信ID
     * @param userId   用户ID
     * @return 是否成功
     */
    boolean markAsRead(Long noticeId, Long userId);

    /**
     * 批量标记站内信为已读
     *
     * @param noticeIds 站内信ID列表
     * @param userId    用户ID
     * @return 成功标记的数量
     */
    int batchMarkAsRead(List<Long> noticeIds, Long userId);

    /**
     * 标记所有站内信为已读
     *
     * @param userId 用户ID
     * @return 成功标记的数量
     */
    int markAllAsRead(Long userId);

    /**
     * 删除站内信
     *
     * @param noticeId 站内信ID
     * @param userId   用户ID
     * @return 是否成功
     */
    boolean deleteNotice(Long noticeId, Long userId);

    /**
     * 批量删除站内信
     *
     * @param noticeIds 站内信ID列表
     * @param userId    用户ID
     * @return 成功删除的数量
     */
    int batchDeleteNotice(List<Long> noticeIds, Long userId);

    /**
     * 清空用户所有站内信
     *
     * @param userId 用户ID
     * @return 成功删除的数量
     */
    int clearAllNotice(Long userId);

    /**
     * 获取用户未读站内信数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    long getUnreadCount(Long userId);

    /**
     * 获取用户站内信统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     */
    Map<String, Object> getUserNoticeStatistics(Long userId);

    /**
     * 获取系统站内信统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getSystemNoticeStatistics();

    /**
     * 清理过期站内信
     *
     * @return 清理数量
     */
    int cleanExpiredNotice();

    /**
     * 推送实时消息给用户
     *
     * @param userId  用户ID
     * @param message 消息内容
     * @return 是否成功
     */
    boolean pushRealtimeMessage(Long userId, Object message);

    /**
     * 推送实时消息给多个用户
     *
     * @param userIds 用户ID列表
     * @param message 消息内容
     * @return 成功推送的用户数量
     */
    int pushRealtimeMessageToUsers(List<Long> userIds, Object message);

    /**
     * 广播实时消息给所有在线用户
     *
     * @param message 消息内容
     * @return 成功推送的用户数量
     */
    int broadcastRealtimeMessage(Object message);
}