package com.dfs.app.service;

import com.dfs.app.dto.*;
import com.dfs.app.dto.common.Request;
import com.dfs.app.dto.common.VerifyOtpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.jasperreports.engine.JRException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public interface AccountDetailService {
    HashMap<String, Object> mpinVerifcation(MpinVerificationRequest mpinVerificationRequest, Request request);

    HashMap<String, Object> getBalance(GetBalanceRequest getBalanceRequest, Request request);

    HashMap<String, Object> changeMpin(ChangeMpinRequest changeMpinRequest, BigDecimal userid);

    HashMap<String, Object> viewLimits(GetBalanceRequest getBalanceRequest, Request request);

    HashMap<String, Object> miniStatement(MiniStatementRequest miniStatmentRequest, Request request) throws ParseException;

    HashMap<String, Object> emailAccountStatement(AccountStatementRequest accountStatementRequest, Request request) throws JRException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, MessagingException;

    HashMap<String, Object> updateEmail(UpdateEmailRequest updateEmailRequest, Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> verifyUpdateEmail(VerifyOtpRequest verifyOtpRequest, Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> sumbitComplaint(ComplaintRequest complaintRequest, Request request, BigDecimal userId);

    HashMap<String, Object> getContactList(ContactlistDTO contactlistDTO, Request request, BigDecimal userId) throws SQLException;
}
