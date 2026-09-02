package com.dfs.agentapp.controller.accountupgrade;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.CustomerUpdateAccountLevelRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.dto.common.Response;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.CustomerAccountUpgradeService;
import com.dfs.agentapp.service.LovService;
import com.dfs.agentapp.service.ThirdPartyService;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.CustomDataNotFoundException;
import com.dfs.agentapp.util.GenericResponseCode;
import com.dfs.agentapp.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/v1/customer")
public class CustomerAccountUpgradeController extends HelperClass {
    @Autowired
    private CommonService commonService;
    @Autowired
    private CustomerAccountUpgradeService accountUpgradeService;
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Value("${sms.template.Type}")
    private String smsTemplateTypeFwr;
    @Autowired
    private LovService lovService;
    @Value("${base.url}")
    private String baseUrl;


    @PostMapping(value = "/upgradeAccount", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> upgradeAccount(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.AFTER_LOGIN);
        CustomerUpdateAccountLevelRequest updateAccountLevelRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), CustomerUpdateAccountLevelRequest.class);
        RequestValidator.validateUpdateAccountLevelRequest(updateAccountLevelRequest, apiRequest);
        HashMap<String, Object> response = null;
        boolean exist = accountUpgradeService.checkExistance(updateAccountLevelRequest.getMobileNumber(), updateAccountLevelRequest.getAccountLevelCode());
        if (exist) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_ALREADY_PARKED_FOR_APPROVAL.getResponseCode());
        }
        switch (updateAccountLevelRequest.getStep()) {
            case "1":
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

            case "2":
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
            case "3":
                response = accountUpgradeService.upgradeToL2(updateAccountLevelRequest, apiRequest, userId, httpServletRequest.getHeader(Constants.AUTHORIZATION));
                break;
            default:
                response = new HashMap<>();
                response.put("responsecode", GenericResponseCode.INVALID_STEP.getResponseCode());
                response.put("messages", GenericResponseCode.INVALID_STEP.getResponseMessage());
                response.put("data", null);
                break;
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
