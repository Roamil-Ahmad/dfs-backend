package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PERSONDATA {
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("PRESENT_ADDRESS")
    private String presentAddress;
    @JsonProperty("DATE_OF_BIRTH")
    private String dateOfBirth;
    @JsonProperty("GENDER")
    private String gender;
    @JsonProperty("BIRTH_PLACE")
    private String birthPlace;
    @JsonProperty("MOTHER_NAME")
    private String motherName;
    @JsonProperty("EXPIRY_DATE")
    private String expiryDate;

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
