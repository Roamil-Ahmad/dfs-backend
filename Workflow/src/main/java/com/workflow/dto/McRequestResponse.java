package com.workflow.dto;

import java.math.BigDecimal;

public class McRequestResponse {

    private BigDecimal requestId;
    private String formName;
    private String makeName;
    private String makeDate;
    private String makerComments;
    private String status;
    private BigDecimal updateIndex;


    public BigDecimal getRequestId() {
        return requestId;
    }

    public void setRequestId(BigDecimal requestId) {
        this.requestId = requestId;
    }


    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(String makeDate) {
        this.makeDate = makeDate;
    }

    public String getMakerComments() {
        return makerComments;
    }

    public void setMakerComments(String makerComments) {
        this.makerComments = makerComments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getUpdateIndex() {
        return updateIndex;
    }

    public void setUpdateIndex(BigDecimal updateIndex) {
        this.updateIndex = updateIndex;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
