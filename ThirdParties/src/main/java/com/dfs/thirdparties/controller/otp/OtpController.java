package com.dfs.thirdparties.controller.otp;

import com.dfs.thirdparties.commons.HelperClass;
import com.dfs.thirdparties.dto.GenerateOtpRequest;
import com.dfs.thirdparties.dto.GenerateOtpResponse;
import com.dfs.thirdparties.dto.VerifyOtpRequest;
import com.dfs.thirdparties.dto.common.Request;
import com.dfs.thirdparties.dto.common.ValidationError;
import com.dfs.thirdparties.service.CommonService;
import com.dfs.thirdparties.service.OtpService;
import com.dfs.thirdparties.util.GenericResponseCode;
import com.dfs.thirdparties.util.JWTSecurity;
import com.dfs.thirdparties.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OtpController extends HelperClass {
    Logger LOG = LoggerFactory.getLogger(OtpController.class);
    @Autowired
    private CommonService commonService;
    @Autowired
    JWTSecurity jwtSecurity;
    @Autowired
    private OtpService otpService;

    @PostMapping("/genrateOtp")
    ResponseEntity<HashMap<String, Object>> generateOtp(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String json=convertObjecttoJson(request);
         BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest,request);
        GenerateOtpRequest generateOtpRequest=fromJson(convertObjecttoJson(request.getPayload()),GenerateOtpRequest.class);
        RequestValidator.validateGenerateOtp(generateOtpRequest);
        HashMap<String, Object> generateOtpResponse=otpService.generateOtp(generateOtpRequest,userId);
        return getCustomizedResponseFormat(HttpStatus.OK,generateOtpResponse);

    }
    @PostMapping("/verifyOtp")
    ResponseEntity<HashMap<String, Object>> verifyOtp(@RequestBody Request request,HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String json=convertObjecttoJson(request);
         BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest,request);
        VerifyOtpRequest verifyOtpRequest=fromJson(convertObjecttoJson(request.getPayload()),VerifyOtpRequest.class);
        RequestValidator.validateVerifyOtp(verifyOtpRequest);
        HashMap<String, Object> generateOtpResponse=otpService.verifyOtp(verifyOtpRequest,userId);
        return getCustomizedResponseFormat(HttpStatus.OK,generateOtpResponse);

    }


}
