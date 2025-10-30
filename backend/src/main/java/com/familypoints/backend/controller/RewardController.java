package com.familypoints.backend.controller;

import com.familypoints.backend.dto.CreateRewardRequest;
import com.familypoints.backend.dto.ExchangeRewardRequest;
import com.familypoints.backend.dto.ReviewExchangeRequest;
import com.familypoints.backend.service.RewardService;
import com.familypoints.backend.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 奖励控制器
 * 处理奖励相关的HTTP请求
 */
@RestController
@RequestMapping("/api/reward")
public class RewardController {
    
    @Autowired
    private RewardService rewardService;
    
    /**
     * 创建奖励接口
     * @param request 创建奖励请求
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 创建的奖励
     */
    @PostMapping
    public Object createReward(@RequestBody CreateRewardRequest request, 
                              @RequestParam Long familyId, 
                              @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("创建奖励成功", rewardService.createReward(request, userId, familyId));
    }
    
    /**
     * 获取奖励详情接口
     * @param rewardId 奖励ID
     * @return 奖励详情
     */
    @GetMapping("/{rewardId}")
    public Object getRewardDetail(@PathVariable Long rewardId) {
        return ResponseUtils.success("获取奖励详情成功", rewardService.getRewardById(rewardId).orElseThrow(() -> new RuntimeException("奖励不存在")));
    }
    
    /**
     * 获取家庭奖励列表接口
     * @param familyId 家庭ID
     * @param status 奖励状态（可选）
     * @return 奖励列表
     */
    @GetMapping("/family/list")
    public Object getFamilyRewards(@RequestParam Long familyId, @RequestParam(required = false) Integer status) {
        return ResponseUtils.success("获取家庭奖励列表成功", rewardService.getFamilyRewards(familyId, status));
    }
    
    /**
     * 获取用户可兑换奖励列表接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 可兑换奖励列表
     */
    @GetMapping("/available/list")
    public Object getAvailableRewards(@RequestParam Long familyId, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("获取可兑换奖励列表成功", rewardService.getAvailableRewardsForUser(userId, familyId));
    }
    
    /**
     * 兑换奖励接口
     * @param request 兑换奖励请求
     * @param userId 用户ID
     * @return 兑换记录
     */
    @PostMapping("/exchange")
    public Object exchangeReward(@RequestBody ExchangeRewardRequest request, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("兑换奖励成功", rewardService.exchangeReward(request, userId));
    }
    
    /**
     * 审核奖励兑换接口
     * @param request 审核兑换请求
     * @param userId 审核者用户ID
     * @return 审核结果
     */
    @PostMapping("/review")
    public Object reviewRewardExchange(@RequestBody ReviewExchangeRequest request, @RequestAttribute("userId") Long userId) {
        rewardService.reviewRewardExchange(request, userId);
        return ResponseUtils.success("审核奖励兑换成功");
    }
    
    /**
     * 获取奖励兑换记录接口
     * @param exchangeId 兑换记录ID
     * @return 兑换记录详情
     */
    @GetMapping("/exchange/{exchangeId}")
    public Object getRewardExchange(@PathVariable Long exchangeId) {
        return ResponseUtils.success("获取奖励兑换记录成功", rewardService.getRewardExchangeById(exchangeId).orElseThrow(() -> new RuntimeException("兑换记录不存在")));
    }
    
    /**
     * 获取奖励的所有兑换记录接口
     * @param rewardId 奖励ID
     * @return 兑换记录列表
     */
    @GetMapping("/{rewardId}/exchanges")
    public Object getRewardExchanges(@PathVariable Long rewardId) {
        return ResponseUtils.success("获取奖励兑换记录列表成功", rewardService.getRewardExchanges(rewardId));
    }
    
    /**
     * 获取用户的奖励兑换记录接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 用户的奖励兑换记录列表
     */
    @GetMapping("/user/exchanges")
    public Object getUserRewardExchanges(@RequestParam Long familyId, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("获取用户奖励兑换记录成功", rewardService.getUserRewardExchanges(userId, familyId));
    }
    
    /**
     * 获取用户积分余额接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 积分余额
     */
    @GetMapping("/points/balance")
    public Object getPointsBalance(@RequestParam Long familyId, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("获取积分余额成功", rewardService.getPointsBalance(userId, familyId));
    }
    
    /**
     * 更新奖励接口
     * @param rewardId 奖励ID
     * @param request 更新奖励请求
     * @param userId 用户ID
     * @return 更新后的奖励
     */
    @PutMapping("/{rewardId}")
    public Object updateReward(@PathVariable Long rewardId, @RequestBody CreateRewardRequest request, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("更新奖励成功", rewardService.updateReward(rewardId, request, userId));
    }
    
    /**
     * 删除奖励接口
     * @param rewardId 奖励ID
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{rewardId}")
    public Object deleteReward(@PathVariable Long rewardId, @RequestAttribute("userId") Long userId) {
        rewardService.deleteReward(rewardId, userId);
        return ResponseUtils.success("删除奖励成功");
    }
    
    /**
     * 获取用户积分历史记录接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 积分历史记录列表
     */
    @GetMapping("/points/logs")
    public Object getUserPointsHistory(@RequestParam Long familyId, 
                                      @RequestAttribute("userId") Long userId,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseUtils.success("获取用户积分历史记录成功", rewardService.getUserPointsHistory(userId, familyId, page, size));
    }
    
    /**
     * 根据类型获取用户积分历史记录接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @param type 积分类型
     * @param page 页码
     * @param size 每页大小
     * @return 积分历史记录列表
     */
    @GetMapping("/points/logs/type")
    public Object getUserPointsHistoryByType(@RequestParam Long familyId,
                                            @RequestAttribute("userId") Long userId,
                                            @RequestParam Integer type,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseUtils.success("根据类型获取用户积分历史记录成功", rewardService.getUserPointsHistoryByType(userId, familyId, type, page, size));
    }
}