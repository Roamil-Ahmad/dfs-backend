package com.workflow.dto;

import java.math.BigDecimal;

public class McActionRequest {

    private String mcRequestId;
    private String mcPeindingRequestId;
    private String checkerId;
    private String checkerComments;
    private String action;
    private BigDecimal updatedIndex;

    public String getMcRequestId() {
        return mcRequestId;
    }

    public void setMcRequestId(String mcRequestId) {
        this.mcRequestId = mcRequestId;
    }

    public String getMcPeindingRequestId() {
        return mcPeindingRequestId;
    }

    public void setMcPeindingRequestId(String mcPeindingRequestId) {
        this.mcPeindingRequestId = mcPeindingRequestId;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getCheckerComments() {
        return checkerComments;
    }

    public void setCheckerComments(String checkerComments) {
        this.checkerComments = checkerComments;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BigDecimal getUpdatedIndex() {
        return updatedIndex;
    }

    public void setUpdatedIndex(BigDecimal updatedIndex) {
        this.updatedIndex = updatedIndex;
    }
}
