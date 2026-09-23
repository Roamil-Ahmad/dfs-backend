package com.dfs.app.controller.corporateportal;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.AccountDetailsRequest;
import com.dfs.app.dto.BulkAccountRequest;
import com.dfs.app.dto.MpinVerificationRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.service.AccountDetailService;
import com.dfs.app.service.BulkAccountService;
import com.dfs.app.service.CorporateAccountService;
import com.dfs.app.service.CorporateAccountService;
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
 * Corporate Portal endpoints served by app: customer MPIN verification and bulk account upload, and account lookup.
 *
 * <p>The portal is a server-side web application with no handset and no app login, so it cannot
 * present the bearer token the mobile endpoints authenticate. These endpoints authenticate the
 * portal itself instead.</p>
 *
 * <p>{@code /v1/corporate/mpinVerification} answers the same request as the mobile
 * {@code /v1/mpinVerification}, running the same validator and the same
 * {@link AccountDetailService#mpinVerifcation} method - which resolves the customer from
 * {@code payload.mobileNumber} and compares the encrypted MPIN. The token was only ever
 * satisfying the controller guard. It exists chiefly so the Transactions service can verify a
 * <b>customer's</b> MPIN on the portal funds-transfer path; the MPIN is still required and still
 * checked.</p>
 *
 * <p>{@code /v1/corporate/bulkAccounts} has no mobile twin - it writes TBL_BULK_ACCOUNTS, which
 * only the portal feeds.</p>
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
    @Autowired
    private BulkAccountService bulkAccountService;
    @Autowired
    private CorporateAccountService corporateAccountService;

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
     * Stores an account the portal has collected, as a row of TBL_BULK_ACCOUNTS.
     *
     * <p>Takes a list so a batch is one call rather than one request per row; a single account is
     * a list of one. The whole batch is written in one transaction, so a bad row rejects the
     * submission outright instead of leaving a partial batch to reconcile.</p>
     *
     * <p>Authenticated by the portal key, like everything else here - the portal has no app login.</p>
     */
    @PostMapping("/v1/corporate/bulkAccounts")
    public ResponseEntity<HashMap<String, Object>> bulkAccounts(@RequestBody Request request,
                                                                HttpServletRequest httpServletRequest)
            throws JsonProcessingException {

        authenticatePortal(httpServletRequest);
        BulkAccountRequest bulkAccountRequest =
                fromJson(convertObjecttoJson(request.getPayload()), BulkAccountRequest.class);
        RequestValidator.validateBulkAccountRequest(bulkAccountRequest);
        HashMap<String, Object> response = bulkAccountService.saveBulkAccounts(bulkAccountRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    /**
     * Describes one wallet: who it belongs to, what it holds and what state it is in.
     *
     * <p>Looked up on the mobile number, which is the account number and is unique. The identity
     * number and account title are decrypted before they leave - the portal has no key.</p>
     */
    @PostMapping("/v1/corporate/accountDetails")
    public ResponseEntity<HashMap<String, Object>> accountDetails(@RequestBody Request request,
                                                                  HttpServletRequest httpServletRequest)
            throws JsonProcessingException {

        authenticatePortal(httpServletRequest);
        AccountDetailsRequest accountDetailsRequest =
                fromJson(convertObjecttoJson(request.getPayload()), AccountDetailsRequest.class);
        RequestValidator.validateAccountDetailsRequest(accountDetailsRequest);
        HashMap<String, Object> response = corporateAccountService.accountDetails(accountDetailsRequest, request);
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
