package com.familypoints.backend.controller;

import com.familypoints.backend.model.entity.UserAccount;
import com.familypoints.backend.service.UserAccountService;
import com.familypoints.backend.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户账号控制器
 * 处理用户相关的HTTP请求
 */
@RestController
@RequestMapping("/api/user")
public class UserAccountController {
    
    /**
     * 测试端点，不需要认证
     * @return 测试结果
     */
    @GetMapping("/test")
    public Object test(HttpServletRequest request) {
        System.out.println("=== DEBUG === Test endpoint accessed");
        System.out.println("=== DEBUG === Request URI: " + request.getRequestURI());
        System.out.println("=== DEBUG === Context path: " + request.getContextPath());
        System.out.println("=== DEBUG === Servlet path: " + request.getServletPath());
        System.out.println("=== DEBUG === Path Info: " + request.getPathInfo());
        System.out.println("=== DEBUG === Query String: " + request.getQueryString());
        return ResponseUtils.success("测试成功", "Hello World");
    }
    
    @Autowired
    private UserAccountService userAccountService;
    
    /**
     * 微信登录接口
     * @param code 微信登录code
     * @return 登录结果
     */
    @PostMapping("/login/wechat")
    public Object wechatLogin(@RequestParam String code, HttpServletRequest request) {
        System.out.println("=== DEBUG === WeChat login endpoint accessed");
        System.out.println("=== DEBUG === Request URI: " + request.getRequestURI());
        System.out.println("=== DEBUG === Context path: " + request.getContextPath());
        System.out.println("=== DEBUG === Servlet path: " + request.getServletPath());
        System.out.println("Received wechat login request with code: " + code);
        UserAccountService.UserLoginResult result = userAccountService.wechatLogin(code);
        return ResponseUtils.success("登录成功", result);
    }
    
    /**
     * 获取用户信息接口
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/info")
    public Object getUserInfo(@RequestAttribute("userId") Long userId) {
        UserAccount user = userAccountService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return ResponseUtils.success("获取用户信息成功", user);
    }
    
    /**
     * 更新用户信息接口
     * @param userId 用户ID
     * @param userAccount 用户信息
     * @return 更新后的用户信息
     */
    @PutMapping("/info")
    public Object updateUserInfo(@RequestAttribute("userId") Long userId, @RequestBody UserAccount userAccount) {
        // 确保只能更新自己的信息
        userAccount.setId(userId);
        UserAccount updatedUser = userAccountService.updateUser(userAccount);
        return ResponseUtils.success("更新用户信息成功", updatedUser);
    }
}