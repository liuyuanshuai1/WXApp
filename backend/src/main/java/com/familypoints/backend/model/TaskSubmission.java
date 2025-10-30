package com.familypoints.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务提交实体类
 * 对应数据库中的task_submission表
 */
@Data
@TableName("task_submission")
public class TaskSubmission {

    /**
     * 提交ID，UUID类型，自动生成
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 任务ID，关联task表
     */
    private String taskId;

    /**
     * 用户ID，关联user_account表
     */
    private String userId;

    /**
     * 提交证明文字
     */
    private String proofText;

    /**
     * 提交证明图片URLs，JSONB格式
     */
    private String proofImageUrls;

    /**
     * 提交状态，pending|approved|rejected
     */
    private String status;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 提交时间
     */
    private LocalDateTime submittedAt;

    /**
     * 审核时间
     */
    private LocalDateTime reviewedAt;

    /**
     * 审核者ID，关联user_account表
     */
    private String reviewerId;

}