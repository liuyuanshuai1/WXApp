package com.familypoints.backend.controller;

import com.familypoints.backend.dto.CreateFamilyRequest;
import com.familypoints.backend.dto.JoinFamilyRequest;
import com.familypoints.backend.service.FamilyService;
import com.familypoints.backend.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 家庭控制器
 * 处理家庭相关的HTTP请求
 */
@RestController
@RequestMapping("/api/family")
public class FamilyController {
    
    @Autowired
    private FamilyService familyService;
    
    /**
     * 创建家庭接口
     * @param request 创建家庭请求
     * @param userId 用户ID
     * @return 创建结果
     */
    @PostMapping
    public Object createFamily(@RequestBody CreateFamilyRequest request, @RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("创建家庭成功", familyService.createFamily(request, userId));
    }
    
    /**
     * 加入家庭接口
     * @param request 加入家庭请求
     * @param userId 用户ID
     * @return 加入结果
     */
    @PostMapping("/join")
    public Object joinFamily(@RequestBody JoinFamilyRequest request, @RequestAttribute("userId") Long userId) {
        familyService.joinFamily(request, userId);
        return ResponseUtils.success("加入家庭成功");
    }
    
    /**
     * 获取家庭信息接口
     * @param familyId 家庭ID
     * @return 家庭信息
     */
    @GetMapping("/{familyId}")
    public Object getFamilyInfo(@PathVariable Long familyId) {
        return ResponseUtils.success("获取家庭信息成功", familyService.getFamilyById(familyId).orElseThrow(() -> new RuntimeException("家庭不存在")));
    }
    
    /**
     * 获取用户的家庭列表接口
     * @param userId 用户ID
     * @return 家庭列表
     */
    @GetMapping("/user/list")
    public Object getUserFamilies(@RequestAttribute("userId") Long userId) {
        return ResponseUtils.success("获取家庭列表成功", familyService.getUserFamilies(userId));
    }
    
    /**
     * 获取家庭成员列表接口
     * @param familyId 家庭ID
     * @return 成员列表
     */
    @GetMapping("/{familyId}/members")
    public Object getFamilyMembers(@PathVariable Long familyId) {
        return ResponseUtils.success("获取家庭成员列表成功", familyService.getFamilyMembers(familyId));
    }
    
    /**
     * 退出家庭接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 退出结果
     */
    @DeleteMapping("/{familyId}/leave")
    public Object leaveFamily(@PathVariable Long familyId, @RequestAttribute("userId") Long userId) {
        familyService.leaveFamily(familyId, userId);
        return ResponseUtils.success("退出家庭成功");
    }
    
    /**
     * 生成新家庭码接口
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 新家庭码
     */
    @PostMapping("/{familyId}/code/generate")
    public Object generateNewFamilyCode(@PathVariable Long familyId, @RequestAttribute("userId") Long userId) {
        // 验证是否为家庭管理员
        if (!familyService.isFamilyAdmin(familyId, userId)) {
            return ResponseUtils.error(403, "无权限操作");
        }
        String newCode = familyService.generateNewFamilyCode(familyId);
        return ResponseUtils.success("生成新家庭码成功", newCode);
    }
}