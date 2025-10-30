package com.familypoints.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.familypoints.backend.dto.CreateTaskRequest;
import com.familypoints.backend.dto.SubmitTaskRequest;
import com.familypoints.backend.dto.ReviewTaskRequest;
import com.familypoints.backend.mapper.TaskMapper;
import com.familypoints.backend.mapper.TaskSubmissionMapper;
import com.familypoints.backend.mapper.MemberMapper;
import com.familypoints.backend.mapper.MemberPointsMapper;
import com.familypoints.backend.mapper.UserAccountMapper;
import com.familypoints.backend.mapper.PointsHistoryMapper;
import com.familypoints.backend.mapper.UserPointsMapper;
import com.familypoints.backend.model.entity.PointsHistory;
import com.familypoints.backend.model.entity.UserPoints;
import com.familypoints.backend.model.Member;
import com.familypoints.backend.model.entity.Task;
import com.familypoints.backend.model.entity.TaskSubmission;
import com.familypoints.backend.service.TaskService;
import com.familypoints.backend.service.FamilyService;
import com.familypoints.backend.service.NotificationService;
import com.familypoints.backend.exception.BusinessException;
import com.familypoints.backend.utils.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 任务服务实现类
 */
@Service
public class TaskServiceImpl implements TaskService {
    
    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private TaskSubmissionMapper taskSubmissionMapper;
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Autowired
    private MemberPointsMapper memberPointsMapper;
    
    @Autowired
    private FamilyService familyService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private PointsHistoryMapper pointsHistoryMapper;
    
    @Autowired
    private UserPointsMapper userPointsMapper;
    
    @Override
    @Transactional
    public Task createTask(CreateTaskRequest request, Long creatorId, Long familyId) {
        // 检查用户是否为家庭的成员
        if (!isUserInFamily(creatorId, familyId)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "用户不在该家庭中");
        }
        
        // 创建任务
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPoints(request.getPoints());
        task.setCreatorId(creatorId);
        task.setFamilyId(familyId);
        task.setStatus(0); // 0表示待领取
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        
        // 设置截止时间
        if (request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }
        
