/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.controller.localfundtrans

Class Name: FTPostApiController

Date and Time:1/3/2025 10:50 PM

Version:1.0
*/
package com.wallet.transaction.controller.apptransactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.*;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.service.TransactionsService;
import com.wallet.transaction.util.Constants;
import com.wallet.transaction.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FTPostApiController extends HelperClass {

    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private CommonService commonService;

    @PostMapping("/v1/fundsTransferLocal")
    public ResponseEntity<HashMap<String, Object>> fundsTransferLocal(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {

        FundTransferRequest fundTransferRequest = fromJson(convertObjecttoJson(request.getPayload()), FundTransferRequest.class);
        RequestValidator.validateFundTransferRequest(fundTransferRequest, request);
        HashMap<String, Object> response = transactionsService.fundsTransferLocal(fundTransferRequest, request, httpServletRequest.getHeader("Authorization"));
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/fundsTransferLocal");

    }
    @PostMapping("/v1/fundTransferCard")
    public ResponseEntity<HashMap<String, Object>> fundTransferOther(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        FundTransferCardRequest fundTransferCardRequest = fromJson(convertObjecttoJson(request.getPayload()), FundTransferCardRequest.class);
        RequestValidator.validateIbftFundTransferRequest(fundTransferCardRequest, request);
        HashMap<String, Object> response = transactionsService.fundsTransferCard(fundTransferCardRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/fundTransferCard");

    }
    @PostMapping("/v1/cardTransferFund")
    public ResponseEntity<HashMap<String, Object>> cardTransferFund(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        CardTransferFundRequest cardTransferFundRequest = fromJson(convertObjecttoJson(request.getPayload()), CardTransferFundRequest.class);
        RequestValidator.validateCardTransferFundRequest(cardTransferFundRequest, request);
        HashMap<String, Object> response = transactionsService.cardTransferFund(cardTransferFundRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/cardTransferFund");

    }

    @PostMapping("/v1/qrVerifyOtp")
    public ResponseEntity<HashMap<String, Object>> qrVerifyOtp(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        VerifyOtpRequest fundTransferRequest = fromJson(convertObjecttoJson(request.getPayload()), VerifyOtpRequest.class);
        HashMap<String, Object> response = transactionsService.qrVerifyOtp(fundTransferRequest, request, httpServletRequest);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/qrVerifyOtp");

    }

    @GetMapping(value = "/v1/getPurposeOfPayment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getPurposeOfPayment() {
        HashMap<String, Object> response = commonService.getPurposeOfPayment();
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getPurposeOfPayment");

    }
    @GetMapping(value = "/v1/getAllBank", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getAllBank() {
        HashMap<String, Object> response = commonService.getAllBank();
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getAllBank");

    }
    @GetMapping(value = "/v1/getAllCurrency", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getAllCurrency() {
        HashMap<String, Object> response = commonService.getAllCurrency();
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getAllCurrency");

    }
    @PostMapping(value = "/v1/getLovs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getLov(@RequestBody List<String> json) {
        HashMap<String, Object> response = commonService.getLovs(json);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/getLovs");

    }

    @PostMapping("/v1/cardtocard")
    public ResponseEntity<HashMap<String, Object>> cardToCard(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        CardToCardRequest cardToCardRequest = fromJson(convertObjecttoJson(request.getPayload()), CardToCardRequest.class);
        RequestValidator.validateCardToCardRequest(cardToCardRequest, request);
        HashMap<String, Object> response = transactionsService.cardTocard(cardToCardRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/cardtocard");

    }
    @PostMapping("/v1/paybill")
    public ResponseEntity<HashMap<String, Object>> payBill(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        PayBillRequest payBillRequest = fromJson(convertObjecttoJson(request.getPayload()), PayBillRequest.class);
        RequestValidator.validatePayBillRequest(payBillRequest, request);
        HashMap<String, Object> response = transactionsService.payBill(payBillRequest, request, token,userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/paybill");

    }

    @PostMapping("/v1/walletToWallet")
    public ResponseEntity<HashMap<String, Object>> walletToWallet(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        WalletToWalletRqst walletToWalletReq = fromJson(convertObjecttoJson(request.getPayload()), WalletToWalletRqst.class);
        HashMap<String, Object> response = transactionsService.walletToWallet(walletToWalletReq, request, token,userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/walletToWallet");

    }

    @PostMapping("/v1/cardtocardcp")
    public ResponseEntity<HashMap<String, Object>> cardToCardCp(@RequestBody Request request, HttpServletRequest httpServletRequest) throws Exception {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        CardToCardCpRequest cardToCardRequest = fromJson(convertObjecttoJson(request.getPayload()), CardToCardCpRequest.class);
        RequestValidator.validateCardToCardCpRequest(cardToCardRequest, request);
        HashMap<String, Object> response = transactionsService.cardTocardCp(cardToCardRequest, request, httpServletRequest.getHeader("Authorization"),userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/cardtocard");

    }

    @PostMapping("/v1/paybillcp")
    public ResponseEntity<HashMap<String, Object>> payBillCp(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId = commonService.authenticateHeaderAndDevice(httpServletRequest, request);
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        PayBillCpRequest payBillRequest = fromJson(convertObjecttoJson(request.getPayload()), PayBillCpRequest.class);
        RequestValidator.validatePayBillCpRequest(payBillRequest, request);
        HashMap<String, Object> response = transactionsService.payBillCp(payBillRequest, request, token,userId);
        return getCustomizedResponseFormat(HttpStatus.OK, response, "/paybill");

    }
}
