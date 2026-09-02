package com.dfs.thirdparties.service;

import com.dfs.thirdparties.dto.GenerateOtpRequest;
import com.dfs.thirdparties.dto.VerifyOtpRequest;
import com.dfs.thirdparties.model.TblGlobalConfig;
import com.dfs.thirdparties.model.TblSmsMessage;

import java.math.BigDecimal;
import java.util.HashMap;

public interface OtpService {

    HashMap<String, Object> generateOtp(GenerateOtpRequest generateOtpRequest, BigDecimal userId);

    HashMap<String, Object> verifyOtp(VerifyOtpRequest verifyOtpRequest, BigDecimal userId);

    TblGlobalConfig findByKeyName(String country_code);

    TblSmsMessage saveMessage(TblSmsMessage tblSmsMessage);
}
