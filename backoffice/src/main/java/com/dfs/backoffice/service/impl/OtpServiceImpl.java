package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.*;
import com.dfs.backoffice.repo.*;
import com.dfs.backoffice.service.CommonService;
import com.dfs.backoffice.service.NotificationService;
import com.dfs.backoffice.service.OtpService;
import com.dfs.backoffice.utils.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OtpServiceImpl extends HelperClass implements OtpService {
    @Autowired
    private LkpOtpTypeRepo lkpOtpTypeRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TblUserRepo tblUserRepo;
    @Autowired
    private TblOtpRepo tblOtpRepo;
    @Autowired
    private TblSmsMessageTemplateRepo tblSmsMessageTemplateRepo;
    @Autowired
    private TblSmsMessageRepo tblSmsMessageRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private NotificationService notificationService;


    @Override
    public Response generateOtp(GenerateOtpRequest generateOtpRequest) {
        Response response = new Response();
        TblUser tblUser = tblUserRepo.getUserByMobileNo(generateOtpRequest.getMobileNumber());
        if (tblUser == null) {
            throw new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        TblAppUser tblAppUsers = tblAppUserRepo.findByUserId(new BigDecimal(tblUser.getUserId()));
        if (tblAppUsers == null) {
            throw new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        LkpOtpType lkpOtpType = lkpOtpTypeRepo.findOtpTypeByOtpCode(generateOtpRequest.getOtpType());
        if (lkpOtpType == null) {
            throw new CustomException(GenericResponseCode.OTP_TYPE_NOT_FOUND.getResponseCode());
        }
        TblOtp tblOtp = tblOtpRepo.findOtpByAppUserIdAndOtpType(lkpOtpType.getOtpType(), tblAppUsers.getAppUserId());

        if (tblOtp != null && tblOtp.getEffectiveTo().compareTo(new Date()) < 0) {
            tblOtp.setVerified("E");
            tblOtpRepo.saveAndFlush(tblOtp);
        }
        if (tblOtp != null) {
            tblOtp.setVerified("E");
            tblOtpRepo.saveAndFlush(tblOtp);
        }

        tblOtp = new TblOtp();
        tblOtp.setTblAppUser(tblAppUsers);
        String otp = OtpGenerator.generateOtp(lkpOtpType.getRegularExpression());
        tblOtp.setOtpin(aeSencryption.encryptwith256(otp));
        tblOtp = saveOTP(tblOtp, lkpOtpType);
        if (tblOtp != null) {
            GenerateOtpResponse generateOtpResponse = new GenerateOtpResponse();
            generateOtpResponse.setExpiryDateTime(tblOtp.getEffectiveTo().toString());
            generateOtpResponse.setMobileNumber(generateOtpRequest.getMobileNumber());
            generateOtpResponse.setOtpCode(otp);
            generateOtpResponse.setOtpType(generateOtpRequest.getOtpType());
            generateOtpResponse.setEmail(generateOtpRequest.getEmail());
            generateOtpResponse.setOtpId(String.valueOf(tblOtp.getOtpsId()));
            generateOtpResponse.setStatus(tblOtp.getVerified());
            if (!generateOtpRequest.getOtpType().equals("LGE") && !generateOtpRequest.getOtpType().equals("EOV")) {
                TblSmsMessageTemplate tblSmsMessageTemplate = tblSmsMessageTemplateRepo.findByIdentifierAndDocTypeCode(generateOtpRequest.getOtpIdentifier(), generateOtpRequest.getOtpTemplateCode());
                if (tblSmsMessageTemplate != null) {
                    TblSmsMessage tblSmsMessage = new TblSmsMessage();
                    tblSmsMessage.setMobileNo(generateOtpRequest.getMobileNumber());
                    tblSmsMessage.setSendFlag("0");
                    tblSmsMessage.setCreatedate(new Date());
                    tblSmsMessage.setCreateuser(new BigDecimal(tblAppUsers.getAppUserId()));
                    tblSmsMessage.setSmsMessageTemplateId(BigDecimal.valueOf(tblSmsMessageTemplate.getSmsMessageTemplateId()));
                    String sms = tblSmsMessageTemplate.getMessageTemplate();
                    sms = sms.replace("%OTP%", otp);
                    tblSmsMessage.setMessage(sms);
                    tblSmsMessageRepo.saveAndFlush(tblSmsMessage);
                    setResponse(response, Constants.ONE, generateOtpResponse, GenericResponseCode.SUCCESS.getResponseCode());
                } else {
                    generateOtpResponse.setOtpCode(otp);
                    setResponse(response, Constants.ONE, generateOtpResponse, GenericResponseCode.SUCCESS.getResponseCode());
                }
            } else {
                GenerateNotificationRequest generateNotificationRequest = new GenerateNotificationRequest();
                generateNotificationRequest.setEmail(tblUser.getEmail());
                generateNotificationRequest.setSubject("DFS Email Verification");
                generateNotificationRequest.setSms("User Verification Code is " + generateOtpResponse.getOtpCode());
                generateNotificationRequest.setType("E");
                notificationService.notify(generateNotificationRequest, new BigDecimal(tblAppUsers.getAppUserId()));
                setResponse(response, Constants.ONE, generateOtpResponse, GenericResponseCode.SUCCESS.getResponseCode());
            }

        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.FAILED_TO_GENERATE_OTP.getResponseCode());
        }

        return response;
    }

    @Override
    public Response verifyOtp(VerifyOtpRequest verifyOtpRequest, BigDecimal userId) {
        Response response = new Response();
        LkpOtpType lkpOtpType = lkpOtpTypeRepo.findOtpTypeByOtpCode(verifyOtpRequest.getOtpType());
        if (lkpOtpType == null) {
            throw new CustomException(GenericResponseCode.OTP_TYPE_NOT_FOUND.getResponseCode());
        }

        TblOtp tblOtp = tblOtpRepo.findOtpByIdAndType(lkpOtpType.getOtpType(), verifyOtpRequest.getOtpId(), userId.longValue());

        if (tblOtp == null) {
            throw new CustomException(GenericResponseCode.OTP_NOT_FOUND.getResponseCode());
        }
        if (tblOtp.getEffectiveTo().compareTo(new Date()) < 0) {
            tblOtp.setVerified("E");
            tblOtpRepo.saveAndFlush(tblOtp);
        }
        if (tblOtp.getVerified().equalsIgnoreCase("E")) {
            throw new CustomException(GenericResponseCode.OTP_EXPIRED.getResponseCode());
        }
        if (tblOtp.getVerified().equalsIgnoreCase("V")) {
            throw new CustomException(GenericResponseCode.OTP_ALREADY_VERIFIED.getResponseCode());
        }
        if (tblOtp.getOtpTries().longValue() <= lkpOtpType.getOtpAttempts().longValue()) {
            if (tblOtp.getOtpin().equalsIgnoreCase(aeSencryption.encryptwith256(verifyOtpRequest.getOtpPin()))) {
                tblOtp.setVerified("V");
                tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblOtp.setLastupdatedate(new Date());
                tblOtp.setLastupdateuser(userId);
                tblOtp = tblOtpRepo.saveAndFlush(tblOtp);
                VerifyOtpResponse verifyOtpResponse = new VerifyOtpResponse();
                verifyOtpResponse.setTries(String.valueOf(tblOtp.getOtpTries()));
                verifyOtpResponse.setOtpId(String.valueOf(tblOtp.getOtpsId()));
                verifyOtpResponse.setStatus(tblOtp.getVerified());
                setResponse(response, Constants.ONE, verifyOtpResponse, GenericResponseCode.SUCCESS.getResponseCode());

            } else {
                tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblOtp.setLastupdatedate(new Date());
                tblOtp.setLastupdateuser(userId);
                tblOtpRepo.saveAndFlush(tblOtp);
                throw new CustomException(GenericResponseCode.WRONG_OTP.getResponseCode());
            }
        } else {
            tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
            tblOtp.setVerified("L");
            tblOtp.setLastupdatedate(new Date());
            tblOtp.setLastupdateuser(userId);
            tblOtpRepo.saveAndFlush(tblOtp);
            throw new CustomException(GenericResponseCode.OTP_LIMIT_EXCEEDS.getResponseCode());
        }
        return response;
    }

    public TblOtp saveOTP(TblOtp tblOtp, LkpOtpType lkpOtpType) {

        tblOtp.setOtpType(lkpOtpType.getOtpType());
        tblOtp.setEffectiveFrom(new Date());
        Date newDate = DateUtils.addMinutes(tblOtp.getEffectiveFrom(), lkpOtpType.getExpiryMinutes().intValue());
        tblOtp.setEffectiveTo(newDate);
        tblOtp.setOtpTries(new BigDecimal(0));
        tblOtp.setCreateuser(new BigDecimal(1));
        tblOtp.setCreatedate(new Date());
        tblOtp.setVerified("A");
        return tblOtpRepo.save(tblOtp);

    }

    @Override
    public Response verifyOtpForgotPassword(VerifyOtpRequest verifyOtpRequest) {
        Response response = new Response();
        TblUser tblUser = tblUserRepo.findByEmail(verifyOtpRequest.getEmail());
        TblAppUser tblAppUser = tblAppUserRepo.findByUserId(new BigDecimal(tblUser.getUserId()));
        LkpOtpType lkpOtpType = lkpOtpTypeRepo.findOtpTypeByOtpCode(verifyOtpRequest.getOtpType());
        if (lkpOtpType == null) {
            throw new CustomException(GenericResponseCode.OTP_TYPE_NOT_FOUND.getResponseCode());
        }

        TblOtp tblOtp = tblOtpRepo.findOtpByIdAndType(lkpOtpType.getOtpType(), verifyOtpRequest.getOtpId(), tblAppUser.getAppUserId());
        if (tblOtp == null) {
            throw new CustomException(GenericResponseCode.OTP_NOT_FOUND.getResponseCode());
        }
        if (tblOtp.getEffectiveTo().compareTo(new Date()) < 0) {
            tblOtp.setVerified("E");
            tblOtpRepo.saveAndFlush(tblOtp);
        }
        if (tblOtp.getVerified().equalsIgnoreCase("E")) {
            throw new CustomException(GenericResponseCode.OTP_EXPIRED.getResponseCode());
        }
        if (tblOtp.getVerified().equalsIgnoreCase("V")) {
            throw new CustomException(GenericResponseCode.OTP_ALREADY_VERIFIED.getResponseCode());
        }
        if (tblOtp.getOtpTries().longValue() <= lkpOtpType.getOtpAttempts().longValue()) {
            if (tblOtp.getOtpin().equalsIgnoreCase(aeSencryption.encryptwith256(verifyOtpRequest.getOtpPin()))) {
                tblOtp.setVerified("V");
                tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblOtp.setLastupdatedate(new Date());
                tblOtp.setLastupdateuser(new BigDecimal(tblAppUser.getAppUserId()));
                tblOtp = tblOtpRepo.saveAndFlush(tblOtp);
                VerifyOtpResponse verifyOtpResponse = new VerifyOtpResponse();
                verifyOtpResponse.setTries(String.valueOf(tblOtp.getOtpTries()));
                verifyOtpResponse.setOtpId(String.valueOf(tblOtp.getOtpsId()));
                verifyOtpResponse.setStatus(tblOtp.getVerified());

                String password = autoGenerate("Numeric", 6);
                String encPassword = aeSencryption.encryptwith256(password);
                String msg = "Your Password has been reset. Your login password is " + password + ". Kindly login and change your password";
                tblAppUser.setPassword(encPassword);
                tblAppUser.setPasswordUpdateFlag(Constants.NO);
                tblAppUser.setUserSms(msg);
                tblAppUserRepo.save(tblAppUser);
                GenerateNotificationRequest generateNotificationRequest = new GenerateNotificationRequest();
                generateNotificationRequest.setEmail(tblUser.getEmail());
                generateNotificationRequest.setSubject("DFS Email Verification");
                generateNotificationRequest.setSms("Your password for Login is " + password + ". Kindly login and change your password");
                generateNotificationRequest.setType("E");
                notificationService.notify(generateNotificationRequest, new BigDecimal(tblAppUser.getAppUserId()));
                setResponse(response, Constants.ONE, verifyOtpResponse, GenericResponseCode.SUCCESS.getResponseCode(), "Password has been sent to your email");
            } else {
                tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblOtp.setLastupdatedate(new Date());
                tblOtp.setLastupdateuser(new BigDecimal(tblAppUser.getAppUserId()));
                tblOtpRepo.saveAndFlush(tblOtp);
                throw new CustomException(GenericResponseCode.WRONG_OTP.getResponseCode());
            }
        } else {
            tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
            tblOtp.setVerified("L");
            tblOtp.setLastupdatedate(new Date());
            tblOtp.setLastupdateuser(new BigDecimal(tblAppUser.getAppUserId()));
            tblOtpRepo.saveAndFlush(tblOtp);
            throw new CustomException(GenericResponseCode.OTP_LIMIT_EXCEEDS.getResponseCode());
        }
        return response;
    }
}
