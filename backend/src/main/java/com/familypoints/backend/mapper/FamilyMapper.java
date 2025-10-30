package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.Family;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 家庭Mapper接口
 * 用于对family表进行数据库操作
 */
@Mapper
public interface FamilyMapper extends BaseMapper<Family> {

    /**
     * 根据家庭码查询家庭
     * @param code 家庭码
     * @return 家庭信息
     */
    Family selectByCode(String code);

    /**
     * 根据拥有者ID查询家庭
     * @param ownerId 拥有者ID
     * @return 家庭信息
     */
    Family selectByOwnerId(String ownerId);

    /**
     * 查询用户所在的家庭
     * @param userId 用户ID
     * @return 家庭信息
     */
    Family selectFamilyByUserId(@Param("userId") String userId);

}