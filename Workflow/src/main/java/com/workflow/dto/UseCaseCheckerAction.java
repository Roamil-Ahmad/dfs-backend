package com.workflow.dto;

public class UseCaseCheckerAction {

    private UseCaseRequest useCaseRequest;

    private McActionRequest mcActionRequest;

    public UseCaseRequest getUseCaseRequest() {
        return useCaseRequest;
    }

    public void setUseCaseRequest(UseCaseRequest useCaseRequest) {
        this.useCaseRequest = useCaseRequest;
    }

    public McActionRequest getMcActionRequest() {
        return mcActionRequest;
    }

    public void setMcActionRequest(McActionRequest mcActionRequest) {
        this.mcActionRequest = mcActionRequest;
    }
}