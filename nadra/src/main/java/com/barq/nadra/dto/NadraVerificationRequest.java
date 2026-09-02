/*Author Name:ahmed.sufyan

Project Name: NadraIntegration

Package Name:com.mfs.NadraIntegration.Dto

Class Name: NadraIntegrationRequest

Date and Time:2/8/2023 12:11 PM

Version:1.0*/
package com.barq.nadra.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;

import java.util.Date;

public class NadraVerificationRequest {

    private String cnic;
    private String mobileNumber;
    /**
     * Sent by callers as a plain "yyyy-MM-dd" string (mobileRegistration's nidIssuanceDate).
     * Optional: callers that do not collect one omit it, and the service falls back to a full
     * verification instead of the issuance-date-qualified TBL_NID_DATA lookup.
     *
     * lenient = FALSE matters: with Jackson's default leniency "15-01-2020" is not rejected, it is
     * rolled over into a nonsense date and silently written to TBL_NID_DATA.ISSUANCE_DATE.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
    private Date cnicIssuanceDate;

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

    public Date getCnicIssuanceDate() {
        return cnicIssuanceDate;
    }

    public void setCnicIssuanceDate(Date cnicIssuanceDate) {
        this.cnicIssuanceDate = cnicIssuanceDate;
    }
}
