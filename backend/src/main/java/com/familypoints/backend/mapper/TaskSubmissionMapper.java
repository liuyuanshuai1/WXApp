package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.entity.TaskSubmission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 任务提交表数据访问层接口
 * 提供任务提交相关的数据库操作方法
 */
@Mapper
public interface TaskSubmissionMapper extends BaseMapper<TaskSubmission> {
    
    /**
     * 根据任务ID查询提交记录
     * @param taskId 任务ID
     * @return 任务提交列表
     */
    List<TaskSubmission> selectByTaskId(@Param("taskId") Long taskId);
    
    /**
     * 根据用户ID查询提交记录
     * @param userId 用户ID
     * @return 任务提交列表
     */
    List<TaskSubmission> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据任务ID和用户ID查询提交记录
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 任务提交记录
     */
    TaskSubmission selectByTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") Long userId);
    
    /**
     * 根据家庭ID查询提交记录
     * @param familyId 家庭ID
     * @return 任务提交列表
     */
    List<TaskSubmission> selectByFamilyId(@Param("familyId") Long familyId);
    
    /**
     * 根据状态查询提交记录
     * @param status 提交状态
     * @return 任务提交列表
     */
    List<TaskSubmission> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据家庭ID和状态查询提交记录
     * @param familyId 家庭ID
     * @param status 提交状态
     * @return 任务提交列表
     */
    List<TaskSubmission> selectByFamilyIdAndStatus(@Param("familyId") Long familyId, @Param("status") Integer status);
}