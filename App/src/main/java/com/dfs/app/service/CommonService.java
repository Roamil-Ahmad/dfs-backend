package com.dfs.app.service;

import com.dfs.app.dto.AccountStatementRequest;
import com.dfs.app.dto.GenerateNotificationRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.dto.common.Response;
import com.dfs.app.model.TblAuthAccessToken;
import com.dfs.app.model.TblCustomerAll;
import com.dfs.app.model.TblRequest;
import com.dfs.app.model.TblResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.jasperreports.engine.JRException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;

public interface CommonService {

    boolean validatePreRegToken(String token);

    boolean validateToken(String token);

    String getResponseMessageByCode(String code);

    HashMap<String, Object> getResponse(String responseCode, Object payload);

    TblRequest saveRequest(String mobileNumber, String imieNumber, String createUserId, String json, String endPoint);

    TblAuthAccessToken createLoginTokenSession(String token, long customerAllId, String mobileNo, String preLogin, long createUserId);

    TblResponse saveResponse(TblRequest tblRequest, String json);

    BigDecimal authenticateHeaderAndDevice(HttpServletRequest httpServletRequest, Request request, String preLogin);

    String encryptWithAes(String text);

    HashMap<String, Object> getAppScreenData(String name, String languageId);

    Response generateNotification(GenerateNotificationRequest generateNotificationRequest, Request apiRequest, String header) throws JsonProcessingException;


    String getMobileNumberByCustomerAllId(TblCustomerAll customerAllId);

    HashMap<String, Object> getFaqs();

    void generateAccountStatement(AccountStatementRequest accountStatementRequest) throws JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, MessagingException;

    String getMobileNumberByAccountNo(String mobileNumber);

    HashMap<String, Object> getTutorials();

    HashMap<String, Object> contactUs();

    HashMap<String, Object> taxCertificate();

    HashMap<String, Object> notification(String appUserId);

    HashMap<String, Object> spending(String accountId);

    HashMap<String, Object> appMenu();
}
