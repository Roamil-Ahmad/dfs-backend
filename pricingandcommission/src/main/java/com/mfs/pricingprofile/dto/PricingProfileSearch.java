package com.mfs.pricingprofile.dto;

public class PricingProfileSearch {
    private String chargesProfileName;
    private String transTypeId;
    private String fromDate;
    private String toDate;

    public String getChargesProfileName() {
        return chargesProfileName;
    }

    public void setChargesProfileName(String chargesProfileName) {
        this.chargesProfileName = chargesProfileName;
    }

    public String getTransTypeId() {
        return transTypeId;
    }

    public void setTransTypeId(String transTypeId) {
        this.transTypeId = transTypeId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
