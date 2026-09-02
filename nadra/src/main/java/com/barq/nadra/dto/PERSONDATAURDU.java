package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PERSONDATAURDU {
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("PRESENT_ADDRESS")
    private String presentAddress;
    @JsonProperty("BIRTH_PLACE")
    private String birthPlace;
    @JsonProperty("MOTHER_NAME")
    private String motherName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }
}
