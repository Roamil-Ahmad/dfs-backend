package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.GenerateOtpRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.VerifyOtpRequest;

import java.math.BigDecimal;

public interface OtpService {

    Response generateOtp(GenerateOtpRequest generateOtpRequest);

    Response verifyOtp(VerifyOtpRequest verifyOtpRequest, BigDecimal userId);

    Response verifyOtpForgotPassword(VerifyOtpRequest verifyOtpRequest);
}
