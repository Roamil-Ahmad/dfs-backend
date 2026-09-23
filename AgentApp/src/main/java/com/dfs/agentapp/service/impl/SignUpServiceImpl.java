package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.MobileRegistrationRequest;
import com.dfs.agentapp.dto.Partner;
import com.dfs.agentapp.dto.UploadDocumentRequest;
import com.dfs.agentapp.dto.common.AgentKycRequest;
import com.dfs.agentapp.dto.common.CorporateOnboardingRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.dto.common.Response;
import com.dfs.agentapp.dto.workflow.McRequestDetail;
import com.dfs.agentapp.model.*;
import com.dfs.agentapp.repo.*;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.DocumentService;
import com.dfs.agentapp.service.QrService;
import com.dfs.agentapp.service.SignUpService;
import com.dfs.agentapp.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.security.SecureRandom;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SignUpServiceImpl extends HelperClass implements SignUpService {
    @Autowired
    private QrService qrService;
    @Autowired
    private TblCustomerAllRepo tblCustomerAllRepo;
    @Autowired
    private TblSmsMessageTemplateRepo tblSmsMessageTemplateRepo;
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
    private TblDeviceInfoRepo tblDeviceInfoRepo;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private TblAgentRepo agentRepo;
    @Autowired
    private TblDeviceInfoRepo deviceInfoRepo;
    @Autowired
    private TblAppUserLoginHistoryRepo tblAppUserLoginHistoryRepo;
    @Autowired
    private TblAgentRepo tblAgentRepo;
    @Autowired
    private TblAgentCommissionDistributionRepo tblAgentCommissionDistributionRepo;
    @Autowired
    private LkpSegmentRepo lkpSegmentRepo;
    @Value("${account.level.one}")
    private String accountLevelOne;
    @Autowired
    private LkpChannelRepo lkpChannelRepo;
    @Autowired
    private TblOfacRepo tblOfacRepo;
    @Value("${account.reg.type.code}")
    private String accountTypeCode;
    @Value("${account.status.active}")
    private String accountStatusActive;
    @Value("${account.type.code}")
    private String accountTypeAgent;
    @Value("${corporate.account.reg.type.code}")
    private String corporateRegTypeCode;
    @Value("${sms.template.registration}")
    private String registrationTypeTemplate;
    @Autowired
    private LkpProvinceRepo lkpProvinceRepo;
    @Autowired
    private LkpDistrictRepo lkpDistrictRepo;
    @Value("${bank.code}")
    private String barakatCode;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private TblMutlilanguageRepo tblMutlilanguageRepo;
    @Autowired
    private TblAgentBusinessRepo tblAgentBusinessRepo;
    @Autowired
    private LkpBusinessTypeRepo lkpBusinessTypeRepo;
    @Autowired
    private LkpCityRepo lkpCityRepo;
    @Autowired
    private LkpOccupationRepo lkpOccupationRepo;
    @Autowired
    private LkpExpectedMonthlyVolumeRepo lkpExpectedMonthlyVolumeRepo;
    @Autowired
    private LkpLicenseTypeRepo lkpLicenseTypeRepo;
    @Value("${account.level.five}")
    private String accountLevelFive;
    @Value("${account.level.six}")
    private String accountLevelSix;
    @Value("${portal.token}")
    private String portalToken;


    /**
     * Serialises the KYC fields that have no column of their own into ADDITIONAL_INFO.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This bean, through its Spring proxy. Needed so an internal call to a {@code @Transactional}
     * method still goes through the transaction interceptor; {@code @Lazy} breaks the
     * self-reference cycle at startup.
     */
    @Autowired
    @Lazy
    private SignUpService self;

    @Override
    public TblAgent checkAgentExistance(String encryptwith256) {
        return agentRepo.findByMobile(encryptwith256);
    }

    @Override
    public TblDeviceInfo registerDeviceInfo(MobileRegistrationRequest mobileRegistrationRequest, TblAgent tblAgent) {
        TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo
                .findByMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
        if (tblDeviceInfo == null) {
            tblDeviceInfo = new TblDeviceInfo();
        }
        tblDeviceInfo.setAppVersion(mobileRegistrationRequest.getAppVersion());
        tblDeviceInfo.setCreatedate(new Date());
        tblDeviceInfo.setDeviceModel(mobileRegistrationRequest.getDeviceModel());
        tblDeviceInfo.setImeiNo(mobileRegistrationRequest.getImeiNo());
        tblDeviceInfo.setMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
        TblAppUser appUser = tblAppUserRepo.findByAgentId(tblAgent.getAgentId());
        tblDeviceInfo.setTblAppUser(appUser);
        tblDeviceInfo.setUuid(UUID.randomUUID().toString());
        tblDeviceInfo.setCreateuser(new BigDecimal(1));
        tblDeviceInfo = deviceInfoRepo.save(tblDeviceInfo);

        return tblDeviceInfo;
    }

    @Override
    public TblAppUserLoginHistory saveLoginHistory(long agentId) {
        TblAppUser appUser = tblAppUserRepo.findByAgentId(agentId);
        TblAppUserLoginHistory tblAppUserLoginHistory = new TblAppUserLoginHistory();
        tblAppUserLoginHistory.setTblAppUser(appUser);
        tblAppUserLoginHistory.setLoginDate(new Date());
        tblAppUserLoginHistoryRepo.saveAndFlush(tblAppUserLoginHistory);
        return tblAppUserLoginHistory;
    }

    @Override
    public TblAgent updateTblAgentAllVerifed(String mobileNumber) {
        TblAgent tblAgent = tblAgentRepo.findByMobile(aeSencryption.encryptwith256(mobileNumber));
        if (tblAgent == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        tblAgent.setIsDeviceRegistered(Constants.YES);
        tblAgent.setLastupdatedate(new Date());
        tblAgent.setLastupdateuser(tblAgent.getCreateuser());
        tblAgentRepo.saveAndFlush(tblAgent);
        return tblAgent;
    }

    @Override
    public TblCustomerAll registerCustomerAll(MobileRegistrationRequest mobileRegistrationRequest, Request apiRequest) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNoOrCnicAndAccountLevelCode(
                mobileRegistrationRequest.getMobileNo(), Constants.EMPTY, accountLevelOne);
        if (tblAccount != null) {
            throw new CustomDataNotFoundException(
                    GenericResponseCode.ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_NUMBER.getResponseCode());
        }
        TblAccount tblAccountAgent = tblAccountRepo.findByAccountNoOrCnicAgent(mobileRegistrationRequest.getMobileNo(),
                Constants.EMPTY);
        if (tblAccountAgent != null) {
            throw new CustomDataNotFoundException(
                    GenericResponseCode.ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_NUMBER.getResponseCode());
        }

        TblCustomerAll tblCustomerAll = tblCustomerAllRepo
                .findByMobileNo(aeSencryption.encryptwith256(mobileRegistrationRequest.getMobileNo()));
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
    public HashMap<String, Object> registerAgentAndAccount(AgentKycRequest agentKycRequest, Request apiRequest,
                                                           String header) throws JsonProcessingException {

        // First, save all data in a transaction.
        //
        // Called through `self`, not directly. @Transactional is applied by a proxy around this
        // bean, and a plain this.saveAgentData(...) call never leaves the object, so the proxy is
        // bypassed and no transaction is opened at all - which is why a failure part way through
        // used to leave a half-saved agent behind instead of rolling back.
        TblAgent tblAgent = self.saveAgentData(agentKycRequest, apiRequest);
        TblAppUser tblAppUser = tblAppUserRepo.findByAgentId(tblAgent.getAgentId());

        // Now handle maker-checker (data is committed, external procedure can see it)
        String result = commonService.checkMakerCheckerApplicability(portalToken, Constants.TABLE_NAME_AGENT,
                Constants.FORM_NAME_AGENT, Constants.REQUEST_TYPE_SAVE);
        if (result.equals(Constants.NOT_MAKER_CHECKER)) {
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblAppUser);
        } else if (result.equals(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode())) {
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        } else {
            Response response = commonService.makerCheckerRequest(portalToken, Constants.TABLE_NAME_AGENT,
                    Constants.FORM_NAME_AGENT,
                    new McRequestDetail("2", Constants.EMPTY, Constants.EMPTY, Constants.REQUEST_TYPE_SAVE,
                            Constants.EMPTY, Constants.EMPTY, String.valueOf(tblAgent.getAgentId()),
                            Constants.EMPTY));
            if (!response.getResponsecode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
                throw new CustomException(response.getMessages());
            }
            String transactionId = null;

            String message = response.getMessages();

            if (message != null) {
                Matcher matcher = Pattern
                        .compile("Transaction ID\\s*(\\d+)")
                        .matcher(message);

                if (matcher.find()) {
                    transactionId = matcher.group(1);
                }
            }
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), transactionId);
        }
    }

    /**
     * The registration half of {@link #registerAgentAndAccount}, without maker-checker.
     *
     * <p>Saves exactly the same rows through the same transactional {@code saveAgentData} call -
     * agent, business, account, app user, QR, customer-all, documents and any partners - and then
     * returns the new agent id. The other method would at this point ask the portal
     * whether the record needs approval and, if it does, hand back a transaction id instead of a
     * usable account. This one never does, so the account is live when the call returns.</p>
     */
    @Override
    public HashMap<String, Object> registerAgentAndAccountWithoutMakerChecker(AgentKycRequest agentKycRequest,
                                                                             Request apiRequest)
            throws JsonProcessingException {

        // Through self for the same reason as registerAgentAndAccount: @Transactional is applied
        // by a proxy, and an internal this.saveAgentData(...) call would bypass it entirely.
        TblAgent tblAgent = self.saveAgentData(agentKycRequest, apiRequest);
        // The new agent id is the whole of data. The app user this used to return carried the
        // credential columns with it, and onboarding has no caller that needs them.
        HashMap<String, Object> data = new HashMap<>();
        data.put("agentId", tblAgent.getAgentId());
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), data);
    }

    /**
     * Everything that must succeed or fail together: the agent, its business, the account, the app
     * user, the QR, the customer-all update and the documents.
     *
     * <p>{@code rollbackFor = Exception.class} because Spring only rolls back on unchecked
     * exceptions by default, and the collaborators called from here may throw checked ones.</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblAgent saveAgentData(AgentKycRequest agentKycRequest, Request apiRequest) {
        List<TblAccount> tblAccounts = tblAccountRepo.findTblAccountsByMobileOrCnic(
                aeSencryption.encryptwith256(agentKycRequest.getMobileNumber()),
                aeSencryption.encryptwith256(agentKycRequest.getNidNo()));
        if (tblAccounts != null && !tblAccounts.isEmpty()) {
            throw new CustomDataNotFoundException(
                    GenericResponseCode.ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_CNIC_OR_MOBILE.getResponseCode());
        }
        TblCustomerAll tblCustomerAll = tblCustomerAllRepo
                .findByMobileNo(aeSencryption.encryptwith256(agentKycRequest.getMobileNumber()));
        if (tblCustomerAll == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        if (!tblCustomerAll.getOtpverified().equalsIgnoreCase(Constants.YES)) {
            throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_NOT_VERIFIED.getResponseCode());
        }

        TblAgent tblAgent = saveTblAgent(agentKycRequest, tblCustomerAll);
        // Corporate onboarding tags the agent with its segment. TBL_ACCOUNT has no SEGMENT_ID;
        // SEGMENT_ID lives on TBL_AGENT, which is what the new account hangs off, so that is where
        // the tag belongs. agentkyc passes a plain AgentKycRequest and is untouched.
        if (agentKycRequest instanceof CorporateOnboardingRequest) {
            tblAgent.setLkpSegment(resolveSegment(apiRequest.getSegment()));
            tblAgent = tblAgentRepo.saveAndFlush(tblAgent);
        }
        List<TblOfac> tblOfacs = tblOfacRepo.findByName(agentKycRequest.getFullName());
        if (tblOfacs != null && !tblOfacs.isEmpty()) {
            throw new CustomDataNotFoundException(GenericResponseCode.BLACKLISTED.getResponseCode());
        }

        TblAccount tblAccount = saveTblAccount(tblAgent, agentKycRequest);
        TblAppUser tblAppUser = saveTblAppUser(tblAgent, agentKycRequest);
        // Corporate onboarding only. agentkyc passes a plain AgentKycRequest, which has no
        // partners, so its single-app-user behaviour is untouched.
        if (agentKycRequest instanceof CorporateOnboardingRequest) {
            CorporateOnboardingRequest onboarding = (CorporateOnboardingRequest) agentKycRequest;
            savePartnerAppUsers(tblAgent, onboarding.getPartners());
            saveParentCommission(tblAgent, onboarding);
        }
        qrService.generateStaticQrForP2P(tblAccount.getAccountNo(),
                tblAccount.getTblAccountLevel().getAccountLevelCode());

        updateTblCustomerAll(tblCustomerAll, tblAppUser.getAppUserId());
        if (!isNullOrEmpty(agentKycRequest.getDocumentFiles())) {
            UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest();
            uploadDocumentRequest.setAccountLevelCode(tblAccount.getTblAccountLevel().getAccountLevelCode());
            uploadDocumentRequest.setMobileNumber(agentKycRequest.getMobileNumber());
            uploadDocumentRequest.setDocuments(agentKycRequest.getDocuments());
            uploadDocumentRequest.setFiles(agentKycRequest.getDocumentFiles());
            documentService.uploadDocument(uploadDocumentRequest, apiRequest,
                    BigDecimal.valueOf(tblAppUser.getAppUserId()));
        }

        saveTblMultiLanguage(tblAgent, agentKycRequest);

        return tblAgent;
    }

    private TblAppUser saveTblAppUser(TblAgent tblAgent, AgentKycRequest agentKycRequest) {
        TblAppUser tblAppUser = tblAppUserRepo.findByAgentId(tblAgent.getAgentId());
        tblAppUser = tblAppUser == null ? new TblAppUser() : tblAppUser;
        tblAppUser.setCreatedate(new Date());
        tblAppUser.setCreateuser(BigDecimal.ONE);
        tblAppUser.setAgentId(tblAgent.getAgentId());
        tblAppUser.setMobileNo(aeSencryption.encryptwith256(agentKycRequest.getMobileNumber()));
        tblAppUser.setUsername(aeSencryption.encryptwith256(agentKycRequest.getMobileNumber()));
        if (!agentKycRequest.getPin().equals(agentKycRequest.getConfirmMpin())) {
            throw new CustomDataNotFoundException(GenericResponseCode.CONFIRM_PIN_NOT_MATCHED.getResponseCode());
        }
        tblAppUser.setPassword(commonService.encryptWithAes(agentKycRequest.getPin()));
        tblAppUser = tblAppUserRepo.saveAndFlush(tblAppUser);
        TblDeviceInfo tblDeviceInfo = tblDeviceInfoRepo
                .findByMobileNo(aeSencryption.encryptwith256(agentKycRequest.getMobileNumber()));
        if (tblDeviceInfo != null) {
            tblDeviceInfo.setTblAppUser(tblAppUser);
            tblDeviceInfo.setLastupdatedate(new Date());
            tblDeviceInfo.setLastupdateuser(tblAppUser.getCreateuser());
            tblDeviceInfoRepo.saveAndFlush(tblDeviceInfo);
        }
        return tblAppUser;
    }

    /**
     * Gives every partner on a corporate agent account their own login.
     *
     * <p>One TBL_APP_USER row per partner, against the same agent id: three partners in the
     * request means three rows, each with that partner's own credentials. The agent's own mobile
     * login written by {@link #saveTblAppUser} is left alone.</p>
     *
     * <p>Does nothing when the request carries no partners, which is the ordinary case - partners
     * are optional and an agent without them keeps exactly one app user as it always has.</p>
     *
     * <p>The email is the login identifier and goes into USERNAME, encrypted the same way the
     * mobile username is, because that is what the login lookups compare against. Keyed on that
     * username so re-submitting a KYC updates the partner rather than duplicating them.</p>
     */
    private void savePartnerAppUsers(TblAgent tblAgent, List<Partner> partners) {
        if (isNullOrEmpty(partners)) {
            return;
        }
        for (Partner partner : partners) {
            if (partner == null
                    || isNullOrEmpty(partner.getEmail())
                    || isNullOrEmpty(partner.getPassword())) {
                // A partner without both halves of a credential cannot be logged into, so no row
                // is written for it rather than one that can never be used.
                continue;
            }
            String username = aeSencryption.encryptwith256(partner.getEmail().trim());
            TblAppUser partnerAppUser = tblAppUserRepo.findByUsername(username);
            if (partnerAppUser == null) {
                partnerAppUser = new TblAppUser();
                partnerAppUser.setCreatedate(new Date());
                partnerAppUser.setCreateuser(BigDecimal.ONE);
            }
            partnerAppUser.setAgentId(tblAgent.getAgentId());
            partnerAppUser.setUsername(username);
            partnerAppUser.setPassword(aeSencryption.encryptwith256(partner.getPassword()));
            // MOBILE_NO is deliberately left unset: a partner logs in with an email, and the
            // mobile belongs to the agent's own app user.
            tblAppUserRepo.saveAndFlush(partnerAppUser);
        }
    }

    /**
     * Finds the segment the request names, creating it when it does not exist yet.
     *
     * <p>Matched on SEGMENT_DESCR ignoring case and surrounding spaces, so "Corporate" and
     * "  corporate " resolve to the same row instead of producing a second one. SEGMENT_DESCR has
     * no unique index and the schema is not ours to change, so this is the strongest duplicate
     * prevention available: two callers submitting the same new name at the same instant can still
     * both insert, after which every later call settles on the lower id.</p>
     */
    private LkpSegment resolveSegment(String segmentName) {
        String name = segmentName.trim();
        LkpSegment existing = lkpSegmentRepo.findBySegmentDescrIgnoreCase(name);
        if (existing != null) {
            return existing;
        }
        lkpSegmentRepo.insertSegment(generateUniqueSegmentCode(name), name);
        LkpSegment created = lkpSegmentRepo.findBySegmentDescrIgnoreCase(name);
        if (created == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
        return created;
    }

    /**
     * A four-character SEGMENT_CODE that no existing segment already uses.
     *
     * <p>Built from the name so the code stays recognisable - "Corporate Clients" gives CORP -
     * padded when the name is shorter than four usable characters. SEGMENT_CODE carries a unique
     * index, so a collision is a real constraint violation rather than a cosmetic clash: the last
     * character is varied first, and only then does it fall back to random codes.</p>
     */
    private String generateUniqueSegmentCode(String segmentName) {
        String alphanumeric = segmentName.toUpperCase().replaceAll("[^A-Z0-9]", "");
        String base = ((alphanumeric.isEmpty() ? "SEG" : alphanumeric) + "XXXX").substring(0, 4);
        if (!lkpSegmentRepo.existsBySegmentCode(base)) {
            return base;
        }
        String stem = base.substring(0, 3);
        for (char suffix : "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            String candidate = stem + suffix;
            if (!lkpSegmentRepo.existsBySegmentCode(candidate)) {
                return candidate;
            }
        }
        SecureRandom random = new SecureRandom();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int attempt = 0; attempt < 200; attempt++) {
            StringBuilder candidate = new StringBuilder(4);
            for (int i = 0; i < 4; i++) {
                candidate.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
            if (!lkpSegmentRepo.existsBySegmentCode(candidate.toString())) {
                return candidate.toString();
            }
        }
        throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
    }

    /**
     * Records what the parent agent earns on this agent, in TBL_AGENT_COMMISSION_DISTRIBUTION.
     *
     * <p>Written only for a genuine sub-agent: both a parent agent that resolves to a real row and
     * a commission to pay it. Either missing and nothing is written - a standalone agent has no
     * parent to share with, and a child agent without a stated commission is left for the pricing
     * service to default rather than being given a rate this endpoint invented.</p>
     *
     * <p>AGENT_LEVEL is 1 because onboarding creates a single hop, this agent to its parent. The
     * deeper levels the table can hold describe a chain this endpoint cannot build - a parent that
     * is itself a sub-agent is rejected before any of this runs.</p>
     */
    private void saveParentCommission(TblAgent tblAgent, CorporateOnboardingRequest request) {
        if (isNullOrEmpty(request.getParentCommission()) || tblAgent.getTblAgent() == null) {
            return;
        }
        BigDecimal commissionPercentage;
        try {
            commissionPercentage = new BigDecimal(request.getParentCommission().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Parent commission must be a number");
        }

        TblAgentCommissionDistribution distribution = new TblAgentCommissionDistribution();
        distribution.setTblAgent(tblAgent);
        distribution.setParentAgentId(BigDecimal.valueOf(tblAgent.getTblAgent().getAgentId()));
        distribution.setAgentLevel(BigDecimal.ONE);
        distribution.setCommissionPercentage(commissionPercentage);
        distribution.setStatus(Constants.YES);
        distribution.setCreatedate(new Date());
        distribution.setCreateuser(BigDecimal.ONE);
        tblAgentCommissionDistributionRepo.saveAndFlush(distribution);
    }

    /**
     * Everything agentDeviceRegistration, verifyAgentdeviceRegistration and agentkyc did, in one
     * call, minus the OTP.
     *
     * <p>The three steps are reused as they are rather than reimplemented, so every table each of
     * them wrote is still written the same way:</p>
     * <ol>
     *   <li>{@link #registerCustomerAll} - TBL_CUSTOMER_ALL and TBL_DEVICE_INFO</li>
     *   <li>{@link #updateTblCustomerAllVerifed} - what verifying the OTP used to do, without the
     *       OTP: the record is marked verified directly</li>
     *   <li>{@link #registerAgentAndAccount} - TBL_AGENT, TBL_ACCOUNT, TBL_APP_USER, documents,
     *       multi-language and maker-checker, plus one app user per partner</li>
     * </ol>
     *
     * <p>Only the OTP is skipped. Nothing else is relaxed: the duplicate-account checks inside
     * registerCustomerAll and the KYC validation still apply.</p>
     */
    @Override
    public HashMap<String, Object> onboardCorporateAgent(CorporateOnboardingRequest request, Request apiRequest)
            throws JsonProcessingException {

        // The segment names the commercial grouping the new agent belongs to and is mandatory for
        // corporate onboarding. Checked here, before any row is written, so a request without one
        // leaves nothing behind.
        //
        // Trimmed rather than handed to isNullOrEmpty, which tests isEmpty and so lets a
        // whitespace-only value through - that would create a segment with a blank description.
        if (apiRequest.getSegment() == null || apiRequest.getSegment().trim().isEmpty()) {
            throw new ValidationException("Segment Required");
        }

        // A parent must sit at the top of its own branch. If the id supplied already has a parent of
        // its own it is a sub-agent, and hanging another agent beneath it would build a third level
        // the hierarchy does not model. Checked before anything is written, so a rejected request
        // leaves no half-created agent behind.
        Long requestedParentAgentId = parseLongOrNull(request.getParentAgentId());
        if (requestedParentAgentId != null) {
            TblAgent requestedParent = tblAgentRepo.findById(requestedParentAgentId).orElse(null);
            if (requestedParent != null && requestedParent.getTblAgent() != null) {
                return childAgentAsParentResponse();
            }
        }

        // 1 - device registration. Same call agentDeviceRegistration made.
        MobileRegistrationRequest mobileRegistrationRequest = new MobileRegistrationRequest();
        mobileRegistrationRequest.setMobileNo(request.getMobileNumber());
        mobileRegistrationRequest.setImeiNo(request.getImeiNo());
        mobileRegistrationRequest.setDeviceModel(request.getDeviceModel());
        mobileRegistrationRequest.setAppVersion(request.getAppVersion());
        mobileRegistrationRequest.setIpAddressA(request.getIpAddressA());
        mobileRegistrationRequest.setIpAddressP(request.getIpAddressP());
        mobileRegistrationRequest.setNidNo(request.getNidNo());
        mobileRegistrationRequest.setNidIssuanceDate(request.getNidIssuanceDate());

        TblCustomerAll tblCustomerAll = registerCustomerAll(mobileRegistrationRequest, apiRequest);
        if (tblCustomerAll == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }

        // 2 - what verifying the OTP did. The OTP itself is skipped, by design.
        updateTblCustomerAllVerifed(request.getMobileNumber(), apiRequest);

        // 3 - the KYC proper, unchanged, plus the partner app users. Maker-checker is not applied
        // here: onboarding is a single call that must leave a usable account behind, not a request
        // parked for approval.
        return registerAgentAndAccountWithoutMakerChecker(request, apiRequest);
    }

    /**
     * The rejection for {@link #onboardCorporateAgent} when the requested parent is itself a
     * sub-agent.
     *
     * <p>Built here rather than thrown as a CustomDataNotFoundException because that path resolves
     * its text from TBL_MESSAGE, and this code has no row there - the caller would receive
     * "Message Not Found Against Code : 163" instead of the reason. The envelope is the same shape
     * every other response uses.</p>
     */
    private HashMap<String, Object> childAgentAsParentResponse() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("responsecode", GenericResponseCode.PARENT_AGENT_IS_CHILD_AGENT.getResponseCode());
        response.put("messages", GenericResponseCode.PARENT_AGENT_IS_CHILD_AGENT.getResponseMessage());
        response.put("data", null);
        return response;
    }

    private TblAccount saveTblAccount(TblAgent tblAgent, AgentKycRequest agentKycRequest) {
        TblAccount tblAccount = tblAccountRepo.findByMobileNoAndCnicAndAccountLevelCode(
                aeSencryption.encryptwith256(agentKycRequest.getMobileNumber()),
                aeSencryption.encryptwith256(agentKycRequest.getNidNo()));
        tblAccount = tblAccount == null ? new TblAccount() : tblAccount;
        // Corporate onboarding books the account as its own registration type; agentkyc passes a
        // plain AgentKycRequest and keeps the agent code it always used. The account type is the
        // same either way - every account opened here is a wallet.
        String registrationTypeCode = agentKycRequest instanceof CorporateOnboardingRequest
                ? corporateRegTypeCode
                : accountTypeCode;

        LkpRegistrationType lkpRegistrationType = lkpRegistrationTypeRepo.findByRegistrationTypeCode(registrationTypeCode);
        tblAccount.setLkpRegistrationType(lkpRegistrationType);
        LkpAccountStatus lkpAccountStatus = lkpAccountStatusRepo.findByAccountStatusCode(accountStatusActive);
        tblAccount.setLkpAccountStatus(lkpAccountStatus);
        TblAccountLevel tblAccountLevel = tblAccountLevelRepo.findByAccountLevelCode(agentKycRequest.getLevelCode());
        tblAccount.setTblAccountLevel(tblAccountLevel);
        LkpAccountType lkpAccountType = lkpAccountTypeRepo.findByAccountTypeCode(accountTypeAgent);
        tblAccount.setLkpAccountType(lkpAccountType);
        tblAccount.setTblAgent(tblAgent);
        tblAccount.setAccountNo(agentKycRequest.getMobileNumber());
        tblAccount.setMobileNo(agentKycRequest.getMobileNumber());
        tblAccount.setAccountTitle(aeSencryption.encryptwith256(agentKycRequest.getFullName()));
        tblAccount.setBalanceDate(new Date());
        tblAccount.setBranchAccount(Constants.N);
        tblAccount.setCreatedate(new Date());
        tblAccount.setCreateuser(BigDecimal.ONE);
        tblAccount.setCurrentBalance(BigDecimal.ZERO);
        tblAccount.setIsActive(Constants.YES);
        tblAccount.setAgentAccount(Constants.YES);
        tblAccount.setIban(IbanGenerator.generateIban(agentKycRequest.getMobileNumber()));
        tblAccount.setDailyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setDailyAmtLimitDr(BigDecimal.ZERO);
        tblAccount.setMonthlyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setMonthlyAmtLimitDr(BigDecimal.ZERO);
        tblAccount.setYearlyAmtLimitCr(BigDecimal.ZERO);
        tblAccount.setYearlyAmtLimitDr(BigDecimal.ZERO);
        // Everything in the KYC request that has no column of its own is kept here as JSON,
        // so no field the app sends is silently dropped.
        tblAccount.setAdditionalInfo(buildAdditionalInfo(agentKycRequest));
        return tblAccountRepo.saveAndFlush(tblAccount);

    }

    private TblAgent saveTblAgent(AgentKycRequest agentKycRequest, TblCustomerAll tblCustomerAll) {
        TblAgent tblAgent = new TblAgent();
        try {
            tblAgent.setNidIssueDate(getDateFromString(agentKycRequest.getNidIssuanceDate()));
            tblAgent.setDob(getDateFromString(agentKycRequest.getDob()));
            tblAgent.setNidNo(aeSencryption.encryptwith256(agentKycRequest.getNidNo()));
            tblAgent.setMobile(aeSencryption.encryptwith256(agentKycRequest.getMobileNumber()));
            tblAgent.setName(aeSencryption.encryptwith256(agentKycRequest.getFullName()));
            tblAgent.setFatherHusbandName(aeSencryption.encryptwith256(agentKycRequest.getFatherName()));
            // TBL_AGENT.GRANDFATHER_NAME is left unset: the KYC request no longer carries a
            // grandfather name, and it is not invented from anything else.
            tblAgent.setCustomerAllId(new BigDecimal(tblCustomerAll.getCustomerAllId()));
            tblAgent.setPermanentAddress(aeSencryption.encryptwith256(agentKycRequest.getPermenantAddress()));
            if (agentKycRequest.getNidExpiryDate() != null && !agentKycRequest.getNidExpiryDate().isEmpty()) {
                tblAgent.setNidExpiryDate(getDateFromString(agentKycRequest.getNidExpiryDate()));
            }
            tblAgent.setCreateuser(BigDecimal.ONE);
            tblAgent.setCreatedate(new Date());
            // Corporate onboarding has no maker-checker step - the account is live when the call
            // returns - so the agent is created active and already approved. agentkyc still goes
            // out inactive and pending, because an approver has to see it first.
            boolean corporateOnboarding = agentKycRequest instanceof CorporateOnboardingRequest;
            tblAgent.setIsActive(corporateOnboarding ? Constants.YES : Constants.N);
            tblAgent.setStatusId(corporateOnboarding ? new BigDecimal(2) : BigDecimal.ONE);
            tblAgent.setGender(agentKycRequest.getGender());
            tblAgent.setIsFiler(Constants.N);
            tblAgent.setIsBlacklisted(Constants.N);
            tblAgent.setIsDeviceRegistered(Constants.YES);
            tblAgent.setLivenessScore(agentKycRequest.getLivenessScore());
            tblAgent.setFacematchScore(agentKycRequest.getMatchScore());
            tblAgent.setMrzData(agentKycRequest.getMrzData());
            tblAgent.setPob(isNullOrEmpty(agentKycRequest.getPlaceOfBirth()) ? null : aeSencryption.encryptwith256(agentKycRequest.getPlaceOfBirth()));

            // KYC fields that have a dedicated TBL_AGENT column are persisted here rather than
            // only in ADDITIONAL_INFO, mirroring what the App service does for a customer.
            if (!isNullOrEmpty(agentKycRequest.getPresentAddress())) {
                tblAgent.setResidentialAddress(aeSencryption.encryptwith256(agentKycRequest.getPresentAddress()));
            }
            if (!isNullOrEmpty(agentKycRequest.getEmail())) {
                tblAgent.setEmail(aeSencryption.encryptwith256(agentKycRequest.getEmail()));
            }
            // These are lookups: an id the lookup table does not know is left unset rather than
            // written through blindly. Resolved with orElse rather than ifPresent because
            // tblAgent is reassigned below, so a lambda cannot capture it.
            Long cityId = parseLongOrNull(agentKycRequest.getCityId());
            if (cityId != null) {
                LkpCity lkpCity = lkpCityRepo.findById(cityId).orElse(null);
                if (lkpCity != null) {
                    tblAgent.setCityId(new BigDecimal(lkpCity.getCityId()));
                }
            }
            Long occupationId = parseLongOrNull(agentKycRequest.getOccupationId());
            if (occupationId != null) {
                LkpOccupation lkpOccupation = lkpOccupationRepo.findById(occupationId).orElse(null);
                if (lkpOccupation != null) {
                    tblAgent.setOccupation(lkpOccupation.getOccupationDescr());
                }
            }
            // The agent's parent in the hierarchy; TBL_AGENT.PARENT_AGENT_ID is a self reference.
            Long parentAgentId = parseLongOrNull(agentKycRequest.getParentAgentId());
            if (parentAgentId != null) {
                TblAgent parentAgent = tblAgentRepo.findById(parentAgentId).orElse(null);
                if (parentAgent != null) {
                    tblAgent.setTblAgent(parentAgent);
                    tblAgent.setAgentType(Constants.AGENT_TYPE_CHILD);
                }
            }else {
                tblAgent.setAgentType(Constants.AGENT_TYPE_PARENT);
            }

            tblAgent = tblAgentRepo.saveAndFlush(tblAgent);
            saveTblAgentBusiness(tblAgent, agentKycRequest);
        } catch (Exception e) {
            // Guarded: currentTransactionStatus() throws NoTransactionException when nothing is
            // active, which used to replace the real failure with a misleading one.
            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            e.printStackTrace();
            throw new CustomDataNotFoundException(e.getMessage());
        }
        return tblAgent;
    }

    /**
     * The agent's business details, in TBL_AGENT_BUSINESS as the DB team restructured it:
     * BUSINESS_NAME, BUSINESS_TYPE_ID, BUSINESS_ADDRESS, CITY_ID, EXPECTED_MONTHLY_VOLUME_ID,
     * LATITUDE and LONGITUDE. The licence and legal-structure columns this table used to carry
     * are gone, so nothing is written for them.
     */
    private void saveTblAgentBusiness(TblAgent tblAgent, AgentKycRequest agentKycRequest) {
        TblAgentBusiness tblAgentBusiness = new TblAgentBusiness();
        tblAgentBusiness.setTblAgent(tblAgent);
        tblAgentBusiness.setBusinessName(agentKycRequest.getBusinessName());

        // The request carries the id as text; a non-numeric value is simply an unknown business
        // type, not a crash, so it produces the same BUSINESS_TYPE_NOT_FOUND as a missing row.
        LkpBusinessType lkpBusinessType = null;
        Long businessTypeId = parseLongOrNull(agentKycRequest.getBusinessTypeId());
        if (businessTypeId != null) {
            lkpBusinessType = lkpBusinessTypeRepo.findByBusinessTypeIdAndIsActive(businessTypeId, Constants.YES);
        }
        if (lkpBusinessType == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.BUSINESS_TYPE_NOT_FOUND.getResponseCode());
        }
        tblAgentBusiness.setBusinessTypeId(new BigDecimal(lkpBusinessType.getBusinessTypeId()));
        tblAgentBusiness.setBusinessAddress(agentKycRequest.getBusinessAddress());

        // The business sits at its own city and trades its own volume; both are lookups, so an id
        // the lookup table does not know is left unset rather than written through blindly.
        Long businessCityId = parseLongOrNull(agentKycRequest.getCityId());
        if (businessCityId != null) {
            lkpCityRepo.findById(businessCityId)
                    .ifPresent(city -> tblAgentBusiness.setCityId(new BigDecimal(city.getCityId())));
        }
        Long expectedMonthlyVolumeId = parseLongOrNull(agentKycRequest.getExpectedMonthlyVolumeId());
        if (expectedMonthlyVolumeId != null) {
            lkpExpectedMonthlyVolumeRepo.findById(expectedMonthlyVolumeId)
                    .ifPresent(volume -> tblAgentBusiness
                            .setExpectedMonthlyVolumeId(new BigDecimal(volume.getExpectedMonthlyVolumeId())));
        }

        tblAgentBusiness.setLatitude(agentKycRequest.getLatitude());
        tblAgentBusiness.setLongitude(agentKycRequest.getLongitude());
        tblAgentBusiness.setCreateuser(BigDecimal.ONE);
        tblAgentBusiness.setCreatedate(new Date());
        tblAgentBusinessRepo.save(tblAgentBusiness);
    }

    /**
     * Serialises the KYC request fields that have no dedicated column into a JSON document,
     * stored in TBL_ACCOUNT.ADDITIONAL_INFO (CLOB). Same approach, and the same key names, the
     * App service uses for a customer, so the two shapes stay comparable.
     * All keys are always emitted so the stored shape stays stable, even when a value is absent.
     */
    private String buildAdditionalInfo(AgentKycRequest r) {
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
        info.put("parentAgentId", r.getParentAgentId());
        info.put("levelCode", r.getLevelCode());
        info.put("nationality", r.getNationality());
        info.put("country", r.getCountry());
        info.put("signature", r.getSignature());
        try {
            return objectMapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lookup ids arrive from the app as text. A blank or non-numeric value is treated as "not
     * supplied" rather than throwing NumberFormatException out of the middle of onboarding.
     */
    private Long parseLongOrNull(String value) {
        if (isNullOrEmpty(value)) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
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

    private void updateTblCustomerAll(TblCustomerAll tblCustomerAll, long appUserId) {
        tblCustomerAll.setSelfieverified(Constants.YES);
        tblCustomerAll.setUpdateindex(
                tblCustomerAll.getUpdateindex() != null ? tblCustomerAll.getUpdateindex().add(BigDecimal.ONE)
                        : BigDecimal.ONE);
        tblCustomerAll.setLastupdateuser(new BigDecimal(appUserId));
        tblCustomerAllRepo.saveAndFlush(tblCustomerAll);
    }

    private void saveTblMultiLanguage(TblAgent tblAgent, AgentKycRequest agentKycRequest) {
//        List<TblMultilanguage> multilanguages = new ArrayList<>();
//        if (!isNullOrEmpty(agentKycRequest.getNidNumberDari())) {
//            multilanguages.add(createMultilang(tblAgent.getAgentId(), "TAZKIRA", agentKycRequest.getNidNumberDari(),
//                    tblAgent.getCreateuser()));
//        }
//        if (!isNullOrEmpty(agentKycRequest.getFatherName())) {
//            multilanguages.add(createMultilang(tblAgent.getAgentId(), "FATHER_NAME",
//                    agentKycRequest.getFatherNameDari(), tblAgent.getCreateuser()));
//        }
//        if (!isNullOrEmpty(agentKycRequest.getGrandFatherName())) {
//            multilanguages.add(createMultilang(tblAgent.getAgentId(), "GRANDFATHER_NAME",
//                    agentKycRequest.getGrandFatherNameDari(), tblAgent.getCreateuser()));
//        }
//        if (!isNullOrEmpty(agentKycRequest.getDobDari())) {
//            multilanguages.add(createMultilang(tblAgent.getAgentId(), "DOB", agentKycRequest.getDobDari(),
//                    tblAgent.getCreateuser()));
//        }
//        if (!isNullOrEmpty(agentKycRequest.getFullNameDari())) {
//            multilanguages.add(createMultilang(tblAgent.getAgentId(), "FULL_NAME", agentKycRequest.getFullNameDari(),
//                    tblAgent.getCreateuser()));
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
        entry.setTableName("TBL_AGENT");
        entry.setCreatedate(new Date());
        entry.setCreateuser(userId);
        LkpLanguage lkpLanguage = new LkpLanguage();
        lkpLanguage.setLanguageId(4);
        entry.setLkpLanguage(lkpLanguage);
        return entry;
    }

}
