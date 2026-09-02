/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.controller.localfundtrans

Class Name: FTPostApiController

Date and Time:1/3/2025 10:50 PM

Version:1.0
*/
package com.wallet.transaction.controller.titlefetch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.CardBalanceInquiryCpRequest;
import com.wallet.transaction.dto.CardBalanceInquiryRequest;
import com.wallet.transaction.dto.CardTitleFetchAppRequest;
import com.wallet.transaction.dto.FetchBillRequest;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.service.TransactionsService;
import com.wallet.transaction.util.Constants;
import com.wallet.transaction.util.RequestValidator;
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

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CardTitleFetchApiController extends HelperClass {

    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private CommonService commonService;

    @PostMapping("/v1/initiateCardTitleFetch")
    public ResponseEntity<HashMap<String, Object>> initiateCardTitleFetch(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        CardTitleFetchAppRequest cardTitleFetchRqst = fromJson(convertObjecttoJson(request.getPayload()), CardTitleFetchAppRequest.class);
        RequestValidator.validateCardTitleFetchAppRequest(cardTitleFetchRqst, request);
        HashMap<String, Object> response = transactionsService.initiateCardTitleFetch(cardTitleFetchRqst, request, token,userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/initiateCardTitleFetch");

    }

    @PostMapping("/v1/cardbalanceinquiry")
    public ResponseEntity<HashMap<String, Object>> cardBalanceInquiry(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        CardBalanceInquiryRequest cardBalanceInquiryRequest = fromJson(convertObjecttoJson(request.getPayload()), CardBalanceInquiryRequest.class);
        RequestValidator.validateCardBalanceInquiryRequest(cardBalanceInquiryRequest, request);
        HashMap<String, Object> response = transactionsService.balanceInquiry(cardBalanceInquiryRequest, request, token,userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/initiateCardTitleFetch");

    }

    @PostMapping("/v1/fetchbill")
    public ResponseEntity<HashMap<String, Object>> fetchBill(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        FetchBillRequest fetchBillRequest = fromJson(convertObjecttoJson(request.getPayload()), FetchBillRequest.class);
        RequestValidator.validateFetchBillRequest(fetchBillRequest, request);
        HashMap<String, Object> response = transactionsService.fetchBill(fetchBillRequest, request, token,userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/fetchBill");

    }


    @PostMapping("/v1/cardbalanceinquirycp")
    public ResponseEntity<HashMap<String, Object>> cardBalanceInquiryCp(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        CardBalanceInquiryCpRequest cardBalanceInquiryRequest = fromJson(convertObjecttoJson(request.getPayload()), CardBalanceInquiryCpRequest.class);
        RequestValidator.validateCardBalanceInquiryCpRequest(cardBalanceInquiryRequest, request);
        HashMap<String, Object> response = transactionsService.cardBalanceInquiryCp(cardBalanceInquiryRequest, request, token,userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/initiateCardTitleFetch");

    }
}
