package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.dto.CustomerUpdateAccountLevelRequest;
import com.dfs.agentapp.dto.GenerateNotificationRequest;
import com.dfs.agentapp.dto.UploadDocumentRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.*;
import com.dfs.agentapp.repo.*;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.CustomerAccountUpgradeService;
import com.dfs.agentapp.service.DocumentService;
import com.dfs.agentapp.util.AESencryption;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.CustomDataNotFoundException;
import com.dfs.agentapp.util.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Service
public class CustomerAccountUpgradeServiceImpl implements CustomerAccountUpgradeService {


    @Autowired
    private TblAccountUpgradeRepo tblAccountUpgradeRepo;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private TblAccountLevelRepo tblAccountLevelRepo;
    @Autowired
    private LKpAccountPurposeRepo lKpAccountPurposeRepo;
    @Autowired
    private LkpSourceOfIncomeRepo lkpSourceOfIncomeRepo;
    @Autowired
    private LkpOccupationRepo lkpOccupationRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TblDocumentRepo tblDocumentRepo;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private LkpStatusRepo lkpStatusRepo;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Value("${account.level.one}")
    private String accountLevelOne;
    @Value("${account.type.individual.code}")
    private String accountTypeWallet;

    @Value("${doc.proofOfAddress.code}")
    private String proofOfAddress;

    @Value("${doc.nidFront.code}")
    private String nidFront;

    @Value("${doc.nidBack.code}")
    private String nidBack;

    @Value("${doc.selfie.code}")
    private String selfie;

    @Value("${status.pending.code}")
    private String pendingStatusCode;

    @Value("${status.approved.code}")
    private String approvedStatusCode;

    @Value("${sms.template.upgrade.parked.L2}")
    private String accountUpgradtionL2ParkedTemplate;
    @Value("${sms.template.upgrade.approved.L2}")
    private String accountUpgradtionL2ApprovedTemplate;

    @Autowired
    private AESencryption aeSencryption;

    @Autowired
    private TblSmsMessageTemplateRepo tblSmsMessageTemplateRepo;

    @Autowired
    private TblOtpRepo tblOtpRepo;

    @Autowired
    private TblDeviceInfoRepo tblDeviceInfoRepo;

    @Override
    public HashMap<String, Object> upgradeToL2(CustomerUpdateAccountLevelRequest request,
                                               Request httpRequest,
                                               BigDecimal userId,
                                               String header) throws JsonProcessingException {
        validateInitialData(request);

        if (!request.getDocuments().isEmpty()) {
            HashMap<String, Object> documentResponse = uploadDocuments(request, httpRequest, userId);
            if (!isSuccessResponse(documentResponse)) {
                return documentResponse;
            }
        }

        TblAccount account = validateAndGetAccount(request);
        return processAccountUpgrade(request, httpRequest, userId, header, account);
    }

    private void validateInitialData(CustomerUpdateAccountLevelRequest request) {
        // Validate OTP
        TblOtp otp = tblOtpRepo.findByOtpsIdAndVerified(Long.valueOf(request.getVerifyOtpRequest().getOtpId()), Constants.VERIFIED);
        if (otp == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.OTP_NOT_FOUND.getResponseCode());
        }

