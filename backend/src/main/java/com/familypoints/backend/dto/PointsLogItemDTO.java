package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 奖励相关DTO类
 */

/**
 * 积分流水项DTO
 */
@Data
public class PointsLogItemDTO {
    private String source;
    private String remark;
    private Integer amount;
    private String time;
    private String type; // income | expense
}