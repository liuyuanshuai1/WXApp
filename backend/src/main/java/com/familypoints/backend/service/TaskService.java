package com.familypoints.backend.service;

import com.familypoints.backend.dto.CreateTaskRequest;
import com.familypoints.backend.dto.SubmitTaskRequest;
import com.familypoints.backend.dto.ReviewTaskRequest;
import com.familypoints.backend.model.entity.Task;
import com.familypoints.backend.model.entity.TaskSubmission;
import java.util.List;
import java.util.Optional;

/**
 * 任务服务接口
 * 提供任务相关的业务逻辑操作
 */
public interface TaskService {
    
    /**
     * 创建任务
     * @param request 创建任务请求
     * @param creatorId 创建者用户ID
     * @param familyId 家庭ID
     * @return 创建的任务
     */
    Task createTask(CreateTaskRequest request, Long creatorId, Long familyId);
    
    /**
     * 获取任务详情
     * @param taskId 任务ID
     * @return 任务对象
     */
    Optional<Task> getTaskById(Long taskId);
    
    /**
     * 获取家庭的任务列表
     * @param familyId 家庭ID
     * @param status 任务状态（可选）
     * @return 任务列表
     */
    List<Task> getFamilyTasks(Long familyId, Integer status);
    
    /**
     * 获取用户可执行的任务列表
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 任务列表
     */
    List<Task> getAvailableTasksForUser(Long userId, Long familyId);
    
    /**
     * 提交任务
     * @param request 提交任务请求
     * @param userId 用户ID
     * @return 任务提交记录
     */
    TaskSubmission submitTask(SubmitTaskRequest request, Long userId);
    
    /**
     * 审核任务提交
     * @param request 审核任务请求
     * @param reviewerId 审核者用户ID
     * @return 是否审核成功
     */
    boolean reviewTaskSubmission(ReviewTaskRequest request, Long reviewerId);
    
    /**
     * 获取任务提交记录
     * @param submissionId 提交记录ID
     * @return 任务提交记录
     */
    Optional<TaskSubmission> getTaskSubmissionById(Long submissionId);
    
    /**
     * 获取任务的所有提交记录
     * @param taskId 任务ID
     * @return 任务提交记录列表
     */
    List<TaskSubmission> getTaskSubmissions(Long taskId);
    
    /**
     * 获取用户的任务提交记录
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 任务提交记录列表
     */
    List<TaskSubmission> getUserTaskSubmissions(Long userId, Long familyId);
    
    /**
     * 更新任务
     * @param taskId 任务ID
     * @param request 更新任务请求
     * @param userId 操作用户ID
     * @return 更新后的任务
     */
    Task updateTask(Long taskId, CreateTaskRequest request, Long userId);
    
    /**
     * 删除任务
     * @param taskId 任务ID
     * @param userId 操作用户ID
     * @return 是否删除成功
     */
    boolean deleteTask(Long taskId, Long userId);
}