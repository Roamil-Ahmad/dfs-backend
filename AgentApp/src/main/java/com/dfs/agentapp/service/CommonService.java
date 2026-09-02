package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.GenerateNotificationRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.dto.common.Response;
import com.dfs.agentapp.dto.workflow.McRequestDetail;
import com.dfs.agentapp.model.TblAuthAccessToken;
import com.dfs.agentapp.model.TblCustomerAll;
import com.dfs.agentapp.model.TblRequest;
import com.dfs.agentapp.model.TblResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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

    /**
     * Authenticates the bearer token alone and returns the app user it belongs to, without
     * comparing a device. Only device registration may use it: a user registering their first
     * handset has no device to be validated against yet.
     */
    BigDecimal authenticateHeaderOnly(HttpServletRequest httpServletRequest);

    String encryptWithAes(String text);

    HashMap<String, Object> getAppScreenData(String name, String languageId);

    Response generateNotification(GenerateNotificationRequest generateNotificationRequest, Request apiRequest, String header) throws JsonProcessingException;
    String checkMakerCheckerApplicability(String authorization, String tableName, String formName, String requestTypeSave);
    Response makerCheckerRequest(String authorization, String tableName, String formName, McRequestDetail mcRequestDetail);
    String getMobileNumberByCustomerAllId(TblCustomerAll tblCustomerAll);

  String getMobileNumberByAccountNo(String mobileNumber);
}
