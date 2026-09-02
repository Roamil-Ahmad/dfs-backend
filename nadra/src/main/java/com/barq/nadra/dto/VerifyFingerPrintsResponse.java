package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"java:S117","java:S100","java:S101","java:S116"})
public class VerifyFingerPrintsResponse {
    @JsonProperty("BIOMETRIC_VERIFICATION")
    private BIOMETRICVERIFICATION bIOMETRIC_VERIFICATION;

    public BIOMETRICVERIFICATION getbIOMETRIC_VERIFICATION() {
        return bIOMETRIC_VERIFICATION;
    }

    public void setbIOMETRIC_VERIFICATION(BIOMETRICVERIFICATION bIOMETRIC_VERIFICATION) {
        this.bIOMETRIC_VERIFICATION = bIOMETRIC_VERIFICATION;
    }
}
