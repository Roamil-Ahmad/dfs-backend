package com.dfs.thirdparties.service.impl;

import com.dfs.thirdparties.commons.OtpGenerator;
import com.dfs.thirdparties.dto.GenerateOtpRequest;
import com.dfs.thirdparties.dto.GenerateOtpResponse;
import com.dfs.thirdparties.dto.VerifyOtpRequest;
import com.dfs.thirdparties.dto.VerifyOtpResponse;
import com.dfs.thirdparties.model.*;
import com.dfs.thirdparties.repo.*;
import com.dfs.thirdparties.service.CommonService;
import com.dfs.thirdparties.service.NotificationService;
import com.dfs.thirdparties.service.OtpService;
import com.dfs.thirdparties.util.AESencryption;
import com.dfs.thirdparties.util.CustomDataNotFoundException;
import com.dfs.thirdparties.util.CustomException;
import com.dfs.thirdparties.util.GenericResponseCode;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Service
public class OtpServiceImpl implements OtpService {
    @Autowired
    private LkpOtpTypeRepo lkpOtpTypeRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblOtpRepo tblOtpRepo;
    @Value("${sms.template.Type}")
    private String otpRegistrationTemplateCode;
    @Autowired
    private TblSmsMessageTemplateRepo tblSmsMessageTemplateRepo;
    @Autowired
    private TblDeviceInfoRepo tblDeviceInfoRepo;
    @Autowired
    private TblSmsMessageRepo tblSmsMessageRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private TblCustomerAllRepo tblCustomerAllRepo;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;


    @Override
    public HashMap<String, Object> generateOtp(GenerateOtpRequest generateOtpRequest, BigDecimal userId) {
        HashMap<String, Object> response = new HashMap<>();
        TblOtp tblOtp = null;
        LkpOtpType lkpOtpType = lkpOtpTypeRepo.findOtpTypeByOtpCode(generateOtpRequest.getOtpType());
        if (lkpOtpType == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.OTP_TYPE_NOT_FOUND.getResponseCode());
        }

