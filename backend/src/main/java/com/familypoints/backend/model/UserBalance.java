package com.familypoints.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户积分余额实体类
 * 对应数据库中的user_balance表
 */
@Data
@TableName("user_balance")
public class UserBalance {

    /**
     * 用户ID，关联user_account表，作为主键
     */
    @TableId(type = IdType.INPUT)
    private String userId;

    /**
     * 积分余额
     */
    private Integer balance;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

}