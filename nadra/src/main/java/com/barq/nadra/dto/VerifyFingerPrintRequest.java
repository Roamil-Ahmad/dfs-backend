package com.barq.nadra.dto;

import java.util.List;

public class VerifyFingerPrintRequest {

    private String cnic;
    private String mobileNumber;
    private String templateType;
    private String areaName;
    private List<CustomerFpData> customerFpData;

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<CustomerFpData> getCustomerFpData() {
        return customerFpData;
    }

    public void setCustomerFpData(List<CustomerFpData> customerFpData) {
        this.customerFpData = customerFpData;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
