package com.familypoints.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 成员实体类
 * 对应数据库中的member表
 */
@Data
@TableName("member")
public class Member {

    /**
     * 成员ID，UUID类型，自动生成
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 家庭ID，关联family表
     */
    private String familyId;

    /**
     * 用户ID，关联user_account表
     */
    private String userId;

    /**
     * 角色类型，parent或child
     */
    private String role;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 加入时间
     */
    private LocalDateTime joinedAt;

}