package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.UserAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户账号Mapper接口
 * 用于对user_account表进行数据库操作
 */
@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    /**
     * 根据openId查询用户
     * @param openId 微信openId
     * @return 用户账号信息
     */
    UserAccount selectByOpenId(String openId);

}