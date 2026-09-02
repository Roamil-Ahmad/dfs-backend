package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"java:S100","java:S116","java:S117","java:S101"})
public class PERSON_DATA {
    @JsonProperty("MOTHER_NAME")
    private String MOTHER_NAME;
    @JsonProperty("PRESENT_ADDRESS")
    private String PRESENT_ADDRESS;
    @JsonProperty("GENDER")
    private  String GENDER;
    @JsonProperty("BIRTH_PLACE")
    private String BIRTH_PLACE;
    @JsonProperty("DATE_OF_BIRTH")
    private String DATE_OF_BIRTH;
    @JsonProperty("EXPIRY_DATE")
    private String EXPIRY_DATE;
    @JsonProperty("NAME")
    private String NAME;

    public String getMOTHER_NAME() {
        return MOTHER_NAME;
    }

    public void setMOTHER_NAME(String MOTHER_NAME) {
        this.MOTHER_NAME = MOTHER_NAME;
    }

    public String getPRESENT_ADDRESS() {
        return PRESENT_ADDRESS;
    }

    public void setPRESENT_ADDRESS(String PRESENT_ADDRESS) {
        this.PRESENT_ADDRESS = PRESENT_ADDRESS;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getBIRTH_PLACE() {
        return BIRTH_PLACE;
    }

    public void setBIRTH_PLACE(String BIRTH_PLACE) {
        this.BIRTH_PLACE = BIRTH_PLACE;
    }

    public String getDATE_OF_BIRTH() {
        return DATE_OF_BIRTH;
    }

    public void setDATE_OF_BIRTH(String DATE_OF_BIRTH) {
        this.DATE_OF_BIRTH = DATE_OF_BIRTH;
    }

    public String getEXPIRY_DATE() {
        return EXPIRY_DATE;
    }

    public void setEXPIRY_DATE(String EXPIRY_DATE) {
        this.EXPIRY_DATE = EXPIRY_DATE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
