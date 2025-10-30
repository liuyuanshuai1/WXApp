package com.familypoints.backend.controller;

import com.familypoints.backend.dto.PointsLogItemDTO;
import com.familypoints.backend.service.PointsHistoryService;
import com.familypoints.backend.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 积分历史控制器
 * 处理积分历史相关的HTTP请求
 */
@RestController
@RequestMapping("/api/points")
public class PointsHistoryController {
    
    @Autowired
    private PointsHistoryService pointsHistoryService;
    
    /**
     * 获取用户积分历史记录接口
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param page 页码，默认为1
     * @param size 每页大小，默认为20
     * @return 积分历史记录列表
     */
    @GetMapping("/logs")
    public Object getPointsHistory(@RequestParam Long userId,
                                  @RequestParam Long familyId,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int size) {
        List<PointsLogItemDTO> pointsLogs = pointsHistoryService.getUserPointsHistory(userId, familyId, page, size);
        return ResponseUtils.success("获取积分历史记录成功", pointsLogs);
    }
    
    /**
     * 根据积分类型获取用户积分历史记录接口
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @param type 积分类型
     * @param page 页码，默认为1
     * @param size 每页大小，默认为20
     * @return 积分历史记录列表
     */
    @GetMapping("/logs/type")
    public Object getPointsHistoryByType(@RequestParam Long userId,
                                        @RequestParam Long familyId,
                                        @RequestParam Integer type,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        List<PointsLogItemDTO> pointsLogs = pointsHistoryService.getUserPointsHistoryByType(userId, familyId, type, page, size);
        return ResponseUtils.success("获取积分历史记录成功", pointsLogs);
    }
}