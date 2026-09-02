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
import com.wallet.transaction.dto.InitiateLocalFTRequest;
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
public class TitleFetchApiController extends HelperClass {

    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private CommonService commonService;

    @PostMapping("/v1/initiateLocalFT")
    public ResponseEntity<HashMap<String, Object>> initiateLocalFT(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        InitiateLocalFTRequest initiateLocalFTRequest = fromJson(convertObjecttoJson(request.getPayload()), InitiateLocalFTRequest.class);
        RequestValidator.validateInitiateLocalFTRequest(initiateLocalFTRequest, request);
        HashMap<String, Object> response = transactionsService.initiateLocalFT(initiateLocalFTRequest, request,token);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/initiateLocalFT");

    }

    @PostMapping("/v1/cashOutTitleFetch")
    public ResponseEntity<HashMap<String, Object>> cashOutTitleFetch(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        InitiateLocalFTRequest initiateLocalFTRequest = fromJson(convertObjecttoJson(request.getPayload()), InitiateLocalFTRequest.class);
        RequestValidator.validateInitiateLocalFTRequest(initiateLocalFTRequest, request);
        HashMap<String, Object> response = transactionsService.cashOutTitleFetch(initiateLocalFTRequest, request,token);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/cashOutTitleFetch");

    }
}
