package com.dfs.agentapp.service.impl;


import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.*;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.dto.common.Response;
import com.dfs.agentapp.dto.workflow.McActionRequest;
import com.dfs.agentapp.dto.workflow.McRequestDetail;
import com.dfs.agentapp.dto.workflow.McResponse;
import com.dfs.agentapp.dto.workflow.ProcedureResponse;
import com.dfs.agentapp.model.*;
import com.dfs.agentapp.repo.*;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CommonServiceImpl extends HelperClass implements CommonService {
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private JWTSecurity jwtSecurity;
    @Autowired
    private TblMessageRepo tblMessageRepo;
    @Autowired
    private TblRequestRepo tblRequestRepo;
    @Autowired
    private TblResponseRepo tblResponseRepo;
    @Autowired
    private TblAuthAccessTokenRepo tblAuthAccessTokenRepo;
    @Autowired
    private TblCustomerAllRepo tblCustomerAllRepo;
    @Autowired
    private TblDeviceInfoRepo tblDeviceInfoRepo;
    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Value("${jwt.expiry.time}")
    private String defaultSessionTime;
    @Autowired
    private TblMutlilanguageRepo tblMutlilanguageRepo;
    @Autowired
    private LkpLanguageRepo lkpLanguageRepo;
    @Value("${generate.notification.url}")
    private String generateNotificationUrl;
    @Value("${workflow.url}")
    private String workflowurl;
    @Autowired
    private TblAccountRepo tblAccountRepo;

    @Value("${account.type.individual.code}")
    private String accountTypeWallet;

    @Override
    public String getResponseMessageByCode(String code) {
        TblMessage tblMessage = tblMessageRepo.findTblMessageByMessageCode(code);
        return tblMessage != null ? tblMessage.getMessageDescr() : GenericResponseCode.MESSAGE_NOT_FOUND_AGAINST_CODE.getResponseMessage() + code;
    }

    @Override
    public HashMap<String, Object> getResponse(String responseCode, Object payload) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responseCode);
        map.put("messages", getResponseMessageByCode(responseCode));
        map.put("data", payload);
        return map;
    }

    @Override
    public boolean validatePreRegToken(String token) {
        Claims claims = jwtSecurity.parseJWT(token);
        // Extract user details from claims
        String imie = (String) claims.get(JwtConstants.IMIE_NUMBER);
        String mobileNumber = (String) claims.get(JwtConstants.MOBILE_NUMBER);
        Long appUserId = tblAppUserRepo.validateDeviceToken(mobileNumber, imie);
        if (appUserId != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public TblRequest saveRequest(String mobileNumber, String imieNumber, String createUserId, String json, String endPoint) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setJsonData(json);
        tblRequest.setCreateuser(new BigDecimal(createUserId));
        tblRequest.setEndPoint(endPoint);
        tblRequest.setImei(imieNumber);
        tblRequest.setMobileNo(mobileNumber);
        tblRequest.setCreatedate(new Date());
        return tblRequestRepo.saveAndFlush(tblRequest);

    }

    @Override
    public TblAuthAccessToken createLoginTokenSession(String token, long id, String mobileNo, String preLogin, long createUserId) {
        TblAuthAccessToken tblAuthAccessToken = null;

        if (preLogin.equalsIgnoreCase(Constants.PRE_LOGIN)) {
            tblAuthAccessToken = tblAuthAccessTokenRepo.findByCustomerAllIdAndIsActiveY(id);
            if (tblAuthAccessToken != null) {
                tblAuthAccessToken.setIsActive(Constants.N);
                tblAuthAccessToken.setLastupdatedate(new Date());
                tblAuthAccessToken.setLastupdateuser(BigDecimal.valueOf(createUserId));
                tblAuthAccessTokenRepo.saveAndFlush(tblAuthAccessToken);
            }
            tblAuthAccessToken = new TblAuthAccessToken();
            TblCustomerAll tblCustomerAll = new TblCustomerAll();
            tblCustomerAll.setCustomerAllId(id);
            tblAuthAccessToken.setTblCustomerAll(tblCustomerAll);
        } else if (preLogin.equalsIgnoreCase(Constants.AFTER_LOGIN)) {
            tblAuthAccessToken = tblAuthAccessTokenRepo.findByAccountNoAndIsActiveY(mobileNo);
            if (tblAuthAccessToken != null) {
                tblAuthAccessToken.setIsActive(Constants.N);
                tblAuthAccessToken.setLastupdatedate(new Date());
                tblAuthAccessToken.setLastupdateuser(BigDecimal.valueOf(createUserId));
                tblAuthAccessTokenRepo.saveAndFlush(tblAuthAccessToken);
            }
            tblAuthAccessToken = new TblAuthAccessToken();
            TblAppUserLoginHistory tblAppUserLoginHistory = new TblAppUserLoginHistory();
            tblAppUserLoginHistory.setAppUserLoginHistoryId(id);
            tblAuthAccessToken.setTblAppUserLoginHistory(tblAppUserLoginHistory);
        }
        tblAuthAccessToken.setAccessToken(token);
        Calendar calendar = Calendar.getInstance();
        tblAuthAccessToken.setIsActive(Constants.YES);
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

    @Override
    public TblResponse saveResponse(TblRequest tblRequest, String additionaldata) {
        TblResponse tblResponse = tblResponseRepo.findByRequestId(tblRequest.getRequestId());
        tblResponse = tblResponse == null ? new TblResponse() : tblResponse;
        tblResponse.setTblRequest(tblRequest);
        tblResponse.setAdditionalData(additionaldata);
        tblResponse.setCreateuser(tblRequest.getCreateuser());
        tblResponse.setCreatedate(new Date());
        return tblResponseRepo.saveAndFlush(tblResponse);
    }

    /**
     * Authenticates the bearer token and returns its app user, with no device comparison.
     *
     * <p>Device registration cannot use {@link #authenticateHeaderAndDevice}: the caller is asking
     * to bind their first handset, so there is nothing yet to validate them against. Everything
     * else - signature, expiry and a live session for that user - is still checked.</p>
     */
    @Override
    public BigDecimal authenticateHeaderOnly(HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }
        Claims claims = jwtSecurity.parseJWT(authHeader.substring(7));
        if (claims == null || !claims.containsKey(JwtConstants.APP_USER_ID)) {
            throw new AuthenticationException(GenericResponseCode.SESSION_EXPIRED.getResponseCode());
        }
        BigDecimal appUserId = new BigDecimal(((Number) claims.get(JwtConstants.APP_USER_ID)).longValue());
        int n = tblAuthAccessTokenRepo.getAuthTokenAuthorization(String.valueOf(appUserId), Constants.EMPTY, authHeader);
        if (n != 1) {
            throw new AuthenticationException(GenericResponseCode.SESSION_EXPIRED.getResponseCode());
        }
        tblAuthAccessTokenRepo.updateTokenValidity(String.valueOf(appUserId), Constants.EMPTY);
        return appUserId;
    }

    @Override
    public BigDecimal authenticateHeaderAndDevice(HttpServletRequest httpServletRequest, Request request, String type) {

        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            Claims claims = jwtSecurity.parseJWT(token);
            if (claims == null) {
                if (type.equalsIgnoreCase(Constants.PRE_LOGIN)) {
                    TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByMobileNo(aeSencryption.encryptwith256(extractMobileNumber(request.getPayload())));
                    if (tblCustomerAll != null) {
                        tblAuthAccessTokenRepo.deactivateTokens(Constants.EMPTY, String.valueOf(tblCustomerAll.getCustomerAllId()));
                    }
                    throw new AuthenticationException(GenericResponseCode.SESSION_EXPIRED.getResponseCode());
                } else if (type.equalsIgnoreCase(Constants.AFTER_LOGIN)) {
                    TblAppUser tblAppUser = tblAppUserRepo.findByMobileNumber(aeSencryption.encryptwith256(extractMobileNumber(request.getPayload())));
                    if (tblAppUser != null) {
                        tblAuthAccessTokenRepo.deactivateTokens(String.valueOf(tblAppUser.getAppUserId()), Constants.EMPTY);
                    }
                    throw new AuthenticationException(GenericResponseCode.SESSION_EXPIRED.getResponseCode());
                }
            }

            // Extract user details from claims
            String imie = (String) claims.get(JwtConstants.IMIE_NUMBER);
            String mobileNumber = (String) claims.get(JwtConstants.MOBILE_NUMBER);
            String uuid = (String) claims.get(JwtConstants.UUID);


            if (imie != null && mobileNumber != null && uuid != null) {


                if (request != null) {
                    // Device verification removed: the token and its live session are what
                    // authorise the call now. No handset lookup, no IMEI or UUID comparison.
                    if (type.equalsIgnoreCase(Constants.PRE_LOGIN)) {
                        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByMobileNo(mobileNumber);
                        int n = tblAuthAccessTokenRepo.getAuthTokenAuthorization(Constants.EMPTY, String.valueOf(tblCustomerAll.getCustomerAllId()), authHeader);
                        if (n != 1) {
                            throw new AuthenticationException(GenericResponseCode.SESSION_EXPIRED.getResponseCode());
                        }
                        tblAuthAccessTokenRepo.updateTokenValidity(Constants.EMPTY, String.valueOf(tblCustomerAll.getCustomerAllId()));
                        return BigDecimal.ONE;
                    } else if (type.equalsIgnoreCase(Constants.AFTER_LOGIN)) {
                        if (claims.containsKey(JwtConstants.APP_USER_ID)) {
                            BigDecimal appUserId = new BigDecimal(((Number) claims.get(JwtConstants.APP_USER_ID)).longValue());
                            int n = tblAuthAccessTokenRepo.getAuthTokenAuthorization(String.valueOf(appUserId), Constants.EMPTY, authHeader);
                            if (n != 1) {
                                throw new AuthenticationException(GenericResponseCode.SESSION_EXPIRED.getResponseCode());
                            }
                            tblAuthAccessTokenRepo.updateTokenValidity(String.valueOf(appUserId), Constants.EMPTY);
                            return appUserId;
                        } else {
                            throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
                        }
                    } else {
                        throw new CustomDataNotFoundException(GenericResponseCode.INVALID_AUTH_TYPE.getResponseCode());
                    }
                } else {
                    throw new CustomDataNotFoundException(GenericResponseCode.BAD_REQUEST.getResponseCode());
                }
            } else {
                throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
            }

        } else {
            throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }
    }

    @Override
    public String encryptWithAes(String text) {
        return aeSencryption.encryptwith256(text);
    }

    @Override
    public HashMap<String, Object> getAppScreenData(String name, String languageId) {
        GetAppScreenResponse getAppScreenResponse = new GetAppScreenResponse();
        List<TblMultilanguage> appData = tblMutlilanguageRepo.findAllByLanguageIdAndAppScreenName(languageId, name);
        if (appData != null && !appData.isEmpty()) {
            List<ScreenHeader> screenHeaders = new ArrayList<>();
            List<ScreenValidator> screenValidators = new ArrayList<>();
            List<ScreenLabel> screenLabels = new ArrayList<>();
            List<ScreenPlaceHolder> screenPlaceHolders = new ArrayList<>();
            List<ScreenButton> screenButtons = new ArrayList<>();
            for (TblMultilanguage tblMultilanguage : appData) {
                if (tblMultilanguage.getColumnName() != null && tblMultilanguage.getColumnName().contains("Header")) {
                    screenHeaders.add(new ScreenHeader(tblMultilanguage.getColumnName(), tblMultilanguage.getValue()));
                } else if (tblMultilanguage.getColumnName() != null && tblMultilanguage.getColumnName().contains("Label")) {
                    screenLabels.add(new ScreenLabel(tblMultilanguage.getColumnName(), tblMultilanguage.getValue()));
                } else if (tblMultilanguage.getColumnName() != null && tblMultilanguage.getColumnName().contains("Placeholder")) {
                    screenPlaceHolders.add(new ScreenPlaceHolder(tblMultilanguage.getColumnName(), tblMultilanguage.getValue()));
                } else if (tblMultilanguage.getColumnName() != null && tblMultilanguage.getColumnName().contains("Validator")) {
                    screenValidators.add(new ScreenValidator(tblMultilanguage.getColumnName(), tblMultilanguage.getValue()));
                } else if (tblMultilanguage.getColumnName() != null && tblMultilanguage.getColumnName().contains("Button")) {
                    screenButtons.add(new ScreenButton(tblMultilanguage.getColumnName(), tblMultilanguage.getValue()));
                }
            }
            getAppScreenResponse.setScreenValidators(screenValidators);
            getAppScreenResponse.setScreenButtons(screenButtons);
            getAppScreenResponse.setScreenPlaceHolders(screenPlaceHolders);
            getAppScreenResponse.setScreenHeaders(screenHeaders);
            getAppScreenResponse.setScreenLabels(screenLabels);
            LkpLanguage lkpLanguage = lkpLanguageRepo.findById(Long.valueOf(languageId)).orElseThrow(() -> {
                throw new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
            });
            getAppScreenResponse.setLanguageCode(lkpLanguage.getLanguageCode());
            getAppScreenResponse.setLanguageId(String.valueOf(lkpLanguage.getLanguageId()));
            getAppScreenResponse.setLanguageName(lkpLanguage.getLanguageDescr());

            return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), getAppScreenResponse);
        }
        return getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
    }

    public String extractMobileNumber(Object object) {
        try {
            if (object instanceof Map) {
                // Handle Map object
                Object mobileNumber = ((Map<?, ?>) object).get("mobileNumber");
                return mobileNumber != null ? mobileNumber.toString() : null;
            } else if (object instanceof String) {
                // Handle JSON String object
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue((String) object, Map.class);
                Object mobileNumber = jsonMap.get("mobileNumber");
                return mobileNumber != null ? mobileNumber.toString() : null;
            }
        } catch (Exception e) {
            // Log and handle exception
            throw new CustomDataNotFoundException(GenericResponseCode.BAD_REQUEST.getResponseCode());
        }
        // Unsupported type or key not found
        return null;
    }

    @Override
    public Response generateNotification(GenerateNotificationRequest generateNotificationRequest, Request apiRequest, String header) throws JsonProcessingException {
        apiRequest.setPayload(generateNotificationRequest);
        return sendRequestAndGetResponse(apiRequest, header, generateNotificationUrl);
    }

    public String checkMcApplicability(String authorization, String tableName, String formName, String requestTypeSave) {

        String url = workflowurl + "/ckeckMcApplicable";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", authorization);
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("tableName", tableName);
        requestParams.put("formName", formName);
        requestParams.put("requestType", requestTypeSave);
        com.dfs.agentapp.dto.workflow.Request request = new com.dfs.agentapp.dto.workflow.Request();
        request.setPayLoad(requestParams);
        return getResponseFromPostAPI(headerMap, request, url);
    }

    public String mcRequest(String authorization, String tableName, String formName, McRequestDetail mcRequestDetail) {
        String urlmcRequest = workflowurl + "/mcRequest";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", authorization);
        Map<String, String> requestParamsMcRequest = new HashMap<>();
        requestParamsMcRequest.put("formName", formName);
        requestParamsMcRequest.put("makerId", String.valueOf(mcRequestDetail.getMakerId()));
        requestParamsMcRequest.put("makerComments", mcRequestDetail.getMakerComments());
        requestParamsMcRequest.put("ftFlag", mcRequestDetail.getFtFlag());
        requestParamsMcRequest.put("tableName", tableName);
        requestParamsMcRequest.put("requestType", mcRequestDetail.getRequestType());
        requestParamsMcRequest.put("updateType", mcRequestDetail.getUpdateType());
        requestParamsMcRequest.put("updateJson", mcRequestDetail.getUpdateJson());
        requestParamsMcRequest.put("refTableId", String.valueOf(mcRequestDetail.getRefTableId()));
        requestParamsMcRequest.put("oldJson", String.valueOf(mcRequestDetail.getOldJson()));
        com.dfs.agentapp.dto.workflow.Request jsonRequest = new com.dfs.agentapp.dto.workflow.Request();
        jsonRequest.setPayLoad(requestParamsMcRequest);
        return getResponseFromPostAPI(headerMap, jsonRequest, urlmcRequest);
    }

    public String mcAction(com.dfs.agentapp.dto.workflow.Request jsonRequest, String authorization, McActionRequest mcActionRequest, BigDecimal userId) {
        String urlmcRequest = workflowurl + "/mcAction";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", authorization);
        Map<String, String> requestParamsMcAction = new HashMap<String, String>();
        requestParamsMcAction.put("mcRequestId", mcActionRequest.getMcRequestId());
        requestParamsMcAction.put("mcPeindingRequestId", mcActionRequest.getMcPeindingRequestId());
        requestParamsMcAction.put("checkerId", String.valueOf(userId));
        requestParamsMcAction.put("checkerComments", mcActionRequest.getCheckerComments());
        requestParamsMcAction.put("action", mcActionRequest.getAction());
        requestParamsMcAction.put("updatedIndex", String.valueOf(mcActionRequest.getUpdatedIndex()));
        jsonRequest.setPayLoad(requestParamsMcAction);
        return getResponseFromPostAPI(headerMap, jsonRequest, urlmcRequest);
    }

    @Override
    public String checkMakerCheckerApplicability(String authorization, String tableName, String formName, String requestTypeSave) {
        String result = "";
        Gson gson = new Gson();
        String checkMcApplicability = this.checkMcApplicability(authorization, tableName, formName, requestTypeSave);
        if (checkMcApplicability != null) {
            com.dfs.agentapp.dto.workflow.Response response1 = gson.fromJson(checkMcApplicability, com.dfs.agentapp.dto.workflow.Response.class);
            ProcedureResponse procedureResponse = gson.fromJson(convertObjecttoJson(response1.getPayload()), ProcedureResponse.class);
            if (procedureResponse == null || procedureResponse.getMcApplicability() <= 0) {
                result = "102010201020";
            }
        } else {
            result = GenericResponseCode.RECORD_NOT_FOUND.getResponseCode();
        }

        return result;
    }

    @Override
    public Response makerCheckerRequest(String authorization, String tableName, String formName, McRequestDetail mcRequestDetail) {
        Response response = new Response();
        Gson gson = new Gson();
        String mcRequest = this.mcRequest(authorization, tableName, formName, mcRequestDetail);
        if (mcRequest != null) {
            com.dfs.agentapp.dto.workflow.Response response2 = gson.fromJson(mcRequest, com.dfs.agentapp.dto.workflow.Response.class);
            McResponse mcResponse = gson.fromJson(convertObjecttoJson(response2.getPayload()), McResponse.class);
            if (mcResponse != null && mcResponse.getStatus() == 1) {
                setResponse(response, mcResponse, GenericResponseCode.SUCCESS.getResponseCode(), mcResponse.getStatusDecsr());
            } else {
                setResponse(response, mcResponse, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), mcResponse != null ? mcResponse.getStatusDecsr() : GenericResponseCode.RECORD_NOT_SAVED.getResponseMessage());
            }
        } else {
            setResponse(response, null, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), null);
        }

        return response;
    }

    public void setResponse(Response response, Object payload, String code, String customMessage) {
        TblMessage tblResponseMessage = tblMessageRepo.findTblMessageByMessageCode(code);
        response.setData(payload);
        response.setResponsecode(tblResponseMessage.getMessageCode());
        response.setMessages(customMessage != null ? customMessage : tblResponseMessage.getMessageDescr());
    }

    @Override
    public String getMobileNumberByCustomerAllId(TblCustomerAll customerAllId) {
        TblAccount tblAccount = tblAccountRepo.findByCustomerAllId(customerAllId.getCustomerAllId());
        if (tblAccount == null) {
            return aeSencryption.decrypt(customerAllId.getMobileNo());
        } else {
            return tblAccount.getMobileNo();
        }
    }

    public String getMobileNumberByAccountNo(String mobileNumber) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNoAndAccountTypeCode(mobileNumber, accountTypeWallet);
        if (tblAccount != null) {
            return tblAccount.getMobileNo();
        }
        return null;
    }
}
