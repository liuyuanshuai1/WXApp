package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.entity.Reward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 奖励表数据访问层接口
 * 提供奖励相关的数据库操作方法
 */
@Mapper
public interface RewardMapper extends BaseMapper<Reward> {
    
    /**
     * 根据家庭ID查询奖励列表
     * @param familyId 家庭ID
     * @return 奖励列表
     */
    List<Reward> selectByFamilyId(@Param("familyId") Long familyId);
    
    /**
     * 根据创建者ID查询奖励列表
     * @param creatorId 创建者ID
     * @return 奖励列表
     */
    List<Reward> selectByCreatorId(@Param("creatorId") Long creatorId);
    
    /**
     * 根据家庭ID和状态查询奖励列表
     * @param familyId 家庭ID
     * @param status 奖励状态
     * @return 奖励列表
     */
    List<Reward> selectByFamilyIdAndStatus(@Param("familyId") Long familyId, @Param("status") Integer status);
    
    /**
     * 根据奖励ID和创建者ID查询奖励
     * @param id 奖励ID
     * @param creatorId 创建者ID
     * @return 奖励对象
     */
    Reward selectByIdAndCreatorId(@Param("id") Long id, @Param("creatorId") Long creatorId);
    
    /**
     * 查询家庭中积分大于或等于指定值的有效奖励
     * @param familyId 家庭ID
     * @param points 积分值
     * @return 奖励列表
     */
    List<Reward> selectValidByFamilyIdAndPoints(@Param("familyId") Long familyId, @Param("points") Integer points);
}