package com.familypoints.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 奖励兑换记录表实体类
 * 存储用户兑换奖励的记录
 */
@TableName("reward_exchange")
public class RewardExchange {
    
    /**
     * 兑换记录ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 奖励ID
     */
    private Long rewardId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 兑换积分
     */
    private Integer points;
    
    /**
     * 兑换状态：0-待审核，1-已通过，2-已拒绝
     */
    private Integer status;
    
    /**
     * 审核者ID
     */
    private Long reviewerId;
    
    /**
     * 审核意见
     */
    private String reviewComment;
    
    /**
     * 兑换时间
     */
    private LocalDateTime exchangeTime;
    
    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;
    
    // getter and setter methods
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRewardId() {
        return rewardId;
    }
    
    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Long getReviewerId() {
        return reviewerId;
    }
    
    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }
    
    public String getReviewComment() {
        return reviewComment;
    }
    
    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
    
    public LocalDateTime getExchangeTime() {
        return exchangeTime;
    }
    
    public void setExchangeTime(LocalDateTime exchangeTime) {
        this.exchangeTime = exchangeTime;
    }
    
    public LocalDateTime getReviewTime() {
        return reviewTime;
    }
    
    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }
}