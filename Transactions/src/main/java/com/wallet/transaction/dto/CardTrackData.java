package com.wallet.transaction.dto;

public class CardTrackData {
    private String pan;
    private String expiry;
    private String serviceCode;
    private String firstName;
    private String lastName;
    private String discretionaryData;
    private String track2data;
    // getters & setters\\\


    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDiscretionaryData() {
        return discretionaryData;
    }

    public void setDiscretionaryData(String discretionaryData) {
        this.discretionaryData = discretionaryData;
    }

    public String getTrack2data() {
        return track2data;
    }

    public void setTrack2data(String track2data) {
        this.track2data = track2data;
    }
}