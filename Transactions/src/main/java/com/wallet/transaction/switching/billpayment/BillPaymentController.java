package com.wallet.transaction.switching.billpayment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Bill payment API - a DFS customer settling a utility bill through 1LINK.
 *
 * Follows the same shape as {@code FTPostApiController} and the IBFT controller: authenticate the
 * header and device, read the payload out of the standard {@code Request} envelope, validate it,
 * let the service return the response map and hand that map to {@code getCustomizedResponseFormat}.
 *
 * These are new endpoints in their own package; no existing transaction API is touched, and the
 * existing {@code /v1/paybill} and {@code /v1/getbiller} APIs keep working unchanged.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BillPaymentController extends HelperClass {

    @Autowired
    private BillPaymentService billPaymentService;
    @Autowired
    private CommonService commonService;

    /** Retrieves what the consumer owes, before the customer commits to paying it. */
    @PostMapping("/v1/billInquiry")
    public ResponseEntity<HashMap<String, Object>> billInquiry(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        BillPaymentRequest billRequest = fromJson(convertObjecttoJson(request.getPayload()), BillPaymentRequest.class);
        BillPaymentRequestValidator.validateBillInquiry(billRequest, request);
        HashMap<String, Object> response = billPaymentService.billInquiry(billRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/billPayment/billInquiry");

    }

    /** Pays the bill: mock lookup, PKG_MW.BILL_PAYMENT, then the switch. */
    @PostMapping("/v1/billPayment")
    public ResponseEntity<HashMap<String, Object>> billPayment(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        BillPaymentRequest billRequest = fromJson(convertObjecttoJson(request.getPayload()), BillPaymentRequest.class);
        BillPaymentRequestValidator.validateBillPayment(billRequest, request);
        HashMap<String, Object> response = billPaymentService.billPayment(billRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/billPayment/billPayment");

    }
}
