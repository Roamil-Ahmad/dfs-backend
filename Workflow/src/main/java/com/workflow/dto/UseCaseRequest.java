package com.workflow.dto;

import java.util.List;

public class UseCaseRequest {

    private long mcConfigId;
    private String useCaseName;
    private String useCaseDescr;
    private String useCaseComments;
    private String isActive;
    private Long statusId;
    private McFormName processName;
    List<UseCaseDetailRequest> useCaseDetailRequests;

    public long getMcConfigId() {
        return mcConfigId;
    }

    public void setMcConfigId(long mcConfigId) {
        this.mcConfigId = mcConfigId;
    }

    public String getUseCaseName() {
        return useCaseName;
    }

    public void setUseCaseName(String useCaseName) {
        this.useCaseName = useCaseName;
    }

    public String getUseCaseDescr() {
        return useCaseDescr;
    }

    public void setUseCaseDescr(String useCaseDescr) {
        this.useCaseDescr = useCaseDescr;
    }

    public String getUseCaseComments() {
        return useCaseComments;
    }

    public void setUseCaseComments(String useCaseComments) {
        this.useCaseComments = useCaseComments;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public McFormName getProcessName() {
        return processName;
    }

    public void setProcessName(McFormName processName) {
        this.processName = processName;
    }

    public List<UseCaseDetailRequest> getUseCaseDetailRequests() {
        return useCaseDetailRequests;
    }

    public void setUseCaseDetailRequests(List<UseCaseDetailRequest> useCaseDetailRequests) {
        this.useCaseDetailRequests = useCaseDetailRequests;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}
