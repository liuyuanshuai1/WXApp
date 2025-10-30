package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.entity.UserPoints;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户积分Mapper接口
 * 提供用户积分数据的CRUD操作
 */
@Mapper
public interface UserPointsMapper extends BaseMapper<UserPoints> {
    
    /**
     * 根据用户ID和家庭ID查询积分信息
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 用户积分信息
     */
    UserPoints selectByUserIdAndFamilyId(Long userId, Long familyId);
    
    /**
     * 根据用户ID查询所有积分信息
     * @param userId 用户ID
     * @return 用户积分信息列表
     */
    UserPoints selectByUserId(Long userId);
    
    /**
     * 更新用户积分
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param totalPoints 总积分
     * @param availablePoints 可用积分
     * @param historyPoints 历史积分
     * @return 更新结果
     */
    int updatePoints(Long userId, Long familyId, Integer totalPoints, Integer availablePoints, Integer historyPoints);
    
    /**
     * 增加用户积分
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param points 增加的积分
     * @return 更新结果
     */
    int addPoints(Long userId, Long familyId, Integer points);
    
    /**
     * 减少用户积分
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param points 减少的积分
     * @return 更新结果
     */
    int reducePoints(Long userId, Long familyId, Integer points);
}