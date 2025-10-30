package com.familypoints.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import com.familypoints.backend.dto.CreateFamilyRequest;
import com.familypoints.backend.dto.JoinFamilyRequest;
import com.familypoints.backend.dto.CreateFamilyResponse;
import com.familypoints.backend.mapper.FamilyMapper;
import com.familypoints.backend.mapper.MemberMapper;
import com.familypoints.backend.model.Family;
import com.familypoints.backend.model.Member;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.familypoints.backend.service.FamilyService;
import com.familypoints.backend.utils.FamilyCodeGenerator;
import com.familypoints.backend.exception.BusinessException;
import com.familypoints.backend.utils.ErrorCode;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 家庭服务实现类
 */
@Service
public class FamilyServiceImpl implements FamilyService {
    
    private static final Logger log = LoggerFactory.getLogger(FamilyServiceImpl.class);
    
    @Autowired
    private FamilyMapper familyMapper;
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Autowired
    private FamilyCodeGenerator familyCodeGenerator;
    
    @Override
    @Transactional
    public CreateFamilyResponse createFamily(CreateFamilyRequest request, Long userId) {
        // 检查用户是否已经创建了家庭
        QueryWrapper<Family> familyQuery = new QueryWrapper<>();
        familyQuery.eq("owner_id", String.valueOf(userId));
        if (familyMapper.selectCount(familyQuery) > 0) {
            throw new BusinessException(ErrorCode.USER_HAS_FAMILY, "用户已有家庭");
        }
        
        // 生成家庭码
        String familyCode = familyCodeGenerator.generateFamilyCode();
        LocalDateTime expiryDateTime = familyCodeGenerator.calculateExpiryTime();
        Long expiryTime = expiryDateTime.toEpochSecond(java.time.ZoneOffset.UTC) * 1000; // 转换为毫秒时间戳
        
        // 创建家庭对象，使用反射机制设置属性
        Family family = new Family();
        try {
            // 使用反射设置属性值，避免调用可能不存在的setter方法
            Field nameField = family.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(family, request.getFamilyName());
            
            Field ownerIdField = family.getClass().getDeclaredField("ownerId");
            ownerIdField.setAccessible(true);
            ownerIdField.set(family, userId.toString());
            
            Field codeField = family.getClass().getDeclaredField("code");
            codeField.setAccessible(true);
            codeField.set(family, familyCode);
            
            Field codeExpireAtField = family.getClass().getDeclaredField("codeExpireAt");
            codeExpireAtField.setAccessible(true);
            codeExpireAtField.set(family, expiryDateTime);
            
            Field createdAtField = family.getClass().getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(family, LocalDateTime.now());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "创建家庭失败: " + e.getMessage());
        }
        
        // 插入家庭数据
        familyMapper.insert(family);
        
