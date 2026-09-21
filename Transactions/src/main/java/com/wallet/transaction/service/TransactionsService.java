/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.service

Interface Name: TransactionsService

Date and Time:1/3/2025 10:53 PM

Version:1.0
*/

package com.wallet.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wallet.transaction.dto.*;
import com.wallet.transaction.dto.common.CashInAgentRequest;
import com.wallet.transaction.dto.common.Request;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

public interface TransactionsService {
    HashMap<String, Object> initiateLocalFT(InitiateLocalFTRequest initiateLocalFTRequest, Request request, String token) throws JsonProcessingException;

    HashMap<String, Object> fundsTransferLocal(FundTransferRequest fundTransferRequest, Request request,String token) throws Exception;

    /** Same transfer, with a flag saying the caller is the Corporate Portal (no app login token). */
    HashMap<String, Object> fundsTransferLocal(FundTransferRequest fundTransferRequest, Request request, String token, boolean portalCall) throws Exception;

    HashMap<String, Object> titleFetchForRequestMoney(TitleFetchRequetMoneyRequest requetMoneyRequest, Request request);

    HashMap<String, Object> requestMoney(RequestMoneyRequest requetMoneyRequest, Request request,String token) throws Exception;

    HashMap<String, Object> getReceivedRequests(GetReceivedMoneyRequest getReceivedMoneyRequest, Request request);

    HashMap<String, Object> getSentRequest(GetReceivedMoneyRequest getReceivedMoneyRequest, Request request);

    HashMap<String, Object> updateRequestStatus(UpdateReceivedMoneyRequest updateReceivedMoneyRequest, Request request,String Token) throws Exception;

    HashMap<String, Object> getRequestMoneyHistory(GetReceivedMoneyRequest getReceivedMoneyRequest, Request request);

    HashMap<String, Object> fundsTransferAgent(FundTransferRequest fundTransferRequest, Request request, String authorization) throws Exception;

    HashMap<String, Object> qrVerifyOtp(VerifyOtpRequest fundTransferRequest, Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException;

    HashMap<String, Object> initiateCardTitleFetch(CardTitleFetchAppRequest cardTitleFetchAppRequest, Request request, String token, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> fundsTransferCard(FundTransferCardRequest fundTransferRequest, Request request, String authorization, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> cardTransferFund(CardTransferFundRequest cardTransferFundRequest, Request request, String authorization, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> balanceInquiry(CardBalanceInquiryRequest cardBalanceInquiryRequest, Request request, String token, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> cardTocard(CardToCardRequest cardToCardRequest, Request request, String authorization, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> fetchBill(FetchBillRequest fetchBillRequest, Request request, String token, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> payBill(PayBillRequest payBillRequest, Request request, String token, BigDecimal userId) throws JsonProcessingException;
    String checkMpinValidation(String mobNo, String mPin, String Token, String imei) throws Exception;

    HashMap<String, Object> cashInAgent(CashInAgentRequest cashInAgentRqst, Request request, String authorization, BigDecimal userId) throws Exception;

    HashMap<String, Object> cashOutAgent(CashOutAgentRequest cashOutAgentRequest, Request request, String authorization, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> cashIn(FundTransferRequest fundTransferRequest, Request request, String authorization) throws Exception;

    HashMap<String, Object> cashOutTitleFetch(InitiateLocalFTRequest initiateLocalFTRequest, Request request, String token) throws JsonProcessingException;

    HashMap<String, Object> cashOut(FundTransferRequest fundTransferRequest, VerifyOtpRequest verifyOtpRequest, Request request, String authorization) throws Exception;

    HashMap<String, Object> walletToWallet(WalletToWalletRqst walletToWalletReq, Request request, String token, BigDecimal userId) throws Exception;

    HashMap<String, Object> purchase(PurchaseRequest walletToWalletReq, Request request, BigDecimal userId) throws JsonProcessingException;
    HashMap<String, Object> cardTocardCp(CardToCardCpRequest cardToCardRequest, Request request, String authorization, BigDecimal userId) throws JsonProcessingException;
    HashMap<String, Object> cardBalanceInquiryCp(CardBalanceInquiryCpRequest cardBalanceInquiryRequest, Request request, String token, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> payBillCp(PayBillCpRequest payBillRequest, Request request, String token, BigDecimal userId) throws JsonProcessingException;
}
