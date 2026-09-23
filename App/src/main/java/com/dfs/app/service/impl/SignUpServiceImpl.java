package com.dfs.app.service.impl;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.EmployeeOnboardConfirmRequest;
import com.dfs.app.dto.GenerateNotificationRequest;
import com.dfs.app.dto.MobileRegistrationRequest;
import com.dfs.app.dto.UploadDocumentRequest;
import com.dfs.app.dto.common.CustomerKycRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.model.*;
import com.dfs.app.repo.*;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.DocumentService;
import com.dfs.app.service.QrService;
import com.dfs.app.service.SignUpService;
import com.dfs.app.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.*;

@Service
public class SignUpServiceImpl extends HelperClass implements SignUpService {

    private static final Logger log = LoggerFactory.getLogger(SignUpServiceImpl.class);
    @Autowired
    private QrService qrService;
    @Autowired
    private TblCustomerAllRepo tblCustomerAllRepo;
    @Autowired
    private TblCustomerRepo tblCustomerRepo;
    @Autowired
    private TblBulkAccountRepo tblBulkAccountRepo;
    @Autowired
    private LkpSegmentRepo lkpSegmentRepo;
    @Value("${employee.onboard.confirm.url}")
    private String employeeOnboardConfirmUrl;
    @Value("${employee.onboard.key}")
    private String employeeOnboardKey;
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
    @Value("${account.reg.type.code}")
    private String accountTypeCode;
    @Value("${account.status.active}")
    private String accountStatusActive;
    @Value("${account.type.code}")
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
    public TblCustomerAll registerCustomerAll(MobileRegistrationRequest mobileRegistrationRequest, Request apiRequest) {
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
        tblCustomerAll.setCreateuser(new BigDecimal(1));
        tblCustomerAll.setCreatedate(new Date());
        tblCustomerAll = tblCustomerAllRepo.save(tblCustomerAll);
        TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo.findByMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
        tblDeviceInfo = tblDeviceInfo == null ? new TblDeviceInfo() : tblDeviceInfo;

        tblDeviceInfo.setMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
        tblDeviceInfo.setImeiNo(tblCustomerAll.getImeiNo());
        tblDeviceInfo.setUuid(tblCustomerAll.getUuid());
        tblDeviceInfo.setDeviceModel(tblCustomerAll.getDeviceModel());
        tblDeviceInfo.setCreateuser(tblCustomerAll.getCreateuser());
        tblDeviceInfo.setCreatedate(tblCustomerAll.getCreatedate());
        tblDeviceInfoRepo.saveAndFlush(tblDeviceInfo);
        return tblCustomerAll;
    }

