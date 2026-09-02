package com.dfs.app.service;

import com.dfs.app.dto.*;
import com.dfs.app.dto.common.Request;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

public interface LoginService {
    HashMap<String, Object> login(LoginRequest loginRequest, Request request);



    HashMap<String, Object> logout(LogoutRequest logoutRequest, BigDecimal request);


    HashMap<String, Object> updateDeviceRegistration(UpdateDeviceRequest updateDeviceRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> verifyUpdateDeviceRegistration(VerifyDeviceRequest verifyDeviceRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> generateOtpToResetPassword(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> verifyOtp(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> resetPassword(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest);

    HashMap<String, Object> validateLoginSession(ValidateSessionRequest validateSessionRequest, Request request);
}
