/*
Author Name:muhammad.kashif

Project Name: Configurations

Package Name:com.mfs.configurations.dto

Class Name: HraAccountData

Date and Time:7/17/2023 3:43 PM

Version:1.0

*/
package com.mfs.pricingprofile.dto;

public class CommissionProfileData {

    private Object tblTransDocs;

    private Object glAccounts;

    private Object agentClass;

    public Object getTblTransDocs() {
        return tblTransDocs;
    }

    public void setTblTransDocs(Object tblTransDocs) {
        this.tblTransDocs = tblTransDocs;
    }

    public Object getGlAccounts() {
        return glAccounts;
    }

    public void setGlAccounts(Object glAccounts) {
        this.glAccounts = glAccounts;
    }

    public Object getAgentClass() {
        return agentClass;
    }

    public void setAgentClass(Object agentClass) {
        this.agentClass = agentClass;
    }
}