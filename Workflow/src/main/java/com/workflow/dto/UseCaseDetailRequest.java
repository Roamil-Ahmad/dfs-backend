package com.workflow.dto;

public class UseCaseDetailRequest {
    private long mcConfigDetailId;
    private String approvalType;
    private String approvalCriteria;
    private String intimateOnly;
    private String userId;
    private String roleId;
    private String isActive;
    private long emailTemplateId;

    public long getMcConfigDetailId() {
        return mcConfigDetailId;
    }

    public void setMcConfigDetailId(long mcConfigDetailId) {
        this.mcConfigDetailId = mcConfigDetailId;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public String getIntimateOnly() {
        return intimateOnly;
    }

    public void setIntimateOnly(String intimateOnly) {
        this.intimateOnly = intimateOnly;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getApprovalCriteria() {
        return approvalCriteria;
    }

    public void setApprovalCriteria(String approvalCriteria) {
        this.approvalCriteria = approvalCriteria;
    }

    public long getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(long emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
