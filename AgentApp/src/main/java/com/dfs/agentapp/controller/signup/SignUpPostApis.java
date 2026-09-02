package com.dfs.agentapp.controller.signup;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.MobileRegistrationRequest;
import com.dfs.agentapp.dto.MobileRegistrationResponse;
import com.dfs.agentapp.dto.common.*;
import com.dfs.agentapp.model.*;
import com.dfs.agentapp.repo.TblGlobalConfigRepo;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.LovService;
import com.dfs.agentapp.service.SignUpService;
import com.dfs.agentapp.service.ThirdPartyService;
import com.dfs.agentapp.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

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
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private LovService lovService;

    @PostMapping(value = "/v1/agentDeviceRegistration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> mobileRegistration(@RequestBody Request apiRequest,
                                                                      HttpServletRequest httpServletRequest) throws JsonProcessingException {

        MobileRegistrationRequest mobileRegistrationRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), MobileRegistrationRequest.class);
        RequestValidator.mobileRegistrationRequestValidation(mobileRegistrationRequest);
        TblRequest tblRequest = commonService.saveRequest(mobileRegistrationRequest.getMobileNo(), mobileRegistrationRequest.getImeiNo(), "1", convertObjecttoJson(apiRequest), "/agentDeviceRegistration");
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
        Response response = thirdPartyService.generateOtp(mobileRegistrationRequest.getMobileNo(), Constants.EMPTY, "R", "S", smsTemplateTypeFwr, "A",
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

    @PostMapping(value = "/v1/verifyAgentdeviceRegistration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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

        // Return the agent signup lookup lists, the same way the App service does on
        // deviceRegistration. Province is deliberately not among them: the agent screens no longer
        // ask for one, so the geo lookup that used to drive this response is gone.
        HashMap<String, Object> successResponse = commonService.getResponse(
                GenericResponseCode.SUCCESS.getResponseCode(),
                lovService.getDeviceRegistrationLovs(verifyOtpRequest.getNidNo()));
        return getCustomizedResponseFormat(HttpStatus.OK, successResponse);

    }

    private ResponseEntity<HashMap<String, Object>> technicalIssueResponse(HttpServletRequest request) {
        return getCustomizedResponseFormat(HttpStatus.OK,
                GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(),
                commonService.getResponseMessageByCode(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode()),
                null, request.getRequestURI());
    }


    @PostMapping(value = "/v1/agentkyc", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> agentkyc(@RequestBody Request apiRequest,
                                                               HttpServletRequest httpServletRequest) throws JsonProcessingException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
        AgentKycRequest agentKycRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), AgentKycRequest.class);
        RequestValidator.validateNidRequest(agentKycRequest);
        HashMap<String, Object> response = signUpService.registerAgentAndAccount(agentKycRequest, apiRequest, httpServletRequest.getHeader(Constants.AUTHORIZATION));
        return getCustomizedResponseFormat(HttpStatus.OK, response);
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
    @PostMapping(value = "/v1/agentkyc",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HashMap<String, Object>> agentkycMultipart(
            @RequestPart("request") String requestJson,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {

        Request apiRequest = fromJson(requestJson, Request.class);
        commonService.authenticateHeaderAndDevice(httpServletRequest, apiRequest, Constants.PRE_LOGIN);
        AgentKycRequest agentKycRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), AgentKycRequest.class);
        agentKycRequest.setDocumentFiles(documents);
        RequestValidator.validateNidRequest(agentKycRequest);
        HashMap<String, Object> response = signUpService.registerAgentAndAccount(agentKycRequest, apiRequest, httpServletRequest.getHeader(Constants.AUTHORIZATION));
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    /**
     * Creates an agent account in a single call.
     *
     * <p>Does what agentDeviceRegistration, verifyAgentdeviceRegistration and agentkyc did in
     * sequence, writing the same rows to the same tables, with the OTP generation and
     * verification left out. Being one call there is no earlier token to present, so it
     * authenticates no header - the same position agentDeviceRegistration was in - and hands back
     * a session token of its own in {@code data.authToken}.</p>
     *
     * <p>Accepts an optional {@code partners} list; each partner becomes an app user of their own
     * against the new agent. The three original endpoints are untouched and still work.</p>
     */
    @PostMapping(value = "/v1/corporateonboarding", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> corporateOnboarding(@RequestBody Request apiRequest,
                                                                   HttpServletRequest httpServletRequest) throws JsonProcessingException {

        CorporateOnboardingRequest onboardingRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()),
                CorporateOnboardingRequest.class);
        // The same validation agentkyc applies; onboarding adds no new mandatory field.
        RequestValidator.validateNidRequest(onboardingRequest);
        HashMap<String, Object> response = signUpService.onboardCorporateAgent(onboardingRequest, apiRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }


    /**
     * Same onboarding, with the identity documents sent as real multipart parts instead of base64
     * inside the body.
     *
     * <p>Preferred whenever documents are attached. Base64 in JSON is held several times over in
     * memory - buffered, parsed into UTF-16, logged and decoded - which is what pushed this
     * container past its limit and had it killed mid-request. A multipart part is streamed to disk
     * and read once.</p>
     *
     * <p>Parts: {@code request} carries the JSON envelope (with {@code payload.documents} left
     * out), and {@code documents} is repeated once per file.</p>
     */
    @PostMapping(value = "/v1/corporateonboarding",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HashMap<String, Object>> corporateOnboardingMultipart(
            @RequestPart("request") String requestJson,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {

        Request apiRequest = fromJson(requestJson, Request.class);
        CorporateOnboardingRequest onboardingRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()),
                CorporateOnboardingRequest.class);
        onboardingRequest.setDocumentFiles(documents);
        RequestValidator.validateNidRequest(onboardingRequest);
        HashMap<String, Object> response = signUpService.onboardCorporateAgent(onboardingRequest, apiRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

}