        LkpSourceOfIncome sourceOfIncome = lkpSourceOfIncomeRepo.findBySourceOfIncomeCodeAndIsActive(request.getSourceOfIncomeCode(), Constants.YES);
        if (sourceOfIncome == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.SOURCE_OF_INCOME_NOT_FOUND.getResponseCode());
        }
        LkpOccupation occupation = lkpOccupationRepo.findByOccupationCodeAndIsActive(request.getOccupationCode(), Constants.YES);
        if (occupation == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.OCCUPATION_NOT_FOUND.getResponseCode());
        }
        LkpAccountPurpose accountPurpose = lKpAccountPurposeRepo.findByAccountPurposeCodeAndIsActive(request.getPurposeOfAccountCode(), Constants.YES);
        if (accountPurpose == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.PURPOSE_OF_ACCOUNT_NOT_FOUND.getResponseCode());
        }
    }

    private TblAccount validateAndGetAccount(CustomerUpdateAccountLevelRequest request) {
        TblAccount account = tblAccountRepo.findByAccountNoAndAccountTypeCode(request.getMobileNumber(), accountTypeWallet);
        if (account == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }

        TblAccountLevel targetLevel = tblAccountLevelRepo.findByAccountLevelCode(request.getAccountLevelCode());
        if (targetLevel == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_LEVEL_NOT_FOUND.getResponseCode());
        }

        if (account.getTblAccountLevel().getAccountLevelId() == (targetLevel.getAccountLevelId())) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_ALEADY_UPDATED.getResponseCode());
        }

        TblAppUser appUser = tblAppUserRepo.findByCustomerAccountNo(request.getMobileNumber());
        if (appUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }

        validateDocuments(appUser, request.getAccountLevelCode());
        return account;
    }

    private void validateDocuments(TblAppUser appUser, String accountLevelCode) {
        TblDocument nidFrontDoc = tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(appUser.getAppUserId(), nidFront, accountLevelOne);
        if (nidFrontDoc == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.NID_FRONT_NOT_FOUND.getResponseCode());
        }
        TblDocument nidBackDoc = tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(appUser.getAppUserId(), nidBack, accountLevelOne);
        if (nidBackDoc == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.NID_BACK_NOT_FOUND.getResponseCode());
        }
        TblDocument selfieDoc = tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(appUser.getAppUserId(), selfie, accountLevelCode);
        if (selfieDoc == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.SELFIE_IMAGE_NOT_FOUND.getResponseCode());
        }
    }

    private HashMap<String, Object> uploadDocuments(CustomerUpdateAccountLevelRequest request,
                                                    Request httpRequest,
                                                    BigDecimal userId)  {
        UploadDocumentRequest uploadRequest = new UploadDocumentRequest();
        uploadRequest.setAccountLevelCode(request.getAccountLevelCode());
        uploadRequest.setDocuments(request.getDocuments());
        uploadRequest.setMobileNumber(request.getMobileNumber());
        return documentService.uploadDocument(uploadRequest, httpRequest, userId, false);
    }

    private HashMap<String, Object> processAccountUpgrade(CustomerUpdateAccountLevelRequest request,
                                                          Request httpRequest,
                                                          BigDecimal userId,
                                                          String header,
                                                          TblAccount account) throws JsonProcessingException {
        TblAccountLevel targetLevel = tblAccountLevelRepo.findByAccountLevelCode(request.getAccountLevelCode());
        LkpStatus pendingStatus = lkpStatusRepo.findByStatusCodeAndIsActive(pendingStatusCode, Constants.YES);
        if (pendingStatus == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }

        TblAccountUpgrade accountUpgrade = tblAccountUpgradeRepo
                .findByTblAccountAccountIdAndTblAccountLevelAccountLevelId(account.getAccountId(), targetLevel.getAccountLevelId());
        if (accountUpgrade == null) {
            accountUpgrade = new TblAccountUpgrade();
            accountUpgrade.setCreatedate(new Date());
            accountUpgrade.setCreateuser(userId);
        } else {
            accountUpgrade.setLastupdatedate(new Date());
            accountUpgrade.setLastupdateuser(userId);
            accountUpgrade.setUpdateindex(accountUpgrade.getUpdateindex() == null ? BigDecimal.ONE : accountUpgrade.getUpdateindex().add(BigDecimal.ONE));
        }

        LkpSourceOfIncome sourceOfIncome = lkpSourceOfIncomeRepo.findBySourceOfIncomeCodeAndIsActive(request.getSourceOfIncomeCode(), Constants.YES);
        LkpProvince province = account.getTblCustomer() != null ? account.getTblCustomer().getLkpProvince() : null;
        LkpAccountPurpose accountPurpose = lKpAccountPurposeRepo.findByAccountPurposeCodeAndIsActive(request.getPurposeOfAccountCode(), Constants.YES);
        LkpOccupation occupation = lkpOccupationRepo.findByOccupationCodeAndIsActive(request.getOccupationCode(), Constants.YES);
        TblAppUser appUser = tblAppUserRepo.findByCustomerAccountNo(request.getMobileNumber());

        accountUpgrade.setLkpSourceOfIncome(sourceOfIncome);
        accountUpgrade.setTblAccountLevel(targetLevel);
        accountUpgrade.setTblAccount(account);
        accountUpgrade.setLkpStatus(pendingStatus);
        accountUpgrade.setLkpProvince(province);
        accountUpgrade.setLkpAccountPurpose(accountPurpose);
        accountUpgrade.setLkpOccupation(occupation);
        accountUpgrade.setTblDocument4(null);
        accountUpgrade.setTblDocument1(null);
        accountUpgrade.setTblDocument2(tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(
                appUser.getAppUserId(), selfie, request.getAccountLevelCode()));
        accountUpgrade.setTblDocument3(tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(
                appUser.getAppUserId(), proofOfAddress, request.getAccountLevelCode()));
        tblAccountUpgradeRepo.saveAndFlush(accountUpgrade);

        TblCustomer customer = account.getTblCustomer();
        customer.setEmail(!isNullOrEmpty(request.getEmail()) ? aeSencryption.encryptwith256(request.getEmail()) : null);
        customer.setEmailVerified(Constants.N);
        customer.setLkpProvince(province);
        customer.setLkpSourceOfIncome(sourceOfIncome);
        customer.setLkpOccupation(occupation);
        if (sourceOfIncome.getSourceOfIncomeCode().equals("OPS")) {
            customer.setSourceOfIncomeOther(request.getSourceOfIncomeOther());
        }
        if (occupation.getOccupationCode().equals("A29")) {
            customer.setOccupationOther(request.getOccupationOther());
        }
        customer.setLkpAccountPurpose(accountPurpose);
        customer.setLastupdatedate(new Date());
        customer.setLastupdateuser(userId);
        customer.setUpdateindex(customer.getUpdateindex() == null ? BigDecimal.ONE : customer.getUpdateindex().add(BigDecimal.ONE));
        tblCustomerRepo.saveAndFlush(customer);

        account.setTblAccountLevel(targetLevel);
        account.setUpdateindex(account.getUpdateindex() == null ? BigDecimal.ONE : account.getUpdateindex().add(BigDecimal.ONE));
        account.setLastupdatedate(new Date());
        tblAccountRepo.saveAndFlush(account);

        Long deviceUpdateStatus = tblDeviceInfoRepo.checkDeviceUpdateStatusByAppUserId(appUser.getAppUserId());

        if (deviceUpdateStatus != 0 && !isNullOrEmpty(customer.getRiskProfile()) && !customer.getRiskProfile().equalsIgnoreCase("L")) {
            LkpStatus approvedStatus = lkpStatusRepo.findByStatusCodeAndIsActive(approvedStatusCode, Constants.YES);
            accountUpgrade.setLkpStatus(approvedStatus);
            tblAccountUpgradeRepo.saveAndFlush(accountUpgrade);
            return sendNotificationAndGetResponse(account, request, httpRequest, header, accountUpgradtionL2ApprovedTemplate,
                    GenericResponseCode.ACCOUNT_UPDATED_SUCESSFULLY);
        } else if (deviceUpdateStatus != 0) {
            return sendNotificationAndGetResponse(account, request, httpRequest, header, accountUpgradtionL2ParkedTemplate,
                    GenericResponseCode.ACCOUNT_PARKED_FOR_APPROVAL);
        }

        LkpStatus approvedStatus = lkpStatusRepo.findByStatusCodeAndIsActive(approvedStatusCode, Constants.YES);
        accountUpgrade.setLkpStatus(approvedStatus);
        tblAccountUpgradeRepo.saveAndFlush(accountUpgrade);
        return sendNotificationAndGetResponse(account, request, httpRequest, header, accountUpgradtionL2ApprovedTemplate,
                GenericResponseCode.ACCOUNT_UPDATED_SUCESSFULLY);
    }

    private HashMap<String, Object> sendNotificationAndGetResponse(TblAccount account,
                                                                   CustomerUpdateAccountLevelRequest request,
                                                                   Request httpRequest,
                                                                   String header,
                                                                   String templateIdentifier,
                                                                   GenericResponseCode responseCode) throws JsonProcessingException {
        HashMap<String, Object> response = commonService.getResponse(responseCode.getResponseCode(), null);
        response.replace("responsecode", responseCode.getResponseCode(), GenericResponseCode.SUCCESS.getResponseCode());

        TblSmsMessageTemplate template = tblSmsMessageTemplateRepo.findByIdentifierAndDocTypeCode(Constants.S, templateIdentifier);
        if (template != null) {
            String sms = template.getMessageTemplate()
                    .replace("%NAME%", aeSencryption.decrypt(account.getAccountTitle()))
                    .replace("<l>", account.getTblAccountLevel().getAccountLevelDescr());
            GenerateNotificationRequest notification = new GenerateNotificationRequest();
            notification.setType(Constants.MOBILE);
            notification.setSms(sms);
            notification.setTemplateId(BigDecimal.valueOf(template.getSmsMessageTemplateId()));
            notification.setMobileNumber(account.getMobileNo());
            CompletableFuture.runAsync(()-> {
                try {
                    commonService.generateNotification(notification, httpRequest, header);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return response;
    }

    private boolean isSuccessResponse(HashMap<String, Object> response) {
        return response.get("responsecode").toString().equals(GenericResponseCode.SUCCESS.getResponseCode());
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public boolean checkExistance(String mobileNumber, String accountLevelCode) {
        TblAccountUpgrade tblAccountUpgrade = tblAccountUpgradeRepo.findByAccountNoAndAccountLevelCode(mobileNumber, accountLevelCode);
        return tblAccountUpgrade != null;
    }

    @Override
    public String getSelfieImage(String mobileNumber) {
        TblAppUser tblAppUser = tblAppUserRepo.findByCustomerAccountNo(mobileNumber);
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        TblDocument sf = tblDocumentRepo.findByAppUserIdDocTypeCodeAccountLevelCode(tblAppUser.getAppUserId(), selfie, accountLevelOne);
        if (sf == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.SELFIE_IMAGE_NOT_FOUND.getResponseCode());
        }
        return sf.getDocumentPath();
    }
}
