package com.familypoints.backend.service;

import com.familypoints.backend.dto.CreateRewardRequest;
import com.familypoints.backend.dto.ExchangeRewardRequest;
import com.familypoints.backend.dto.ReviewExchangeRequest;
import com.familypoints.backend.dto.PointsBalanceDTO;
import com.familypoints.backend.dto.PointsLogItemDTO;
import com.familypoints.backend.model.entity.Reward;
import com.familypoints.backend.model.entity.RewardExchange;
import java.util.List;
import java.util.Optional;

/**
 * 奖励服务接口
 * 提供奖励相关的业务逻辑操作
 */
public interface RewardService {
    
    /**
     * 创建奖励
     * @param request 创建奖励请求
     * @param creatorId 创建者用户ID
     * @param familyId 家庭ID
     * @return 创建的奖励
     */
    Reward createReward(CreateRewardRequest request, Long creatorId, Long familyId);
    
    /**
     * 获取奖励详情
     * @param rewardId 奖励ID
     * @return 奖励对象
     */
    Optional<Reward> getRewardById(Long rewardId);
    
    /**
     * 获取家庭的奖励列表
     * @param familyId 家庭ID
     * @param status 奖励状态（可选）
     * @return 奖励列表
     */
    List<Reward> getFamilyRewards(Long familyId, Integer status);
    
    /**
     * 获取用户可兑换的奖励列表
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 奖励列表
     */
    List<Reward> getAvailableRewardsForUser(Long userId, Long familyId);
    
    /**
     * 兑换奖励
     * @param request 兑换奖励请求
     * @param userId 用户ID
     * @return 奖励兑换记录
     */
    RewardExchange exchangeReward(ExchangeRewardRequest request, Long userId);
    
    /**
     * 审核奖励兑换
     * @param request 审核兑换请求
     * @param reviewerId 审核者用户ID
     * @return 是否审核成功
     */
    boolean reviewRewardExchange(ReviewExchangeRequest request, Long reviewerId);
    
    /**
     * 获取奖励兑换记录
     * @param exchangeId 兑换记录ID
     * @return 奖励兑换记录
     */
    Optional<RewardExchange> getRewardExchangeById(Long exchangeId);
    
    /**
     * 获取奖励的所有兑换记录
     * @param rewardId 奖励ID
     * @return 奖励兑换记录列表
     */
    List<RewardExchange> getRewardExchanges(Long rewardId);
    
    /**
     * 获取用户的奖励兑换记录
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 奖励兑换记录列表
     */
    List<RewardExchange> getUserRewardExchanges(Long userId, Long familyId);
    
    /**
     * 获取用户在家庭中的积分余额
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 积分余额DTO
     */
    PointsBalanceDTO getPointsBalance(Long userId, Long familyId);
    
    /**
     * 更新奖励
     * @param rewardId 奖励ID
     * @param request 更新奖励请求
     * @param userId 操作用户ID
     * @return 更新后的奖励
     */
    Reward updateReward(Long rewardId, CreateRewardRequest request, Long userId);
    
    /**
     * 获取用户的积分历史记录
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param page 页码
     * @param size 每页大小
     * @return 积分历史记录列表
     */
    List<PointsLogItemDTO> getUserPointsHistory(Long userId, Long familyId, int page, int size);
    
    /**
     * 根据积分类型获取用户的积分历史记录
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param type 积分类型
     * @param page 页码
     * @param size 每页大小
     * @return 积分历史记录列表
     */
    List<PointsLogItemDTO> getUserPointsHistoryByType(Long userId, Long familyId, Integer type, int page, int size);
    
    /**
     * 删除奖励
     * @param rewardId 奖励ID
     * @param userId 操作用户ID
     * @return 是否删除成功
     */
    boolean deleteReward(Long rewardId, Long userId);
}