package com.familypoints.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 奖励实体类
 * 对应数据库中的reward表
 */
@Data
@TableName("reward")
public class Reward {

    /**
     * 奖励ID，UUID类型，自动生成
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 家庭ID，关联family表
     */
    private String familyId;

    /**
     * 奖励标题
     */
    private String title;

    /**
     * 奖励描述
     */
    private String description;

    /**
     * 奖励类型，physical|virtual|privilege
     */
    private String type;

    /**
     * 所需积分
     */
    private Integer pointsRequired;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 状态，on_shelf|off_shelf
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

}