        // 将创建者添加为家庭成员（管理员）
        Member member = new Member();
        try {
            // 使用反射设置Member对象的属性
            Field familyIdField = member.getClass().getDeclaredField("familyId");
            familyIdField.setAccessible(true);
            
            // 使用反射获取family的id属性
            Field familyIdGetterField = family.getClass().getDeclaredField("id");
            familyIdGetterField.setAccessible(true);
            Object familyIdObj = familyIdGetterField.get(family);
            String familyId = familyIdObj != null ? String.valueOf(familyIdObj) : null;
            
            familyIdField.set(member, familyId);
            
            Field userIdField = member.getClass().getDeclaredField("userId");
            userIdField.setAccessible(true);
            userIdField.set(member, userId.toString());
            
            Field roleField = member.getClass().getDeclaredField("role");
            roleField.setAccessible(true);
            roleField.set(member, "parent");
            
            Field joinedAtField = member.getClass().getDeclaredField("joinedAt");
            joinedAtField.setAccessible(true);
            joinedAtField.set(member, LocalDateTime.now());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "添加家庭成员失败: " + e.getMessage());
        }
        memberMapper.insert(member);
        
        // 创建响应对象，使用反射机制设置属性
        try {
            // 使用反射创建CreateFamilyResponse实例
            Class<?> responseClass = Class.forName("com.familypoints.backend.dto.CreateFamilyResponse");
            Object response = responseClass.getDeclaredConstructor().newInstance();
            
            // 设置familyId
            Field idField = family.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(family);
            String familyId = idValue != null ? String.valueOf(idValue) : null;
            
            Field familyIdField = responseClass.getDeclaredField("familyId");
            familyIdField.setAccessible(true);
            familyIdField.set(response, familyId);
            
            // 设置familyCode
            Field familyCodeField = responseClass.getDeclaredField("familyCode");
            familyCodeField.setAccessible(true);
            familyCodeField.set(response, familyCode);
            
            // 设置createTime
            Field createdAtField = family.getClass().getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            LocalDateTime createdAt = (LocalDateTime) createdAtField.get(family);
            
            Field createTimeField = responseClass.getDeclaredField("createTime");
            createTimeField.setAccessible(true);
            createTimeField.set(response, createdAt != null ? createdAt.toString() : null);
            
            return (com.familypoints.backend.dto.CreateFamilyResponse) response;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "创建响应对象失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public boolean joinFamily(JoinFamilyRequest request, Long userId) {
        // 查找家庭
        Family family = familyMapper.selectByCode(request.getFamilyCode());
        if (family == null) {
            throw new BusinessException(ErrorCode.FAMILY_NOT_FOUND, "家庭不存在");
        }
        
        // 检查家庭码是否过期
        try {
            Field codeExpireAtField = family.getClass().getDeclaredField("codeExpireAt");
            codeExpireAtField.setAccessible(true);
            LocalDateTime codeExpireAt = (LocalDateTime) codeExpireAtField.get(family);
            if (codeExpireAt != null && familyCodeGenerator.isCodeExpired(codeExpireAt)) {
                throw new BusinessException(ErrorCode.FAMILY_CODE_EXPIRED, "家庭码已过期");
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "检查家庭码状态失败: " + e.getMessage());
        }
        
        // 检查用户是否已经是该家庭的成员
        QueryWrapper<Member> memberQuery = new QueryWrapper<>();
        try {
            Field idField = family.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(family);
            memberQuery.eq("family_id", idValue != null ? String.valueOf(idValue) : null).eq("user_id", userId.toString());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "获取家庭ID失败: " + e.getMessage());
        }
        if (memberMapper.selectCount(memberQuery) > 0) {
            throw new BusinessException(ErrorCode.ALREADY_IN_FAMILY, "用户已在该家庭中");
        }
        
        // 检查用户是否已在其他家庭中
        memberQuery = new QueryWrapper<>();
        memberQuery.eq("user_id", userId.toString());
        if (memberMapper.selectCount(memberQuery) > 0) {
            throw new BusinessException(ErrorCode.USER_HAS_FAMILY, "用户已有家庭");
        }
        
        // 添加用户为家庭成员
        Member member = new Member();
        try {
            // 使用反射设置Member对象的属性
            Field familyIdField = member.getClass().getDeclaredField("familyId");
            familyIdField.setAccessible(true);
            
            // 使用反射获取family的id属性
            Field familyIdGetterField = family.getClass().getDeclaredField("id");
            familyIdGetterField.setAccessible(true);
            Object familyIdObj = familyIdGetterField.get(family);
            String familyId = familyIdObj != null ? String.valueOf(familyIdObj) : null;
            
            familyIdField.set(member, familyId);
            
            Field userIdField = member.getClass().getDeclaredField("userId");
            userIdField.setAccessible(true);
            userIdField.set(member, userId.toString());
            
            Field roleField = member.getClass().getDeclaredField("role");
            roleField.setAccessible(true);
            roleField.set(member, request.getRole() != null ? request.getRole() : "child");
            
            Field joinedAtField = member.getClass().getDeclaredField("joinedAt");
            joinedAtField.setAccessible(true);
            joinedAtField.set(member, LocalDateTime.now());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "添加家庭成员失败: " + e.getMessage());
        }
        memberMapper.insert(member);
        
        return true;
    }
    
    @Override
    public Optional<com.familypoints.backend.model.entity.Family> getFamilyById(Long familyId) {
        // 先查询model包下的Family
        Family modelFamily = null;
        try {
            // 优先尝试使用Long类型参数
            modelFamily = familyMapper.selectById(familyId);
        } catch (Exception e) {
            try {
                // 如果失败，尝试转换为String类型
                modelFamily = familyMapper.selectById(String.valueOf(familyId));
            } catch (Exception ex) {
                log.warn("查询家庭失败，家庭ID: {}", familyId, ex);
            }
        }
        
        if (modelFamily == null) {
            return Optional.empty();
        }
        
        // 转换为entity包下的Family
        com.familypoints.backend.model.entity.Family entityFamily = new com.familypoints.backend.model.entity.Family();
        try {
            // 使用反射获取modelFamily的属性
            Field idField = modelFamily.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(modelFamily);
            if (idValue != null) {
                entityFamily.setId(Long.valueOf(String.valueOf(idValue)));
            }
            
            Field nameField = modelFamily.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            entityFamily.setName((String) nameField.get(modelFamily));
            
            Field ownerIdField = modelFamily.getClass().getDeclaredField("ownerId");
            ownerIdField.setAccessible(true);
            String ownerIdStr = (String) ownerIdField.get(modelFamily);
            entityFamily.setOwnerId(ownerIdStr != null ? Long.parseLong(ownerIdStr) : null);
            
            Field codeField = modelFamily.getClass().getDeclaredField("code");
            codeField.setAccessible(true);
            entityFamily.setCode((String) codeField.get(modelFamily));
            
            Field codeExpireAtField = modelFamily.getClass().getDeclaredField("codeExpireAt");
            codeExpireAtField.setAccessible(true);
            LocalDateTime codeExpireAt = (LocalDateTime) codeExpireAtField.get(modelFamily);
            entityFamily.setCodeExpiry(codeExpireAt != null ? codeExpireAt.toEpochSecond(java.time.ZoneOffset.UTC) * 1000 : null);
            
            Field createdAtField = modelFamily.getClass().getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            LocalDateTime createdAt = (LocalDateTime) createdAtField.get(modelFamily);
            entityFamily.setCreateTime(createdAt);
            entityFamily.setUpdateTime(createdAt);
        } catch (Exception e) {
          throw new BusinessException(ErrorCode.SERVER_ERROR, "转换家庭数据失败: " + e.getMessage());
      }
        
        return Optional.of(entityFamily);
    }
    
    @Override
    public Optional<com.familypoints.backend.model.entity.Family> getFamilyByCode(String code) {
        // 先查询model包下的Family
        Family modelFamily = familyMapper.selectByCode(code);
        if (modelFamily == null) {
            return Optional.empty();
        }
        
        // 转换为entity包下的Family，使用反射机制获取属性
        com.familypoints.backend.model.entity.Family entityFamily = new com.familypoints.backend.model.entity.Family();
        try {
            // 使用反射获取modelFamily的属性
            Field idField = modelFamily.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(modelFamily);
            if (idValue != null) {
                entityFamily.setId(Long.valueOf(String.valueOf(idValue)));
            }
            
            Field nameField = modelFamily.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            entityFamily.setName((String) nameField.get(modelFamily));
            
            Field ownerIdField = modelFamily.getClass().getDeclaredField("ownerId");
            ownerIdField.setAccessible(true);
            String ownerIdStr = (String) ownerIdField.get(modelFamily);
            entityFamily.setOwnerId(ownerIdStr != null ? Long.parseLong(ownerIdStr) : null);
            
            Field codeField = modelFamily.getClass().getDeclaredField("code");
            codeField.setAccessible(true);
            entityFamily.setCode((String) codeField.get(modelFamily));
            
            Field codeExpireAtField = modelFamily.getClass().getDeclaredField("codeExpireAt");
            codeExpireAtField.setAccessible(true);
            LocalDateTime codeExpireAt = (LocalDateTime) codeExpireAtField.get(modelFamily);
            entityFamily.setCodeExpiry(codeExpireAt != null ? codeExpireAt.toEpochSecond(java.time.ZoneOffset.UTC) * 1000 : null);
            
            Field createdAtField = modelFamily.getClass().getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            LocalDateTime createdAt = (LocalDateTime) createdAtField.get(modelFamily);
            entityFamily.setCreateTime(createdAt);
            entityFamily.setUpdateTime(createdAt);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "转换家庭数据失败: " + e.getMessage());
        }
        
        return Optional.of(entityFamily);
    }
    
    @Override
    public List<com.familypoints.backend.model.entity.Family> getUserFamilies(Long userId) {
        // 创建entity包下的Family列表
        List<com.familypoints.backend.model.entity.Family> entityFamilies = new ArrayList<>();
        
        // 查询model包下的Family
        try {
            // 将Long类型的userId转换为String类型后再传入方法
            Family modelFamily = familyMapper.selectFamilyByUserId(String.valueOf(userId));
            if (modelFamily != null) {
                // 转换为entity包下的Family，使用反射机制获取属性
                com.familypoints.backend.model.entity.Family entityFamily = new com.familypoints.backend.model.entity.Family();
                try {
                    // 使用反射获取modelFamily的属性
                    Field idField = modelFamily.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Object idValue = idField.get(modelFamily);
                    // 设置id值
                    Field entityIdField = entityFamily.getClass().getDeclaredField("id");
                    entityIdField.setAccessible(true);
                    entityIdField.set(entityFamily, idValue);
                    
                    // 添加到结果列表
                    entityFamilies.add(entityFamily);
                } catch (Exception ex) {
                    log.warn("转换家庭信息失败，用户ID: {}", userId, ex);
                }
            }
        } catch (Exception e) {
            log.warn("获取用户家庭信息失败，用户ID: {}", userId, e);
        }
        
        return entityFamilies;
    }
    
    @Override
    public List<com.familypoints.backend.model.entity.Member> getFamilyMembers(Long familyId) {
        // 查询model包下的Member列表
        List<Member> modelMembers = new ArrayList<>();
        try {
            // 根据MemberMapper接口定义，使用String类型参数
            modelMembers = memberMapper.selectByFamilyId(String.valueOf(familyId));
        } catch (Exception e) {
            log.warn("查询家庭成员失败，家庭ID: {}", familyId, e);
            return Collections.emptyList();
        }
        
        List<com.familypoints.backend.model.entity.Member> entityMembers = new ArrayList<>();
        
        // 转换为entity包下的Member列表
        for (Member modelMember : modelMembers) {
            com.familypoints.backend.model.entity.Member entityMember = new com.familypoints.backend.model.entity.Member();
            try {
                // 设置基本属性
                Field idField = modelMember.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                String idStr = (String) idField.get(modelMember);
                entityMember.setId(Long.parseLong(idStr));
                
                Field familyIdField = modelMember.getClass().getDeclaredField("familyId");
                familyIdField.setAccessible(true);
                String familyIdStr = (String) familyIdField.get(modelMember);
                entityMember.setFamilyId(Long.parseLong(familyIdStr));
                
                Field userIdField = modelMember.getClass().getDeclaredField("userId");
                userIdField.setAccessible(true);
                String userIdStr = (String) userIdField.get(modelMember);
                entityMember.setUserId(Long.parseLong(userIdStr));
                
                // 处理role字段转换
                Field roleField = modelMember.getClass().getDeclaredField("role");
                roleField.setAccessible(true);
                String roleStr = (String) roleField.get(modelMember);
                Integer roleInt = "parent".equals(roleStr) ? 1 : 0;
                entityMember.setRole(roleInt);
                
                Field joinedAtField = modelMember.getClass().getDeclaredField("joinedAt");
                joinedAtField.setAccessible(true);
                entityMember.setJoinTime((LocalDateTime) joinedAtField.get(modelMember));
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.SERVER_ERROR, "转换成员数据失败: " + e.getMessage());
            }
            entityMembers.add(entityMember);
        }
        
        return entityMembers;
    }
    
    @Override
    @Transactional
    public boolean leaveFamily(Long familyId, Long userId) {
        // 检查用户是否为家庭的成员
        Member member = null;
        try {
            // 根据MemberMapper接口定义，使用String类型参数
            member = memberMapper.selectByUserIdAndFamilyId(String.valueOf(userId), String.valueOf(familyId));
        } catch (Exception e) {
            log.warn("查询用户家庭关系失败，用户ID: {}, 家庭ID: {}", userId, familyId, e);
        }
        
        if (member == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "用户不在该家庭中");
        }
        
        // 删除用户在该家庭中的记录
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        try {
            // 使用QueryWrapper删除记录，确保使用String类型参数
            queryWrapper.eq("user_id", String.valueOf(userId)).eq("family_id", String.valueOf(familyId));
            memberMapper.delete(queryWrapper);
        } catch (Exception e) {
            log.warn("删除用户家庭关系失败，用户ID: {}, 家庭ID: {}", userId, familyId, e);
            throw new BusinessException(ErrorCode.SERVER_ERROR, "退出家庭失败");
        }
        
        return true;
    }
    
    @Override
    @Transactional
    public String generateNewFamilyCode(Long familyId) {
        // 生成新的家庭码和过期时间
        String newFamilyCode = familyCodeGenerator.generateFamilyCode();
        LocalDateTime expiryDateTime = familyCodeGenerator.calculateExpiryTime();
        
        // 更新家庭码，使用反射机制设置属性
        Family family = new Family();
        try {
            // 使用反射设置属性值，避免调用可能不存在的setter方法
            Field idField = family.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            // 直接使用String类型以避免类型转换问题
            idField.set(family, String.valueOf(familyId));
            
            Field codeField = family.getClass().getDeclaredField("code");
            codeField.setAccessible(true);
            codeField.set(family, newFamilyCode);
            
            Field codeExpireAtField = family.getClass().getDeclaredField("codeExpireAt");
            codeExpireAtField.setAccessible(true);
            codeExpireAtField.set(family, expiryDateTime);
            
            // 检查是否有updateTime字段
            try {
                Field updateTimeField = family.getClass().getDeclaredField("updateTime");
                updateTimeField.setAccessible(true);
                updateTimeField.set(family, LocalDateTime.now());
            } catch (NoSuchFieldException e) {
                // 如果没有updateTime字段，忽略这个错误
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "更新家庭码失败: " + e.getMessage());
        }
        
        // 更新家庭码
        try {
            // 尝试使用Long类型更新
            if (familyMapper.updateById(family) == 0) {
                // 如果失败，重新构建Family对象并使用String类型ID
                Family stringFamily = new Family();
                Field idField = stringFamily.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(stringFamily, String.valueOf(familyId));
                
                Field codeField = stringFamily.getClass().getDeclaredField("code");
                codeField.setAccessible(true);
                codeField.set(stringFamily, newFamilyCode);
                
                Field codeExpireAtField = stringFamily.getClass().getDeclaredField("codeExpireAt");
                codeExpireAtField.setAccessible(true);
                codeExpireAtField.set(stringFamily, expiryDateTime);
                
                familyMapper.updateById(stringFamily);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "更新家庭码失败: " + e.getMessage());
        }
        
        return newFamilyCode;
    }
    
    /**
     * 检查用户是否为家庭管理员
     */
    @Override
    public boolean isFamilyAdmin(Long familyId, Long userId) {
        Member member = null;
        try {
            // 直接使用String类型参数调用MemberMapper
            member = memberMapper.selectByUserIdAndFamilyId(String.valueOf(userId), String.valueOf(familyId));
        } catch (Exception e) {
            log.warn("查询用户家庭关系失败，用户ID: {}, 家庭ID: {}", userId, familyId, e);
            return false;
        }
        
        // 成员不存在，不是管理员
        if (member == null) {
            return false;
        }
        
        try {
            // 获取role属性
            Object role = member.getClass().getDeclaredField("role").get(member);
            // 支持String和Long类型的role值
            if (role != null) {
                String roleStr = role.toString();
                return "parent".equals(roleStr) || "1".equals(roleStr);
            }
            return false;
        } catch (Exception e) {
            log.warn("获取成员角色失败，用户ID: {}, 家庭ID: {}", userId, familyId, e);
            return false;
        }
    }
    
    /**
     * 获取家庭的管理员ID
     */
    @Override
    public Long getFamilyAdminId(Long familyId) {
        try {
            // 查询家庭信息获取ownerId
            Family family = familyMapper.selectById(familyId);
            if (family != null) {
                // 使用反射获取ownerId
                Field ownerIdField = family.getClass().getDeclaredField("ownerId");
                ownerIdField.setAccessible(true);
                String ownerId = (String) ownerIdField.get(family);
                return ownerId != null ? Long.valueOf(ownerId) : null;
            }
        } catch (Exception e) {
            log.warn("获取家庭管理员ID失败，家庭ID: {}", familyId, e);
        }
        return null;
    }
}