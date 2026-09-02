package com.dfs.backoffice.dto;

import java.math.BigDecimal;

public class AgentApprovalRequest {
    private Long agentId;
    private BigDecimal statusId;
    private McActionRequest mcActionRequest;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public McActionRequest getMcActionRequest() {
        return mcActionRequest;
    }

    public void setMcActionRequest(McActionRequest mcActionRequest) {
        this.mcActionRequest = mcActionRequest;
    }

    public BigDecimal getStatusId() {
        return statusId;
    }

    public void setStatusId(BigDecimal statusId) {
        this.statusId = statusId;
    }
}
