package com.familypoints.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("notification")
public class Notification {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    private String userId;
    private String familyId;
    private String type;
    private String title;
    private String content;
    private String status;
    private String relatedId;
    private LocalDateTime createdAt;
    
    // 构造函数
    public Notification() {}
    
    public Notification(String userId, String familyId, String type, String title, String content, String relatedId) {
        this.userId = userId;
        this.familyId = familyId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.relatedId = relatedId;
        this.status = "unread";
        this.createdAt = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getFamilyId() {
        return familyId;
    }
    
    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRelatedId() {
        return relatedId;
    }
    
    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}