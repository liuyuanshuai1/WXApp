package com.familypoints.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 兑换申请实体类
 * 对应数据库中的exchange表
 */
@Data
@TableName("exchange")
public class Exchange {

    /**
     * 兑换ID，UUID类型，自动生成
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 奖励ID，关联reward表
     */
    private String rewardId;

    /**
     * 用户ID，关联user_account表
     */
    private String userId;

    /**
     * 家庭ID，关联family表
     */
    private String familyId;

    /**
     * 兑换状态，pending|approved|rejected
     */
    private String status;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 审核时间
     */
    private LocalDateTime reviewedAt;

    /**
     * 审核者ID，关联user_account表
     */
    private String reviewerId;

}