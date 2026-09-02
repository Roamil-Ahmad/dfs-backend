package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.*;
import com.dfs.backoffice.repo.*;
import com.dfs.backoffice.service.CommonService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private TblMessageRepo tblMessageRepo;
    @Autowired
    private TblNidBlockListRepo tblNidBlockListRepo;
    @Autowired
    private TblSmsMessageRepo tblSmsMessageRepo;
    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;
    @Autowired
    private TblAuthAccessTokenRepo tblAuthAccessTokenRepo;
    @Value("${jwt.expiry.time}")
    private String defaultSessionTime;
    @Override
    public String getResponseMessageByCode(String code) {
        TblMessage tblMessage = tblMessageRepo.findTblMessageByMessageCode(code);
        return tblMessage != null ? tblMessage.getMessageDescr() : GenericResponseCode.GENERAL_PROCESSING_ERROR.getResponseMessage() + code;
    }

    @Override
    public Response getResponse(String responseCode, String message, Object payload) {
        Response response = new Response();
        response.setResponseCode(responseCode);
        response.setMessage(message);
        response.setPayload(payload);
        return response;
    }

    @Override
    public boolean checkBlockListStatus(long cnic) {
        TblNidBlockList tblCnicBlockList = tblNidBlockListRepo.findById(cnic).orElse(null);
        if (tblCnicBlockList != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public TblSmsMessage saveSmSMessage(TblSmsMessage tblSmsMessage) {
        return tblSmsMessageRepo.save(tblSmsMessage);
    }

    @Override
    public TblAuthAccessToken createLoginTokenSession(String token, long appUserLoginHistoryId, String afterLogin, long appUserId) {
        TblAuthAccessToken tblAuthAccessToken = null;
        tblAuthAccessToken = tblAuthAccessTokenRepo.findByAppUserLoginHistoryIdAndIsActiveY(appUserLoginHistoryId);
        if (tblAuthAccessToken != null) {
            tblAuthAccessToken.setIsActive(Constants.NO);
            tblAuthAccessToken.setLastupdatedate(new Date());
            tblAuthAccessToken.setLastupdateuser(BigDecimal.valueOf(appUserId));
            tblAuthAccessTokenRepo.saveAndFlush(tblAuthAccessToken);
        }
        tblAuthAccessToken = new TblAuthAccessToken();
        tblAuthAccessToken.setAppUserLoginHistoryId(new BigDecimal(appUserLoginHistoryId));

        tblAuthAccessToken.setAccessToken(token);
        Calendar calendar = Calendar.getInstance();
        tblAuthAccessToken.setIsActive("Y");
        tblAuthAccessToken.setEffectiveFrom(calendar.getTime());
        TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName(Constants.LOG_OUT_TIME);
        if (tblGlobalConfig != null) {
            defaultSessionTime = tblGlobalConfig.getKeyValue();
        }
        calendar.add(Calendar.MINUTE, Integer.valueOf(defaultSessionTime));
        tblAuthAccessToken.setEffectiveTo(calendar.getTime());
        tblAuthAccessToken.setCreateuser(BigDecimal.ONE);
        tblAuthAccessToken.setCreatedate(new Date());
        return tblAuthAccessTokenRepo.saveAndFlush(tblAuthAccessToken);
    }
}
