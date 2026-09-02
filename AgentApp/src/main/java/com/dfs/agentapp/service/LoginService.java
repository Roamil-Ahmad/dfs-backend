package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.RegisterDeviceRequest;
import com.dfs.agentapp.dto.*;
import com.dfs.agentapp.dto.common.Request;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

public interface LoginService {
    HashMap<String, Object> login(LoginRequest loginRequest, Request request);

    HashMap<String, Object> logout(LogoutRequest logoutRequest, BigDecimal request);

    HashMap<String, Object> updateDeviceRegistration(UpdateDeviceRequest updateDeviceRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> generateOtpToResetPassword(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> verifyUpdateDeviceRegistration(VerifyDeviceRequest verifyDeviceRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> verifyOtp(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> resetPassword(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest);

    /**
     * Binds a handset to the authenticated app user, creating the device row or replacing the one
     * already held. Each user - the agent and every partner - registers their own.
     */
    HashMap<String, Object> registerDeviceForUser(RegisterDeviceRequest registerDeviceRequest, java.math.BigDecimal appUserId);
}
