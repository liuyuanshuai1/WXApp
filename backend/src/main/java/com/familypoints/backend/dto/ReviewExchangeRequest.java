package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 审核兑换请求DTO
 * 用于接收审核奖励兑换的请求参数
 */
@Data
public class ReviewExchangeRequest {
    /**
     * 兑换记录ID
     */
    private String exchangeId;
    
    /**
     * 是否通过审核
     */
    private Boolean approved;
    
    /**
     * 审核备注
     */
    private String remark;
    
    // Getter and Setter methods
    public String getExchangeId() {
        return exchangeId;
    }
    
    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }
    
    public Boolean getApproved() {
        return approved;
    }
    
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}