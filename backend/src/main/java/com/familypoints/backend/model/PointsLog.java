package com.familypoints.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 积分流水实体类
 * 对应数据库中的points_log表
 */
@Data
@TableName("points_log")
public class PointsLog {

    /**
     * 流水ID，UUID类型，自动生成
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户ID，关联user_account表
     */
    private String userId;

    /**
     * 家庭ID，关联family表
     */
    private String familyId;

    /**
     * 积分类型，income或expense
     */
    private String type;

    /**
     * 来源，task|reward|manual
     */
    private String source;

    /**
     * 积分数量
     */
    private Integer amount;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

}