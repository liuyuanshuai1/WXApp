package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 成员积分表数据访问层接口
 * 提供成员积分相关的数据库操作方法
 */
@Mapper
public interface MemberPointsMapper extends BaseMapper<Member> {
    
    /**
     * 根据用户ID和家庭ID查询成员积分
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 成员积分对象
     */
    Member selectByUserIdAndFamilyId(@Param("userId") Long userId, @Param("familyId") Long familyId);
    
    /**
     * 根据家庭ID查询所有成员积分列表
     * @param familyId 家庭ID
     * @return 成员积分列表
     */
    java.util.List<Member> selectByFamilyId(@Param("familyId") Long familyId);
    
    /**
     * 更新成员积分
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param pointsChange 积分变化值（正数为增加，负数为减少）
     * @return 更新影响行数
     */
    int updatePoints(@Param("userId") Long userId, @Param("familyId") Long familyId, @Param("pointsChange") Integer pointsChange);
    
    /**
     * 检查成员积分是否足够
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param requiredPoints 需要的积分数量
     * @return 1表示足够，0表示不足
     */
    Integer checkPointsBalance(@Param("userId") Long userId, @Param("familyId") Long familyId, @Param("requiredPoints") Integer requiredPoints);
}