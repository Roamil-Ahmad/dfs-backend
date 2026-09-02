package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RESPONSEDATA {
    @JsonProperty("RESPONSE_STATUS")
    private RESPONSESTATUS responsestatus;
    @JsonProperty("SESSION_ID")
    private String sessionId;
    @JsonProperty("CITIZEN_NUMBER")
    private String citizenNumber;
    @JsonProperty("PERSON_DATA")
    private PERSONDATA personData;
    @JsonProperty("PERSON_DATA_URDU")
    private PERSONDATAURDU personDataurdu;
    @JsonProperty("SECONDRY_CITIZEN_NUMBER")
    private String secondaryCitizenNumber;
    @JsonProperty("SECONDRY_CITIZEN_URDU_NAME")
    private String secondaryCitizenUrduName;

    public RESPONSESTATUS getResponsestatus() {
        return responsestatus;
    }

    public void setResponsestatus(RESPONSESTATUS responsestatus) {
        this.responsestatus = responsestatus;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCitizenNumber() {
        return citizenNumber;
    }

    public void setCitizenNumber(String citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

    public PERSONDATA getPersonData() {
        return personData;
    }

    public void setPersonData(PERSONDATA personData) {
        this.personData = personData;
    }

    public PERSONDATAURDU getPersonDataurdu() {
        return personDataurdu;
    }

    public void setPersonDataurdu(PERSONDATAURDU personDataurdu) {
        this.personDataurdu = personDataurdu;
    }

    public String getSecondaryCitizenNumber() {
        return secondaryCitizenNumber;
    }

    public void setSecondaryCitizenNumber(String secondaryCitizenNumber) {
        this.secondaryCitizenNumber = secondaryCitizenNumber;
    }

    public String getSecondaryCitizenUrduName() {
        return secondaryCitizenUrduName;
    }

    public void setSecondaryCitizenUrduName(String secondaryCitizenUrduName) {
        this.secondaryCitizenUrduName = secondaryCitizenUrduName;
    }
}
