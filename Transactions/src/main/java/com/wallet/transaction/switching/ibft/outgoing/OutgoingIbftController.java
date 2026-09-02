package com.wallet.transaction.switching.ibft.outgoing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.switching.ibft.IbftRequestValidator;
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
 * Outgoing IBFT API - funds leaving a DFS customer to another bank.
 *
 * Follows the same shape as {@code FTPostApiController}: authenticate the header and device,
 * read the payload out of the standard {@code Request} envelope, validate it, let the service
 * return the response map and hand that map to {@code getCustomizedResponseFormat}.
 *
 * These are new endpoints in their own package; no existing transaction API is touched.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OutgoingIbftController extends HelperClass {

    @Autowired
    private OutgoingIbftService outgoingIbftService;
    @Autowired
    private CommonService commonService;

    /**
     * The bank list the customer picks from. Called before titleFetch: the app shows these,
     * the customer chooses one, and the chosen {@code bankImd} is sent back on both the inquiry
     * and the transfer.
     */
    @PostMapping("/v1/ibft/bankList")
    public ResponseEntity<HashMap<String, Object>> ibftBankList(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        HashMap<String, Object> response = outgoingIbftService.bankList();
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/ibft/bankList");
    }

    /** Resolves and confirms the beneficiary before the customer commits to the transfer. */
    @PostMapping("/v1/ibft/titleFetch")
    public ResponseEntity<HashMap<String, Object>> ibftTitleFetch(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        OutgoingIbftRequest outgoingIbftRequest = fromJson(convertObjecttoJson(request.getPayload()), OutgoingIbftRequest.class);
        IbftRequestValidator.validateOutgoingTitleFetch(outgoingIbftRequest, request);
        HashMap<String, Object> response = outgoingIbftService.titleFetch(outgoingIbftRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/ibft/titleFetch");

    }

    /** Executes the transfer: mock lookup, PKG_MW.OUTGOING_IBFT, then the advice to the switch. */
    @PostMapping("/v1/ibft/advice")
    public ResponseEntity<HashMap<String, Object>> ibftTransfer(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        OutgoingIbftRequest outgoingIbftRequest = fromJson(convertObjecttoJson(request.getPayload()), OutgoingIbftRequest.class);
        IbftRequestValidator.validateOutgoingTransfer(outgoingIbftRequest, request);
        HashMap<String, Object> response = outgoingIbftService.transfer(outgoingIbftRequest, request, userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/ibft/advice");

    }
}
