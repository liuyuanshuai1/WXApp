package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 创建家庭响应DTO
 * 用于返回创建家庭操作的结果信息
 */
@Data
public class CreateFamilyResponse {
    /**
     * 家庭ID
     */
    private String familyId;
    
    /**
     * 家庭码
     */
    private String familyCode;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    // Getter and Setter methods
    public String getFamilyId() {
        return familyId;
    }
    
    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
    
    public String getFamilyCode() {
        return familyCode;
    }
    
    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}