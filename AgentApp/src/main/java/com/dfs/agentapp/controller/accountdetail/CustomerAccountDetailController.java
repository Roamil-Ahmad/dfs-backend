package com.dfs.agentapp.controller.accountdetail;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.*;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.dto.common.VerifyOtpRequest;
import com.dfs.agentapp.service.AccountDetailService;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.CustomerAccountDetailService;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/v1/customer")
public class CustomerAccountDetailController extends HelperClass {
  @Autowired
  private CustomerAccountDetailService customerAccountDetailService;
  @Autowired
  private CommonService commonService;

  @PostMapping("/accountdetail")
  public ResponseEntity<HashMap<String, Object>> accountDetail(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

    commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
    MpinVerificationRequest mpinVerificationRequest = fromJson(convertObjecttoJson(request.getPayload()), MpinVerificationRequest.class);
    HashMap<String, Object> response = customerAccountDetailService.accountDetail(mpinVerificationRequest, request);
    return getCustomizedResponseFormat(HttpStatus.OK, response);
  }

  /**
   * Depositor lookup, called before /v1/internalcashin so the agent can confirm who they are
   * taking cash from and the app can fill depositorName and depositorDob.
   */
  @PostMapping("/cashInInquiry")
  public ResponseEntity<HashMap<String, Object>> cashInInquiry(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

    commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
    CashInInquiryRequest cashInInquiryRequest = fromJson(convertObjecttoJson(request.getPayload()), CashInInquiryRequest.class);
    RequestValidator.validateCashInInquiry(cashInInquiryRequest);
    HashMap<String, Object> response = customerAccountDetailService.cashInInquiry(cashInInquiryRequest, request);
    return getCustomizedResponseFormat(HttpStatus.OK, response);
  }
}
