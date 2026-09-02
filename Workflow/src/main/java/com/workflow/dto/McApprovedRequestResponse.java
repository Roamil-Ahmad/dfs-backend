package com.workflow.dto;

public class McApprovedRequestResponse {

    private String formName;
    private String makeName;
    private String makeDate;
    private String makerComments;
    private String checkerName;
    private String checkDate;
    private String checkerComments;
    private String status;

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

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckerComments() {
        return checkerComments;
    }

    public void setCheckerComments(String checkerComments) {
        this.checkerComments = checkerComments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
