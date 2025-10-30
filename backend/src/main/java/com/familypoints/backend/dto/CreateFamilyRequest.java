package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 创建家庭请求DTO
 * 用于接收创建家庭的请求参数
 */
@Data
public class CreateFamilyRequest {
    /**
     * 家庭名称
     */
    private String familyName;
    
    // Getter and Setter methods
    public String getFamilyName() {
        return familyName;
    }
    
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}