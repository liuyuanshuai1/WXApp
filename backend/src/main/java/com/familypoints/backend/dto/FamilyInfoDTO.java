package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 家庭相关DTO类
 */

/**
 * 家庭信息DTO
 */
@Data
public class FamilyInfoDTO {
    private String familyId;
    private String name;
    private String code;
    private String ownerId;
    private Integer maxMembers;
    private String codeExpireAt;
    private String createdAt;
}