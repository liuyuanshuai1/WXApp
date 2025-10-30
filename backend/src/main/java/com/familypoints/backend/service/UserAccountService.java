package com.familypoints.backend.service;

import com.familypoints.backend.model.entity.UserAccount;
import java.util.Optional;

/**
 * 用户账号服务接口
 * 提供用户账号相关的业务逻辑操作
 */
public interface UserAccountService {
    
    /**
     * 通过微信openId获取用户信息
     * @param openId 微信openId
     * @return 用户账号对象
     */
    Optional<UserAccount> getUserByOpenId(String openId);
    
    /**
     * 创建新用户
     * @param userAccount 用户账号对象
     * @return 创建的用户账号
     */
    UserAccount createUser(UserAccount userAccount);
    
    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户账号对象
     */
    Optional<UserAccount> getUserById(Long userId);
    
    /**
     * 更新用户信息
     * @param userAccount 用户账号对象
     * @return 更新后的用户账号
     */
    UserAccount updateUser(UserAccount userAccount);
    
    /**
     * 微信登录或注册
     * @param code 微信登录code
     * @return 包含用户信息和token的结果
     */
    UserLoginResult wechatLogin(String code);
    
    /**
     * 微信登录结果封装类
     */
    class UserLoginResult {
        private UserAccount user;
        private String token;
        
        public UserLoginResult(UserAccount user, String token) {
            this.user = user;
            this.token = token;
        }
        
        public UserAccount getUser() {
            return user;
        }
        
        public String getToken() {
            return token;
        }
    }
}