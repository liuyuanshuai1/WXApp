package com.familypoints.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.familypoints.backend.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成员Mapper接口
 * 用于对member表进行数据库操作
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 根据家庭ID查询所有成员
     * @param familyId 家庭ID
     * @return 成员列表
     */
    List<Member> selectByFamilyId(String familyId);

    /**
     * 根据用户ID查询成员信息
     * @param userId 用户ID
     * @return 成员信息
     */
    Member selectByUserId(String userId);

    /**
     * 查询用户在指定家庭中的成员信息
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 成员信息
     */
    Member selectByUserIdAndFamilyId(@Param("userId") String userId, @Param("familyId") String familyId);

    /**
     * 查询家庭中的成员数量
     * @param familyId 家庭ID
     * @return 成员数量
     */
    int countByFamilyId(String familyId);

}