package com.dfs.app.service.impl;


import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.*;
import com.dfs.app.dto.common.Request;
import com.dfs.app.dto.common.Response;
import com.dfs.app.model.*;
import com.dfs.app.repo.*;
import com.dfs.app.service.CommonService;
import com.dfs.app.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    @Autowired
    private TblAccountRepo tblAccountRepo;

    @Autowired
    private LkpFaqCategoryRepo lkpFaqCategoryRepo;
    @Value("${jasperJsonFilePath.server}")
    private String reportPathServer;
    @Value("${jasperJsonFilePath.widnows}")
    private String reportPathWindows;
    public static final String WINDOWS = "Windows";
    public static final String OS_NAME = "os.name";
    @Autowired
    private Environment env;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${account.type.code}")
    private String accountTypeWallet;


    @Autowired
    private TblTutorialRepo tutorialRepo;

    @Autowired
    private TblContactusRepo contactusRepo;
    @Autowired
    private TblNotificationRepo tblNotificationRepo;
    @Autowired
    private LkpCmsCategoryRepo lkpCmsCategoryRepo;

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
        tblResponse.setCreateuser(BigDecimal.ONE);
        tblResponse.setCreatedate(new Date());
        return tblResponseRepo.saveAndFlush(tblResponse);
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
                    TblAppUser tblAppUser = tblAppUserRepo.findByAccountNo(extractMobileNumber(request.getPayload()));
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
                    TblDeviceInfo tblDeviceInfo = null;
                    if (type.equalsIgnoreCase(Constants.PRE_LOGIN)) {
                        tblDeviceInfo = tblDeviceInfoRepo.findByMobileNo(mobileNumber);
                    } else if (type.equalsIgnoreCase(Constants.AFTER_LOGIN)) {
                        tblDeviceInfo = tblDeviceInfoRepo.findByAccountNo(mobileNumber);

                    }
                    if (tblDeviceInfo != null && tblDeviceInfo.getImeiNo().equalsIgnoreCase(request.getImieNo()) && tblDeviceInfo.getUuid().equalsIgnoreCase(uuid)) {

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
                                BigDecimal appUserId = new BigDecimal((Integer) claims.get(JwtConstants.APP_USER_ID));
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
                        throw new CustomDataNotFoundException(GenericResponseCode.DEVICE_AUTHENTICATION_FAILED.getResponseCode());
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
    @Cacheable(value = "appScreenData", key = "#name + '-' + #languageId")
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
            LkpLanguage lkpLanguage = lkpLanguageRepo.findById(Long.valueOf(languageId)).orElseThrow(() -> new CustomException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
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

    @Override
    public String getMobileNumberByCustomerAllId(TblCustomerAll customerAllId) {
        TblAccount tblAccount = tblAccountRepo.findByCustomerAllId(customerAllId.getCustomerAllId());
        if (tblAccount == null) {
            return aeSencryption.decrypt(customerAllId.getMobileNo());
        } else {
            return tblAccount.getMobileNo();
        }
    }

    @Override
    @Cacheable("faqs")
    public HashMap<String, Object> getFaqs() {
        List<LkpFaqCategory> faqCategories = lkpFaqCategoryRepo.findByIsActive("Y");
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (LkpFaqCategory category : faqCategories) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("faqCategory", category.getFaqCategory());

            List<Map<String, Object>> faqs = new ArrayList<>();
            for (TblFaq faq : category.getTblFaqs()) {
                Map<String, Object> faqMap = new HashMap<>();
                faqMap.put("question", faq.getQuestion());

                // Split answer1 into multiple lines
                String[] lines = faq.getAnswer().split("\\n");
                faqMap.put("answer", Arrays.asList(lines));

                faqs.add(faqMap);
            }

            categoryMap.put("tblFaqs", faqs);
            responseList.add(categoryMap);
        }
        return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), responseList);
    }

    @Async
    public void generateAccountStatement(AccountStatementRequest accountStatementRequest) throws JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, MessagingException {
        List<Object[]> results = tblAccountRepo.findAccountByAppUserId(Long.parseLong(accountStatementRequest.getAppUserId()));
        Object[] row = results.get(0);
        String conString = env.getProperty("spring.datasource.url");
        String conDriver = env.getProperty("spring.datasource.driver-class");
        String userName = env.getProperty("spring.datasource.username");
        String userPassword = env.getProperty("spring.datasource.password");
        String osName = System.getProperty(OS_NAME);
        if (osName.startsWith(WINDOWS)) {
            reportPathServer = reportPathWindows;
        }
        String reportName = reportPathServer + "ACCOUNT_STATEMENT_REPORT.jrxml";
        // 2. Parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("FROMDATE", accountStatementRequest.getFromDate());
        parameters.put("TODATE", accountStatementRequest.getToDate());
        parameters.put("APPUSERID", accountStatementRequest.getAppUserId());
        parameters.put("ACCOUNTHOLDER", row[0]);
        parameters.put("ACCOUNTNUMBER", row[1]);
        parameters.put("IBAN", row[2]);
        parameters.put("STATEMENTPERIOD", accountStatementRequest.getFromDate() + "-" + accountStatementRequest.getToDate());
        parameters.put("LEVEL", row[3]);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportName);
        Class.forName(conDriver).newInstance();
        Connection datasource = DriverManager.getConnection(conString, userName, userPassword);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
        // 6. Send email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(accountStatementRequest.getEmail());
        helper.setSubject("Generated Report");
        helper.setText("Please find the attached PDF report.");
        helper.addAttachment("Account Statement.pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));
        mailSender.send(message);
    }

    public String getMobileNumberByAccountNo(String mobileNumber) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNoAndAccountTypeCode(mobileNumber, accountTypeWallet);
        if (tblAccount != null) {
            return tblAccount.getMobileNo();
        }
        return null;
    }

    @Override
    @Cacheable("tutorials")
    public HashMap<String, Object> getTutorials() {
        List<TblTutorial> tblTutorials = tutorialRepo.findByIsActive("Y");
        return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblTutorials);
    }

    @Override
    @Cacheable("contactUs")
    public HashMap<String, Object> contactUs() {
        List<TblContactus> tblContactusList = contactusRepo.findAll();
        return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), tblContactusList.get(0));
    }

    @Override
    public HashMap<String, Object> taxCertificate() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sum", "2000");
        map.put("period", financialYearCalculator());
        map.put("amount", "8000");
        return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), map);
    }

    @Override
    public HashMap<String, Object> notification(String appUserId) {

        List<TblNotification> trxnNotifications = tblNotificationRepo.findByNotificationTypeAndAppUserId("T", new BigDecimal(appUserId));
        List<TblNotification> promNotifications = tblNotificationRepo.findByNotificationType("P");
        HashMap<String, Object> map = new HashMap<>();
        map.put("transactions", trxnNotifications);
        map.put("promotions", promNotifications);
        return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), map);
    }

    @Override
    public HashMap<String, Object> spending(String accountId) {
        List<Object[]> spendingList = tblAccountRepo.getTransactionSpendings(accountId);

        // Initialize default values
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("amountReceived", "0");
        resultMap.put("countReceived", "0");
        resultMap.put("amountSent", "0");
        resultMap.put("countSent", "0");

        for (Object[] row : spendingList) {
            if (row[2] != null) {
                String amountType = row[2].toString(); // "Received" or "Sent"
                String amountKey = "amount" + amountType;
                String countKey = "count" + amountType;

                resultMap.put(amountKey, row[0] != null ? row[0].toString() : "0");
                resultMap.put(countKey, row[1] != null ? row[1].toString() : "0");
            }
        }

        return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), resultMap);
    }

    @Override
    @Cacheable("appMenu")
    public HashMap<String, Object> appMenu() {
        List<LkpCmsCategory> lkpCmsCategories = lkpCmsCategoryRepo.findByIsActive("Y");
        return getResponse(GenericResponseCode.SUCCESS.getResponseCode(), lkpCmsCategories);
    }
}
