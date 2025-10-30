package com.familypoints.backend.controller;

import com.familypoints.backend.dto.CreateTaskRequest;
import com.familypoints.backend.dto.SubmitTaskRequest;
import com.familypoints.backend.dto.ReviewTaskRequest;
import com.familypoints.backend.service.TaskService;
import com.familypoints.backend.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 任务控制器
 * 处理任务相关的HTTP请求
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    /**
     * 创建任务接口
     * @param request 创建任务请求
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 创建的任务
     */
    @PostMapping
    public Object createTask(@RequestBody CreateTaskRequest request, 
                            @RequestParam Long familyId, 
                            @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("创建任务成功", taskService.createTask(request, userId, familyId));
    }
    
    /**
     * 获取任务详情接口
     * @param taskId 任务ID
     * @return 任务详情
     */
    @GetMapping("/{taskId}")
    public Object getTaskDetail(@PathVariable Long taskId) {
        return ResponseUtils.success("获取任务详情成功", taskService.getTaskById(taskId).orElseThrow(() -> new RuntimeException("任务不存在")));
    }
    
    /**
     * 获取家庭任务列表接口
     * @param familyId 家庭ID
     * @param status 任务状态（可选）
     * @return 任务列表
     */
    @GetMapping("/family/list")
    public Object getFamilyTasks(@RequestParam Long familyId, @RequestParam(required = false) Integer status) {
        return ResponseUtils.success("获取家庭任务列表成功", taskService.getFamilyTasks(familyId, status));
    }
    
    /**
     * 获取用户可执行任务列表接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 可执行任务列表
     */
    @GetMapping("/available/list")
    public Object getAvailableTasks(@RequestParam Long familyId, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("获取可执行任务列表成功", taskService.getAvailableTasksForUser(userId, familyId));
    }
    
    /**
     * 提交任务接口
     * @param request 提交任务请求
     * @param userId 用户ID
     * @return 提交记录
     */
    @PostMapping("/submit")
    public Object submitTask(@RequestBody SubmitTaskRequest request, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("提交任务成功", taskService.submitTask(request, userId));
    }
    
    /**
     * 审核任务提交接口
     * @param request 审核任务请求
     * @param userId 审核者用户ID
     * @return 审核结果
     */
    @PostMapping("/review")
    public Object reviewTask(@RequestBody ReviewTaskRequest request, @RequestAttribute("userId") Long userId) {
        taskService.reviewTaskSubmission(request, userId);
        return ResponseUtils.success("审核任务成功");
    }
    
    /**
     * 获取任务提交记录接口
     * @param submissionId 提交记录ID
     * @return 提交记录详情
     */
    @GetMapping("/submission/{submissionId}")
    public Object getTaskSubmission(@PathVariable Long submissionId) {
        return ResponseUtils.success("获取任务提交记录成功", taskService.getTaskSubmissionById(submissionId).orElseThrow(() -> new RuntimeException("提交记录不存在")));
    }
    
    /**
     * 获取任务的所有提交记录接口
     * @param taskId 任务ID
     * @return 提交记录列表
     */
    @GetMapping("/{taskId}/submissions")
    public Object getTaskSubmissions(@PathVariable Long taskId) {
        return ResponseUtils.success("获取任务提交记录列表成功", taskService.getTaskSubmissions(taskId));
    }
    
    /**
     * 获取用户的任务提交记录接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 用户的任务提交记录列表
     */
    @GetMapping("/user/submissions")
    public Object getUserTaskSubmissions(@RequestParam Long familyId, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("获取用户任务提交记录成功", taskService.getUserTaskSubmissions(userId, familyId));
    }
    
    /**
     * 更新任务接口
     * @param taskId 任务ID
     * @param request 更新任务请求
     * @param userId 用户ID
     * @return 更新后的任务
     */
    @PutMapping("/{taskId}")
    public Object updateTask(@PathVariable Long taskId, @RequestBody CreateTaskRequest request, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("更新任务成功", taskService.updateTask(taskId, request, userId));
    }
    
    /**
     * 删除任务接口
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{taskId}")
    public Object deleteTask(@PathVariable Long taskId, @RequestAttribute("userId") Long userId) {
        taskService.deleteTask(taskId, userId);
        return ResponseUtils.success("删除任务成功");
    }
}