    @Override
    public TblCustomerAll updateTblCustomerAllVerifed(String mobileNumber, Request request) {
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo.findByMobileNo(aeSencryption.encryptwith256(mobileNumber));
        if (tblCustomerAll != null) {
            LkpChannel lkpChannel = lkpChannelRepo.findByChannelCodeAndIsActive(request.getChannel(), Constants.YES);
            if (lkpChannel != null) {
                tblCustomerAll.setLkpChannel(lkpChannel);
            }
            tblCustomerAll.setOtpverified(Constants.YES);
            tblCustomerAll.setLastupdatedate(new Date());
            tblCustomerAll.setLastupdateuser(BigDecimal.ONE);

            return tblCustomerAllRepo.saveAndFlush(tblCustomerAll);
        }

        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public HashMap<String, Object> registerCustomerAndAccount(CustomerKycRequest customerKycRequest, Request apiRequest, String header) throws JsonProcessingException {
        List<TblAccount> tblAccounts = tblAccountRepo.findTblAccountsByMobileOrCnic(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()), aeSencryption.encryptwith256(customerKycRequest.getNidNumber()));
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

        // An account parked by the employee-onboard partner carries the segment it should get. No
        // row means an ordinary signup, which keeps the segment it always had.
        TblBulkAccount bulkAccount = findBulkAccount(customerKycRequest.getMobileNumber(),
                customerKycRequest.getNidNumber());
        LkpSegment bulkSegment = resolveBulkSegment(bulkAccount);

        TblCustomer tblCustomer = saveTblCustomer(customerKycRequest, tblCustomerAll, bulkSegment);
        if (tblCustomer == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }

        List<TblOfac> tblOfacs = tblOfacRepo.findByName(customerKycRequest.getFullName());
        if (tblOfacs != null && !tblOfacs.isEmpty()) {
            throw new CustomDataNotFoundException(GenericResponseCode.BLACKLISTED.getResponseCode());
        }

        TblAccount tblAccount = saveTblAccount(tblCustomer, customerKycRequest);
        TblAppUser tblAppUser = saveTblAppUser(tblCustomer, customerKycRequest);
        qrService.generateStaticQrForP2P(tblAccount.getAccountNo(), tblAccount.getTblAccountLevel().getAccountLevelCode());
        TblSmsMessageTemplate tblSmsMessageTemplate = tblSmsMessageTemplateRepo.findByIdentifierAndDocTypeCode(Constants.S, registrationTypeTemplate);
        if (tblSmsMessageTemplate != null) {
            String sms = tblSmsMessageTemplate.getMessageTemplate();
            sms = sms.replace("%NAME%", aeSencryption.decrypt(tblAccount.getAccountTitle()));
            GenerateNotificationRequest generateNotificationRequest = new GenerateNotificationRequest();
            generateNotificationRequest.setType(Constants.MOBILE);
            generateNotificationRequest.setSms(sms);
            generateNotificationRequest.setTemplateId(BigDecimal.valueOf(tblSmsMessageTemplate.getSmsMessageTemplateId()));
            generateNotificationRequest.setMobileNumber(tblAccount.getMobileNo());
            commonService.generateNotification(generateNotificationRequest, apiRequest, header);
        }
        updateTblCustomerAll(tblCustomerAll, tblAppUser.getAppUserId());
        UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest();
        uploadDocumentRequest.setAccountLevelCode(tblAccount.getTblAccountLevel().getAccountLevelCode());
        uploadDocumentRequest.setMobileNumber(customerKycRequest.getMobileNumber());
        uploadDocumentRequest.setDocuments(customerKycRequest.getDocuments());
        uploadDocumentRequest.setFiles(customerKycRequest.getDocumentFiles());
        documentService.uploadDocument(uploadDocumentRequest, apiRequest, BigDecimal.valueOf(tblAppUser.getAppUserId()));
        saveTblMultiLanguage(tblCustomer, customerKycRequest);
        // The partner that parked this request is told the outcome once the account is really
        // committed - telling it OPEN for a row that then rolled back would leave the two systems
        // disagreeing with no way back.
        confirmEmployeeOnboardAfterCommit(bulkAccount, customerKycRequest.getMobileNumber(),
                tblAccount.getAccountNo(), tblCustomer.getCustomerId());

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

    private void updateTblCustomerAll(TblCustomerAll tblCustomerAll, long appUserId) {
        tblCustomerAll.setSelfieverified(Constants.YES);
        tblCustomerAll.setUpdateindex(tblCustomerAll.getUpdateindex() != null ? tblCustomerAll.getUpdateindex().add(BigDecimal.ONE) : BigDecimal.ONE);
        tblCustomerAll.setLastupdateuser(new BigDecimal(appUserId));
        tblCustomerAllRepo.saveAndFlush(tblCustomerAll);
    }

    private TblAppUser saveTblAppUser(TblCustomer tblCustomer, CustomerKycRequest customerKycRequest) {
        TblAppUser tblAppUser = tblAppUserRepo.findByCustomerId(tblCustomer.getCustomerId());
        tblAppUser = tblAppUser == null ? new TblAppUser() : tblAppUser;
        tblAppUser.setCreatedate(new Date());
        tblAppUser.setCreateuser(BigDecimal.ONE);
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
            tblDeviceInfo.setLastupdateuser(tblAppUser.getCreateuser());
            tblDeviceInfoRepo.saveAndFlush(tblDeviceInfo);
        }
        return tblAppUser;
    }

    private TblAccount saveTblAccount(TblCustomer tblCustomer, CustomerKycRequest customerKycRequest) {
        TblAccount tblAccount = tblAccountRepo.findByMobileNoAndCnicAndAccountLevelCode(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()), aeSencryption.encryptwith256(customerKycRequest.getNidNumber()), accountLevelOne);
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
        tblAccount.setCreateuser(BigDecimal.ONE);
        tblAccount.setCurrentBalance(BigDecimal.ZERO);
        tblAccount.setAgentAccount(Constants.N);
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

    /** The bulk row parked for this mobile and identity number, or null for an ordinary signup. */
    private TblBulkAccount findBulkAccount(String mobileNumber, String nidNumber) {
        if (isNullOrEmpty(mobileNumber) || isNullOrEmpty(nidNumber)) {
            return null;
        }
        List<TblBulkAccount> rows =
                tblBulkAccountRepo.findByMobileNoAndNidNo(mobileNumber.trim(), nidNumber.trim());
        return rows.isEmpty() ? null : rows.get(0);
    }