        if (generateOtpRequest.getUserType().equals("C")) {
            tblOtp = setTblOtpForCustomer(generateOtpRequest, lkpOtpType);
        } else if (generateOtpRequest.getUserType().equals("A")) {
            tblOtp = setTblOtpForAgent(generateOtpRequest, lkpOtpType);
        }
        String otp = OtpGenerator.generateOtp(lkpOtpType.getRegularExpression());
        tblOtp.setOtpin(aeSencryption.encryptwith256(otp));
        saveOTP(tblOtp, lkpOtpType);
        if (tblOtp != null) {
            GenerateOtpResponse generateOtpResponse = new GenerateOtpResponse();
            generateOtpResponse.setExpiryDateTime(tblOtp.getEffectiveTo().toString());
            generateOtpResponse.setMobileNumber(generateOtpRequest.getMobileNumber());
            generateOtpResponse.setEmail(generateOtpRequest.getEmail());
            generateOtpResponse.setOtpCode(otp);
            generateOtpResponse.setOtpId(String.valueOf(tblOtp.getOtpsId()));
            generateOtpResponse.setStatus(tblOtp.getVerified());
            TblSmsMessageTemplate tblSmsMessageTemplate = tblSmsMessageTemplateRepo.findByIdentifierAndDocTypeCode(generateOtpRequest.getIdentifier(), generateOtpRequest.getSmsTemplateCode());
            if (tblSmsMessageTemplate != null) {
                if (generateOtpRequest.getOtpType().charAt(0) == 'E') {
                    String sms = tblSmsMessageTemplate.getMessageTemplate();
                    sms = sms.replace("%OTP%", otp);
                    sms = sms.replace("<#>", generateOtpRequest.getEmail());
                    String deviceInfo = tblDeviceInfoRepo.getDeviceInfo(aeSencryption.encryptwith256(generateOtpRequest.getMobileNumber()));
                    sms = sms.replace("%DMODEL%", deviceInfo != null ? deviceInfo : "");
                    if (notificationService.sendSimpleEmail(generateOtpRequest.getEmail(), tblSmsMessageTemplate.getTblTransDoc().getTransDocsDescr(), sms)) {
                        response = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), generateOtpResponse);
                    } else {
                        throw new CustomException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
                    }

                } else {
                    TblSmsMessage tblSmsMessage = new TblSmsMessage();
                    tblSmsMessage.setMobileNo(generateOtpRequest.getMobileNumber());
                    tblSmsMessage.setSendFlag("1");
                    tblSmsMessage.setCreatedate(new Date());
                    tblSmsMessage.setCreateuser(userId);
                    tblSmsMessage.setSmsMessageTemplateId(BigDecimal.valueOf(tblSmsMessageTemplate.getSmsMessageTemplateId()));
                    String sms = tblSmsMessageTemplate.getMessageTemplate();
                    sms = sms.replace("%OTP%", otp);
                    sms = sms.replace("<#>", generateOtpRequest.getMobileNumber());
                    String deviceInfo = tblDeviceInfoRepo.getDeviceInfo(aeSencryption.encryptwith256(generateOtpRequest.getMobileNumber()));
                    sms = sms.replace("%DMODEL%", deviceInfo != null ? deviceInfo : "");
                    tblSmsMessage.setMessage(sms);
                    tblSmsMessageRepo.saveAndFlush(tblSmsMessage);
                    response = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), generateOtpResponse);
                }

            } else {
                generateOtpResponse.setOtpCode(otp);
                response = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), generateOtpResponse);
            }
        } else {
            response = commonService.getResponse(GenericResponseCode.FAILED_TO_GENERATE_OTP.getResponseCode(), null);

        }


        return response;
    }

    private TblOtp setTblOtpForAgent(GenerateOtpRequest generateOtpRequest, LkpOtpType lkpOtpType) {

        TblOtp tblOtp;
        Long agentId = null;
        TblCustomerAll customerAllId = tblCustomerAllRepo.findByMobileNo(aeSencryption.encryptwith256(generateOtpRequest.getMobileNumber()));
        if (customerAllId == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        agentId = tblAppUserRepo.getAgentIdByCustomerAllId(customerAllId.getCustomerAllId());


        if (agentId != null) {
            tblOtp = tblOtpRepo.findOtpByAgentIdAndOtpType(lkpOtpType.getOtpType(), agentId);
        } else {
            tblOtp = tblOtpRepo.findOtpByCustomerAllIdAndOtpType(lkpOtpType.getOtpType(), customerAllId.getCustomerAllId());
        }
        if (tblOtp != null && tblOtp.getEffectiveTo().compareTo(new Date()) < 0) {
            tblOtp.setVerified("E");
            tblOtpRepo.saveAndFlush(tblOtp);
            tblOtp = null;
        }
        if (tblOtp == null) {
            tblOtp = new TblOtp();
        }


        if (agentId != null) {
            tblOtp.setAgentId(new BigDecimal(agentId));
        } else {
            tblOtp.setTblCustomerAll(customerAllId);
        }

        return tblOtp;
    }

    private TblOtp setTblOtpForCustomer(GenerateOtpRequest generateOtpRequest, LkpOtpType lkpOtpType) {
        TblOtp tblOtp;
        Long customerId = null;
        TblCustomerAll customerAllId = tblCustomerAllRepo.findByMobileNo(aeSencryption.encryptwith256(generateOtpRequest.getMobileNumber()));
        if (customerAllId == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        customerId = tblAppUserRepo.getCustomerIdByCustomerAllId(customerAllId.getCustomerAllId());


        if (customerId != null) {
            tblOtp = tblOtpRepo.findOtpByCustomerIdAndOtpType(lkpOtpType.getOtpType(), customerId);
        } else {
            tblOtp = tblOtpRepo.findOtpByCustomerAllIdAndOtpType(lkpOtpType.getOtpType(), customerAllId.getCustomerAllId());
        }
        if (tblOtp != null && tblOtp.getEffectiveTo().compareTo(new Date()) < 0) {
            tblOtp.setVerified("E");
            tblOtpRepo.saveAndFlush(tblOtp);
            tblOtp = null;
        }
        if (tblOtp == null) {
            tblOtp = new TblOtp();
        }


        if (customerId != null) {
            tblOtp.setCustomerId(new BigDecimal(customerId));
        } else {
            tblOtp.setTblCustomerAll(customerAllId);
        }
        return tblOtp;
    }

    @Override
    public HashMap<String, Object> verifyOtp(VerifyOtpRequest verifyOtpRequest, BigDecimal userId) {
        HashMap<String, Object> response = new HashMap<>();
        TblOtp tblOtp = null;
        LkpOtpType lkpOtpType = lkpOtpTypeRepo.findOtpTypeByOtpCode(verifyOtpRequest.getOtpType());
        if (lkpOtpType == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.OTP_TYPE_NOT_FOUND.getResponseCode());
        }

        if (verifyOtpRequest.getUserType().equalsIgnoreCase("C")) {
            Long customerId = null;
            Long customerAllId = tblAppUserRepo.getCustomerAllIdByMobileNumber(aeSencryption.encryptwith256(verifyOtpRequest.getMobileNumber()));
            if (customerAllId == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
            }
            customerId = tblAppUserRepo.getCustomerIdByCustomerAllId(customerAllId);

            if (customerId != null) {
                tblOtp = tblOtpRepo.findOtpByIdAndType(lkpOtpType.getOtpType(), verifyOtpRequest.getOtpId(), customerId, null);
            } else {
                tblOtp = tblOtpRepo.findOtpByIdAndType(lkpOtpType.getOtpType(), verifyOtpRequest.getOtpId(), null, customerAllId);
            }
        } else if (verifyOtpRequest.getUserType().equalsIgnoreCase("A")) {
            Long agentId = null;
            Long customerAllId = tblAppUserRepo.getCustomerAllIdByMobileNumber(aeSencryption.encryptwith256(verifyOtpRequest.getMobileNumber()));
            if (customerAllId == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
            }
            agentId = tblAppUserRepo.getAgentIdByCustomerAllId(customerAllId);

            if (agentId != null) {
                tblOtp = tblOtpRepo.findOtpByIdAndTypeAgent(lkpOtpType.getOtpType(), verifyOtpRequest.getOtpId(), agentId, null);
            } else {
                tblOtp = tblOtpRepo.findOtpByIdAndTypeAgent(lkpOtpType.getOtpType(), verifyOtpRequest.getOtpId(), null, customerAllId);
            }
        }
        if (tblOtp == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.OTP_NOT_FOUND.getResponseCode());
        }
        if (tblOtp != null && tblOtp.getEffectiveTo().compareTo(new Date()) < 0) {
            tblOtp.setVerified("E");
            tblOtpRepo.saveAndFlush(tblOtp);
        }
        if (tblOtp.getVerified().equalsIgnoreCase("E")) {
            throw new CustomDataNotFoundException(GenericResponseCode.OTP_EXPIRED.getResponseCode());
        }
        if (tblOtp.getVerified().equalsIgnoreCase("V")) {
            throw new CustomDataNotFoundException(GenericResponseCode.OTP_ALREADY_VERIFIED.getResponseCode());
        }
        if (tblOtp.getOtpTries().longValue() <= lkpOtpType.getOtpAttempts().longValue()) {
            if (tblOtp.getOtpin().equalsIgnoreCase(aeSencryption.encryptwith256(verifyOtpRequest.getOtpPin()))) {
                tblOtp.setVerified("V");
                tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblOtp.setLastupdatedate(new Date());
                tblOtp.setLastupdateuser(userId);
                tblOtp = tblOtpRepo.saveAndFlush(tblOtp);
                if (tblOtp != null) {
                    VerifyOtpResponse verifyOtpResponse = new VerifyOtpResponse();
                    verifyOtpResponse.setTries(String.valueOf(tblOtp.getOtpTries()));
                    verifyOtpResponse.setOtpId(String.valueOf(tblOtp.getOtpsId()));
                    verifyOtpResponse.setStatus(tblOtp.getVerified());
                    response = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), verifyOtpResponse);

                } else {
                    response = commonService.getResponse(GenericResponseCode.FAILED_TO_VERIFY_OTP.getResponseCode(), null);

                }

            } else {
                tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
                tblOtp.setLastupdatedate(new Date());
                tblOtp.setLastupdateuser(userId);
                tblOtpRepo.saveAndFlush(tblOtp);
                throw new CustomDataNotFoundException(GenericResponseCode.WRONG_OTP.getResponseCode());
            }
        } else {
            tblOtp.setOtpTries(tblOtp.getOtpTries() != null ? tblOtp.getOtpTries().add(BigDecimal.ONE) : BigDecimal.ONE);
            tblOtp.setVerified("L");
            tblOtp.setLastupdatedate(new Date());
            tblOtp.setLastupdateuser(userId);
            tblOtpRepo.saveAndFlush(tblOtp);
            throw new CustomDataNotFoundException(GenericResponseCode.OTP_LIMIT_EXCEEDS.getResponseCode());
        }

        return response;
    }

    @Override
    public TblGlobalConfig findByKeyName(String country_code) {
        return tblGlobalConfigRepo.findByKeyName(country_code);
    }

    @Override
    public TblSmsMessage saveMessage(TblSmsMessage tblSmsMessage) {
        return tblSmsMessageRepo.saveAndFlush(tblSmsMessage);
    }

    public void saveOTP(TblOtp tblOtp, LkpOtpType lkpOtpType) {

        tblOtp.setOtpType(lkpOtpType.getOtpType());
        tblOtp.setEffectiveFrom(new Date());
        Date newDate = DateUtils.addMinutes(tblOtp.getEffectiveFrom(), lkpOtpType.getExpiryMinutes().intValue());
        tblOtp.setEffectiveTo(newDate);
        tblOtp.setOtpTries(new BigDecimal(0));
        tblOtp.setCreateuser(new BigDecimal(1));
        tblOtp.setCreatedate(new Date());
        tblOtp.setVerified("A");
        tblOtpRepo.save(tblOtp);

    }

}
