package com.barq.nadra.controller;

import com.barq.nadra.dto.*;
import com.barq.nadra.service.nadra.NadraApis;
import com.barq.nadra.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

@CrossOrigin(origins = Constants.S_1, allowedHeaders = Constants.S_1)
@RestController
@RestControllerAdvice
public class NadraPostApis extends HelperClass {

    @Autowired
    private NadraApis nadraApis;

    @PostMapping("/v1/cnicVerification")
    public ResponseEntity<HashMap<String, Object>> cnicVerification(@RequestBody NadraVerificationRequest nadraVerificationRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        NadraVerificationResponse nadraCheck = nadraApis.checkNadraExistance(nadraVerificationRequest);
        if (nadraCheck == null) {
            nadraCheck = nadraApis.checkNadraRecord(nadraVerificationRequest, BigDecimal.ONE, httpServletRequest);
        }
        if (nadraCheck == null || !Constants.NADRA_SUCESS_CODE.equals(nadraCheck.getCode())) {
            // No verified record: either NADRA rejected the CNIC or TBL_NID_MOCK has no row for it.
            String message = (nadraCheck != null && nadraCheck.getMessage() != null && !nadraCheck.getMessage().isEmpty())
                    ? nadraCheck.getMessage()
                    : Constants.NID_VERIFICATION_FAILED;
            return getCustomizedResponseFormat(HttpStatus.OK, Constants.RESPONSE_CODE_FAILURE, message, null);
        }
        return getCustomizedResponseFormat(HttpStatus.OK, Constants.RESPONSE_CODE_SUCCESS, "SUCCESS", nadraCheck);

    }

    @PostMapping("/v1/bioVerification")
    public ResponseEntity<HashMap<String, Object>> bioVerification(@RequestBody VerifyFingerPrintRequest verifyFingerPrintRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        Response response = null;
        response = nadraApis.inAppNadraBvs(verifyFingerPrintRequest, BigDecimal.ONE, httpServletRequest);
        if (response != null && Constants.RESPONSE_CODE_SUCCESS.equals(response.getResponsecode())) {
            return getCustomizedResponseFormat(HttpStatus.OK, Constants.RESPONSE_CODE_SUCCESS, "SUCCESS", response.getData());
        } else {
            return getCustomizedResponseFormat(HttpStatus.OK, response.getResponsecode(), response.getMessages(), response.getData());
        }

    }
}
