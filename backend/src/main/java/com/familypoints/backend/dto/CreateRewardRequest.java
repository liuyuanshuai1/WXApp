package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 创建奖励请求DTO
 * 用于接收创建奖励的请求参数
 */
@Data
public class CreateRewardRequest {
    /**
     * 家庭ID
     */
    private String familyId;
    
    /**
     * 奖励标题
     */
    private String title;
    
    /**
     * 奖励描述
     */
    private String description;
    
    /**
     * 奖励类型：physical|virtual|privilege
     */
    private String type;
    
    /**
     * 所需积分
     */
    private Integer pointsRequired;
    
    /**
     * 库存数量
     */
    private Integer stock;
    
    /**
     * 状态：on_shelf|off_shelf
     */
    private String status;
    
    // Getter and Setter methods
    public String getFamilyId() {
        return familyId;
    }
    
    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
    
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getPointsRequired() {
        return pointsRequired;
    }
    
    public void setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}