package com.workflow.dto;

public class McActionResponse {

    private String requestStatus;
    private int status;
    private String statusDecsr;

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDecsr() {
        return statusDecsr;
    }

    public void setStatusDecsr(String statusDecsr) {
        this.statusDecsr = statusDecsr;
    }
}
