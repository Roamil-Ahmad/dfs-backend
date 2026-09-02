package com.mfs.pricingprofile.dto;

public class ProcedureResponse {

    private int status;
    private String statusDescr;
    private int mcApplicability;


    public String getStatusDescr() {
        return statusDescr;
    }

    public void setStatusDescr(String statusDescr) {
        this.statusDescr = statusDescr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMcApplicability() {
        return mcApplicability;
    }

    public void setMcApplicability(int mcApplicability) {
        this.mcApplicability = mcApplicability;
    }
}
