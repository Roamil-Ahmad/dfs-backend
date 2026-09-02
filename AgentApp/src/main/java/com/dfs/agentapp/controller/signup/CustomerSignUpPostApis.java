package com.dfs.agentapp.controller.signup;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.LatLongProviceResponse;
import com.dfs.agentapp.dto.MobileRegistrationRequest;
import com.dfs.agentapp.dto.MobileRegistrationResponse;
import com.dfs.agentapp.dto.common.*;
import com.dfs.agentapp.model.TblAuthAccessToken;
import com.dfs.agentapp.model.TblCustomerAll;
import com.dfs.agentapp.model.TblGlobalConfig;
import com.dfs.agentapp.model.TblRequest;
import com.dfs.agentapp.repo.TblGlobalConfigRepo;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.CustomerSignUpService;
import com.dfs.agentapp.service.LovService;
import com.dfs.agentapp.service.ThirdPartyService;
import com.dfs.agentapp.util.*;
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
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/v1/customer")
public class CustomerSignUpPostApis extends HelperClass {

  @Autowired
  private CommonService commonService;
  @Autowired
  private CustomerSignUpService signUpService;
  @Autowired
  private ThirdPartyService thirdPartyService;
  @Autowired
  private TblGlobalConfigRepo tblGlobalConfigRepo;
  @Autowired
  private JWTSecurity jwtSecurity;
  @Value("${jwt.expiry.time}")
  private String expiryTime;
  @Value("${sms.template.Type}")
  private String smsTemplateTypeFwr;
  @Value("${geo.code.url}")
  private String geoCodeurl;
  @Autowired
  private LovService lovService;