        taskMapper.insert(task);
        return task;
    }
    
    @Override
    public Optional<Task> getTaskById(Long taskId) {
        Task task = taskMapper.selectById(taskId);
        return Optional.ofNullable(task);
    }
    
    @Override
    public List<Task> getFamilyTasks(Long familyId, Integer status) {
        if (status != null) {
            return taskMapper.selectByFamilyIdAndStatus(familyId, status);
        }
        return taskMapper.selectByFamilyId(familyId);
    }
    
    @Override
    public List<Task> getAvailableTasksForUser(Long userId, Long familyId) {
        // 检查用户是否为家庭的成员
        if (!isUserInFamily(userId, familyId)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "用户不在该家庭中");
        }
        
        // 获取所有待领取的任务
        List<Task> tasks = taskMapper.selectByFamilyIdAndStatus(familyId, 0);
        
        // 过滤掉用户已经提交过的任务
        return tasks.stream().filter(task -> {
            QueryWrapper<TaskSubmission> query = new QueryWrapper<>();
            query.eq("task_id", task.getId()).eq("user_id", userId);
            return taskSubmissionMapper.selectCount(query) == 0;
        }).toList();
    }
    
    @Override
    @Transactional
    public TaskSubmission submitTask(SubmitTaskRequest request, Long userId) {
        // 获取任务信息
        Task task = taskMapper.selectById(request.getTaskId());
        if (task == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "任务不存在");
        }

        // 检查用户是否为家庭的成员
        if (!isUserInFamily(userId, task.getFamilyId())) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "用户不在该家庭中");
        }

        // 检查用户是否已经提交过该任务
        QueryWrapper<TaskSubmission> query = new QueryWrapper<>();
        query.eq("task_id", task.getId()).eq("user_id", userId);
        if (taskSubmissionMapper.selectCount(query) > 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "任务已提交");
        }
        
        // 创建任务提交记录
        TaskSubmission submission = new TaskSubmission();
        submission.setTaskId(task.getId());
        submission.setUserId(userId);
        submission.setContent(request.getContent());
        // 将图片URL列表转换为JSON字符串
        String imagesJson = null;
        try {
            imagesJson = objectMapper.writeValueAsString(request.getImages());
        } catch (JsonProcessingException e) {
            log.warn("转换图片URL列表为JSON失败", e);
        }
        submission.setImages(imagesJson);
        submission.setStatus(0); // 0表示待审核
        submission.setSubmitTime(LocalDateTime.now());
        
        taskSubmissionMapper.insert(submission);
        
        // 更新任务状态为待审核
        task.setStatus(1); // 1表示审核中
        task.setUpdateTime(LocalDateTime.now());
        taskMapper.updateById(task);
        
        // 发送任务提交通知给家庭管理员
        try {
            // 获取家庭管理员ID（这里简化处理，实际应该查询数据库）
            Long familyAdminId = familyService.getFamilyAdminId(task.getFamilyId());
            if (familyAdminId != null) {
                notificationService.createTaskSubmittedNotification(
                    task.getId().toString(), 
                    userId.toString(), 
                    familyAdminId.toString()
                );
            }
        } catch (Exception e) {
            log.warn("发送任务提交通知失败", e);
        }
        
        return submission;
    }
    
    @Override
    @Transactional
    public boolean reviewTaskSubmission(ReviewTaskRequest request, Long reviewerId) {
        // 获取任务提交记录
        TaskSubmission submission = taskSubmissionMapper.selectById(request.getSubmissionId());
        if (submission == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "任务提交记录不存在");
        }

        // 获取任务信息
        Task task = taskMapper.selectById(submission.getTaskId());

        // 检查审核者是否为家庭的管理员
        if (!familyService.isFamilyAdmin(task.getFamilyId(), reviewerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无审核权限");
        }
        
        // 更新提交记录状态
        submission.setStatus(request.getApproved() ? 1 : 2); // 1表示通过，2表示拒绝
        submission.setReviewerId(reviewerId);
        submission.setReviewTime(LocalDateTime.now());
        submission.setReviewComment(request.getReviewComment());
        taskSubmissionMapper.updateById(submission);
        
        // 如果审核通过，发放积分
        if (request.getApproved()) {
            // 更新任务状态为已完成
            task.setStatus(2); // 2表示已完成
            task.setExecutorId(submission.getUserId());
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);
            
            // 发放积分给执行用户
            awardPointsToUser(submission.getUserId(), task.getFamilyId(), task.getPoints());
        } else {
            // 如果审核拒绝，重置任务状态为待领取
            task.setStatus(0); // 0表示待领取
            task.setExecutorId(null);
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);
        }
        
        // 发送任务审核结果通知给提交者
        try {
            notificationService.createTaskReviewedNotification(
                task.getId().toString(), 
                submission.getUserId().toString(), 
                request.getApproved()
            );
        } catch (Exception e) {
            log.warn("发送任务审核结果通知失败", e);
        }
        
        return true;
    }
    
    @Override
    public Optional<TaskSubmission> getTaskSubmissionById(Long submissionId) {
        TaskSubmission submission = taskSubmissionMapper.selectById(submissionId);
        return Optional.ofNullable(submission);
    }
    
    @Override
    public List<TaskSubmission> getTaskSubmissions(Long taskId) {
        return taskSubmissionMapper.selectByTaskId(taskId);
    }
    
    @Override
    public List<TaskSubmission> getUserTaskSubmissions(Long userId, Long familyId) {
        // 检查用户是否为家庭的成员
        if (!isUserInFamily(userId, familyId)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "用户不在该家庭中");
        }
        
        return taskSubmissionMapper.selectByUserId(userId);
    }
    
    @Override
    @Transactional
    public Task updateTask(Long taskId, CreateTaskRequest request, Long userId) {
        // 获取任务信息
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "任务不存在");
        }

        // 检查用户是否为任务创建者或家庭管理员
        if (!task.getCreatorId().equals(userId) && !familyService.isFamilyAdmin(task.getFamilyId(), userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作");
        }

        // 检查任务状态是否允许更新
        if (task.getStatus() != 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "任务状态不允许更新");
        }
        
        // 更新任务信息
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPoints(request.getPoints());
        task.setDeadline(request.getDeadline());
        task.setUpdateTime(LocalDateTime.now());
        
        taskMapper.updateById(task);
        return task;
    }
    
    @Override
    @Transactional
    public boolean deleteTask(Long taskId, Long userId) {
        // 获取任务信息
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "任务不存在");
        }

        // 检查用户是否为任务创建者或家庭管理员
        if (!task.getCreatorId().equals(userId) && !familyService.isFamilyAdmin(task.getFamilyId(), userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作");
        }

        // 检查任务状态是否允许删除
        if (task.getStatus() != 0) {
            // 使用权限不足错误代码
            throw new BusinessException(ErrorCode.FORBIDDEN, "任务状态不允许删除");
        }
        
        taskMapper.deleteById(taskId);
        return true;
    }
    
    /**
     * 检查用户是否为家庭的成员
     */
    private boolean isUserInFamily(Long userId, Long familyId) {
        try {
            // 根据MemberMapper接口定义，使用String类型参数
            return memberMapper.selectByUserIdAndFamilyId(String.valueOf(userId), String.valueOf(familyId)) != null;
        } catch (Exception e) {
            log.warn("查询用户家庭关系失败，用户ID: {}, 家庭ID: {}", userId, familyId, e);
            return false;
        }
    }
    
    /**
     * 向用户发放积分
     */
    @Transactional
    public boolean awardPointsToUser(Long userId, Long familyId, Integer points) {
        // 检查用户是否在该家庭中
        Member member = null;
        try {
            // 使用String类型参数调用MemberMapper
            member = memberMapper.selectByUserIdAndFamilyId(String.valueOf(userId), String.valueOf(familyId));
        } catch (Exception ex) {
            log.warn("查询用户家庭关系失败，用户ID: {}, 家庭ID: {}", userId, familyId, ex);
        }
        
        if (member == null) {
            // 修改错误码常量
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "用户不在该家庭中");
        }
        
        // 获取用户当前积分信息
        UserPoints userPoints = userPointsMapper.selectByUserIdAndFamilyId(userId, familyId);
        if (userPoints == null) {
            log.warn("获取用户积分信息失败，用户ID: {}, 家庭ID: {}", userId, familyId);
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "用户积分信息不存在");
        }
        
        // 记录变动前的积分
        Integer previousPoints = userPoints.getTotalPoints();
        
        // 尝试更新积分 - 使用Long类型参数
        try {
            int result = memberPointsMapper.updatePoints(userId, familyId, points);
            if (result > 0) {
                // 更新成功后，记录积分历史
                try {
                    PointsHistory pointsHistory = new PointsHistory();
                    pointsHistory.setUserId(userId);
                    pointsHistory.setFamilyId(familyId);
                    pointsHistory.setType(0); // 0表示任务奖励
                    pointsHistory.setPointsChange(points);
                    pointsHistory.setPreviousPoints(previousPoints);
                    pointsHistory.setCurrentPoints(previousPoints + points);
                    pointsHistory.setRemark("任务奖励");
                    pointsHistory.setCreateTime(LocalDateTime.now());
                    
                    pointsHistoryMapper.insert(pointsHistory);
                } catch (Exception e) {
                    log.warn("记录积分历史失败，用户ID: {}, 家庭ID: {}, 积分: {}", userId, familyId, points, e);
                    // 注意：即使记录积分历史失败，也不应回滚积分更新操作
                }
                
                return true;
            }
        } catch (Exception e) {
            log.warn("更新用户积分失败，用户ID: {}, 家庭ID: {}", userId, familyId, e);
        }
        
        // 如果更新失败，返回错误
        log.warn("更新用户积分失败，用户ID: {}, 家庭ID: {}", userId, familyId);
        throw new BusinessException(ErrorCode.SERVER_ERROR, "更新用户积分失败");
    }
}