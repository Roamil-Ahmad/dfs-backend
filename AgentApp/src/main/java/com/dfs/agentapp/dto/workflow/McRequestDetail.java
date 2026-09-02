/*Author Name:muhammad.anas
Project Name: Lending
Package Name:com.workflow.lending.dto
Class Name: McRequestDetail
Date and Time:12/6/2023 6:21 PM
Version:1.0*/
package com.dfs.agentapp.dto.workflow;

public class McRequestDetail {
    private String makerId;
    private String makerComments;
    private String ftFlag;
    private String requestType;
    private String updateType;
    private String updateJson;
    private String refTableId;
    private Object oldJson;

    public String getMakerComments() {
        return makerComments;
    }

    public void setMakerComments(String makerComments) {
        this.makerComments = makerComments;
    }

    public String getFtFlag() {
        return ftFlag;
    }

    public void setFtFlag(String ftFlag) {
        this.ftFlag = ftFlag;
    }


    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUpdateJson() {
        return updateJson;
    }

    public void setUpdateJson(String updateJson) {
        this.updateJson = updateJson;
    }


    public String getMakerId() {
        return makerId;
    }

    public void setMakerId(String makerId) {
        this.makerId = makerId;
    }

    public String getRefTableId() {
        return refTableId;
    }

    public void setRefTableId(String refTableId) {
        this.refTableId = refTableId;
    }

    public Object getOldJson() {
        return oldJson;
    }

    public void setOldJson(Object oldJson) {
        this.oldJson = oldJson;
    }

    // Parameterized constructor
    public McRequestDetail(String makerId, String makerComments, String ftFlag,
                           String requestType, String updateType, String updateJson, String refTableId, String oldJson) {
        this.makerId = makerId;
        this.makerComments = makerComments;
        this.ftFlag = ftFlag;
        this.requestType = requestType;
        this.updateType = updateType;
        this.updateJson = updateJson;
        this.refTableId = refTableId;
        this.oldJson = oldJson;
    }


}