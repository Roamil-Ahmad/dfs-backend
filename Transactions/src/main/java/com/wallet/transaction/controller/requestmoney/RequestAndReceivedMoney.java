/*
Author Name: romail.ahmed

Project Name: transactions

Package Name: com.wallet.transaction.controller.requestmoney

Class Name: RequestAndReceivedMoney

Date and Time:1/11/2025 7:57 PM

Version:1.0
*/
package com.wallet.transaction.controller.requestmoney;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.GetReceivedMoneyRequest;
import com.wallet.transaction.dto.RequestMoneyRequest;
import com.wallet.transaction.dto.TitleFetchRequetMoneyRequest;
import com.wallet.transaction.dto.UpdateReceivedMoneyRequest;
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
public class RequestAndReceivedMoney extends HelperClass {

    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private CommonService commonService;


    @PostMapping("/v1/titleFetchForRequestMoney")
    public ResponseEntity<HashMap<String, Object>> titleFetchForRequestMoney(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        TitleFetchRequetMoneyRequest requetMoneyRequest = fromJson(convertObjecttoJson(request.getPayload()), TitleFetchRequetMoneyRequest.class);
        RequestValidator.validateTitleFetchForRequestMoneyt(requetMoneyRequest, request);
        HashMap<String, Object> response = transactionsService.titleFetchForRequestMoney(requetMoneyRequest, request);
        ResponseEntity<HashMap<String, Object>> customizedResponseFormat = getCustomizedResponseFormat(HttpStatus.OK, response, "/titleFetchForRequestMoney");
        return customizedResponseFormat;

    }


    @PostMapping("/v1/requestMoney")
    public ResponseEntity<HashMap<String, Object>> requestMoney(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        RequestMoneyRequest requetMoneyRequest = fromJson(convertObjecttoJson(request.getPayload()), RequestMoneyRequest.class);
        RequestValidator.validateRequestMoneyt(requetMoneyRequest, request);
        HashMap<String, Object> response = transactionsService.requestMoney(requetMoneyRequest, request, httpServletRequest.getHeader("Authorization"));
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/requestMoney");

    }


    @PostMapping("/v1/getReceivedRequests")
    public ResponseEntity<HashMap<String, Object>> getReceivedRequests(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        GetReceivedMoneyRequest getReceivedMoneyRequest = fromJson(convertObjecttoJson(request.getPayload()), GetReceivedMoneyRequest.class);
        RequestValidator.validateGetReceivedMoneyReq(getReceivedMoneyRequest, request);
        HashMap<String, Object> response = transactionsService.getReceivedRequests(getReceivedMoneyRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getReceivedRequests");

    }

    @PostMapping("/v1/getSentRequest")
    public ResponseEntity<HashMap<String, Object>> getSentRequest(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        GetReceivedMoneyRequest getReceivedMoneyRequest = fromJson(convertObjecttoJson(request.getPayload()), GetReceivedMoneyRequest.class);
        RequestValidator.validateGetReceivedMoneyReq(getReceivedMoneyRequest, request);
        HashMap<String, Object> response = transactionsService.getSentRequest(getReceivedMoneyRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getSentRequest");

    }

    @PostMapping("/v1/updateRequestStatus")
    public ResponseEntity<HashMap<String, Object>> updateRequestStatus(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        UpdateReceivedMoneyRequest updateReceivedMoneyRequest = fromJson(convertObjecttoJson(request.getPayload()), UpdateReceivedMoneyRequest.class);
        RequestValidator.validateupdateRequestStatus(updateReceivedMoneyRequest, request);
        HashMap<String, Object> response = transactionsService.updateRequestStatus(updateReceivedMoneyRequest, request, httpServletRequest.getHeader("Authorization"));
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/updateRequestStatus");

    }



    @PostMapping("/v1/getRequestMoneyHistory")
    public ResponseEntity<HashMap<String, Object>> getRequestMoneyHistory(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        GetReceivedMoneyRequest getReceivedMoneyRequest = fromJson(convertObjecttoJson(request.getPayload()), GetReceivedMoneyRequest.class);
        RequestValidator.validateGetReceivedMoneyReq(getReceivedMoneyRequest, request);
        HashMap<String, Object> response = transactionsService.getRequestMoneyHistory(getReceivedMoneyRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getRequestMoneyHistory");

    }



}
