package com.dfs.agentapp.controller.debitcard;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.UpdateCardRequest;
import com.dfs.agentapp.dto.card.*;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.DebitCardService;
import com.dfs.agentapp.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Debit card endpoints for the agent app.
 *
 * <p>The same nine endpoints app exposes, on the same paths and with the same request and response
 * shapes, so a client that already speaks to app needs no change beyond the base URL.</p>
 *
 * <p>Authentication is agentapp's own: the four endpoints that act on a signed-in user go through
 * {@code commonService.authenticateHeaderAndDevice(..., AFTER_LOGIN)}, exactly as every other
 * agentapp endpoint does, so an agent token is what opens them - not an app token.</p>
 *
 * <p>The remaining five are open here because they are open in app, and the two must behave alike:
 * cardLov is reference data, checkCardStatus and cardInquiry are lookups, fetchCardLimits reads
 * limits, and updateCard is the card processor writing back the issued PAN. See the note in the
 * handover: opening updateCard to anyone who can reach the port is worth revisiting, but it is
 * app's behaviour today and changing only one of the two would make them disagree.</p>
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DebitCardPostApis extends HelperClass {
    @Autowired
    private DebitCardService debitCardService;
    @Autowired
    private CommonService commonService;

    @PostMapping("/v1/newCardRequest")
    public ResponseEntity<HashMap<String, Object>> newCardRequest(@RequestBody Request request,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request,
                Constants.AFTER_LOGIN);
        NewRequest newCardRequest = fromJson(convertObjecttoJson(request.getPayload()), NewRequest.class);
        HashMap<String, Object> response = debitCardService.newCardRequest(newCardRequest, request, userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

    @PostMapping("/v1/changeCardPin")
    public ResponseEntity<HashMap<String, Object>> changeCardPin(@RequestBody Request request,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        ChangePinRequest changePinRequest = fromJson(convertObjecttoJson(request.getPayload()), ChangePinRequest.class);
        HashMap<String, Object> response = debitCardService.changeCardPin(changePinRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/forgotCardPin")
    public ResponseEntity<HashMap<String, Object>> forgotCardPin(@RequestBody Request request,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        GeneratePinRequest forgotPinRequest = fromJson(convertObjecttoJson(request.getPayload()),
                GeneratePinRequest.class);
        HashMap<String, Object> response = debitCardService.forgotCardPin(forgotPinRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/updateCardStatus")
    public ResponseEntity<HashMap<String, Object>> updateCardStatus(@RequestBody Request request,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        UpdateStatusRequest updateStatusRequest = fromJson(convertObjecttoJson(request.getPayload()),
                UpdateStatusRequest.class);
        HashMap<String, Object> response = debitCardService.updateCardStatus(updateStatusRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/cardInquiry")
    public ResponseEntity<HashMap<String, Object>> cardInquiry(@RequestBody Request request)
            throws JsonProcessingException {
        InquiryRequest inquiryRequest = fromJson(convertObjecttoJson(request.getPayload()), InquiryRequest.class);
        HashMap<String, Object> response = debitCardService.cardInquiry(inquiryRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @GetMapping("/v1/cardLov")
    public ResponseEntity<HashMap<String, Object>> cardLov() throws JsonProcessingException {
        HashMap<String, Object> response = debitCardService.cardLov();
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/updateCard")
    public ResponseEntity<HashMap<String, Object>> updateCard(@RequestBody Request request)
            throws JsonProcessingException {
        UpdateCardRequest updateCardRequest = fromJson(convertObjecttoJson(request.getPayload()),
                UpdateCardRequest.class);
        HashMap<String, Object> response = debitCardService.updateCard(updateCardRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @GetMapping("/v1/checkCardStatus/{accountId}")
    public ResponseEntity<HashMap<String, Object>> checkCardStatus(@PathVariable String accountId)
            throws JsonProcessingException {
        HashMap<String, Object> response = debitCardService.checkCardStatus(accountId);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @PostMapping("/v1/fetchCardLimits")
    public ResponseEntity<HashMap<String, Object>> fetchLimits(@RequestBody Request request,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {
        FetchLimitRequest fetchLimitRequest = fromJson(convertObjecttoJson(request.getPayload()),
                FetchLimitRequest.class);
        HashMap<String, Object> response = debitCardService.fetchLimits(fetchLimitRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

}
