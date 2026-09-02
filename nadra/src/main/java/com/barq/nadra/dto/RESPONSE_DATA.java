package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
@SuppressWarnings({"java:S117","java:S100","java:S101","java:S116"})
public class RESPONSE_DATA {
    @JsonProperty("RESPONSE_STATUS")
    private RESPONSE_STATUS responseStatus;
    @JsonProperty("CITIZEN_NUMBER")
    private long citizenNumber;
    @JsonProperty("PERSON_DATA")
    private PERSON_DATA personData;
    @JsonProperty("SESSION_ID")
    private String SESSION_ID;

    public RESPONSE_STATUS getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(RESPONSE_STATUS responseStatus) {
        this.responseStatus = responseStatus;
    }

    public long getCitizenNumber() {
        return citizenNumber;
    }

    public void setCitizenNumber(long citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

    public PERSON_DATA getPersonData() {
        return personData;
    }

    public void setPersonData(PERSON_DATA personData) {
        this.personData = personData;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }
}
