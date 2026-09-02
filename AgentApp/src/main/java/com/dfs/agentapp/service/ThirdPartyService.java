package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.common.*;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ThirdPartyService {
    Response generateOtp(String mobileNumber, String email, String type, String identifier, String docTypeCode, String userType, Request request, String authToken) throws JsonProcessingException;

    Response verifyOtp(VerifyOtpRequest verifyOtpRequest, Request apiRequest, String token) throws JsonProcessingException;
    /**
     * Verifies a CNIC against the nadra service before the registration OTP is issued.
     * Returns the service's own envelope; responsecode "000" means verified.
     */
    Response verifyCnic(String cnic, String mobileNumber, String cnicIssuanceDate, String authToken) throws JsonProcessingException;

}
