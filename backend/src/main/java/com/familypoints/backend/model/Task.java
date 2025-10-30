package com.familypoints.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务实体类
 * 对应数据库中的task表
 */
@Data
@TableName("task")
public class Task {

    /**
     * 任务ID，UUID类型，自动生成
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 家庭ID，关联family表
     */
    private String familyId;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 奖励积分
     */
    private Integer points;

    /**
     * 任务类型，normal或daily
     */
    private String type;

    /**
     * 是否可重复执行
     */
    private Boolean repeatable;

    /**
     * 截止时间
     */
    private LocalDateTime deadline;

    /**
     * 任务状态，active或inactive
     */
    private String status;

    /**
     * 创建者ID，关联user_account表
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

}