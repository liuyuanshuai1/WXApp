package com.familypoints.backend.service.impl;

// 导入必要的包
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.familypoints.backend.mapper.UserAccountMapper;
import com.familypoints.backend.model.UserAccount; // 使用mapper对应的UserAccount类型
import com.familypoints.backend.service.UserAccountService;
import com.familypoints.backend.utils.JwtUtils;
import com.familypoints.backend.exception.BusinessException;
import com.familypoints.backend.utils.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * 用户账号服务实现类
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {
    
    private static final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);
    
    @Autowired
    private UserAccountMapper userAccountMapper;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public Optional<com.familypoints.backend.model.entity.UserAccount> getUserByOpenId(String openId) {
        QueryWrapper<com.familypoints.backend.model.UserAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("open_id", openId);
        com.familypoints.backend.model.UserAccount user = userAccountMapper.selectOne(queryWrapper);
        return Optional.ofNullable(createEntityUser(user));
    }
    
    @Override
    @Transactional
    public com.familypoints.backend.model.entity.UserAccount createUser(com.familypoints.backend.model.entity.UserAccount userAccount) {
        // 检查用户是否已存在
        Optional<com.familypoints.backend.model.entity.UserAccount> existingUser = getUserByOpenId(userAccount.getOpenId());
        if (existingUser.isPresent()) {
            throw new BusinessException(ErrorCode.RESOURCE_EXIST, "用户已存在");
        }
        
        // 创建新用户
        com.familypoints.backend.model.UserAccount modelUser = new com.familypoints.backend.model.UserAccount();
        // 设置必要字段
        modelUser.setOpenId(userAccount.getOpenId());
        
        userAccountMapper.insert(modelUser);
        return createEntityUser(modelUser);
    }
    
    @Override
    public Optional<com.familypoints.backend.model.entity.UserAccount> getUserById(Long userId) {
        com.familypoints.backend.model.UserAccount user = userAccountMapper.selectById(userId);
        return Optional.ofNullable(createEntityUser(user));
    }
    
    @Override
    @Transactional
    public com.familypoints.backend.model.entity.UserAccount updateUser(com.familypoints.backend.model.entity.UserAccount userAccount) {
        com.familypoints.backend.model.UserAccount existingUser = userAccountMapper.selectById(userAccount.getId());
        if (existingUser == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "用户不存在");
        }
        
        // 更新用户信息
        com.familypoints.backend.model.UserAccount modelUser = new com.familypoints.backend.model.UserAccount();
        // 设置必要字段
        modelUser.setId(userAccount.getId().toString()); // 注意：model层使用String类型ID
        modelUser.setOpenId(userAccount.getOpenId());
        
        userAccountMapper.updateById(modelUser);
        return createEntityUser(existingUser); // 返回更新前的用户信息
    }
    
    @Override
    @Transactional
    public UserAccountService.UserLoginResult wechatLogin(String code) {
        try {
            // 简化微信登录逻辑，直接使用code作为临时的openId
            // 在实际应用中，这里应该调用微信API获取真实的openId
            String openId = code; // 临时方案，后续需要替换为真实的openId获取逻辑
            
            // 查找用户或创建新用户
            com.familypoints.backend.model.UserAccount user = userAccountMapper.selectByOpenId(openId);
            
            if (user == null) {
                // 创建新用户
                com.familypoints.backend.model.UserAccount newUser = new com.familypoints.backend.model.UserAccount();
                newUser.setOpenId(openId);
                
                userAccountMapper.insert(newUser);
                user = newUser;
            }
            
            // 转换为实体类并生成token
            com.familypoints.backend.model.entity.UserAccount entityUser = createEntityUser(user);
            String token = jwtUtils.generateToken(entityUser.getId().toString(), entityUser.getOpenId());

            // 返回包含用户信息和token的登录结果
            return new UserAccountService.UserLoginResult(entityUser, token);
        } catch (Exception e) {
            log.error("微信登录失败", e);
            throw new BusinessException(ErrorCode.SERVER_ERROR, "微信登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建实体层的UserAccount对象
     * 从数据模型层对象转换，只设置必要的字段
     */
    private com.familypoints.backend.model.entity.UserAccount createEntityUser(com.familypoints.backend.model.UserAccount model) {
        if (model == null) {
            return null;
        }
        
        com.familypoints.backend.model.entity.UserAccount entity = new com.familypoints.backend.model.entity.UserAccount();
        // 设置必要的字段
        // 注意：model层使用String类型ID，entity层使用Long类型ID
        if (model.getId() != null) {
            try {
                entity.setId(Long.valueOf(model.getId()));
            } catch (NumberFormatException e) {
                // 如果转换失败，保持ID为null
                log.warn("无法将用户ID从String转换为Long: {}", model.getId());
            }
        }
        entity.setOpenId(model.getOpenId());
        
        return entity;
    }
}