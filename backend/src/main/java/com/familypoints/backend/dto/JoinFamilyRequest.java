package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 加入家庭请求DTO
 * 用于接收加入家庭的请求参数
 */
@Data
public class JoinFamilyRequest {
    /**
     * 家庭邀请码
     */
    private String familyCode;
    
    /**
     * 角色，可选，默认child
     */
    private String role;
    
    // Getter and Setter methods
    public String getFamilyCode() {
        return familyCode;
    }
    
    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}