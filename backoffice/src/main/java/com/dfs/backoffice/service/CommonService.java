package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.TblAuthAccessToken;
import com.dfs.backoffice.model.TblSmsMessage;

public interface CommonService {

    String getResponseMessageByCode(String code);

    Response getResponse(String responseCode, String message, Object payload);

    boolean checkBlockListStatus(long cnic);

    TblSmsMessage saveSmSMessage(TblSmsMessage tblSmsMessage);

    TblAuthAccessToken createLoginTokenSession(String token, long appUserLoginHistoryId, String afterLogin, long appUserId);
}
