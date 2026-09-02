package com.dfs.app.controller.signup;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.LatLongProviceResponse;
import com.dfs.app.dto.MobileRegistrationRequest;
import com.dfs.app.dto.MobileRegistrationResponse;
import com.dfs.app.dto.common.*;
import com.dfs.app.model.*;
import com.dfs.app.repo.TblGlobalConfigRepo;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.LovService;
import com.dfs.app.service.SignUpService;
import com.dfs.app.service.ThirdPartyService;
import com.dfs.app.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class SignUpPostApis extends HelperClass {
    Logger LOG = LoggerFactory.getLogger(SignUpPostApis.class);

    @Autowired
    private CommonService commonService;
    @Autowired
    private SignUpService signUpService;
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

    @PostMapping(value = "/v1/mobileRegistration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> mobileRegistration(@RequestBody Request apiRequest,
                                                                      HttpServletRequest httpServletRequest) throws JsonProcessingException {

        MobileRegistrationRequest mobileRegistrationRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), MobileRegistrationRequest.class);
        RequestValidator.mobileRegistrationRequestValidation(mobileRegistrationRequest);
        TblRequest tblRequest = commonService.saveRequest(mobileRegistrationRequest.getMobileNo(), mobileRegistrationRequest.getImeiNo(), "1", convertObjecttoJson(apiRequest), "/mobileRegistration");
        TblCustomerAll tblCustomerAll = signUpService.registerCustomerAll(mobileRegistrationRequest, apiRequest);
        if (tblCustomerAll == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }

        HashMap<String, Object> hmClaims = new HashMap<>();

        hmClaims.put(JwtConstants.IMIE_NUMBER, tblCustomerAll.getImeiNo());
        hmClaims.put(JwtConstants.MOBILE_NUMBER, tblCustomerAll.getMobileNo());
        hmClaims.put(JwtConstants.UUID, tblCustomerAll.getUuid());
        apiRequest.setImieNo(tblCustomerAll.getImeiNo());
        TblGlobalConfig tblGlobalConfig = tblGlobalConfigRepo.findByKeyName(Constants.LOG_OUT_TIME);
        if (tblGlobalConfig != null) {
            expiryTime = tblGlobalConfig.getKeyValue();
        }
        String token = jwtSecurity.createJWTWithClaims(mobileRegistrationRequest.getMobileNo(), hmClaims, Integer.valueOf(expiryTime));
        TblAuthAccessToken tblAuthAccessToken = commonService.createLoginTokenSession(token, tblCustomerAll.getCustomerAllId(), tblCustomerAll.getMobileNo(), Constants.PRE_LOGIN, 1);
        if (tblAuthAccessToken == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.FAILED_TO_CREATE_SESSION.getResponseCode());
        }
        commonService.saveResponse(tblRequest, "SESSION CREATED");

        // CNIC must be verified with NADRA before any registration OTP is issued.
        Response cnicVerificationResponse = thirdPartyService.verifyCnic(mobileRegistrationRequest.getNidNo(),
                mobileRegistrationRequest.getMobileNo(), mobileRegistrationRequest.getNidIssuanceDate(), token);
        if (cnicVerificationResponse == null) {
            return getCustomizedResponseFormat(HttpStatus.OK, GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                    commonService.getResponseMessageByCode(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode()),
                    null, httpServletRequest.getRequestURI());
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
                        new MobileRegistrationResponse(token, generateOtpResponse), httpServletRequest.getRequestURI());

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

    @PostMapping(value = "/v1/deviceRegistration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> deviceRegistration(@RequestBody Request apiRequest,
                                                                      HttpServletRequest httpServletRequest) throws JsonProcessingException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
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

        VerifyOtpResponse verifyOtpResponse = fromJson(convertObjecttoJson(response.getData()), VerifyOtpResponse.class);
        TblCustomerAll tblCustomerAll = signUpService.updateTblCustomerAllVerifed(verifyOtpRequest.getMobileNumber(), apiRequest);

        if (tblCustomerAll == null) {
            return technicalIssueResponse(httpServletRequest);
        }

        // enrich the response with the signup lookup lists (data was previously null), plus the
        // mother-name and birth-place challenges built from this CNIC's nadra record
        HashMap<String, Object> successResponse = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                lovService.getDeviceRegistrationLovs(verifyOtpRequest.getNidNo()));
        return getCustomizedResponseFormat(HttpStatus.OK, successResponse);
    }

    @PostMapping(value = "/v1/customerKyc", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> customerKyc(@RequestBody Request apiRequest,
                                                               HttpServletRequest httpServletRequest) throws JsonProcessingException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
        CustomerKycRequest customerKycRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), CustomerKycRequest.class);
        RequestValidator.validateNidRequest(customerKycRequest);
        HashMap<String, Object> response = signUpService.registerCustomerAndAccount(customerKycRequest, apiRequest, httpServletRequest.getHeader(Constants.AUTHORIZATION));
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @GetMapping("/v1/getdfsid/{id}")
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



    /**
     * Same operation as the JSON endpoint, but the identity documents arrive as real multipart
     * parts instead of base64 inside the body.
     *
     * <p>Base64 in JSON costs roughly ten times the file size in heap: the raw body is buffered,
     * parsed into UTF-16 strings, serialised again by the logging aspect and finally decoded.
     * With several documents that was enough to push the container past its memory limit and have
     * it killed mid-request, which the client saw as "unexpected end of stream". A multipart part
     * is streamed and decoded once.</p>
     *
     * <p>The envelope is taken as a plain string rather than a typed part so the client does not
     * have to set a Content-Type on it - a common source of 415s from mobile HTTP libraries.</p>
     */
    @PostMapping(value = "/v1/customerKyc",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HashMap<String, Object>> customerKycMultipart(
            @RequestPart("request") String requestJson,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {

        Request apiRequest = fromJson(requestJson, Request.class);
        commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
        CustomerKycRequest customerKycRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), CustomerKycRequest.class);
        customerKycRequest.setDocumentFiles(documents);
        RequestValidator.validateNidRequest(customerKycRequest);
        HashMap<String, Object> response = signUpService.registerCustomerAndAccount(customerKycRequest, apiRequest, httpServletRequest.getHeader(Constants.AUTHORIZATION));
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }
}
