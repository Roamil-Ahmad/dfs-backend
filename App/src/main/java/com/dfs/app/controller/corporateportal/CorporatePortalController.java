package com.dfs.app.controller.corporateportal;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.MpinVerificationRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.service.AccountDetailService;
import com.dfs.app.util.AuthenticationException;
import com.dfs.app.util.GenericResponseCode;
import com.dfs.app.util.RequestValidator;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Customer MPIN verification for the Corporate Portal.
 *
 * <p>The mobile endpoint {@code /v1/mpinVerification} authenticates the caller's app login token.
 * The portal has no app login, so it cannot present one. This endpoint answers the same request
 * with the same response, authenticated as the portal itself.</p>
 *
 * <p>Nothing is reimplemented: it runs the same validator and calls the same
 * {@link AccountDetailService#mpinVerifcation} method as its mobile twin, which resolves the
 * customer from {@code payload.mobileNumber} and compares the encrypted MPIN. The token was only
 * ever satisfying the controller guard.</p>
 *
 * <p>This exists chiefly so the Transactions service can verify a <b>customer's</b> MPIN on the
 * portal funds-transfer path. The MPIN itself is still required and still checked - dropping the
 * app token does not drop the MPIN.</p>
 *
 * <p><b>Authentication.</b> A shared key in {@code X-Portal-Key}, compared constant-time against
 * {@code corporate.portal.apiKey}. The property has no default, so a deployment that has not set
 * {@code CORPORATE_PORTAL_API_KEY} fails to start rather than exposing MPIN checking unprotected.
 * The mobile endpoint is untouched and still requires a bearer token.</p>
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CorporatePortalController extends HelperClass {

    /** Header the portal presents its shared key in. */
    private static final String PORTAL_KEY_HEADER = "X-Portal-Key";

    /**
     * Stands in for the handset id the shared validator insists on. The portal has no device, and
     * the service layer never reads this field.
     */
    private static final String PORTAL_DEVICE_MARKER = "CORPORATE-PORTAL";

    @Autowired
    private AccountDetailService accountDetailService;

    @Value("${corporate.portal.apiKey}")
    private String corporatePortalApiKey;

    @PostMapping("/v1/corporate/mpinVerification")
    public ResponseEntity<HashMap<String, Object>> mpinVerification(@RequestBody Request request,
                                                                    HttpServletRequest httpServletRequest)
            throws JsonProcessingException {

        authenticatePortal(httpServletRequest);
        if (request.getImieNo() == null || request.getImieNo().trim().isEmpty()) {
            request.setImieNo(PORTAL_DEVICE_MARKER);
        }
        MpinVerificationRequest mpinVerificationRequest =
                fromJson(convertObjecttoJson(request.getPayload()), MpinVerificationRequest.class);
        RequestValidator.validateMpinVerificationRequest(mpinVerificationRequest, request);
        HashMap<String, Object> response = accountDetailService.mpinVerifcation(mpinVerificationRequest, request);
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
}
