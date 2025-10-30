package com.familypoints.backend.service.impl;

import com.familypoints.backend.dto.PointsLogItemDTO;
import com.familypoints.backend.mapper.PointsHistoryMapper;
import com.familypoints.backend.model.entity.PointsHistory;
import com.familypoints.backend.service.PointsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分历史服务实现类
 * 实现积分历史相关的业务逻辑操作
 */
@Service
public class PointsHistoryServiceImpl implements PointsHistoryService {
    
    @Autowired
    private PointsHistoryMapper pointsHistoryMapper;
    
    @Override
    public List<PointsLogItemDTO> getUserPointsHistory(Long userId, Long familyId, int page, int size) {
        // 计算分页参数
        int offset = (page - 1) * size;
        
        // 查询用户的积分历史记录
        List<PointsHistory> pointsHistories = pointsHistoryMapper.selectByUserIdAndFamilyId(userId, familyId);
        
        // 分页处理
        int endIndex = Math.min(offset + size, pointsHistories.size());
        List<PointsHistory> pagedHistories = pointsHistories.subList(offset, endIndex);
        
        // 转换为PointsLogItemDTO
        return pagedHistories.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<PointsLogItemDTO> getUserPointsHistoryByType(Long userId, Long familyId, Integer type, int page, int size) {
        // 计算分页参数
        int offset = (page - 1) * size;
        
        // 查询用户的积分历史记录
        List<PointsHistory> pointsHistories = pointsHistoryMapper.selectByUserIdAndType(userId, type);
        
        // 分页处理
        int endIndex = Math.min(offset + size, pointsHistories.size());
        List<PointsHistory> pagedHistories = pointsHistories.subList(offset, endIndex);
        
        // 转换为PointsLogItemDTO
        return pagedHistories.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 将PointsHistory实体转换为PointsLogItemDTO
     * @param pointsHistory 积分历史记录实体
     * @return PointsLogItemDTO
     */
    private PointsLogItemDTO convertToDTO(PointsHistory pointsHistory) {
        PointsLogItemDTO dto = new PointsLogItemDTO();
        
        // 设置积分来源
        switch (pointsHistory.getType()) {
            case 0:
                dto.setSource("task");
                dto.setType("income");
                break;
            case 1:
                dto.setSource("reward");
                dto.setType("expense");
                break;
            case 2:
                dto.setSource("manual");
                // 根据积分变动数量判断是收入还是支出
                dto.setType(pointsHistory.getPointsChange() > 0 ? "income" : "expense");
                break;
            default:
                dto.setSource("unknown");
                dto.setType("unknown");
        }
        
        // 设置备注
        dto.setRemark(getRemarkByType(pointsHistory.getType(), pointsHistory.getRelatedId()));
        
        // 设置积分数量
        dto.setAmount(pointsHistory.getPointsChange());
        
        // 设置时间
        if (pointsHistory.getCreateTime() != null) {
            dto.setTime(pointsHistory.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        return dto;
    }
    
    /**
     * 根据积分类型和相关ID获取备注信息
     * @param type 积分类型
     * @param relatedId 相关ID
     * @return 备注信息
     */
    private String getRemarkByType(Integer type, Long relatedId) {
        switch (type) {
            case 0:
                return "任务奖励";
            case 1:
                return "奖励兑换";
            case 2:
                return "积分调整";
            default:
                return "未知";
        }
    }
}