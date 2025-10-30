package com.familypoints.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 积分历史记录表实体类
 * 存储用户积分变动的详细记录
 */
@TableName("points_history")
public class PointsHistory {
    
    /**
     * 记录ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 家庭ID
     */
    private Long familyId;
    
    /**
     * 积分变动类型：0-任务奖励，1-奖励兑换，2-积分调整
     */
    private Integer type;
    
    /**
     * 相关业务ID（如任务ID或奖励ID）
     */
    private Long relatedId;
    
    /**
     * 积分变动数量（正数为增加，负数为减少）
     */
    private Integer pointsChange;
    
    /**
     * 变动前积分
     */
    private Integer previousPoints;
    
    /**
     * 变动后积分
     */
    private Integer currentPoints;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    // getter and setter methods
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getFamilyId() {
        return familyId;
    }
    
    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public Long getRelatedId() {
        return relatedId;
    }
    
    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }
    
    public Integer getPointsChange() {
        return pointsChange;
    }
    
    public void setPointsChange(Integer pointsChange) {
        this.pointsChange = pointsChange;
    }
    
    public Integer getPreviousPoints() {
        return previousPoints;
    }
    
    public void setPreviousPoints(Integer previousPoints) {
        this.previousPoints = previousPoints;
    }
    
    public Integer getCurrentPoints() {
        return currentPoints;
    }
    
    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}