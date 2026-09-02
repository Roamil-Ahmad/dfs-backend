/*
Author Name:muhammad.kashif

Project Name: Workflow

Package Name:com.workflow.workflow.Dto

Class Name: McConfigRequest

Date and Time:3/13/2023 5:00 PM

Version:1.0

*/
package com.workflow.dto;

public class McConfigRequest {

    private long mcConfigId;
    private String configDescription;
    private String configName;
    private String tableName;
    private String formName;
    private String requestType;
    private String isActive;

    public long getMcConfigId() {
        return mcConfigId;
    }

    public void setMcConfigId(long mcConfigId) {
        this.mcConfigId = mcConfigId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}