package com.workflow.dto;

import java.math.BigDecimal;

public class AuthorizationHistoryResponse {

    private BigDecimal escalationLevel;
    private String checkedBy;
    private String checkedOn;
    private String authorizerComments;
    private String intimationOn;
    private String intimationTo;
    private String authorizationStatus;
    private String maker;
    private BigDecimal mcRequestId;

    public BigDecimal getEscalationLevel() {
        return escalationLevel;
    }

    public void setEscalationLevel(BigDecimal escalationLevel) {
        this.escalationLevel = escalationLevel;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getCheckedOn() {
        return checkedOn;
    }

    public void setCheckedOn(String checkedOn) {
        this.checkedOn = checkedOn;
    }

    public String getAuthorizerComments() {
        return authorizerComments;
    }

    public void setAuthorizerComments(String authorizerComments) {
        this.authorizerComments = authorizerComments;
    }

    public String getIntimationOn() {
        return intimationOn;
    }

    public void setIntimationOn(String intimationOn) {
        this.intimationOn = intimationOn;
    }

    public String getIntimationTo() {
        return intimationTo;
    }

    public void setIntimationTo(String intimationTo) {
        this.intimationTo = intimationTo;
    }

    public String getAuthorizationStatus() {
        return authorizationStatus;
    }

    public void setAuthorizationStatus(String authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public BigDecimal getMcRequestId() {
        return mcRequestId;
    }

    public void setMcRequestId(BigDecimal mcRequestId) {
        this.mcRequestId = mcRequestId;
    }
}
