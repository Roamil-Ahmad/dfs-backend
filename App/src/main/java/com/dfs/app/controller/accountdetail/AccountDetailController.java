package com.dfs.app.controller.accountdetail;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.*;
import com.dfs.app.dto.common.Request;
import com.dfs.app.dto.common.VerifyOtpRequest;
import com.dfs.app.service.AccountDetailService;
import com.dfs.app.service.CommonService;
import com.dfs.app.util.Constants;
import com.dfs.app.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountDetailController extends HelperClass {
    @Autowired
    private AccountDetailService accountDetailService;
    @Autowired
    private CommonService commonService;

    @PostMapping("/v1/mpinVerification")
    public ResponseEntity<HashMap<String, Object>> mpinVerification(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        MpinVerificationRequest mpinVerificationRequest = fromJson(convertObjecttoJson(request.getPayload()), MpinVerificationRequest.class);
        RequestValidator.validateMpinVerificationRequest(mpinVerificationRequest, request);
        HashMap<String, Object> response = accountDetailService.mpinVerifcation(mpinVerificationRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/changempin")
    public ResponseEntity<HashMap<String, Object>> changeMpin(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        ChangeMpinRequest changeMpinRequest = fromJson(convertObjecttoJson(request.getPayload()), ChangeMpinRequest.class);
        RequestValidator.validateChangeMpinRequestRequest(changeMpinRequest, request);
        HashMap<String, Object> response = accountDetailService.changeMpin(changeMpinRequest, userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/getbalance")
    public ResponseEntity<HashMap<String, Object>> getBalance(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        GetBalanceRequest getBalanceRequest = fromJson(convertObjecttoJson(request.getPayload()), GetBalanceRequest.class);
        RequestValidator.validateGetBalanceRequest(getBalanceRequest, request);
        HashMap<String, Object> response = accountDetailService.getBalance(getBalanceRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/viewlimits")
    public ResponseEntity<HashMap<String, Object>> viewLimits(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        GetBalanceRequest getBalanceRequest = fromJson(convertObjecttoJson(request.getPayload()), GetBalanceRequest.class);
        RequestValidator.validateGetBalanceRequest(getBalanceRequest, request);
        HashMap<String, Object> response = accountDetailService.viewLimits(getBalanceRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/miniStatment")
    public ResponseEntity<HashMap<String, Object>> miniStatment(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException, ParseException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        MiniStatementRequest miniStatementRequest = fromJson(convertObjecttoJson(request.getPayload()), MiniStatementRequest.class);
        RequestValidator.validateMiniStatmentRequest(miniStatementRequest, request);
        HashMap<String, Object> response = accountDetailService.miniStatement(miniStatementRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/emailAccountStatment")
    public ResponseEntity<HashMap<String, Object>> emailAccountStatement(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException, JRException, SQLException, MessagingException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        AccountStatementRequest accountStatementRequest = fromJson(convertObjecttoJson(request.getPayload()), AccountStatementRequest.class);
        RequestValidator.validateEmailAccountStatementRequest(accountStatementRequest, request);
        HashMap<String, Object> response = accountDetailService.emailAccountStatement(accountStatementRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/updateEmail")
    public ResponseEntity<HashMap<String, Object>> updateEmail(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException, JRException, SQLException, MessagingException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        UpdateEmailRequest updateEmailRequest = fromJson(convertObjecttoJson(request.getPayload()), UpdateEmailRequest.class);
        RequestValidator.validateUpdateEmailRequest(updateEmailRequest, request);
        HashMap<String, Object> response = accountDetailService.updateEmail(updateEmailRequest, request, httpServletRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/verifyUpdateEmail")
    public ResponseEntity<HashMap<String, Object>> verifyUpdateEmail(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException, JRException, SQLException, MessagingException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        VerifyOtpRequest verifyOtpRequest = fromJson(convertObjecttoJson(request.getPayload()), VerifyOtpRequest.class);
        verifyOtpRequest.setOtpType("EPU");
        verifyOtpRequest.setUserType("C");
        RequestValidator.validateVerifyOtp(verifyOtpRequest);
        HashMap<String, Object> response = accountDetailService.verifyUpdateEmail(verifyOtpRequest, request, httpServletRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/submitComplaint")
    public ResponseEntity<HashMap<String, Object>> submitComplaint(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        ComplaintRequest complaintRequest = fromJson(convertObjecttoJson(request.getPayload()), ComplaintRequest.class);
        RequestValidator.validateComplaintRequest(complaintRequest);
        HashMap<String, Object> response = accountDetailService.sumbitComplaint(complaintRequest, request, userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/getContactList")
    public ResponseEntity<HashMap<String, Object>> getContactList(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException, SQLException {

        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        ContactlistDTO contactlistDTO = fromJson(convertObjecttoJson(request.getPayload()), ContactlistDTO.class);
        RequestValidator.validategetContactListRequest(contactlistDTO);
        HashMap<String, Object> response = accountDetailService.getContactList(contactlistDTO, request, userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }
}
