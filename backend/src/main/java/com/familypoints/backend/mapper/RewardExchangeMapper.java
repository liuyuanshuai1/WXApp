package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.entity.RewardExchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 奖励兑换记录表数据访问层接口
 * 提供奖励兑换相关的数据库操作方法
 */
@Mapper
public interface RewardExchangeMapper extends BaseMapper<RewardExchange> {
    
    /**
     * 根据用户ID查询兑换记录
     * @param userId 用户ID
     * @return 奖励兑换列表
     */
    List<RewardExchange> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据奖励ID查询兑换记录
     * @param rewardId 奖励ID
     * @return 奖励兑换列表
     */
    List<RewardExchange> selectByRewardId(@Param("rewardId") Long rewardId);
    
    /**
     * 根据家庭ID查询兑换记录
     * @param familyId 家庭ID
     * @return 奖励兑换列表
     */
    List<RewardExchange> selectByFamilyId(@Param("familyId") Long familyId);
    
    /**
     * 根据状态查询兑换记录
     * @param status 兑换状态
     * @return 奖励兑换列表
     */
    List<RewardExchange> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据家庭ID和状态查询兑换记录
     * @param familyId 家庭ID
     * @param status 兑换状态
     * @return 奖励兑换列表
     */
    List<RewardExchange> selectByFamilyIdAndStatus(@Param("familyId") Long familyId, @Param("status") Integer status);
    
    /**
     * 查询用户在特定奖励上的兑换次数
     * @param userId 用户ID
     * @param rewardId 奖励ID
     * @param status 状态（可选，不传则查询所有状态）
     * @return 兑换次数
     */
    Integer countByUserIdAndRewardId(@Param("userId") Long userId, @Param("rewardId") Long rewardId, @Param("status") Integer status);
}