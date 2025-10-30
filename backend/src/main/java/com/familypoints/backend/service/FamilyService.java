package com.familypoints.backend.service;

import com.familypoints.backend.dto.CreateFamilyRequest;
import com.familypoints.backend.dto.JoinFamilyRequest;
import com.familypoints.backend.dto.CreateFamilyResponse;
import com.familypoints.backend.model.entity.Family;
import com.familypoints.backend.model.entity.Member;
import java.util.List;
import java.util.Optional;

/**
 * 家庭服务接口
 * 提供家庭相关的业务逻辑操作
 */
public interface FamilyService {
    
    /**
     * 创建家庭
     * @param request 创建家庭请求
     * @param userId 创建者用户ID
     * @return 创建家庭响应
     */
    CreateFamilyResponse createFamily(CreateFamilyRequest request, Long userId);
    
    /**
     * 加入家庭
     * @param request 加入家庭请求
     * @param userId 用户ID
     * @return 是否加入成功
     */
    boolean joinFamily(JoinFamilyRequest request, Long userId);
    
    /**
     * 通过家庭ID获取家庭信息
     * @param familyId 家庭ID
     * @return 家庭对象
     */
    Optional<Family> getFamilyById(Long familyId);
    
    /**
     * 通过家庭码获取家庭信息
     * @param code 家庭码
     * @return 家庭对象
     */
    Optional<Family> getFamilyByCode(String code);
    
    /**
     * 获取用户所在的所有家庭
     * @param userId 用户ID
     * @return 家庭列表
     */
    List<Family> getUserFamilies(Long userId);
    
    /**
     * 获取家庭的所有成员
     * @param familyId 家庭ID
     * @return 成员列表
     */
    List<Member> getFamilyMembers(Long familyId);
    
    /**
     * 退出家庭
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 是否退出成功
     */
    boolean leaveFamily(Long familyId, Long userId);
    
    /**
     * 生成新的家庭码
     * @param familyId 家庭ID
     * @return 新的家庭码
     */
    String generateNewFamilyCode(Long familyId);
    
    /**
     * 检查用户是否为家庭的管理员
     * @param familyId 家庭ID
     * @param userId 用户ID
     * @return 是否为管理员
     */
    boolean isFamilyAdmin(Long familyId, Long userId);
    
    /**
     * 获取家庭的管理员ID
     * @param familyId 家庭ID
     * @return 管理员用户ID
     */
    Long getFamilyAdminId(Long familyId);
}