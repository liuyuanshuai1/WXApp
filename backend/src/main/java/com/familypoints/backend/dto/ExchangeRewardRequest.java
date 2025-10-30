package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 兑换奖励请求DTO
 * 用于接收兑换奖励的请求参数
 */
@Data   
public class ExchangeRewardRequest {
    /**
     * 奖励ID
     */
    private String rewardId;
    
    // Getter and Setter methods
    public String getRewardId() {
        return rewardId;
    }
    
    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }
}