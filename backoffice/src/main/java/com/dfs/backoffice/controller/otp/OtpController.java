package com.dfs.backoffice.controller.otp;


import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.GenerateOtpRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.VerifyOtpRequest;
import com.dfs.backoffice.service.CommonService;
import com.dfs.backoffice.service.OtpService;
import com.dfs.backoffice.utils.JWTSecurity;
import com.dfs.backoffice.utils.JwtConstants;
import com.dfs.backoffice.utils.RequestValidator;
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

    @PostMapping("/generateOtp")
    ResponseEntity<Response> generateOtp(@RequestBody GenerateOtpRequest generateOtpRequest, HttpServletRequest httpServletRequest) {
        RequestValidator.validateGenerateOtp(generateOtpRequest);
        Response response = otpService.generateOtp(generateOtpRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping("/verifyOtp")
    ResponseEntity<Response> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest, HttpServletRequest httpServletRequest) {
        RequestValidator.validateVerifyOtp(verifyOtpRequest);
        Response response = otpService.verifyOtp(verifyOtpRequest, new BigDecimal((String) httpServletRequest.getAttribute(JwtConstants.APP_USER_ID)));
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping("/verifyOtpForgotPassword")
    ResponseEntity<Response> verifyOtpForgotPassword(@RequestBody VerifyOtpRequest verifyOtpRequest, HttpServletRequest httpServletRequest) {
        RequestValidator.validateVerifyOtp(verifyOtpRequest);
        Response response = otpService.verifyOtpForgotPassword(verifyOtpRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }
}
