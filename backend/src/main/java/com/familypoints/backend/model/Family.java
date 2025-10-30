package com.familypoints.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 家庭实体类
 * 对应数据库中的family表
 */
@Data
@TableName("family")
public class Family {

    /**
     * 家庭ID，UUID类型，自动生成
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 家庭名称
     */
    private String name;

    /**
     * 家庭码，唯一标识
     */
    private String code;

    /**
     * 家庭拥有者ID，关联user_account表
     */
    private String ownerId;

    /**
     * 最大成员数，默认6人
     */
    private Integer maxMembers;

    /**
     * 家庭码过期时间
     */
    private LocalDateTime codeExpireAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

}