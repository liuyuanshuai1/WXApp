package com.familypoints.backend.dto;

import lombok.Data;
import java.util.List;

/**
 * 任务相关DTO类
 */

/**
 * 任务列表项DTO
 */
@Data
public class TaskItemDTO {
    private String taskId;
    private String title;
    private Integer points;
    private String status;
    private String description;
    private String deadline;
    private String type;
    private Boolean repeatable;
}