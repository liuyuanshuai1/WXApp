package com.familypoints.backend.service;

import com.familypoints.backend.model.entity.Notification;

import java.util.List;

public interface NotificationService {
    
    /**
     * 发送通知给指定用户
     * @param notification 通知对象
     */
    void sendNotificationToUser(Notification notification);
    
    /**
     * 批量发送通知给家庭成员
     * @param notification 通知对象
     * @param familyId 家庭ID
     */
    void sendNotificationToFamily(Notification notification, String familyId);
    
    /**
     * 获取用户的所有通知
     * @param userId 用户ID
     * @return 通知列表
     */
    List<Notification> getUserNotifications(String userId);
    
    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     */
    void markAsRead(String notificationId);
    
    /**
     * 删除通知
     * @param notificationId 通知ID
     */
    void deleteNotification(String notificationId);
    
    /**
     * 创建任务提交通知
     * @param taskId 任务ID
     * @param submitterId 提交者ID
     * @param reviewerId 审核者ID
     */
    void createTaskSubmittedNotification(String taskId, String submitterId, String reviewerId);
    
    /**
     * 创建任务审核通知
     * @param taskId 任务ID
     * @param submitterId 提交者ID
     * @param approved 是否通过
     */
    void createTaskReviewedNotification(String taskId, String submitterId, boolean approved);
    
    /**
     * 创建奖励兑换通知
     * @param rewardId 奖励ID
     * @param exchangerId 兑换者ID
     * @param reviewerId 审核者ID
     */
    void createRewardExchangedNotification(String rewardId, String exchangerId, String reviewerId);
    
    /**
     * 创建奖励审核通知
     * @param rewardId 奖励ID
     * @param exchangerId 兑换者ID
     * @param approved 是否通过
     */
    void createRewardReviewedNotification(String rewardId, String exchangerId, boolean approved);
}