    /**
     * The segment the bulk row names, or null to leave the database default in place.
     *
     * <p>A row naming a segment that LKP_SEGMENT does not have is treated as naming none: the
     * customer is still onboarded, on the default segment, rather than the whole KYC failing over a
     * lookup the portal got wrong.</p>
     */
    private LkpSegment resolveBulkSegment(TblBulkAccount bulkAccount) {
        if (bulkAccount == null || isNullOrEmpty(bulkAccount.getSegmentDescr())) {
            return null;
        }
        List<LkpSegment> segments =
                lkpSegmentRepo.findBySegmentDescrIgnoreCase(bulkAccount.getSegmentDescr().trim());
        return segments.isEmpty() ? null : segments.get(0);
    }

    /**
     * Tells the employee-onboard partner what became of the request it parked.
     *
     * <p>Sent after the transaction commits, not before: the account has to exist for the partner
     * to be told it does. If there was no parked request there is nobody to tell.</p>
     *
     * <p>A failure to reach the partner never fails the onboarding - the account is already open,
     * and refusing it now would be worse than a missed notification.</p>
     */
    private void confirmEmployeeOnboardAfterCommit(TblBulkAccount bulkAccount, String mobileNumber,
                                                   String accountNo, long customerId) {
        if (bulkAccount == null) {
            return;
        }
        Runnable send = () -> sendEmployeeOnboardConfirm(mobileNumber, "OPEN", accountNo,
                String.valueOf(customerId), "Account opened");
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send.run();
                }
            });
        } else {
            send.run();
        }
    }

    /** Posts the confirmation. Never throws - see the caller. */
    private void sendEmployeeOnboardConfirm(String mobileNumber, String status, String accountNo,
                                            String customerId, String message) {
        try {
            EmployeeOnboardConfirmRequest confirm = new EmployeeOnboardConfirmRequest();
            confirm.setParkRef("BA-" + mobileNumber);
            confirm.setStatus(status);
            confirm.setDfsAccountNo(accountNo);
            confirm.setDfsCustomerId(customerId);
            confirm.setMessage(message);
            Map<String, String> headers = new HashMap<>();
            headers.put("content-type", "application/json");
            headers.put("accept", "application/json");
            headers.put("X-Employee-Onboard-Key", employeeOnboardKey);
            getResponseFromPostAPI(headers, confirm, employeeOnboardConfirmUrl);
        } catch (Exception e) {
            // The account is open either way; the partner can reconcile on parkRef.
            log.error("employee-onboard confirm failed for parkRef BA-{}", mobileNumber, e);
        }
    }

    private TblCustomer saveTblCustomer(CustomerKycRequest customerKycRequest, TblCustomerAll tblCustomerAll,
                                          LkpSegment bulkSegment) {
        TblCustomer tblCustomer = tblCustomerRepo.findByMobileNumberOrNidNo(aeSencryption.encryptwith256(customerKycRequest.getMobileNumber()), aeSencryption.encryptwith256(customerKycRequest.getNidNumber()));
        tblCustomer = tblCustomer == null ? new TblCustomer() : tblCustomer;
        // A bulk row naming a segment wins; otherwise the column is left alone and the database
        // default applies, exactly as before. Only meaningful on insert - the mapping is not
        // updatable, so an existing customer keeps the segment it already has.
        if (bulkSegment != null) {
            tblCustomer.setLkpSegment(bulkSegment);
        }
        tblCustomer.setNidIssueDate(getDateFromString(customerKycRequest.getNidIssuanceDate()));
        tblCustomer.setDob(getDateFromString(customerKycRequest.getDob()));
        tblCustomer.setNidNo(aeSencryption.encryptwith256(customerKycRequest.getNidNumber()));
        tblCustomer.setFullName(aeSencryption.encryptwith256(customerKycRequest.getFullName()));
        tblCustomer.setFirstName(aeSencryption.encryptwith256(customerKycRequest.getFirstName()));
        tblCustomer.setLastName(aeSencryption.encryptwith256(customerKycRequest.getLastName()));
        tblCustomer.setFatherName(aeSencryption.encryptwith256(customerKycRequest.getFatherName()));
        tblCustomer.setTblCustomerAll(tblCustomerAll);
        tblCustomer.setAddressP(aeSencryption.encryptwith256(customerKycRequest.getPermenantAddress()));
        if (customerKycRequest.getNidExpiryDate() != null && !customerKycRequest.getNidExpiryDate().isEmpty()) {
            tblCustomer.setNidExpiryDate(getDateFromString(customerKycRequest.getNidExpiryDate()));
        }

        tblCustomer.setIsBlackListed(Constants.N);
        tblCustomer.setBiometricVerified(Constants.YES);
        tblCustomer.setCreateuser(BigDecimal.ONE);
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
            return objectMapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getDateFromString(String nidIssuanceDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(nidIssuanceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
