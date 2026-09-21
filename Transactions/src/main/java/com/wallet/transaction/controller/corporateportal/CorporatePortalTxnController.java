package com.wallet.transaction.controller.corporateportal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.FundTransferRequest;
import com.wallet.transaction.dto.InitiateLocalFTRequest;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.BillPaymentService;
import com.wallet.transaction.service.TransactionsService;
import com.wallet.transaction.switching.billpayment.BillPaymentRequest;
import com.wallet.transaction.switching.billpayment.BillPaymentRequestValidator;
import com.wallet.transaction.switching.ibft.IbftRequestValidator;
import com.wallet.transaction.switching.ibft.outgoing.OutgoingIbftRequest;
import com.wallet.transaction.switching.ibft.outgoing.OutgoingIbftService;
import com.wallet.transaction.util.AuthenticationException;
import com.wallet.transaction.util.GenericResponseCode;
import com.wallet.transaction.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Transaction APIs for the Corporate Portal: IBFT, bill payment and local funds transfer.
 *
 * <p>The portal is a server-side web application. It has no handset, no device binding and no app
 * login, so it cannot present the bearer token the mobile endpoints authenticate. These endpoints
 * answer the same requests with the same responses, authenticated as the portal itself.</p>
 *
 * <p>Nothing is reimplemented. Each method runs the same validator and calls the same service
 * method as its mobile twin, so the two cannot drift apart: a change to the business rules changes
 * both. The mobile endpoints are untouched and still require a token.</p>
 *
 * <p><b>Authentication.</b> A shared key in the {@code X-Portal-Key} header, compared constant-time
 * against {@code corporate.portal.apiKey}. The property has no default, so a deployment that has
 * not set {@code CORPORATE_PORTAL_API_KEY} fails to start rather than exposing money movement
 * unprotected. The key is never logged or echoed.</p>
 *
 * <p><b>The MPIN is still required.</b> Dropping the app login token does not drop the MPIN:
 * {@code fundsTransferLocal} still verifies the <b>customer's</b> MPIN, against app's portal-key
 * endpoint rather than its token-authenticated one. A wrong MPIN is refused exactly as on mobile.</p>
 *
 * <p>Each request names its own subject through the payload, exactly as the mobile calls do. The
 * portal key authorises the caller as the portal; it does not select the account.</p>
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CorporatePortalTxnController extends HelperClass {

    /** Header the portal presents its shared key in. */
    private static final String PORTAL_KEY_HEADER = "X-Portal-Key";

    /**
     * Stands in for the handset id the shared validators insist on. The portal has no device, and
     * the service layer never reads this field - it is a validator formality only, so a fixed
     * marker keeps the shared validators usable without the portal inventing a device id.
     */
    private static final String PORTAL_DEVICE_MARKER = "CORPORATE-PORTAL";

    @Autowired
    private OutgoingIbftService outgoingIbftService;
    // Two different classes share the simple name BillPaymentService: the one in .service owns
    // getbiller, the one in .switching.billpayment owns billInquiry/billPayment. The switching
    // one is fully qualified because both cannot be imported at once.
    @Autowired
    private BillPaymentService billPaymentService;
    @Autowired
    private com.wallet.transaction.switching.billpayment.BillPaymentService switchingBillPaymentService;
    @Autowired
    private TransactionsService transactionsService;

    @Value("${corporate.portal.apiKey}")
    private String corporatePortalApiKey;

    // ------------------------------------------------------------------ IBFT

    /** Mirrors POST /v1/ibft/bankList. The banks the portal lets the customer pick from. */
    @PostMapping("/v1/corporate/ibft/bankList")
    public ResponseEntity<HashMap<String, Object>> ibftBankList(@RequestBody Request request,
                                                                HttpServletRequest httpServletRequest) {
        authenticatePortal(httpServletRequest);
        HashMap<String, Object> response = outgoingIbftService.bankList();
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/corporate/ibft/bankList");
    }

    /** Mirrors POST /v1/ibft/titleFetch. Resolves the beneficiary before committing. */
    @PostMapping("/v1/corporate/ibft/titleFetch")
    public ResponseEntity<HashMap<String, Object>> ibftTitleFetch(@RequestBody Request request,
                                                                  HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        OutgoingIbftRequest outgoingIbftRequest =
                fromJson(convertObjecttoJson(request.getPayload()), OutgoingIbftRequest.class);
        IbftRequestValidator.validateOutgoingTitleFetch(outgoingIbftRequest, request);
        HashMap<String, Object> response = outgoingIbftService.titleFetch(outgoingIbftRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/corporate/ibft/titleFetch");
    }

    /**
     * Mirrors POST /v1/ibft/advice. Executes the transfer.
     *
     * <p>The mobile endpoint takes a user id from the token and passes it here; the service does
     * not read it, so the portal passes zero rather than inventing an identity.</p>
     */
    @PostMapping("/v1/corporate/ibft/advice")
    public ResponseEntity<HashMap<String, Object>> ibftAdvice(@RequestBody Request request,
                                                              HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        OutgoingIbftRequest outgoingIbftRequest =
                fromJson(convertObjecttoJson(request.getPayload()), OutgoingIbftRequest.class);
        IbftRequestValidator.validateOutgoingTransfer(outgoingIbftRequest, request);
        HashMap<String, Object> response = outgoingIbftService.transfer(outgoingIbftRequest, request, BigDecimal.ZERO);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/corporate/ibft/advice");
    }

    // ---------------------------------------------------------- Bill payment

    /** Mirrors GET /v1/getbiller. The biller list. */
    @GetMapping("/v1/corporate/getbiller")
    public ResponseEntity<HashMap<String, Object>> getBiller(HttpServletRequest httpServletRequest) {
        authenticatePortal(httpServletRequest);
        HashMap<String, Object> response = billPaymentService.getbiller();
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/corporate/getbiller");
    }

    /** Mirrors POST /v1/billInquiry. What the consumer owes, before paying it. */
    @PostMapping("/v1/corporate/billInquiry")
    public ResponseEntity<HashMap<String, Object>> billInquiry(@RequestBody Request request,
                                                               HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        BillPaymentRequest billRequest =
                fromJson(convertObjecttoJson(request.getPayload()), BillPaymentRequest.class);
        BillPaymentRequestValidator.validateBillInquiry(billRequest, request);
        HashMap<String, Object> response = switchingBillPaymentService.billInquiry(billRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/corporate/billInquiry");
    }

    /** Mirrors POST /v1/billPayment. Pays the bill. */
    @PostMapping("/v1/corporate/billPayment")
    public ResponseEntity<HashMap<String, Object>> billPayment(@RequestBody Request request,
                                                               HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        BillPaymentRequest billRequest =
                fromJson(convertObjecttoJson(request.getPayload()), BillPaymentRequest.class);
        BillPaymentRequestValidator.validateBillPayment(billRequest, request);
        HashMap<String, Object> response = switchingBillPaymentService.billPayment(billRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/corporate/billPayment");
    }

    // -------------------------------------------------- Local funds transfer

    /**
     * Mirrors POST /v1/initiateLocalFT. Resolves the beneficiary title and fee.
     *
     * <p>The mobile endpoint forwards its token so an OTP can be raised, but only for
     * {@code type=QR} above the configured threshold. The portal is not a QR channel and passes no
     * token; send any other type and no OTP is involved.</p>
     */
    @PostMapping("/v1/corporate/initiateLocalFT")
    public ResponseEntity<HashMap<String, Object>> initiateLocalFT(@RequestBody Request request,
                                                                   HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        InitiateLocalFTRequest initiateLocalFTRequest =
                fromJson(convertObjecttoJson(request.getPayload()), InitiateLocalFTRequest.class);
        RequestValidator.validateInitiateLocalFTRequest(initiateLocalFTRequest, request);
        HashMap<String, Object> response = transactionsService.initiateLocalFT(initiateLocalFTRequest, request, null);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/corporate/initiateLocalFT");
    }

    /**
     * Mirrors POST /v1/fundsTransferLocal. Moves the money.
     *
     * <p>Runs the same implementation, flagged as a portal call so the <b>customer</b> MPIN is
     * verified against app's portal-key endpoint instead of its token-authenticated one. The MPIN
     * is still mandatory and still checked by app.</p>
     */
    @PostMapping("/v1/corporate/fundsTransferLocal")
    public ResponseEntity<HashMap<String, Object>> fundsTransferLocal(@RequestBody Request request,
                                                                      HttpServletRequest httpServletRequest)
            throws Exception {
        authenticatePortal(httpServletRequest);
        applyPortalDeviceMarker(request);
        FundTransferRequest fundTransferRequest =
                fromJson(convertObjecttoJson(request.getPayload()), FundTransferRequest.class);
        RequestValidator.validateFundTransferRequest(fundTransferRequest, request);
        HashMap<String, Object> response =
                transactionsService.fundsTransferLocal(fundTransferRequest, request, null, true);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/corporate/fundsTransferLocal");
    }

    // ----------------------------------------------------------------- Auth

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

    /** Supplies the device marker when the caller sent none, so the shared validators pass. */
    private void applyPortalDeviceMarker(Request request) {
        if (request.getImieNo() == null || request.getImieNo().trim().isEmpty()) {
            request.setImieNo(PORTAL_DEVICE_MARKER);
        }
    }
}