  @PostMapping(value = "/mobileRegistration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HashMap<String, Object>> mobileRegistration(@RequestBody Request apiRequest,
                                                                    HttpServletRequest httpServletRequest) throws JsonProcessingException {

    BigDecimal userId =  commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.AFTER_LOGIN);
    MobileRegistrationRequest mobileRegistrationRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), MobileRegistrationRequest.class);
    RequestValidator.mobileRegistrationRequestValidation(mobileRegistrationRequest);
    TblRequest tblRequest = commonService.saveRequest(mobileRegistrationRequest.getMobileNo(), mobileRegistrationRequest.getImeiNo(), String.valueOf(userId), convertObjecttoJson(apiRequest), "/mobileRegistration");
    TblCustomerAll tblCustomerAll = signUpService.registerCustomerAll(mobileRegistrationRequest, apiRequest, userId);
    if (tblCustomerAll == null) {
      throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
    }
    apiRequest.setImieNo(tblCustomerAll.getImeiNo());
    commonService.saveResponse(tblRequest, "SESSION CREATED");
    String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);

    // CNIC must be verified with NADRA before any registration OTP is issued.
    Response cnicVerificationResponse = thirdPartyService.verifyCnic(mobileRegistrationRequest.getNidNo(),
        mobileRegistrationRequest.getMobileNo(), mobileRegistrationRequest.getNidIssuanceDate(), token);
    if (cnicVerificationResponse == null) {
      return technicalIssueResponse(httpServletRequest);
    }
    if (!GenericResponseCode.SUCCESS.getResponseCode().equalsIgnoreCase(cnicVerificationResponse.getResponsecode())) {
      commonService.saveResponse(tblRequest, cnicVerificationResponse.getMessages());
      return getCustomizedResponseFormat(HttpStatus.OK, cnicVerificationResponse.getResponsecode(),
          cnicVerificationResponse.getMessages(), null, httpServletRequest.getRequestURI());
    }

    Response response = thirdPartyService.generateOtp(mobileRegistrationRequest.getMobileNo(), Constants.EMPTY, "R", "S", smsTemplateTypeFwr, "C",
        apiRequest, token);
    if (response != null) {
      if (response.getResponsecode().equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
        GenerateOtpResponse generateOtpResponse = fromJson(convertObjecttoJson(response.getData()), GenerateOtpResponse.class);
        return getCustomizedResponseFormat(HttpStatus.OK, GenericResponseCode.SUCCESS.getResponseCode(),
            commonService.getResponseMessageByCode(GenericResponseCode.SUCCESS.getResponseCode()),
            new MobileRegistrationResponse("", generateOtpResponse), httpServletRequest.getRequestURI());

      } else {
        return getCustomizedResponseFormat(HttpStatus.OK, response.getResponsecode(), response.getMessages(),
            null, httpServletRequest.getRequestURI());
      }

    } else {
      return getCustomizedResponseFormat(HttpStatus.OK, GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
          commonService.getResponseMessageByCode(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode()),
          null, httpServletRequest.getRequestURI());
    }

  }

  @PostMapping(value = "/deviceRegistration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HashMap<String, Object>> deviceRegistration(@RequestBody Request apiRequest,
                                                                    HttpServletRequest httpServletRequest) throws JsonProcessingException {

    BigDecimal userId =  commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.AFTER_LOGIN);
    String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);

    VerifyOtpRequest verifyOtpRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), VerifyOtpRequest.class);
    RequestValidator.validateVerifyOtp(verifyOtpRequest);

    Response response = thirdPartyService.verifyOtp(verifyOtpRequest, apiRequest, token);

    if (response == null) {
      return technicalIssueResponse(httpServletRequest);
    }

    if (!GenericResponseCode.SUCCESS.getResponseCode().equalsIgnoreCase(response.getResponsecode())) {
      return getCustomizedResponseFormat(HttpStatus.OK, response.getResponsecode(), response.getMessages(),
          null, httpServletRequest.getRequestURI());
    }

    TblCustomerAll tblCustomerAll = signUpService.updateTblCustomerAllVerifed(verifyOtpRequest.getMobileNumber(), apiRequest, userId);

    if (tblCustomerAll == null) {
      return technicalIssueResponse(httpServletRequest);
    }

    // preserve the existing province payload, adding the signup lookup lists alongside it
    ResponseEntity<HashMap<String, Object>> provinceResponse = buildProvinceResponse(apiRequest, httpServletRequest);
    HashMap<String, Object> provinceBody = provinceResponse.getBody();
    HashMap<String, Object> lovs = lovService.getDeviceRegistrationLovs();
    String responseCode = GenericResponseCode.SUCCESS.getResponseCode();
    if (provinceBody != null) {
      lovs.put("province", provinceBody.get("data"));
      if (provinceBody.get("responsecode") != null) {
        responseCode = String.valueOf(provinceBody.get("responsecode"));
      }
    }
    return getCustomizedResponseFormat(HttpStatus.OK, commonService.getResponse(responseCode, lovs));
  }

  @PostMapping(value = "/kyc", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HashMap<String, Object>> customerKyc(@RequestBody Request apiRequest,
                                                             HttpServletRequest httpServletRequest) throws JsonProcessingException {

    BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.AFTER_LOGIN);
    CustomerKycRequest customerKycRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), CustomerKycRequest.class);
    RequestValidator.validateNidRequest(customerKycRequest);
    HashMap<String, Object> response = signUpService.registerCustomerAndAccount(customerKycRequest, apiRequest, httpServletRequest.getHeader(Constants.AUTHORIZATION), userId);
    return getCustomizedResponseFormat(HttpStatus.OK, response);
  }

  @GetMapping("/getdfsid/{id}")
  public ResponseEntity<HashMap<String, Object>> getAllProvince(@PathVariable String id, HttpServletRequest httpServletRequest) {
    HashMap<String, Object> response = signUpService.getdfsid(Long.parseLong(id));
    return getCustomizedResponseFormat(HttpStatus.OK, response);

  }


  private ResponseEntity<HashMap<String, Object>> buildProvinceResponse(Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
//        String lat = apiRequest.getLatitude();
//        String lon = apiRequest.getLogitude();
    // setting mock to disable functionality
    String lat = null;
    String lon = null;
    if (isNullOrEmpty(lat) || isNullOrEmpty(lon)) {
      HashMap<String, Object> allProvinces = lovService.getAllProvince();
      return getCustomizedResponseFormat(HttpStatus.OK, allProvinces);
    }

    String url = geoCodeurl + "?lat=" + lat + "&lon=" + lon;
    Object resp = getResponseFromGetAPI(url);

    if (resp == null) {
      HashMap<String, Object> allProvinces = lovService.getAllProvince();
      return getCustomizedResponseFormat(HttpStatus.OK, allProvinces);
    }

    Response response1 = fromJson(convertObjecttoJson(resp), Response.class);
    if (isNullOrEmpty(response1) || !response1.getResponsecode().equalsIgnoreCase(GenericResponseCode.SUCCESS.getResponseCode())) {
      HashMap<String, Object> allProvinces = lovService.getAllProvince();
      return getCustomizedResponseFormat(HttpStatus.OK, allProvinces);
    }
    LatLongProviceResponse latLongProviceResponse = fromJson(convertObjecttoJson(response1.getData()), LatLongProviceResponse.class);
    List<LovResponse> lovResponses = lovService.getProviceByName(latLongProviceResponse.getProvince());
    if (isNullOrEmpty(lovResponses)) {
      HashMap<String, Object> allProvinces = lovService.getAllProvince();
      return getCustomizedResponseFormat(HttpStatus.OK, allProvinces);
    }

    HashMap<String, Object> successResponse = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), lovResponses);
    return getCustomizedResponseFormat(HttpStatus.OK, successResponse);
  }


  private ResponseEntity<HashMap<String, Object>> technicalIssueResponse(HttpServletRequest request) {
    return getCustomizedResponseFormat(HttpStatus.OK,
        GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
        commonService.getResponseMessageByCode(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode()),
        null, request.getRequestURI());
  }


}
