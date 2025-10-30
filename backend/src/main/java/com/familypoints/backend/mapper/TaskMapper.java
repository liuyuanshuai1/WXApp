package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 任务表数据访问层接口
 * 提供任务相关的数据库操作方法
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    
    /**
     * 根据家庭ID查询任务列表
     * @param familyId 家庭ID
     * @return 任务列表
     */
    List<Task> selectByFamilyId(@Param("familyId") Long familyId);
    
    /**
     * 根据创建者ID查询任务列表
     * @param creatorId 创建者ID
     * @return 任务列表
     */
    List<Task> selectByCreatorId(@Param("creatorId") Long creatorId);
    
    /**
     * 根据执行用户ID查询任务列表
     * @param executorId 执行用户ID
     * @return 任务列表
     */
    List<Task> selectByExecutorId(@Param("executorId") Long executorId);
    
    /**
     * 根据家庭ID和状态查询任务列表
     * @param familyId 家庭ID
     * @param status 任务状态
     * @return 任务列表
     */
    List<Task> selectByFamilyIdAndStatus(@Param("familyId") Long familyId, @Param("status") Integer status);
    
    /**
     * 根据任务ID和创建者ID查询任务
     * @param id 任务ID
     * @param creatorId 创建者ID
     * @return 任务对象
     */
    Task selectByIdAndCreatorId(@Param("id") Long id, @Param("creatorId") Long creatorId);
}