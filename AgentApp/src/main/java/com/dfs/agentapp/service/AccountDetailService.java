package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.*;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.dto.common.VerifyOtpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;

public interface AccountDetailService {
    HashMap<String, Object> mpinVerifcation(MpinVerificationRequest mpinVerificationRequest, Request request);

    HashMap<String, Object> getBalance(GetBalanceRequest getBalanceRequest, Request request);

    HashMap<String, Object> changeMpin(ChangeMpinRequest changeMpinRequest, BigDecimal userid);

    HashMap<String, Object> viewLimits(GetBalanceRequest getBalanceRequest, Request request);

    HashMap<String, Object> miniStatement(MiniStatementRequest miniStatmentRequest, Request request) throws ParseException;
    HashMap<String, Object> updateEmail(UpdateEmailRequest updateEmailRequest, Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException;
    HashMap<String, Object> verifyUpdateEmail(VerifyOtpRequest verifyOtpRequest, Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> sumbitComplaint(ComplaintRequest complaintRequest, Request request, BigDecimal userId);
}
