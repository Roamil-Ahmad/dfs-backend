package com.workflow.dto;

public class McFormName {
    private String tableName;
    private String formName;
    private String viewDetailUrl;
    private String editDetailUrl;
    private String requestType;

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

    public String getViewDetailUrl() {
        return viewDetailUrl;
    }

    public void setViewDetailUrl(String viewDetailUrl) {
        this.viewDetailUrl = viewDetailUrl;
    }

    public String getEditDetailUrl() {
        return editDetailUrl;
    }

    public void setEditDetailUrl(String editDetailUrl) {
        this.editDetailUrl = editDetailUrl;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
