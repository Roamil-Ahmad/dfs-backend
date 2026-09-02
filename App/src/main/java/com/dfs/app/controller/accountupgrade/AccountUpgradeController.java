package com.dfs.app.controller.accountupgrade;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.UpdateAccountLevelRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.dto.common.Response;
import com.dfs.app.service.AccountUpgradeService;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.LovService;
import com.dfs.app.service.ThirdPartyService;
import com.dfs.app.util.Constants;
import com.dfs.app.util.CustomDataNotFoundException;
import com.dfs.app.util.GenericResponseCode;
import com.dfs.app.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountUpgradeController extends HelperClass {
    @Autowired
    private CommonService commonService;
    @Autowired
    private AccountUpgradeService accountUpgradeService;
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Value("${sms.template.Type}")
    private String smsTemplateTypeFwr;
    @Autowired
    private LovService lovService;
    @Value("${base.url}")
    private String baseUrl;


//    @PostMapping("/v1/upgradeAccount")
//    public ResponseEntity<HashMap<String, Object>> login(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
//
//        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
//        UpdateAccountLevelRequest updateAccountLevelRequest = fromJson(convertObjecttoJson(request.getPayload()), UpdateAccountLevelRequest.class);
//        RequestValidator.validateUpdateAccountLevelRequest(updateAccountLevelRequest, request);
//        boolean exist = accountUpgradeService.checkExistance(updateAccountLevelRequest.getMobileNumber(), updateAccountLevelRequest.getAccountLevelCode());
//        if (exist) {
//            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_ALREADY_PARKED_FOR_APPROVAL.getResponseCode());
//        }
//        HashMap<String, Object> response = accountUpgradeService.upgradeToL2(updateAccountLevelRequest, request, userId, httpServletRequest.getHeader(Constants.AUTHORIZATION));
//        return getCustomizedResponseFormat(HttpStatus.OK, response);
//
//    }

    @PostMapping(value = "/v1/upgradeAccount", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> upgradeAccount(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.AFTER_LOGIN);
        UpdateAccountLevelRequest updateAccountLevelRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), UpdateAccountLevelRequest.class);
        RequestValidator.validateUpdateAccountLevelRequest(updateAccountLevelRequest, apiRequest);
        HashMap<String, Object> response = null;
        boolean exist = accountUpgradeService.checkExistance(updateAccountLevelRequest.getMobileNumber(), updateAccountLevelRequest.getAccountLevelCode());
        if (exist) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_ALREADY_PARKED_FOR_APPROVAL.getResponseCode());
        }
        if (updateAccountLevelRequest.getStep().equals("1")) {
            String mobileNumber = commonService.getMobileNumberByAccountNo(updateAccountLevelRequest.getMobileNumber());
            if (isNullOrEmpty(mobileNumber)) {
                throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
            }
            Response generateOtp = thirdPartyService.generateOtp(mobileNumber, Constants.EMPTY, "R", "S", smsTemplateTypeFwr, "C",
                    apiRequest, httpServletRequest.getHeader(Constants.AUTHORIZATION));
            if (generateOtp != null) {

                return getCustomizedResponseFormat(HttpStatus.OK, generateOtp.getResponsecode(), generateOtp.getMessages(),
                        generateOtp.getData(), httpServletRequest.getRequestURI());
            } else {
                return getCustomizedResponseFormat(HttpStatus.OK, GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), GenericResponseCode.TECHNICAL_ISSUE.getResponseMessage(),
                        null, httpServletRequest.getRequestURI());
            }

        } else if (updateAccountLevelRequest.getStep().equals("2")) {
            Response verifyOtp = thirdPartyService.verifyOtp(updateAccountLevelRequest.getVerifyOtpRequest(), apiRequest, httpServletRequest.getHeader(Constants.AUTHORIZATION));

            if (verifyOtp == null) {
                return technicalIssueResponse(httpServletRequest);
            }

            if (!GenericResponseCode.SUCCESS.getResponseCode().equalsIgnoreCase(verifyOtp.getResponsecode())) {
                return getCustomizedResponseFormat(HttpStatus.OK, verifyOtp.getResponsecode(), verifyOtp.getMessages(),
                        null, httpServletRequest.getRequestURI());
            }
            response = lovService.getAccountUpgradeLovs();
            ((Map<String, Object>) response.get("data")).put(
                    "selfieImage", baseUrl + accountUpgradeService.getSelfieImage(updateAccountLevelRequest.getMobileNumber())
            );
            return getCustomizedResponseFormat(HttpStatus.OK, response);
        } else if (updateAccountLevelRequest.getStep().equals("3")) {
            response = accountUpgradeService.upgradeToL2(updateAccountLevelRequest, apiRequest, userId, httpServletRequest.getHeader(Constants.AUTHORIZATION));
        } else {
            response = new HashMap<>();
            response.put("responsecode", GenericResponseCode.INVALID_STEP.getResponseCode());
            response.put("messages", GenericResponseCode.INVALID_STEP.getResponseMessage());
            response.put("data", null);
        }
        return getCustomizedResponseFormat(HttpStatus.OK, response.get("responsecode").toString(), response.get("messages").toString(), response.get("data"), httpServletRequest.getRequestURI());
    }

    private ResponseEntity<HashMap<String, Object>> technicalIssueResponse(HttpServletRequest request) {
        return getCustomizedResponseFormat(HttpStatus.OK,
                GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                commonService.getResponseMessageByCode(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode()),
                null, request.getRequestURI());
    }

}
