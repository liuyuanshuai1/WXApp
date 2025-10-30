package com.familypoints.backend.service.impl;

import com.familypoints.backend.model.entity.Notification;
import com.familypoints.backend.service.NotificationService;
import com.familypoints.backend.websocket.NotificationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    // 模拟数据库存储
    private static final Map<String, Notification> notificationStorage = new HashMap<>();
    
    @Autowired
    private NotificationWebSocketHandler webSocketHandler;
    
    @Override
    public void sendNotificationToUser(Notification notification) {
        // 保存到存储中
        notificationStorage.put(notification.getId(), notification);
        
        // 通过WebSocket发送通知
        webSocketHandler.sendNotificationToUser(Long.valueOf(notification.getUserId()), notification);
    }
    
    @Override
    public void sendNotificationToFamily(Notification notification, String familyId) {
        // 在实际实现中，这里需要查询家庭成员列表
        // 然后为每个成员创建通知并发送
        // 这里简化处理，只是示例
        notificationStorage.put(notification.getId(), notification);
        webSocketHandler.broadcastMessage(notification);
    }
    
    @Override
    public List<Notification> getUserNotifications(String userId) {
        List<Notification> userNotifications = new ArrayList<>();
        for (Notification notification : notificationStorage.values()) {
            if (notification.getUserId().equals(userId)) {
                userNotifications.add(notification);
            }
        }
        return userNotifications;
    }
    
    @Override
    public void markAsRead(String notificationId) {
        Notification notification = notificationStorage.get(notificationId);
        if (notification != null) {
            notification.setStatus("read");
            notificationStorage.put(notificationId, notification);
        }
    }
    
    @Override
    public void deleteNotification(String notificationId) {
        notificationStorage.remove(notificationId);
    }
    
    @Override
    public void createTaskSubmittedNotification(String taskId, String submitterId, String reviewerId) {
        Notification notification = new Notification(
            reviewerId,
            getFamilyIdByUserId(reviewerId), // 需要根据用户ID获取家庭ID
            "task_submitted",
            "新任务提交",
            "您有一个新的任务提交需要审核",
            taskId
        );
        notification.setId(UUID.randomUUID().toString());
        sendNotificationToUser(notification);
    }
    
    @Override
    public void createTaskReviewedNotification(String taskId, String submitterId, boolean approved) {
        String status = approved ? "已通过" : "未通过";
        Notification notification = new Notification(
            submitterId,
            getFamilyIdByUserId(submitterId), // 需要根据用户ID获取家庭ID
            "task_reviewed",
            "任务审核结果",
            "您的任务提交审核" + status,
            taskId
        );
        notification.setId(UUID.randomUUID().toString());
        sendNotificationToUser(notification);
    }
    
    @Override
    public void createRewardExchangedNotification(String rewardId, String exchangerId, String reviewerId) {
        Notification notification = new Notification(
            reviewerId,
            getFamilyIdByUserId(reviewerId), // 需要根据用户ID获取家庭ID
            "reward_exchanged",
            "奖励兑换申请",
            "您有一个奖励兑换申请需要审核",
            rewardId
        );
        notification.setId(UUID.randomUUID().toString());
        sendNotificationToUser(notification);
    }
    
    @Override
    public void createRewardReviewedNotification(String rewardId, String exchangerId, boolean approved) {
        String status = approved ? "已通过" : "未通过";
        Notification notification = new Notification(
            exchangerId,
            getFamilyIdByUserId(exchangerId), // 需要根据用户ID获取家庭ID
            "reward_reviewed",
            "奖励审核结果",
            "您的奖励兑换申请审核" + status,
            rewardId
        );
        notification.setId(UUID.randomUUID().toString());
        sendNotificationToUser(notification);
    }
    
    // 辅助方法：根据用户ID获取家庭ID（实际项目中需要查询数据库）
    private String getFamilyIdByUserId(String userId) {
        // 这里简化处理，返回固定值
        // 实际项目中需要查询数据库获取用户所属家庭
        return "family-1";
    }
}