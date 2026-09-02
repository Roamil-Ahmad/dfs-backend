package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.dto.RegisterDeviceRequest;
import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.*;
import com.dfs.agentapp.dto.common.*;
import com.dfs.agentapp.model.*;
import com.dfs.agentapp.repo.*;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.LoginService;
import com.dfs.agentapp.service.ThirdPartyService;
import com.dfs.agentapp.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class LoginServiceImpl extends HelperClass implements LoginService {

    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TblCustomerAllRepo tblCustomerAllRepo;
    @Autowired
    private TblAppUserLoginHistoryRepo tblAppUserLoginHistoryRepo;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;
    @Autowired
    private TblAuthAccessTokenRepo tblAuthAccessTokenRepo;
    @Autowired
    private TblDeviceInfoRepo tblDeviceInfoRepo;
    @Autowired
    private JWTSecurity jwtSecurity;
    @Autowired
    private TblAgentRepo tblAgentRepo;
    @Value("${jwt.expiry.time}")
    private String tokenExpiryTime;
    @Value("${doc.selfie.code}")
    private String docSelfieCode;
    @Autowired
    private TblDocumentRepo tblDocumentRepo;
    @Autowired
    private LkpDocumentTypeRepo lkpDocumentTypeRepo;
    @Value("${base.url}")
    private String baseUrl;
    @Value("${unlock.time}")
    private String unlockTime;
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Value("${sms.template.Type}")
    private String smsTemplateTypeFwr;
    @Value("${sms.template.type.reset.pin}")
    private String smsTemplateTypeRp;
    @Autowired
    private LkpChannelRepo lkpChannelRepo;
    @Autowired
    private TblOtpRepo tblOtpRepo;

    @Override
    public HashMap<String, Object> login(LoginRequest loginRequest, Request request) {
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByUsername(aeSencryption.encryptwith256(loginRequest.getUsername()));
        if (HelperClass.isNullOrEmpty(tblCustomerAll)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }
//        if (HelperClass.isNullOrEmpty(tblCustomerAll.getImeiNo()) || !request.getImieNo().equals(tblCustomerAll.getImeiNo())) {
//            throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_INVALID.getResponseCode());
//        }
        TblAppUser tblAppUser = tblAppUserRepo.findByUsernameAndPassword(aeSencryption.encryptwith256(loginRequest.getUsername()), aeSencryption.encryptwith256(loginRequest.getPassword()));
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_USERNAME.getResponseCode());
        }
        // password will be check here
        validatePassword(tblAppUser, loginRequest.getPassword(), unlockTime);

        TblAgent tblAgent = tblAgentRepo.findByIdAndIsActive(tblAppUser.getAgentId(), Constants.YES);
        if (tblAgent == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        TblAccount tblAccount = tblAccountRepo.findByAgentIdAccountTypeCodeAccountRegType(tblAgent.getAgentId(), loginRequest.getAccountTypeCode(), loginRequest.getAccountRegTypeCode());
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        LoginResponse loginResponse = new LoginResponse();

        TblAppUserLoginHistory tblAppUserLoginHistory = new TblAppUserLoginHistory();
        tblAppUserLoginHistory.setTblAppUser(tblAppUser);
        tblAppUserLoginHistory.setLoginDate(new Date());
        tblAppUserLoginHistoryRepo.saveAndFlush(tblAppUserLoginHistory);
        tblAppUser.setLastlogin(new Date());
        tblAppUser.setFireBaseToken(loginRequest.getFireBaseToken());
        tblAppUserRepo.saveAndFlush(tblAppUser);
        HashMap<String, Object> hmClaims = new HashMap<String, Object>();

        // Device identity comes from this user's own registered handset. Reading it from
        // TBL_CUSTOMER_ALL would hand every partner of a corporate agent the agent's device, which
        // is what made partners indistinguishable. The fall back applies only to a user who has
        // not registered a device yet, so that they can still sign in and go register one - the
        // validation on later calls looks the device up by app user and will refuse until they do.
        TblDeviceInfo userDevice = tblDeviceInfoRepo.findByAppUserId(tblAppUser.getAppUserId());
        String deviceImei = userDevice != null ? userDevice.getImeiNo() : tblCustomerAll.getImeiNo();
        String deviceUuid = userDevice != null ? userDevice.getUuid() : tblCustomerAll.getUuid();

        hmClaims.put(JwtConstants.IMIE_NUMBER, deviceImei);
        hmClaims.put(JwtConstants.MOBILE_NUMBER, tblAccount.getAccountNo());
        hmClaims.put(JwtConstants.UUID, deviceUuid);
        hmClaims.put(JwtConstants.APP_USER_ID, tblAppUser.getAppUserId());
        TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName(Constants.LOG_OUT_TIME);
        if (tblGlobalConfig != null) {
            tokenExpiryTime = tblGlobalConfig.getKeyValue();
        }


        String token = jwtSecurity.createJWTWithClaims(tblCustomerAll.getMobileNo(), hmClaims, 1440);
        TblAuthAccessToken tblAuthAccessToken = commonService.createLoginTokenSession(token, tblAppUserLoginHistory.getAppUserLoginHistoryId(), tblCustomerAll.getMobileNo(), Constants.AFTER_LOGIN, tblAppUser.getAppUserId());

        if (tblAuthAccessToken == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.FAILED_TO_CREATE_SESSION.getResponseCode());
        }
        LkpDocumentType lkpDocumentType = lkpDocumentTypeRepo.findByDocumentTypeCode(docSelfieCode);
        if (lkpDocumentType != null) {
            TblDocument tblDocument = tblDocumentRepo.findByDocumentTypeIdAndAppUserIdAndAccountLevelId(lkpDocumentType.getDocumentTypeId(), tblAppUser.getAppUserId(), tblAccount.getTblAccountLevel().getAccountLevelId());
            if (tblDocument != null) {
                loginResponse.setSelfie(baseUrl + tblDocument.getDocumentPath());
            }
        }
        loginResponse.setToken(token);
        loginResponse.setAppUserId(tblAppUser.getAppUserId());
        loginResponse.setAccountId(tblAccount.getAccountId());
        loginResponse.setAccountNo(tblAccount.getAccountNo());
        loginResponse.setAccountStatusDescr(tblAccount.getLkpAccountStatus().getAccountStatusDescr());
        loginResponse.setAccountStatusId(tblAccount.getLkpAccountStatus().getAccountStatusId());
        loginResponse.setAccountTitle(aeSencryption.decrypt(tblAccount.getAccountTitle()));
        loginResponse.setAccountType(tblAccount.getLkpAccountType().getAccountTypeDescr());
        loginResponse.setDfsId(aeSencryption.decrypt(tblAppUser.getUsername()));
        loginResponse.setCurrentBalance(tblAccount.getCurrentBalance());
        loginResponse.setDailyCrLmt(tblAccount.getDailyAmtLimitCr());
        loginResponse.setDailyDrLmt(tblAccount.getDailyAmtLimitDr());
        loginResponse.setMonthlyCrLmt(tblAccount.getMonthlyAmtLimitCr());
        loginResponse.setMonthlyDrLmt(tblAccount.getMonthlyAmtLimitDr());
        loginResponse.setYearlyCrLmt(tblAccount.getYearlyAmtLimitCr());
        loginResponse.setYearlyDrLmt(tblAccount.getYearlyAmtLimitDr());
        loginResponse.setAgentId(tblAgent.getAgentId());
        loginResponse.setIban(tblAccount.getIban());
        loginResponse.setLevelDescr(tblAccount.getTblAccountLevel().getAccountLevelDescr());
        loginResponse.setNidNo(aeSencryption.decrypt(tblAgent.getNidNo()));
        loginResponse.setBiometricallyVerified("");
        loginResponse.setAccountLevelCode(tblAccount.getTblAccountLevel().getAccountLevelCode());
        loginResponse.setQr(tblAccount.getQrCode());
        loginResponse.setFatherName(aeSencryption.decrypt(tblAgent.getFatherHusbandName()));
        loginResponse.setGrandFatherName(aeSencryption.decrypt(tblAgent.getGrandfatherName()));
        loginResponse.setPasswordFlag(tblAppUser.getPasswordUpdateFlag());
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), loginResponse);
    }

    @Override
    public HashMap<String, Object> logout(LogoutRequest logoutRequest, BigDecimal request) {
        TblAuthAccessToken tblAuthAccessToken = tblAuthAccessTokenRepo.findByMobileNumberAndIsActiveY(aeSencryption.encryptwith256(logoutRequest.getMobileNumber()));
        if (tblAuthAccessToken != null) {

            tblAuthAccessToken.setIsActive(Constants.N);
            tblAuthAccessToken.setLastupdatedate(new Date());
            tblAuthAccessToken.setLastupdateuser(request);
            tblAuthAccessTokenRepo.saveAndFlush(tblAuthAccessToken);
        }
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);

    }

    private void validatePassword(TblAppUser tblAppUser, String password, String unlockTime) {
        String username = aeSencryption.decrypt(tblAppUser.getUsername());
        if (!tblAppUser.getPassword().equals(aeSencryption.encryptwith256(password))) {
            tblAppUser.setLoginCounts(tblAppUser.getLoginCounts() == null ? 1 : tblAppUser.getLoginCounts() + 1);
            // removing second condition will extend unlock time after every hit after 3 tries
            if (tblAppUser.getLoginCounts() >= 3 && (!String.valueOf(tblAppUser.getStatus()).equalsIgnoreCase("B"))) {
                tblAppUser.setStatus("B");
                tblAppUser.setUnlockTime(addMinutesToDate(unlockTime, new Date()));
                tblAppUserRepo.saveAndFlush(tblAppUser);
                Date currentTime = new Date();
                //Get message from database
                String unlockMsg = commonService.getResponseMessageByCode(GenericResponseCode.USER_IS_LOCKED.getResponseCode());
                long diffInMillis = tblAppUser.getUnlockTime().getTime() - currentTime.getTime();
                double totalMinutes = Math.ceil(diffInMillis / (60.0 * 1000));  // Round up
                long minutes = (long) totalMinutes;
                long hours = minutes / 60;
                long remainingMinutes = minutes % 60;
                String remainingTimeMsg = String.format(unlockMsg, hours, remainingMinutes);
                throw new CustomException(remainingTimeMsg);
            } else if (tblAppUser.getLoginCounts() >= 3) {
                Date currentTime = new Date();
                String unlockMsg = commonService.getResponseMessageByCode(GenericResponseCode.USER_IS_LOCKED.getResponseCode());
                long diffInMillis = tblAppUser.getUnlockTime().getTime() - currentTime.getTime();
                double totalMinutes = Math.ceil(diffInMillis / (60.0 * 1000));  // Round up
                long minutes = (long) totalMinutes;
                long hours = minutes / 60;
                long remainingMinutes = minutes % 60;
                String remainingTimeMsg = String.format(unlockMsg, hours, remainingMinutes);
                throw new CustomException(remainingTimeMsg);
            }
            tblAppUserRepo.saveAndFlush(tblAppUser);
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_PASSWORD.getResponseCode());
        } else {
            // Check if the user is blocked and the unlock time has passed
            if ("B".equals(String.valueOf(tblAppUser.getStatus())) && tblAppUser.getUnlockTime() != null) {
                Date currentTime = new Date();
                // If the current time is after the unlock time, unlock the user
                if (currentTime.after(tblAppUser.getUnlockTime())) {
                    tblAppUser.setStatus("U");         // Unblock the user
                    tblAppUser.setUnlockTime(null);    // Clear the unlock time
                    tblAppUser.setLoginCounts(null);   // Reset login count

                } else {
                    String unlockMsg = commonService.getResponseMessageByCode(GenericResponseCode.USER_IS_LOCKED.getResponseCode());
                    long diffInMillis = tblAppUser.getUnlockTime().getTime() - currentTime.getTime();
                    double totalMinutes = Math.ceil(diffInMillis / (60.0 * 1000));  // Round up
                    long minutes = (long) totalMinutes;
                    long hours = minutes / 60;
                    long remainingMinutes = minutes % 60;
                    String remainingTimeMsg = String.format(unlockMsg, hours, remainingMinutes);
                    throw new CustomException(remainingTimeMsg);
                }
            } else {
                tblAppUser.setUnlockTime(null);    // Clear the unlock time
                tblAppUser.setLoginCounts(null);
            }
            tblAppUserRepo.saveAndFlush(tblAppUser);
        }
    }

    public Date addMinutesToDate(String dateStr, Date date) {
        try {
            // Parse the minutes from the string input
            int minutesToAdd = Integer.parseInt(dateStr);

            // Use Calendar to add minutes to the given date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutesToAdd);

            // Return the updated date
            return calendar.getTime();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }

    /**
     * Binds a handset to the authenticated user.
     *
     * <p>The app user comes from the token, never from the payload, so a partner can only ever
     * register a device against themselves. The row is reused when one already exists, so a user
     * changing handset replaces their binding rather than accumulating rows.</p>
     *
     * <p>A fresh UUID is minted on every registration. That is the secret half of the binding: the
     * token carries it and validation compares it, so a handset cannot be adopted by copying an
     * IMEI alone. The user signs in again afterwards to receive a token carrying the new value.</p>
     */
    @Override
    public HashMap<String, Object> registerDeviceForUser(RegisterDeviceRequest registerDeviceRequest,
                                                         BigDecimal appUserId) {
        TblAppUser tblAppUser = tblAppUserRepo.findById(appUserId.longValue()).orElse(null);
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo.findByAppUserId(appUserId.longValue());
        if (tblDeviceInfo == null) {
            tblDeviceInfo = new TblDeviceInfo();
            tblDeviceInfo.setCreatedate(new Date());
            tblDeviceInfo.setCreateuser(appUserId);
        } else {
            tblDeviceInfo.setLastupdatedate(new Date());
            tblDeviceInfo.setLastupdateuser(appUserId);
        }
        tblDeviceInfo.setTblAppUser(tblAppUser);
        tblDeviceInfo.setImeiNo(registerDeviceRequest.getImeiNo());
        tblDeviceInfo.setDeviceModel(registerDeviceRequest.getDeviceModel());
        tblDeviceInfo.setAppVersion(registerDeviceRequest.getAppVersion());
        tblDeviceInfo.setMacAddress(registerDeviceRequest.getMacAddress());
        // Left as the owning user's own mobile, which is null for a partner: the device is keyed
        // by app user now, so nothing resolves it by mobile any more.
        tblDeviceInfo.setMobileNo(tblAppUser.getMobileNo());
        tblDeviceInfo.setUuid(UUID.randomUUID().toString());
        tblDeviceInfoRepo.saveAndFlush(tblDeviceInfo);
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
    }

    @Override
    public HashMap<String, Object> updateDeviceRegistration(UpdateDeviceRequest updateDeviceRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        HashMap<String, Object> resp;
        Response response;
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByUsername(aeSencryption.encryptwith256(updateDeviceRequest.getUsername()));
        if (HelperClass.isNullOrEmpty(tblCustomerAll)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }
        TblAppUser tblAppUser = tblAppUserRepo.findByUsername(aeSencryption.encryptwith256(updateDeviceRequest.getUsername()));
        if (HelperClass.isNullOrEmpty(tblAppUser)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }

        HashMap<String, Object> hmClaims = new HashMap<>();

        hmClaims.put(JwtConstants.IMIE_NUMBER, tblCustomerAll.getImeiNo());
        hmClaims.put(JwtConstants.MOBILE_NUMBER, tblCustomerAll.getMobileNo());
        hmClaims.put(JwtConstants.UUID, tblCustomerAll.getUuid());
        hmClaims.put(JwtConstants.APP_USER_ID, tblAppUser.getAppUserId());
        TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName(Constants.LOG_OUT_TIME);
        if (tblGlobalConfig != null) {
            tokenExpiryTime = tblGlobalConfig.getKeyValue();
        }
        String token = jwtSecurity.createJWTWithClaims(aeSencryption.decrypt(tblCustomerAll.getMobileNo()), hmClaims, Integer.valueOf(tokenExpiryTime));
        TblAuthAccessToken tblAuthAccessToken = commonService.createLoginTokenSession(token, tblCustomerAll.getCustomerAllId(), tblCustomerAll.getMobileNo(), Constants.PRE_LOGIN, 1);
        if (tblAuthAccessToken == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.FAILED_TO_CREATE_SESSION.getResponseCode());
        }
        apiRequest.setImieNo(tblCustomerAll.getImeiNo());
        // need to change
        String mobileNumber = commonService.getMobileNumberByCustomerAllId(tblCustomerAll);
        response = thirdPartyService.generateOtp(mobileNumber, Constants.EMPTY, "R", "S", smsTemplateTypeFwr, "A", apiRequest, token);
        if (response != null) {
            if (response.getResponsecode().equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
                GenerateOtpResponse generateOtpResponse = fromJson(convertObjecttoJson(response.getData()), GenerateOtpResponse.class);
                resp = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), new MobileRegistrationResponse(token, generateOtpResponse));

            } else {
                resp = commonService.getResponse(response.getResponsecode(), null);

            }

        } else {
            resp = commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), null);

        }
        return resp;
    }

    @Override
    public HashMap<String, Object> generateOtpToResetPassword(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        HashMap<String, Object> resp;
        Response response;
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByUsername(aeSencryption.encryptwith256(resetPasswordRequest.getMobileNumber()));
        if (HelperClass.isNullOrEmpty(tblCustomerAll)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }
        TblAppUser tblAppUser = tblAppUserRepo.findByUsername(aeSencryption.encryptwith256(resetPasswordRequest.getMobileNumber()));
        if (HelperClass.isNullOrEmpty(tblAppUser)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }

        HashMap<String, Object> hmClaims = new HashMap<>();

        hmClaims.put(JwtConstants.IMIE_NUMBER, tblCustomerAll.getImeiNo());
        hmClaims.put(JwtConstants.MOBILE_NUMBER, tblCustomerAll.getMobileNo());
        hmClaims.put(JwtConstants.UUID, tblCustomerAll.getUuid());
        hmClaims.put(JwtConstants.APP_USER_ID, tblAppUser.getAppUserId());
        TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName(Constants.LOG_OUT_TIME);
        if (tblGlobalConfig != null) {
            tokenExpiryTime = tblGlobalConfig.getKeyValue();
        }
        String token = jwtSecurity.createJWTWithClaims(aeSencryption.decrypt(tblCustomerAll.getMobileNo()), hmClaims, Integer.valueOf(tokenExpiryTime));
        TblAuthAccessToken tblAuthAccessToken = commonService.createLoginTokenSession(token, tblCustomerAll.getCustomerAllId(), tblCustomerAll.getMobileNo(), Constants.PRE_LOGIN, 1);
        if (tblAuthAccessToken == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.FAILED_TO_CREATE_SESSION.getResponseCode());
        }
        apiRequest.setImieNo(tblCustomerAll.getImeiNo());
        String mobileNumber = commonService.getMobileNumberByCustomerAllId(tblCustomerAll);
        response = thirdPartyService.generateOtp(mobileNumber, Constants.EMPTY, "R", "S", smsTemplateTypeRp, "A", apiRequest, token);
        if (response != null) {
            if (response.getResponsecode().equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
                GenerateOtpResponse generateOtpResponse = fromJson(convertObjecttoJson(response.getData()), GenerateOtpResponse.class);
                resp = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), new MobileRegistrationResponse(token, generateOtpResponse));

            } else {
                resp = commonService.getResponse(response.getResponsecode(), null);

            }

        } else {
            resp = commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), null);

        }
        return resp;
    }

    @Override
    public HashMap<String, Object> verifyUpdateDeviceRegistration(VerifyDeviceRequest verifyDeviceRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        HashMap<String, Object> resp;
        Response response;
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        VerifyOtpRequest verifyOtpRequest = verifyDeviceRequest.getVerifyOtpRequest();
        RequestValidator.validateVerifyOtp(verifyOtpRequest);
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByUsername(aeSencryption.encryptwith256(verifyDeviceRequest.getMobileNumber()));
        if (HelperClass.isNullOrEmpty(tblCustomerAll)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }
        apiRequest.setImieNo(tblCustomerAll.getImeiNo());
        response = thirdPartyService.verifyOtp(verifyOtpRequest, apiRequest, token);
        if (response != null) {
            if (response.getResponsecode().equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
                VerifyOtpResponse verifyOtpResponse = fromJson(convertObjecttoJson(response.getData()), VerifyOtpResponse.class);
                TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo.findByMobileNo(tblCustomerAll.getMobileNo());
                if (HelperClass.isNullOrEmpty(tblDeviceInfo)) {
                    throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
                }
                tblCustomerAll.setAppVersion(verifyDeviceRequest.getAppVersion());
                tblCustomerAll.setUuid(UUID.randomUUID().toString());
                tblCustomerAll.setIpAddressA(verifyDeviceRequest.getIpAddressA());
                tblCustomerAll.setIpAddressP(verifyDeviceRequest.getIpAddressP());
                tblCustomerAll.setImeiNo(verifyDeviceRequest.getImeiNo());
                tblCustomerAll.setDeviceModel(verifyDeviceRequest.getDeviceModel());
                LkpChannel lkpChannel = lkpChannelRepo.findByChannelCodeAndIsActive(apiRequest.getChannel(), Constants.YES);
                if (lkpChannel != null) {
                    tblCustomerAll.setLkpChannel(lkpChannel);
                }
                tblCustomerAll.setOtpverified("Y");
                tblCustomerAll.setLastupdatedate(new Date());
                tblCustomerAll.setLastupdateuser(tblCustomerAll.getCreateuser());
                tblCustomerAll.setUpdateindex(tblCustomerAll.getUpdateindex() != null ? tblCustomerAll.getUpdateindex().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblCustomerAll = tblCustomerAllRepo.save(tblCustomerAll);
                tblDeviceInfo.setImeiNo(tblCustomerAll.getImeiNo());
                tblDeviceInfo.setUuid(tblCustomerAll.getUuid());
                tblDeviceInfo.setDeviceModel(tblCustomerAll.getDeviceModel());
                tblDeviceInfo.setLastupdatedate(new Date());
                tblDeviceInfo.setLastupdateuser(tblDeviceInfo.getCreateuser());
                tblDeviceInfo.setUpdateindex(tblDeviceInfo.getUpdateindex() != null ? tblDeviceInfo.getUpdateindex().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblDeviceInfoRepo.saveAndFlush(tblDeviceInfo);
                resp = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), verifyOtpResponse);
            } else {
                resp = commonService.getResponse(response.getResponsecode(), response.getData());
            }
        } else {
            resp = commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), null);
        }
        return resp;
    }

    @Override
    public HashMap<String, Object> verifyOtp(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        HashMap<String, Object> resp;
        Response response;
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        VerifyOtpRequest verifyOtpRequest = resetPasswordRequest.getVerifyOtpRequest();
        RequestValidator.validateVerifyOtp(verifyOtpRequest);
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByUsername(aeSencryption.encryptwith256(resetPasswordRequest.getMobileNumber()));
        if (HelperClass.isNullOrEmpty(tblCustomerAll)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }
        apiRequest.setImieNo(tblCustomerAll.getImeiNo());
        response = thirdPartyService.verifyOtp(verifyOtpRequest, apiRequest, token);
        if (response != null) {
            if (response.getResponsecode().equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
                VerifyOtpResponse verifyOtpResponse = fromJson(convertObjecttoJson(response.getData()), VerifyOtpResponse.class);
                resp = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), verifyOtpResponse);
            } else {
                resp = commonService.getResponse(response.getResponsecode(), response.getData());
            }
        } else {
            resp = commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), null);
        }
        return resp;
    }

    @Override
    public HashMap<String, Object> resetPassword(ResetPasswordRequest resetPasswordRequest, Request apiRequest, HttpServletRequest httpServletRequest) {
        HashMap<String, Object> resp;
        TblOtp tblOtp = tblOtpRepo.findByOtpsIdAndVerified(Long.valueOf(resetPasswordRequest.getVerifyOtpResponse().getOtpId()), resetPasswordRequest.getVerifyOtpResponse().getStatus());
        if (tblOtp == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.OTP_NOT_FOUND.getResponseCode());
        }
        TblAppUser tblAppUser = tblAppUserRepo.findByUsername(aeSencryption.encryptwith256(resetPasswordRequest.getMobileNumber()));
        if (HelperClass.isNullOrEmpty(tblAppUser)) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new CustomDataNotFoundException(GenericResponseCode.CONFIRM_PIN_NOT_MATCHED.getResponseCode());
        }
        tblAppUser.setPassword(commonService.encryptWithAes(resetPasswordRequest.getNewPassword()));
        tblAppUserRepo.saveAndFlush(tblAppUser);
        tblOtp.setVerified("C");
        tblOtpRepo.saveAndFlush(tblOtp);
        resp = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);

        return resp;
    }
}
