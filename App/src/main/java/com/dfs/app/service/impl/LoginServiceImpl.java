package com.dfs.app.service.impl;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.*;
import com.dfs.app.dto.common.*;
import com.dfs.app.model.*;
import com.dfs.app.repo.*;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.LoginService;
import com.dfs.app.service.ThirdPartyService;
import com.dfs.app.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
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
    private TblCustomerRepo tblCustomerRepo;
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
    private JWTSecurity jwtSecurity;
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Value("${jwt.expiry.time}")
    private String tokenExpiryTime;
    @Value("${unlock.time}")
    private String unlockTime;
    @Value("${sms.template.Type}")
    private String smsTemplateTypeFwr;
    @Value("${account.type.code}")
    private String accountTypeWallet;
    @Value("${sms.template.type.reset.pin}")
    private String smsTemplateTypeRp;
    @Autowired
    private TblDeviceInfoRepo tblDeviceInfoRepo;
    @Autowired
    private LkpChannelRepo lkpChannelRepo;

    @Autowired
    private TblOtpRepo tblOtpRepo;


    @Autowired
    private LkpDocumentTypeRepo lkpDocumentTypeRepo;

    @Value("${doc.selfie.code}")
    private String docSelfieCode;

    @Value("${base.url}")
    private String baseUrl;

    @Autowired
    private TblDocumentRepo tblDocumentRepo;
    @Autowired
    private TblLoginTokenRepo tblLoginTokenRepo;
    @Autowired
    private TblMutlilanguageRepo tblMutlilanguageRepo;


    @Override
    public HashMap<String, Object> login(LoginRequest loginRequest, Request request) {
        TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName(Constants.UNLOCK_TIME);
        if (tblGlobalConfig != null) {
            unlockTime = tblGlobalConfig.getKeyValue();
        }

        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByUsername(aeSencryption.encryptwith256(loginRequest.getUsername()));
        if (HelperClass.isNullOrEmpty(tblCustomerAll)) {
            throw new CustomDataNotFoundException(GenericResponseCode.SIGN_UP_FIRST.getResponseCode());
        }
        if (HelperClass.isNullOrEmpty(tblCustomerAll.getImeiNo()) || !request.getImieNo().equals(tblCustomerAll.getImeiNo())) {
            throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_INVALID.getResponseCode());
        }
        TblAppUser tblAppUser = tblAppUserRepo.findAppUserByUserName(aeSencryption.encryptwith256(loginRequest.getUsername()));
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_USERNAME.getResponseCode());
        }

        // password will be check here
        validatePassword(tblAppUser, loginRequest.getPassword(), unlockTime);

        if (HelperClass.isNullOrEmpty(tblCustomerAll.getImeiNo()) || !request.getImieNo().equals(tblCustomerAll.getImeiNo())) {
            throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_INVALID.getResponseCode());
        }
        TblCustomer tblCustomer = tblCustomerRepo.findByIdAndIsActive(tblAppUser.getCustomerId().longValue(), Constants.YES);
        if (tblCustomer == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        TblAccount tblAccount = tblAccountRepo.findByCustomerIdAccountTypeCodeAccountRegType(tblCustomer.getCustomerId(), loginRequest.getAccountTypeCode(), loginRequest.getAccountRegTypeCode());
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
        HashMap<String, Object> hmClaims = new HashMap<>();
        hmClaims.put(JwtConstants.IMIE_NUMBER, tblCustomerAll.getImeiNo());
        hmClaims.put(JwtConstants.MOBILE_NUMBER, tblAccount.getAccountNo());
        hmClaims.put(JwtConstants.UUID, tblCustomerAll.getUuid());
        hmClaims.put(JwtConstants.APP_USER_ID, tblAppUser.getAppUserId());
        tblGlobalConfig = tblGlobalConfigRepo.findByKeyName(Constants.LOG_OUT_TIME);
        if (tblGlobalConfig != null) {
            tokenExpiryTime = tblGlobalConfig.getKeyValue();
        }
        String sessionToken = null;
        String token = jwtSecurity.createJWTWithClaims(tblCustomerAll.getMobileNo(), hmClaims, Integer.parseInt(tokenExpiryTime));
        if (isNullOrEmpty(tblAppUser.getTwoFAEnabled()) || "N".equals(tblAppUser.getTwoFAEnabled())) {
            sessionToken = jwtSecurity.createJWTWithClaimsForDays(tblAccount.getAccountNo(), hmClaims, 7);
            TblLoginToken tblLoginToken = tblLoginTokenRepo.findByAppUserIdAndIsActive(tblAppUser.getAppUserId(), Constants.YES);
            if (tblLoginToken != null) {
                tblLoginToken.setIsActive(Constants.N);
                tblLoginToken.setLastupdateuser(tblAppUser.getUserId());
                tblLoginToken.setLastupdatedate(new Date());
                tblLoginToken.setUpdateindex(tblLoginToken.getUpdateindex() != null ? tblLoginToken.getUpdateindex().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblLoginTokenRepo.saveAndFlush(tblLoginToken);
            }
            tblLoginToken = new TblLoginToken();
            tblLoginToken.setCreatedate(new Date());
            tblLoginToken.setCreateuser(BigDecimal.valueOf(tblAppUser.getAppUserId()));
            tblLoginToken.setLoginToken(sessionToken);
            Calendar calendar = Calendar.getInstance();
            // Set current date-time as token effective from
            tblLoginToken.setIsActive(Constants.YES);
            tblLoginToken.setEffectiveFrom(calendar.getTime());
            // Add 7 days to current time for token expiration
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            tblLoginToken.setEffectiveTo(calendar.getTime());
            tblLoginToken.setTblAppUser(tblAppUser);
            tblLoginTokenRepo.saveAndFlush(tblLoginToken);
        }
        TblAuthAccessToken tblAuthAccessToken = commonService.createLoginTokenSession(token, tblAppUserLoginHistory.getAppUserLoginHistoryId(), tblAccount.getAccountNo(), Constants.AFTER_LOGIN, tblAppUser.getAppUserId());

        if (tblAuthAccessToken == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.FAILED_TO_CREATE_SESSION.getResponseCode());
        }

        loginResponse.setToken(token);
        loginResponse.setCustomerRiskProfile(tblCustomer.getRiskProfile());
        loginResponse.setAutoLoginToken(sessionToken);
        loginResponse.setAppUserId(tblAppUser.getAppUserId());
        loginResponse.setAccountId(tblAccount.getAccountId());
        loginResponse.setAccountNo(tblAccount.getAccountNo());
        loginResponse.setUsername(aeSencryption.decrypt(tblAppUser.getUsername()));
        LkpDocumentType lkpDocumentType = lkpDocumentTypeRepo.findByDocumentTypeCode(docSelfieCode);
        if (lkpDocumentType != null) {
            TblDocument tblDocument = tblDocumentRepo.findByDocumentTypeIdAndAppUserIdAndAccountLevelId(lkpDocumentType.getDocumentTypeId(), tblAppUser.getAppUserId(), tblAccount.getTblAccountLevel().getAccountLevelId());
            if (tblDocument != null) {
                loginResponse.setSelfie(baseUrl + tblDocument.getDocumentPath());
            }
        }
        TblMultilanguage tblMultilanguage = tblMutlilanguageRepo.findByColumnNameAndAndRefId("FULL_NAME", new BigDecimal(tblCustomer.getCustomerId()));
        loginResponse.setAccountStatusDescr(tblAccount.getLkpAccountStatus().getAccountStatusDescr());
        loginResponse.setAccountStatusId(tblAccount.getLkpAccountStatus().getAccountStatusId());
        loginResponse.setAccountTitle(aeSencryption.decrypt(tblAccount.getAccountTitle()));
        loginResponse.setAccountTitleDari(tblMultilanguage != null ? tblMultilanguage.getValue() : aeSencryption.decrypt(tblAccount.getAccountTitle()));
        loginResponse.setAccountType(tblAccount.getLkpAccountType().getAccountTypeDescr());
        loginResponse.setDfsId(tblCustomer.getCustomerUid());
        loginResponse.setCurrentBalance(tblAccount.getCurrentBalance());
        loginResponse.setDailyCrLmt(tblAccount.getDailyAmtLimitCr());
        loginResponse.setDailyDrLmt(tblAccount.getDailyAmtLimitDr());
        loginResponse.setMonthlyCrLmt(tblAccount.getMonthlyAmtLimitCr());
        loginResponse.setMonthlyDrLmt(tblAccount.getMonthlyAmtLimitDr());
        loginResponse.setYearlyCrLmt(tblAccount.getYearlyAmtLimitCr());
        loginResponse.setYearlyDrLmt(tblAccount.getYearlyAmtLimitDr());
        loginResponse.setCustomerId(tblCustomer.getCustomerId());
        loginResponse.setEmail(aeSencryption.decrypt(tblCustomer.getEmail()));
        loginResponse.setIban(tblAccount.getIban());
        loginResponse.setLevelDescr(tblAccount.getTblAccountLevel().getAccountLevelDescr());
        loginResponse.setNidNo(aeSencryption.decrypt(tblCustomer.getNidNo()));
        loginResponse.setBiometricallyVerified(tblCustomerAll.getBioverified());
        loginResponse.setAccountLevelCode(tblAccount.getTblAccountLevel().getAccountLevelCode());
        loginResponse.setQr(tblAccount.getQrCode());
        loginResponse.setFatherName(aeSencryption.decrypt(tblCustomer.getFatherName()));
        loginResponse.setGrandFatherName(aeSencryption.decrypt(tblCustomer.getGrandfatherName()));
        loginResponse.setTwoFAEnabled(tblAppUser.getTwoFAEnabled());
        loginResponse.setExpiry(tblCustomer.getNidExpiryDate());
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), loginResponse);
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

    @Override
    public HashMap<String, Object> logout(LogoutRequest logoutRequest, BigDecimal request) {
        TblAuthAccessToken tblAuthAccessToken = tblAuthAccessTokenRepo.findByAccountNoAndIsActiveY(logoutRequest.getMobileNumber());
        if (tblAuthAccessToken != null) {
            tblAuthAccessToken.setIsActive(Constants.N);
            tblAuthAccessToken.setLastupdatedate(new Date());
            tblAuthAccessToken.setLastupdateuser(request);
            tblAuthAccessTokenRepo.saveAndFlush(tblAuthAccessToken);
        }
        TblLoginToken tblLoginToken = tblLoginTokenRepo.findByAccountNoAndIsActive(logoutRequest.getMobileNumber(), Constants.YES);
        if (tblLoginToken != null) {
            tblLoginToken.setIsActive(Constants.N);
            tblLoginToken.setLastupdatedate(new Date());
            tblLoginToken.setLastupdateuser(request);
            tblLoginTokenRepo.saveAndFlush(tblLoginToken);
        }
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
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
        response = thirdPartyService.generateOtp(mobileNumber, Constants.EMPTY, "R", "S", smsTemplateTypeFwr, "C", apiRequest, token);
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
        response = thirdPartyService.generateOtp(mobileNumber, Constants.EMPTY, "R", "S", smsTemplateTypeRp, "C", apiRequest, token);
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

    @Override
    public HashMap<String, Object> validateLoginSession(ValidateSessionRequest validateSessionRequest, Request request) {

        String authHeader = validateSessionRequest.getAutoLoginToken();
        if (isNullOrEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }
        String token = authHeader.substring(7);

        Claims claims = jwtSecurity.parseJWT(token);
        if (claims == null) {

            TblAppUser tblAppUser = tblAppUserRepo.findByAccountNo(validateSessionRequest.getAccountNo());
            if (tblAppUser != null) {
                tblAuthAccessTokenRepo.deactivateTokens(String.valueOf(tblAppUser.getAppUserId()), Constants.EMPTY);
            }
            throw new AuthenticationException(GenericResponseCode.SESSION_EXPIRED.getResponseCode());

        }

        // Extract user details from claims
        String imie = (String) claims.get(JwtConstants.IMIE_NUMBER);
        if (!imie.equals(request.getImieNo())) {
            throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }
        BigDecimal appUserId = new BigDecimal((Integer) claims.get(JwtConstants.APP_USER_ID));
        TblAppUser tblAppUser = tblAppUserRepo.findById(appUserId.longValue())
                .orElseThrow(() -> new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode()));


        int n = tblLoginTokenRepo.authorizeLoginToken(appUserId.longValue(), authHeader);
        if (n != 1) {
            throw new AuthenticationException(GenericResponseCode.SESSION_EXPIRED.getResponseCode());
        }
        TblCustomer tblCustomer = tblCustomerRepo.findByIdAndIsActive(tblAppUser.getCustomerId().longValue(), Constants.YES);
        if (tblCustomer == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        TblAccount tblAccount = tblAccountRepo.findByAccountNoAndAccountTypeCode(validateSessionRequest.getAccountNo(), accountTypeWallet);
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        LoginResponse loginResponse = new LoginResponse();
        HashMap<String, Object> hmClaims = new HashMap<>();

        hmClaims.put(JwtConstants.IMIE_NUMBER, tblCustomer.getTblCustomerAll().getImeiNo());
        hmClaims.put(JwtConstants.MOBILE_NUMBER, tblAccount.getAccountNo());
        hmClaims.put(JwtConstants.APP_USER_ID, tblAppUser.getAppUserId());
        hmClaims.put(JwtConstants.UUID, tblCustomer.getTblCustomerAll().getUuid());
        TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName(Constants.LOG_OUT_TIME);
        if (tblGlobalConfig != null) {
            tokenExpiryTime = tblGlobalConfig.getKeyValue();
        }


        String token1 = jwtSecurity.createJWTWithClaims(tblAccount.getAccountNo(), hmClaims, Integer.parseInt(tokenExpiryTime));
        TblAppUserLoginHistory tblAppUserLoginHistory = tblAppUserLoginHistoryRepo.getLatestAppUserByAppUserId(tblAppUser.getAppUserId());
        if (tblAppUserLoginHistory == null) {
            tblAppUserLoginHistory = new TblAppUserLoginHistory();
            tblAppUserLoginHistory.setTblAppUser(tblAppUser);
            tblAppUserLoginHistory.setLoginDate(new Date());
            tblAppUserLoginHistoryRepo.saveAndFlush(tblAppUserLoginHistory);
        }
        TblAuthAccessToken tblAuthAccessToken = commonService.createLoginTokenSession(token1, tblAppUserLoginHistory.getAppUserLoginHistoryId(), tblAccount.getAccountNo(), Constants.AFTER_LOGIN, tblAppUser.getAppUserId());

        if (tblAuthAccessToken == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.FAILED_TO_CREATE_SESSION.getResponseCode());
        }
        tblAppUser.setFireBaseToken(validateSessionRequest.getFireBaseToken());
        tblAppUserRepo.saveAndFlush(tblAppUser);
        loginResponse.setToken(token1);
        loginResponse.setAutoLoginToken(authHeader);
        loginResponse.setAppUserId(tblAppUser.getAppUserId());
        loginResponse.setAccountId(tblAccount.getAccountId());
        loginResponse.setAccountNo(tblAccount.getAccountNo());
        loginResponse.setUsername(aeSencryption.decrypt(tblAppUser.getUsername()));
        LkpDocumentType lkpDocumentType = lkpDocumentTypeRepo.findByDocumentTypeCode(docSelfieCode);
        if (lkpDocumentType != null) {
            TblDocument tblDocument = tblDocumentRepo.findByDocumentTypeIdAndAppUserIdAndAccountLevelId(lkpDocumentType.getDocumentTypeId(), tblAppUser.getAppUserId(), tblAccount.getTblAccountLevel().getAccountLevelId());
            if (tblDocument != null) {
                loginResponse.setSelfie(baseUrl + tblDocument.getDocumentPath());
            }
        }
        TblMultilanguage tblMultilanguage = tblMutlilanguageRepo.findByColumnNameAndAndRefId("FULL_NAME", new BigDecimal(tblCustomer.getCustomerId()));
        loginResponse.setAccountStatusDescr(tblAccount.getLkpAccountStatus().getAccountStatusDescr());
        loginResponse.setAccountStatusId(tblAccount.getLkpAccountStatus().getAccountStatusId());
        loginResponse.setAccountTitle(aeSencryption.decrypt(tblAccount.getAccountTitle()));
        loginResponse.setAccountTitleDari(tblMultilanguage != null ? tblMultilanguage.getValue() : aeSencryption.decrypt(tblAccount.getAccountTitle()));
        loginResponse.setAccountType(tblAccount.getLkpAccountType().getAccountTypeDescr());
        loginResponse.setDfsId(tblCustomer.getCustomerUid());
        loginResponse.setCurrentBalance(tblAccount.getCurrentBalance());
        loginResponse.setDailyCrLmt(tblAccount.getDailyAmtLimitCr());
        loginResponse.setDailyDrLmt(tblAccount.getDailyAmtLimitDr());
        loginResponse.setMonthlyCrLmt(tblAccount.getMonthlyAmtLimitCr());
        loginResponse.setMonthlyDrLmt(tblAccount.getMonthlyAmtLimitDr());
        loginResponse.setYearlyCrLmt(tblAccount.getYearlyAmtLimitCr());
        loginResponse.setYearlyDrLmt(tblAccount.getYearlyAmtLimitDr());
        loginResponse.setCustomerId(tblCustomer.getCustomerId());
        loginResponse.setIban(tblAccount.getIban());
        loginResponse.setLevelDescr(tblAccount.getTblAccountLevel().getAccountLevelDescr());
        loginResponse.setNidNo(aeSencryption.decrypt(tblCustomer.getNidNo()));
        loginResponse.setBiometricallyVerified(tblCustomer.getTblCustomerAll().getBioverified());
        loginResponse.setAccountLevelCode(tblAccount.getTblAccountLevel().getAccountLevelCode());
        loginResponse.setQr(tblAccount.getQrCode());
        loginResponse.setFatherName(aeSencryption.decrypt(tblCustomer.getFatherName()));
        loginResponse.setGrandFatherName(aeSencryption.decrypt(tblCustomer.getGrandfatherName()));
        loginResponse.setEmail(aeSencryption.decrypt(tblCustomer.getEmail()));
        loginResponse.setCustomerRiskProfile(tblCustomer.getRiskProfile());
        loginResponse.setExpiry(tblCustomer.getNidExpiryDate());
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), loginResponse);


    }
}
