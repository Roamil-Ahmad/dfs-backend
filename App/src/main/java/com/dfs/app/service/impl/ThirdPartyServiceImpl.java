package com.dfs.app.service.impl;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.ValidateGoogleCode;
import com.dfs.app.dto.common.*;
import com.dfs.app.dto.NadraVerificationRequest;
import com.dfs.app.service.ThirdPartyService;
import com.dfs.app.util.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartyServiceImpl extends HelperClass implements ThirdPartyService {
    @Value("${generate.otp.url}")
    private String generateOtpUrl;
    @Value("${verify.otp.url}")
    private String verifyOtpUrl;
    @Value("${nadra.cnic.verification.url}")
    private String nadraCnicVerificationUrl;

    @Override
    public Response generateOtp(String mobileNumber, String email, String type, String identifier, String docTypeCode, String userType, Request request, String authToken) throws JsonProcessingException {
        GenerateOtpRequest generateOtpRequest = new GenerateOtpRequest();
        generateOtpRequest.setEmail(email);
        generateOtpRequest.setMobileNumber(mobileNumber);
        generateOtpRequest.setOtpType(type);
        generateOtpRequest.setUserType(userType);
        generateOtpRequest.setIdentifier(identifier);
        generateOtpRequest.setSmsTemplateCode(docTypeCode);
        request.setPayload(generateOtpRequest);

        return sendRequestAndGetResponse(request, authToken, generateOtpUrl);

    }

    @Override
    public Response verifyOtp(VerifyOtpRequest verifyOtpRequest, Request request, String authToken) throws JsonProcessingException {
        request.setPayload(verifyOtpRequest);
        return sendRequestAndGetResponse(request, authToken, verifyOtpUrl);

    }

    @Override
    public Object generateQr(String url) {
        return getResponseFromGetAPI(url);
    }

    @Override
    public Response validateGooleCode(ValidateGoogleCode validateGoogleCode, Request request, String url) throws JsonProcessingException {
        return sendRequestAndGetResponseGooleAuthentiocationService(validateGoogleCode, "", url);
    }

    @Override
    public Response verifyCnic(String cnic, String mobileNumber, String cnicIssuanceDate, String authToken) throws JsonProcessingException {
        // The nadra endpoint consumes a plain body rather than the Request envelope the other
        // downstream services use, so the DTO is posted directly.
        NadraVerificationRequest nadraVerificationRequest = new NadraVerificationRequest();
        nadraVerificationRequest.setCnic(cnic);
        nadraVerificationRequest.setMobileNumber(mobileNumber);
        nadraVerificationRequest.setCnicIssuanceDate(cnicIssuanceDate);
        String result = getResponseFromPostAPI(createHeaderMapBackOffice(authToken), nadraVerificationRequest, nadraCnicVerificationUrl);
        return fromJson(result, Response.class);
    }

}
