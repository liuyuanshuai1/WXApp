package com.familypoints.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 创建任务请求DTO
 * 用于接收创建任务的请求参数
 */
@Data
public class CreateTaskRequest {
    /**
     * 任务标题
     */
    private String title;
    
    /**
     * 任务描述
     */
    private String description;
    
    /**
     * 任务积分
     */
    private Integer points;
    
    /**
     * 截止时间
     */
    private LocalDateTime deadline;
    
    // Getter and Setter methods
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public LocalDateTime getDeadline() {
        return deadline;
    }
    
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}