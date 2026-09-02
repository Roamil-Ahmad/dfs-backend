package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"java:S117","java:S100","java:S101","java:S116"})
public class Root {
    @JsonProperty("BIOMETRIC_VERIFICATION")
    public BIOMETRIC_VERIFICATION BIOMETRIC_VERIFICATION;

    public com.barq.nadra.dto.BIOMETRIC_VERIFICATION getBIOMETRIC_VERIFICATION() {
        return BIOMETRIC_VERIFICATION;
    }

    public void setBIOMETRIC_VERIFICATION(com.barq.nadra.dto.BIOMETRIC_VERIFICATION BIOMETRIC_VERIFICATION) {
        this.BIOMETRIC_VERIFICATION = BIOMETRIC_VERIFICATION;
    }
}
