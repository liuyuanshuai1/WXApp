package com.familypoints.backend.service;

import com.familypoints.backend.dto.PointsLogItemDTO;
import java.util.List;

/**
 * 积分历史服务接口
 * 提供积分历史相关的业务逻辑操作
 */
public interface PointsHistoryService {
    
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
}