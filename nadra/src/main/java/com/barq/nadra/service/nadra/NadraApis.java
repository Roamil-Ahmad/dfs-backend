package com.barq.nadra.service.nadra;

import com.barq.nadra.dto.NadraVerificationRequest;
import com.barq.nadra.dto.NadraVerificationResponse;
import com.barq.nadra.dto.Response;
import com.barq.nadra.dto.VerifyFingerPrintRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface NadraApis {

    NadraVerificationResponse checkNadraExistance(NadraVerificationRequest nadraVerificationRequest);


    NadraVerificationResponse checkNadraRecord(NadraVerificationRequest nadraVerificationRequest, BigDecimal userId, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    Response inAppNadraBvs(VerifyFingerPrintRequest verifyFingerPrintRequest, BigDecimal loggedUserDetail, HttpServletRequest request) throws JsonProcessingException;
}
