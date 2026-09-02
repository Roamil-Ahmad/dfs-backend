package com.workflow.dto;

public class McRequestDto {

    private String formName;
    private String makerId;
    private String makerComments;
    private String ftFlag;
    private String tableName;
    private String requestType;
    private String updateType;
    private String updateJson;
    private String oldJson;
    private String refTableId;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getMakerId() {
        return makerId;
    }

    public void setMakerId(String makerId) {
        this.makerId = makerId;
    }

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public String getOldJson() {
        return oldJson;
    }

    public void setOldJson(String oldJson) {
        this.oldJson = oldJson;
    }

    public String getRefTableId() {
        return refTableId;
    }

    public void setRefTableId(String refTableId) {
        this.refTableId = refTableId;
    }

}
