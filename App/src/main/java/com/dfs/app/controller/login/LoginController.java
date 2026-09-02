package com.dfs.app.controller.login;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.*;
import com.dfs.app.dto.common.Request;
import com.dfs.app.model.TblGlobalConfig;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.LoginService;
import com.dfs.app.util.Constants;
import com.dfs.app.util.GenericResponseCode;
import com.dfs.app.util.RequestValidator;
import com.dfs.app.util.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

public class LoginController extends HelperClass {
    Logger LOG = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService loginService;
    @Autowired
    private CommonService commonService;

    @PostMapping("/v1/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody Request request) throws JsonProcessingException {

        LoginRequest loginRequest = fromJson(convertObjecttoJson(request.getPayload()), LoginRequest.class);
        RequestValidator.validateLoginRequest(loginRequest, request);
        HashMap<String, Object> response = loginService.login(loginRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/validateLoginSession")
    public ResponseEntity<HashMap<String, Object>> validateLoginSession(@RequestBody Request request) throws JsonProcessingException {
        ValidateSessionRequest validateSessionRequest = fromJson(convertObjecttoJson(request.getPayload()), ValidateSessionRequest.class);
        RequestValidator.validateSessionRequest(validateSessionRequest, request);
        HashMap<String, Object> response = loginService.validateLoginSession(validateSessionRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/logout")
    public ResponseEntity<HashMap<String, Object>> logout(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        LogoutRequest logoutRequest = fromJson(convertObjecttoJson(request.getPayload()), LogoutRequest.class);
        RequestValidator.validateLogoutRequest(logoutRequest, request);
        HashMap<String, Object> response = loginService.logout(logoutRequest, userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping(value = "/v1/updateDevice", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> updateDevice(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        UpdateDeviceRequest updateDeviceRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), UpdateDeviceRequest.class);
        RequestValidator.validateUpdateDeviceRequest(updateDeviceRequest);
        HashMap<String, Object> response = loginService.updateDeviceRegistration(updateDeviceRequest, apiRequest, httpServletRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response.get("responsecode").toString(), response.get("messages").toString(), response.get("data"), httpServletRequest.getRequestURI());
    }

    @PostMapping(value = "/v1/verifyUpdateDevice", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> verifyUpdateDevice(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        VerifyDeviceRequest verifyDeviceRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), VerifyDeviceRequest.class);
        RequestValidator.validateVerifyDeviceRequest(verifyDeviceRequest);
        HashMap<String, Object> response = loginService.verifyUpdateDeviceRegistration(verifyDeviceRequest, apiRequest, httpServletRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response.get("responsecode").toString(), response.get("messages").toString(), response.get("data"), httpServletRequest.getRequestURI());
    }

    @PostMapping(value = "/v1/resetpin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> resetPin(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        ResetPasswordRequest resetPasswordRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), ResetPasswordRequest.class);
        RequestValidator.validateResetPasswordRequestRequest(resetPasswordRequest);
        HashMap<String, Object> response = null;
        if (resetPasswordRequest.getStep().equals("1")) {
            response = loginService.generateOtpToResetPassword(resetPasswordRequest, apiRequest, httpServletRequest);
        } else if (resetPasswordRequest.getStep().equals("2")) {
            commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
            response = loginService.verifyOtp(resetPasswordRequest, apiRequest, httpServletRequest);

        } else if (resetPasswordRequest.getStep().equals("3")) {
            commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
            response = loginService.resetPassword(resetPasswordRequest, apiRequest, httpServletRequest);
        } else {
            response = new HashMap<>();
            response.put("responsecode", GenericResponseCode.INVALID_STEP.getResponseCode());
            response.put("messages", GenericResponseCode.INVALID_STEP.getResponseMessage());
            response.put("data", null);
        }
        return getCustomizedResponseFormat(HttpStatus.OK, response.get("responsecode").toString(), response.get("messages").toString(), response.get("data"), httpServletRequest.getRequestURI());
    }


}

