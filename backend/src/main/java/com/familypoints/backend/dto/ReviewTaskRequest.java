package com.familypoints.backend.dto;

import lombok.Data;

/**
 * 审核任务请求DTO
 * 用于接收审核任务提交的请求参数
 */
@Data
public class ReviewTaskRequest {
    /**
     * 提交记录ID
     */
    private Long submissionId;
    
    /**
     * 审核结果：true-通过，false-拒绝
     */
    private Boolean approved;
    
    /**
     * 审核意见
     */
    private String reviewComment;
    
    // Getter and Setter methods
    public Long getSubmissionId() {
        return submissionId;
    }
    
    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }
    
    public Boolean getApproved() {
        return approved;
    }
    
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
    
    public String getReviewComment() {
        return reviewComment;
    }
    
    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}