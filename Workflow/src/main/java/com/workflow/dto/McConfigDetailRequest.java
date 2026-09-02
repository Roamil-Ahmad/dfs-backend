/*
Author Name:muhammad.kashif

Project Name: Workflow

Package Name:com.workflow.workflow.Dto

Class Name: McConfigDetail

Date and Time:3/13/2023 5:23 PM

Version:1.0

*/
package com.workflow.dto;

public class McConfigDetailRequest {

    private long mcConfigDetailId;
    private long mcConfigId;
    private String seq;
    private String userId;
    private String roleId;
    private String createuser;

    public long getMcConfigDetailId() {
        return mcConfigDetailId;
    }

    public void setMcConfigDetailId(long mcConfigDetailId) {
        this.mcConfigDetailId = mcConfigDetailId;
    }

    public long getMcConfigId() {
        return mcConfigId;
    }

    public void setMcConfigId(long mcConfigId) {
        this.mcConfigId = mcConfigId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
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

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }
}