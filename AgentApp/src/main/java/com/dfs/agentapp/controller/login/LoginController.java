package com.dfs.agentapp.controller.login;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.*;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.LoginService;
import com.dfs.agentapp.util.AESencryption;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.RequestValidator;
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

    @PostMapping("/v1/logout")
    public ResponseEntity<HashMap<String, Object>> logout(@RequestBody Request request,HttpServletRequest httpServletRequest) throws JsonProcessingException {

        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        LogoutRequest logoutRequest = fromJson(convertObjecttoJson(request.getPayload()), LogoutRequest.class);
        RequestValidator.validateLogoutRequest(logoutRequest, request);
        HashMap<String, Object> response = loginService.logout(logoutRequest, userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }



}

