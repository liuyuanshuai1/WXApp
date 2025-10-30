package com.familypoints.backend.dto;

import lombok.Data;
import java.util.List;

/**
 * 提交任务请求DTO
 * 用于接收提交任务的请求参数
 */
@Data
public class SubmitTaskRequest {
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 提交内容
     */
    private String content;
    
    /**
     * 图片URL列表
     */
    private List<String> images;
    
    // Getter and Setter methods
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
}