package com.dfs.app.controller.debitcard;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.UpdateCardRequest;
import com.dfs.app.dto.card.*;
import com.dfs.app.dto.common.Request;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.DebitCardService;
import com.dfs.app.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

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
