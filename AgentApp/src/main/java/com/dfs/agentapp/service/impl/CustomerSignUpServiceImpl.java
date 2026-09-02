package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.service.CustomerSignUpService;
import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.GenerateNotificationRequest;
import com.dfs.agentapp.dto.MobileRegistrationRequest;
import com.dfs.agentapp.dto.UploadDocumentRequest;
import com.dfs.agentapp.dto.common.CustomerKycRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.*;
import com.dfs.agentapp.repo.*;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.DocumentService;
import com.dfs.agentapp.service.QrService;
import com.dfs.agentapp.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class CustomerSignUpServiceImpl extends HelperClass implements CustomerSignUpService {
    @Autowired
    private QrService qrService;
    @Autowired
    private TblCustomerAllRepo tblCustomerAllRepo;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private LkpRegistrationTypeRepo lkpRegistrationTypeRepo;
    @Autowired
    private LkpAccountStatusRepo lkpAccountStatusRepo;
    @Autowired
    private TblAccountLevelRepo tblAccountLevelRepo;
    @Autowired
    private LkpAccountTypeRepo lkpAccountTypeRepo;
    @Autowired
    private LkpChannelRepo lkpChannelRepo;
    @Autowired
    private TblDeviceInfoRepo tblDeviceInfoRepo;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private CommonService commonService;
    @Value("${account.level.one}")
    private String accountLevelOne;
    @Value("${account.cus.reg.type.code}")
    private String accountTypeCode;
    @Value("${account.status.active}")
    private String accountStatusActive;
    @Value("${account.type.individual.code}")
    private String accountTypeWallet;
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private TblSmsMessageTemplateRepo tblSmsMessageTemplateRepo;
    @Value("${sms.template.registration}")
    private String registrationTypeTemplate;
    @Autowired
    private LkpProvinceRepo lkpProvinceRepo;
    @Autowired
    private TblOfacRepo tblOfacRepo;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private LkpDistrictRepo lkpDistrictRepo;

    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;

    @Value("${bank.code}")
    private String barakatCode;
    @Autowired
    private TblMutlilanguageRepo tblMutlilanguageRepo;
    @Autowired
    private LkpOccupationRepo lkpOccupationRepo;
    @Autowired
    private LKpAccountPurposeRepo lKpAccountPurposeRepo;
    @Autowired
    private LkpCityRepo lkpCityRepo;

    @Override
    public TblCustomerAll registerCustomerAll(MobileRegistrationRequest mobileRegistrationRequest, Request apiRequest, BigDecimal userId) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNoOrCnicAndAccountLevelCode(mobileRegistrationRequest.getMobileNo(), Constants.EMPTY, accountLevelOne);
        if (tblAccount != null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_NUMBER.getResponseCode());
        }
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
        if (tblCustomerAll == null) {
            tblCustomerAll = new TblCustomerAll();
        }
        tblCustomerAll.setAppVersion(mobileRegistrationRequest.getAppVersion());
        tblCustomerAll.setUuid(UUID.randomUUID().toString());
        tblCustomerAll.setIpAddressA(mobileRegistrationRequest.getIpAddressA());
        tblCustomerAll.setIpAddressP(mobileRegistrationRequest.getIpAddressP());
        tblCustomerAll.setImeiNo(mobileRegistrationRequest.getImeiNo());
        tblCustomerAll.setDeviceModel(mobileRegistrationRequest.getDeviceModel());
        tblCustomerAll.setMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
        tblCustomerAll.setOtpverified(Constants.N);
        tblCustomerAll.setLkpNetwork(null);
        LkpChannel lkpChannel = lkpChannelRepo.findByChannelCodeAndIsActive(apiRequest.getChannel(), Constants.YES);
        if (lkpChannel != null) {
            tblCustomerAll.setLkpChannel(lkpChannel);
        }
        tblCustomerAll.setSelfieverified(Constants.N);
        tblCustomerAll.setBioverified(Constants.N);
        tblCustomerAll.setCreateuser(userId);
        tblCustomerAll.setCreatedate(new Date());
        tblCustomerAll = tblCustomerAllRepo.save(tblCustomerAll);
        TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo.findByMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
        tblDeviceInfo = tblDeviceInfo == null ? new TblDeviceInfo() : tblDeviceInfo;

        tblDeviceInfo.setMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
        tblDeviceInfo.setImeiNo(tblCustomerAll.getImeiNo());
        tblDeviceInfo.setUuid(tblCustomerAll.getUuid());
        tblDeviceInfo.setDeviceModel(tblCustomerAll.getDeviceModel());
        tblDeviceInfo.setCreateuser(userId);
        tblDeviceInfo.setCreatedate(tblCustomerAll.getCreatedate());
        tblDeviceInfo.setTblAppUser(null);
        tblDeviceInfoRepo.saveAndFlush(tblDeviceInfo);
        return tblCustomerAll;
    }

    @Override
    public TblCustomerAll updateTblCustomerAllVerifed(String mobileNumber, Request request, BigDecimal userId) {
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByMobileNo(aeSencryption.encryptwith256(mobileNumber));
        if (tblCustomerAll != null) {
            LkpChannel lkpChannel = lkpChannelRepo.findByChannelCodeAndIsActive(request.getChannel(), Constants.YES);
            if (lkpChannel != null) {
                tblCustomerAll.setLkpChannel(lkpChannel);
            }
            tblCustomerAll.setOtpverified(Constants.YES);
            tblCustomerAll.setLastupdatedate(new Date());
            tblCustomerAll.setLastupdateuser(userId);

            return tblCustomerAllRepo.saveAndFlush(tblCustomerAll);
        }

        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public HashMap<String, Object> registerCustomerAndAccount(CustomerKycRequest customerKycRequest, Request apiRequest, String header, BigDecimal userId) throws JsonProcessingException {
        List<TblAccount> tblAccounts = tblAccountRepo.findCustomerTblAccountsByMobileOrCnic(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()), aeSencryption.encryptwith256(customerKycRequest.getNidNo()));
        if (tblAccounts != null && !tblAccounts.isEmpty()) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_CNIC_OR_MOBILE.getResponseCode());
        }
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByMobileNo(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()));
        if (tblCustomerAll == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        if (!tblCustomerAll.getOtpverified().equalsIgnoreCase(Constants.YES)) {
            throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_NOT_VERIFIED.getResponseCode());
        }

        TblCustomer tblCustomer = saveTblCustomer(customerKycRequest, tblCustomerAll, userId);
        if (tblCustomer == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }

        List<TblOfac> tblOfacs = tblOfacRepo.findByName(customerKycRequest.getFullName());
        if (tblOfacs != null && !tblOfacs.isEmpty()) {
            throw new CustomDataNotFoundException(GenericResponseCode.BLACKLISTED.getResponseCode());
        }

        TblAccount tblAccount = saveTblAccount(tblCustomer, customerKycRequest, userId);
        TblAppUser tblAppUser = saveTblAppUser(tblCustomer, customerKycRequest, userId);
        qrService.generateCustomerStaticQrForP2P(tblAccount.getAccountNo(), tblAccount.getTblAccountLevel().getAccountLevelCode());
        TblSmsMessageTemplate tblSmsMessageTemplate = tblSmsMessageTemplateRepo.findByIdentifierAndDocTypeCode(Constants.S, registrationTypeTemplate);
        if (tblSmsMessageTemplate != null) {
            String sms = tblSmsMessageTemplate.getMessageTemplate();
            sms = sms.replace("%NAME%", aeSencryption.decrypt(tblAccount.getAccountTitle()));
            GenerateNotificationRequest generateNotificationRequest = new GenerateNotificationRequest();
            generateNotificationRequest.setType(Constants.MOBILE);
            generateNotificationRequest.setSms(sms);
            generateNotificationRequest.setTemplateId(BigDecimal.valueOf(tblSmsMessageTemplate.getSmsMessageTemplateId()));
            generateNotificationRequest.setMobileNumber(tblAccount.getMobileNo());
            CompletableFuture.runAsync(()-> {
                try {
                    commonService.generateNotification(generateNotificationRequest, apiRequest, header);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        updateTblCustomerAll(tblCustomerAll, userId);
        UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest();
        uploadDocumentRequest.setAccountLevelCode(tblAccount.getTblAccountLevel().getAccountLevelCode());
        uploadDocumentRequest.setMobileNumber(customerKycRequest.getMobileNumber());
        uploadDocumentRequest.setDocuments(customerKycRequest.getDocuments());
        documentService.uploadDocument(uploadDocumentRequest, apiRequest, userId, false);
        saveTblMultiLanguage(tblCustomer, customerKycRequest);
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblAppUser);


    }

    private void saveTblMultiLanguage(TblCustomer tblCustomer, CustomerKycRequest customerKycRequest) {
//        List<TblMultilanguage> multilanguages = new ArrayList<>();
//        if (!isNullOrEmpty(customerKycRequest.getNidNumberDari())) {
//            multilanguages.add(createMultilang(tblCustomer.getCustomerId(), "TAZKIRA", customerKycRequest.getNidNumberDari(), tblCustomer.getCreateuser()));
//        }
//        if (!isNullOrEmpty(customerKycRequest.getFatherName())) {
//            multilanguages.add(createMultilang(tblCustomer.getCustomerId(), "FATHER_NAME", customerKycRequest.getFatherNameDari(), tblCustomer.getCreateuser()));
//        }
//        if (!isNullOrEmpty(customerKycRequest.getGrandFatherName())) {
//            multilanguages.add(createMultilang(tblCustomer.getCustomerId(), "GRANDFATHER_NAME", customerKycRequest.getGrandFatherNameDari(), tblCustomer.getCreateuser()));
//        }
//        if (!isNullOrEmpty(customerKycRequest.getDobDari())) {
//            multilanguages.add(createMultilang(tblCustomer.getCustomerId(), "DOB", customerKycRequest.getDobDari(), tblCustomer.getCreateuser()));
//        }
//        if (!isNullOrEmpty(customerKycRequest.getFullNameDari())) {
//            multilanguages.add(createMultilang(tblCustomer.getCustomerId(), "FULL_NAME", customerKycRequest.getFullNameDari(), tblCustomer.getCreateuser()));
//        }
//        if (!multilanguages.isEmpty()) {
//            tblMutlilanguageRepo.saveAll(multilanguages);
//        }
    }

    private TblMultilanguage createMultilang(long refId, String columnName, String value, BigDecimal userId) {
        TblMultilanguage entry = new TblMultilanguage();
        entry.setColumnName(columnName);
        entry.setValue(value);
        entry.setRefId(new BigDecimal(refId));
        entry.setTableName("TBL_CUSTOMER");
        entry.setCreatedate(new Date());
        entry.setCreateuser(userId);
        LkpLanguage lkpLanguage = new LkpLanguage();
        lkpLanguage.setLanguageId(4);
        entry.setLkpLanguage(lkpLanguage);
        return entry;
    }

    @Override
    public HashMap<String, Object> getdfsid(long id) {
        TblCustomer customer = tblCustomerRepo.findByCustomerId(id);
        if (customer != null) {
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), customer.getCustomerUid());
        }
        return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), "");
    }

    private void updateTblCustomerAll(TblCustomerAll tblCustomerAll, BigDecimal appUserId) {
        tblCustomerAll.setSelfieverified(Constants.YES);
        tblCustomerAll.setUpdateindex(tblCustomerAll.getUpdateindex() != null ? tblCustomerAll.getUpdateindex().add(BigDecimal.ONE) : BigDecimal.ONE);
        tblCustomerAll.setLastupdateuser(appUserId);
        tblCustomerAllRepo.saveAndFlush(tblCustomerAll);
    }

    private TblAppUser saveTblAppUser(TblCustomer tblCustomer, CustomerKycRequest customerKycRequest, BigDecimal userId) {
        TblAppUser tblAppUser = tblAppUserRepo.findByCustomerId(tblCustomer.getCustomerId());
        tblAppUser = tblAppUser == null ? new TblAppUser() : tblAppUser;
        tblAppUser.setCreatedate(new Date());
        tblAppUser.setCreateuser(userId);
        tblAppUser.setCustomerId(new BigDecimal(tblCustomer.getCustomerId()));
        tblAppUser.setMobileNo(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()));
        tblAppUser.setUsername(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()));
        if (!customerKycRequest.getPin().equals(customerKycRequest.getConfirmMpin())) {
            throw new CustomDataNotFoundException(GenericResponseCode.CONFIRM_PIN_NOT_MATCHED.getResponseCode());
        }
        tblAppUser.setPassword(commonService.encryptWithAes(customerKycRequest.getPin()));
        tblAppUser = tblAppUserRepo.saveAndFlush(tblAppUser);
        TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo.findByMobileNo(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()));
        if (tblDeviceInfo != null) {
            tblDeviceInfo.setTblAppUser(tblAppUser);
            tblDeviceInfo.setLastupdatedate(new Date());
            tblDeviceInfo.setLastupdateuser(userId);
            tblDeviceInfoRepo.saveAndFlush(tblDeviceInfo);
        }
        return tblAppUser;
    }

    private TblAccount saveTblAccount(TblCustomer tblCustomer, CustomerKycRequest customerKycRequest, BigDecimal userId) {
        TblAccount tblAccount = tblAccountRepo.findByMobileNoAndCnicAndAccountLevelCode(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()), aeSencryption.encryptwith256(customerKycRequest.getNidNo()), accountLevelOne);
        tblAccount = tblAccount == null ? new TblAccount() : tblAccount;
        LkpRegistrationType lkpRegistrationType = lkpRegistrationTypeRepo.findByRegistrationTypeCode(accountTypeCode);
        tblAccount.setLkpRegistrationType(lkpRegistrationType);
        LkpAccountStatus lkpAccountStatus = lkpAccountStatusRepo.findByAccountStatusCode(accountStatusActive);
        tblAccount.setLkpAccountStatus(lkpAccountStatus);
        TblAccountLevel tblAccountLevel = tblAccountLevelRepo.findByAccountLevelCode(accountLevelOne);
        tblAccount.setTblAccountLevel(tblAccountLevel);
        LkpAccountType lkpAccountType = lkpAccountTypeRepo.findByAccountTypeCode(accountTypeWallet);
        tblAccount.setLkpAccountType(lkpAccountType);
        tblAccount.setTblCustomer(tblCustomer);
        tblAccount.setAccountNo(customerKycRequest.getMobileNumber());
        tblAccount.setMobileNo(customerKycRequest.getMobileNumber());
        tblAccount.setAccountTitle(aeSencryption.encryptwith256(customerKycRequest.getFullName()));
        tblAccount.setBalanceDate(new Date());
        tblAccount.setBranchAccount(Constants.N);
        tblAccount.setCreatedate(new Date());
        tblAccount.setCreateuser(userId);
        tblAccount.setCurrentBalance(BigDecimal.ZERO);
        tblAccount.setIsActive(Constants.YES);
        tblAccount.setIban(IbanGenerator.generateIban(customerKycRequest.getMobileNumber()));
        tblAccount.setDailyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setDailyAmtLimitDr(BigDecimal.ZERO);
        tblAccount.setMonthlyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setMonthlyAmtLimitDr(BigDecimal.ZERO);
        tblAccount.setYearlyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setYearlyAmtLimitDr(BigDecimal.ZERO);
        tblAccount.setAdditionalInfo(buildAdditionalInfo(customerKycRequest));
        return tblAccountRepo.saveAndFlush(tblAccount);


    }

    private TblCustomer saveTblCustomer(CustomerKycRequest customerKycRequest, TblCustomerAll tblCustomerAll, BigDecimal userId) {
        TblCustomer tblCustomer = tblCustomerRepo.findByMobileNumberOrNidNo(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()), aeSencryption.encryptwith256(customerKycRequest.getNidNo()));
        tblCustomer = tblCustomer == null ? new TblCustomer() : tblCustomer;
        tblCustomer.setNidIssueDate(getDateFromString(customerKycRequest.getNidIssuanceDate()));
        tblCustomer.setDob(getDateFromString(customerKycRequest.getDob()));
        tblCustomer.setNidNo(aeSencryption.encryptwith256(customerKycRequest.getNidNo()));
        tblCustomer.setFullName(aeSencryption.encryptwith256(customerKycRequest.getFullName()));
        tblCustomer.setFirstName(aeSencryption.encryptwith256(customerKycRequest.getFirstName()));
        tblCustomer.setLastName(aeSencryption.encryptwith256(customerKycRequest.getLastName()));
        tblCustomer.setFatherName(aeSencryption.encryptwith256(customerKycRequest.getFatherName()));
        tblCustomer.setGrandfatherName(aeSencryption.encryptwith256(customerKycRequest.getGrandFatherName()));
        tblCustomer.setTblCustomerAll(tblCustomerAll);
        tblCustomer.setAddressP(aeSencryption.encryptwith256(customerKycRequest.getPermenantAddress()));
        if (customerKycRequest.getNidExpiryDate() != null && !customerKycRequest.getNidExpiryDate().isEmpty()) {
            tblCustomer.setNidExpiryDate(getDateFromString(customerKycRequest.getNidExpiryDate()));
        }
        tblCustomer.setIsBlackListed(Constants.N);
        tblCustomer.setBiometricVerified(Constants.YES);
        tblCustomer.setCreateuser(userId);
        tblCustomer.setCreatedate(new Date());
        tblCustomer.setEmailVerified(Constants.N);
        tblCustomer.setIsActive(Constants.YES);
        tblCustomer.setGender(customerKycRequest.getGender());
        tblCustomer.setIsFiler(Constants.N);
        tblCustomer.setNidVerified(Constants.YES);
        tblCustomer.setLivenessScore(customerKycRequest.getLivenessScore());
        tblCustomer.setFaceMatchScore(customerKycRequest.getMatchScore());
        tblCustomer.setMrzData(customerKycRequest.getMrzData());
        tblCustomer.setNationality(customerKycRequest.getNationality());
        tblCustomer.setCountry(customerKycRequest.getCountry());
        tblCustomer.setSignature(customerKycRequest.getSignature());
        tblCustomer.setPob(aeSencryption.encryptwith256(customerKycRequest.getPlaceOfBirth()));
        // KYC fields that have a dedicated column are persisted here rather than only in ADDITIONAL_INFO
        if (!isNullOrEmpty(customerKycRequest.getEmail())) {
            tblCustomer.setEmail(aeSencryption.encryptwith256(customerKycRequest.getEmail()));
        }
        if (!isNullOrEmpty(customerKycRequest.getPresentAddress())) {
            tblCustomer.setAddressC(aeSencryption.encryptwith256(customerKycRequest.getPresentAddress()));
        }
        if (!isNullOrEmpty(customerKycRequest.getOccupationId())) {
            lkpOccupationRepo.findById(Long.valueOf(customerKycRequest.getOccupationId()))
                    .ifPresent(tblCustomer::setLkpOccupation);
        }
        if (!isNullOrEmpty(customerKycRequest.getAccountPurposeId())) {
            lKpAccountPurposeRepo.findById(Long.valueOf(customerKycRequest.getAccountPurposeId()))
                    .ifPresent(tblCustomer::setLkpAccountPurpose);
        }
        if (!isNullOrEmpty(customerKycRequest.getCityId())) {
            lkpCityRepo.findById(Long.valueOf(customerKycRequest.getCityId()))
                    .ifPresent(tblCustomer::setLkpCity);
        }
        return tblCustomerRepo.saveAndFlush(tblCustomer);

    }

    /**
     * Serialises the KYC request fields that have no dedicated column into a JSON
     * document, stored in TBL_ACCOUNT.ADDITIONAL_INFO (CLOB).
     * All keys are always emitted so the stored shape stays stable, even when a
     * value is absent from the request.
     */
    private String buildAdditionalInfo(CustomerKycRequest r) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("linkRaast", r.getLinkRaast());
        info.put("tandCAccepted", r.getTandCAccepted());
        info.put("dualNationality", r.getDualNationality());
        info.put("dualCountry", r.getDualCountry());
        info.put("usBorn", r.getUsBorn());
        info.put("referenceMobNumber", r.getReferenceMobNumber());
        info.put("selfDecleration", r.getSelfDecleration());
        info.put("latitude", r.getLatitude());
        info.put("longitude", r.getLongitude());
        info.put("dualCurrentAddress", r.getDualCurrentAddress());
        info.put("dualCurrentAddressCountry", r.getDualCurrentAddressCountry());
        info.put("dualMailingAddress", r.getDualMailingAddress());
        info.put("dualMailingAddressCountry", r.getDualMailingAddressCountry());
        info.put("dualPermanentAddress", r.getDualPermanentAddress());
        info.put("dualPlaceOfBirth", r.getDualPlaceOfBirth());
        info.put("dualPlaceOfBirthCountry", r.getDualPlaceOfBirthCountry());
        info.put("usCitizen", r.getUsCitizen());
        info.put("taxpayerIdentificationNumber", r.getTaxpayerIdentificationNumber());
        info.put("noTinDescription", r.getNoTinDescription());
        info.put("noTinReason", r.getNoTinReason());
        info.put("expectedMonthlyVolumeId", r.getExpectedMonthlyVolumeId());
        info.put("fundProviderCnic", r.getFundProviderCnic());
        info.put("fundProviderFatherName", r.getFundProviderFatherName());
        info.put("fundProviderName", r.getFundProviderName());
        info.put("fundProviderRelationId", r.getFundProviderRelationId());
        info.put("recoveryQuestionId", r.getRecoveryQuestionId());
        info.put("recoveryAnswer", r.getRecoveryAnswer());
        info.put("selectedBirthPlace", r.getSelectedBirthPlace());
        info.put("selectedMotherName", r.getSelectedMotherName());
        info.put("faceLivenessScore", r.getFaceLivenessScore());
        info.put("pmd", r.getPmd());
        info.put("businessName", r.getBusinessName());
        info.put("businessTypeId", r.getBusinessTypeId());
        info.put("businessAddress", r.getBusinessAddress());
        info.put("accountPurposeId", r.getAccountPurposeId());
        info.put("customerTypeId", r.getCustomerTypeId());
        info.put("fatherHusbandName", r.getFatherHusbandName());
        try {
            return ADDITIONAL_INFO_MAPPER.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final com.fasterxml.jackson.databind.ObjectMapper ADDITIONAL_INFO_MAPPER =
            new com.fasterxml.jackson.databind.ObjectMapper();

    private Date getDateFromString(String nidIssuanceDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(nidIssuanceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
