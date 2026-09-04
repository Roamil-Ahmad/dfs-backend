package com.dfs.agentapp.controller.corporateportal;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.ChangeMpinRequest;
import com.dfs.agentapp.dto.GetBalanceRequest;
import com.dfs.agentapp.dto.MiniStatementRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.service.AccountDetailService;
import com.dfs.agentapp.util.AuthenticationException;
import com.dfs.agentapp.util.GenericResponseCode;
import com.dfs.agentapp.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.HashMap;

/**
 * changempin, getbalance and miniStatment for the Corporate Portal.
 *
 * <p>The portal is a server-side web application. It has no handset, no device binding and no app
 * login, so it cannot present the bearer token the mobile endpoints authenticate. These three
 * endpoints answer the same requests with the same responses, authenticated as the portal itself
 * rather than as an end user.</p>
 *
 * <p>Nothing is reimplemented here. Each method runs the same validator and calls the same
 * {@link AccountDetailService} method as its mobile twin, so the two cannot drift apart: a change
 * to the business rules changes both. The mobile endpoints in
 * {@code AccountDetailController} are untouched and still require a token.</p>
 *
 * <p><b>Authentication.</b> A shared key in the {@code X-Portal-Key} header, compared against
 * {@code corporate.portal.apiKey}. The property has no default, so a deployment that has not set
 * {@code CORPORATE_PORTAL_API_KEY} fails to start rather than exposing account data unprotected.
 * The comparison is constant-time, and the key is never logged or echoed.</p>
 *
 * <p>Each request still names its own subject through {@code payload.mobileNumber}, exactly as the
 * mobile calls do, and the service layer resolves the account from it. The portal key authorises
 * the caller as the portal; it does not select the account.</p>
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CorporatePortalController extends HelperClass {

    /** Header the portal presents its shared key in. */
    private static final String PORTAL_KEY_HEADER = "X-Portal-Key";

    /**
     * Stands in for the handset id the mobile validators insist on. The portal has no device, and
     * the service layer never reads this field - it is a validator formality only, so a fixed
     * marker keeps the shared validators usable without the portal inventing a device id.
     */
    private static final String PORTAL_DEVICE_MARKER = "CORPORATE-PORTAL";

    @Autowired
    private AccountDetailService accountDetailService;

    @Value("${corporate.portal.apiKey}")
    private String corporatePortalApiKey;

    @PostMapping("/v1/corporate/changempin")
    public ResponseEntity<HashMap<String, Object>> changeMpin(@RequestBody Request request,
                                                             HttpServletRequest httpServletRequest)
            throws JsonProcessingException {

        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        ChangeMpinRequest changeMpinRequest =
                fromJson(convertObjecttoJson(request.getPayload()), ChangeMpinRequest.class);
        RequestValidator.validateChangeMpinRequestRequest(changeMpinRequest, request);
        // The app user is resolved inside changeMpin from the payload's mobile number, so the id
        // passed here is unused. The mobile endpoint takes it from the token; the portal has none.
        HashMap<String, Object> response = accountDetailService.changeMpin(changeMpinRequest, BigDecimal.ZERO);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/corporate/getbalance")
    public ResponseEntity<HashMap<String, Object>> getBalance(@RequestBody Request request,
                                                              HttpServletRequest httpServletRequest)
            throws JsonProcessingException {

        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        GetBalanceRequest getBalanceRequest =
                fromJson(convertObjecttoJson(request.getPayload()), GetBalanceRequest.class);
        RequestValidator.validateGetBalanceRequest(getBalanceRequest, request);
        HashMap<String, Object> response = accountDetailService.getBalance(getBalanceRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/corporate/miniStatment")
    public ResponseEntity<HashMap<String, Object>> miniStatment(@RequestBody Request request,
                                                                HttpServletRequest httpServletRequest)
            throws JsonProcessingException, ParseException {

        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        MiniStatementRequest miniStatementRequest =
                fromJson(convertObjecttoJson(request.getPayload()), MiniStatementRequest.class);
        RequestValidator.validateMiniStatmentRequest(miniStatementRequest, request);
        HashMap<String, Object> response = accountDetailService.miniStatement(miniStatementRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    /**
     * Lets the portal through, or refuses it.
     *
     * <p>Rejects a blank configured key outright rather than letting an unset deployment accept a
     * blank header, and compares with {@link MessageDigest#isEqual} so a wrong key takes the same
     * time to reject whatever its prefix. The supplied value is never written anywhere.</p>
     */
    private void authenticatePortal(HttpServletRequest httpServletRequest) {
        String presented = httpServletRequest.getHeader(PORTAL_KEY_HEADER);
        if (presented == null || presented.isEmpty()
                || corporatePortalApiKey == null || corporatePortalApiKey.isEmpty()) {
            throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }
        if (!MessageDigest.isEqual(presented.getBytes(StandardCharsets.UTF_8),
                corporatePortalApiKey.getBytes(StandardCharsets.UTF_8))) {
            throw new AuthenticationException(GenericResponseCode.INVALID_TOKEN.getResponseCode());
        }
    }

    /**
     * Supplies the device marker when the caller sent none, so the shared validators pass.
     *
     * <p>A value the portal does send is left alone.</p>
     */
    private void applyPortalDeviceMarker(Request request) {
        if (request.getImieNo() == null || request.getImieNo().trim().isEmpty()) {
            request.setImieNo(PORTAL_DEVICE_MARKER);
        }
    }
}
