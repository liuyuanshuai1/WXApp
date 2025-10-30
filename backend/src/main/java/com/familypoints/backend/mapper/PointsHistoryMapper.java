package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.entity.PointsHistory;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 积分历史记录Mapper接口
 * 提供积分历史记录的CRUD操作
 */
@Mapper
public interface PointsHistoryMapper extends BaseMapper<PointsHistory> {
    
    /**
     * 根据用户ID查询积分历史记录
     * @param userId 用户ID
     * @return 积分历史记录列表
     */
    List<PointsHistory> selectByUserId(Long userId);
    
    /**
     * 根据用户ID和家庭ID查询积分历史记录
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 积分历史记录列表
     */
    List<PointsHistory> selectByUserIdAndFamilyId(Long userId, Long familyId);
    
    /**
     * 根据家庭ID查询积分历史记录
     * @param familyId 家庭ID
     * @return 积分历史记录列表
     */
    List<PointsHistory> selectByFamilyId(Long familyId);
    
    /**
     * 根据积分类型查询历史记录
     * @param userId 用户ID
     * @param type 积分类型
     * @return 积分历史记录列表
     */
    List<PointsHistory> selectByUserIdAndType(Long userId, Integer type);
}