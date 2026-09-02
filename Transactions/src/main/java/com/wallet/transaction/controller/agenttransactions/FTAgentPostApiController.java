/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.controller.localfundtrans

Class Name: FTPostApiController

Date and Time:1/3/2025 10:50 PM

Version:1.0
*/
package com.wallet.transaction.controller.agenttransactions;

import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.CashOutAgentRequest;
import com.wallet.transaction.dto.CashOutRequest;
import com.wallet.transaction.dto.FundTransferRequest;
import com.wallet.transaction.dto.PurchaseRequest;
import com.wallet.transaction.dto.common.CashInAgentRequest;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.service.TransactionsService;
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
public class FTAgentPostApiController extends HelperClass {

    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private CommonService commonService;

    @PostMapping("/v1/fundsTransferAgent")
    public ResponseEntity<HashMap<String, Object>> fundsTransferAgent(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        FundTransferRequest fundTransferRequest = fromJson(convertObjecttoJson(request.getPayload()), FundTransferRequest.class);
        RequestValidator.validateFundTransferAgentRequest(fundTransferRequest, request, true);
        HashMap<String, Object> response = transactionsService.fundsTransferAgent(fundTransferRequest, request, httpServletRequest.getHeader("Authorization"));
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/fundsTransferAgent");

    }

    @PostMapping("/v1/cashin")
    public ResponseEntity<HashMap<String, Object>> cashIn(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        CashInAgentRequest cashInAgentRqst = fromJson(convertObjecttoJson(request.getPayload()), CashInAgentRequest.class);
        RequestValidator.validatecashInAgentRequest(cashInAgentRqst, request);
        HashMap<String, Object> response = transactionsService.cashInAgent(cashInAgentRqst, request, httpServletRequest.getHeader("Authorization"), userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/cashin");

    }

    @PostMapping("/v1/cashout")
    public ResponseEntity<HashMap<String, Object>> cashOut(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        CashOutAgentRequest cashOutAgentRequest = fromJson(convertObjecttoJson(request.getPayload()), CashOutAgentRequest.class);
        RequestValidator.validatecashOutAgentRequest(cashOutAgentRequest, request);
        HashMap<String, Object> response = transactionsService.cashOutAgent(cashOutAgentRequest, request, httpServletRequest.getHeader("Authorization"), userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/cashin");

    }


    @PostMapping("/v1/internalcashin")
    public ResponseEntity<HashMap<String, Object>> internalCashIn(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        FundTransferRequest fundTransferRequest = fromJson(convertObjecttoJson(request.getPayload()), FundTransferRequest.class);
        RequestValidator.validateFundTransferAgentRequest(fundTransferRequest, request, true);
        HashMap<String, Object> response = transactionsService.cashIn(fundTransferRequest, request, httpServletRequest.getHeader("Authorization"));
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/internalcashin");

    }

    @PostMapping("/v1/internalcashout")
    public ResponseEntity<HashMap<String, Object>> internalCashOut(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        CashOutRequest cashOutRequest = fromJson(convertObjecttoJson(request.getPayload()), CashOutRequest.class);
        RequestValidator.validateFundTransferAgentRequest(cashOutRequest.getFundTransferRequest(), request, false);
        HashMap<String, Object> response = transactionsService.cashOut(cashOutRequest.getFundTransferRequest(), cashOutRequest.getVerifyOtpRequest(), request, httpServletRequest.getHeader("Authorization"));
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/internalcashout");

    }

    @PostMapping("/v1/purchase")
    public ResponseEntity<HashMap<String, Object>> purchase(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        PurchaseRequest walletToWalletReq = fromJson(convertObjecttoJson(request.getPayload()), PurchaseRequest.class);
        HashMap<String, Object> response = transactionsService.purchase(walletToWalletReq, request, userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/purchase");

    }


}
