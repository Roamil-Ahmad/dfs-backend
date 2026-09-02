package com.wallet.transaction.util;


import com.wallet.transaction.dto.*;
import com.wallet.transaction.dto.aps.CashInAgentRqst;
import com.wallet.transaction.dto.common.CashInAgentRequest;
import com.wallet.transaction.dto.common.Request;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RequestValidator {
    public static <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) ||
                (input instanceof List && ((List<?>) input).isEmpty()) ||
                (input instanceof Map && ((Map<?, ?>) input).isEmpty()) ||
                (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }


    public static void validateInitiateLocalFTRequest(InitiateLocalFTRequest initiateLocalFTRequest, Request request) {
        if (isNullOrEmpty(initiateLocalFTRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(initiateLocalFTRequest.getAccountNo())) {
            throw new ValidationException("Account No Required");
        }
        if (isNullOrEmpty(initiateLocalFTRequest.getAccountType())) {
            throw new ValidationException("Account Type Required");
        }
        if (isNullOrEmpty(initiateLocalFTRequest.getAmount())) {
            throw new ValidationException("Amount Required");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("IMEI Required");
        }
        if (isNullOrEmpty(initiateLocalFTRequest.getNidNo())) {
            throw new ValidationException("NidNo Required");
        }

    }

    public static void validateFundTransferRequest(FundTransferRequest fundTransferRequest, Request request) {

        if (isNullOrEmpty(fundTransferRequest.getAccountNo())) {
            throw new ValidationException("Account No Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getAccountType())) {
            throw new ValidationException("Account Type Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getAmount())) {
            throw new ValidationException("Amount Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(fundTransferRequest.getNidNo())) {
            throw new ValidationException("NidNo Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getTransPurposeId())) {
            throw new ValidationException("Trans Purpose Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getMpin())) {
            throw new ValidationException("Mpin required");
        }

        if (isNullOrEmpty(fundTransferRequest.getAppUserId())) {
            throw new ValidationException("Appuser id is required");
        }


    }

    public static void validateTitleFetchForRequestMoneyt(TitleFetchRequetMoneyRequest requetMoneyRequest, Request request) {
        if (isNullOrEmpty(requetMoneyRequest.getRequesteMobileNo())) {
            throw new ValidationException("Requeste Account No Required");
        }
        if (isNullOrEmpty(requetMoneyRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
    }

    public static void validateRequestMoneyt(RequestMoneyRequest requetMoneyRequest, Request request) {
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("IMEI Required");
        }
        if (isNullOrEmpty(requetMoneyRequest.getMpin())) {
            throw new ValidationException("Mpin Required");
        }
        if (isNullOrEmpty(requetMoneyRequest.getRequesteeAccountNo())) {
            throw new ValidationException("Requestee Account No Required");
        }
        if (isNullOrEmpty(requetMoneyRequest.getNidNo())) {
            throw new ValidationException("NidNo Required");
        }
        if (isNullOrEmpty(requetMoneyRequest.getAmount())) {
            throw new ValidationException("Amount Required");
        }
        if (isNullOrEmpty(requetMoneyRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
    }

    public static void validateGetReceivedMoneyReq(GetReceivedMoneyRequest getReceivedMoneyRequest, Request request) {

        if (isNullOrEmpty(getReceivedMoneyRequest.getNidNo())) {
            throw new ValidationException("NidNo Required");
        }
        if (isNullOrEmpty(getReceivedMoneyRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
    }

    public static void validateupdateRequestStatus(UpdateReceivedMoneyRequest updateReceivedMoneyRequest, Request request) {

        if (isNullOrEmpty(updateReceivedMoneyRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(updateReceivedMoneyRequest.getNidNo())) {
            throw new ValidationException("NidNo Required");
        }
        if (isNullOrEmpty(updateReceivedMoneyRequest.getAmount())) {
            throw new ValidationException("Amount Required");
        }
        if (isNullOrEmpty(updateReceivedMoneyRequest.getAccountNo())) {
            throw new ValidationException("Requeste Mobile No Required");
        }
        if (isNullOrEmpty(updateReceivedMoneyRequest.getStatus())) {
            throw new ValidationException("Status Required");
        }
        if (isNullOrEmpty(updateReceivedMoneyRequest.getRequestMoneyId())) {
            throw new ValidationException("Request Money Required");
        }
    }

    public static void validateFundTransferAgentRequest(FundTransferRequest fundTransferRequest, Request request, boolean verifyMpin) {

        if (isNullOrEmpty(fundTransferRequest.getAccountNo())) {
            throw new ValidationException("Account No Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getAmount())) {
            throw new ValidationException("Amount Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(fundTransferRequest.getNidNo())) {
            throw new ValidationException("NidNo Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getTransPurposeId())) {
            throw new ValidationException("Trans Purpose Required");
        }
        if (verifyMpin && isNullOrEmpty(fundTransferRequest.getMpin())) {
            throw new ValidationException("Mpin required");
        }

    }

    public static void validateIbftFundTransferRequest(FundTransferCardRequest fundTransferRequest, Request request) {
        if (isNullOrEmpty(fundTransferRequest.getAccountNo())) {
            throw new ValidationException("Account No Required");
        }

        if (isNullOrEmpty(fundTransferRequest.getAmount())) {
            throw new ValidationException("Amount Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(fundTransferRequest.getNidNo())) {
            throw new ValidationException("NidNo Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getTransPurposeId())) {
            throw new ValidationException("Trans Purpose Required");
        }
        if (isNullOrEmpty(fundTransferRequest.getMpin())) {
            throw new ValidationException("Mpin required");
        }
    }

    public static void validateCardTitleFetchAppRequest(CardTitleFetchAppRequest cardTitleFetchRqst, Request request) {
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(cardTitleFetchRqst.getType())) {
            throw new ValidationException("Type Required");
        }
        if (isNullOrEmpty(cardTitleFetchRqst.getPan())) {
            throw new ValidationException("Pan Required");
        }
        if (isNullOrEmpty(cardTitleFetchRqst.getAmount())) {
            throw new ValidationException("Mpin Amount Required");
        }
        if (isNullOrEmpty(cardTitleFetchRqst.getAccountNo())) {
            throw new ValidationException("Account No Required");
        }

    }

    public static void validateCardTransferFundRequest(CardTransferFundRequest cardTransferFundRequest, Request request) {
        // Request-level validation
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(cardTransferFundRequest.getMobileNumber())) {
            throw new ValidationException("MOBILE NUMBER IS REQUIRED");
        }
        if (isNullOrEmpty(cardTransferFundRequest.getAccountNo())) {
            throw new ValidationException("ACCOUNT NUMBER IS REQUIRED");
        }

        if (isNullOrEmpty(cardTransferFundRequest.getPan())) {
            throw new ValidationException("CARD PAN IS REQUIRED");
        }

        if (isNullOrEmpty(cardTransferFundRequest.getAmount())) {
            throw new ValidationException("AMOUNT IS REQUIRED");
        }

        if (isNullOrEmpty(cardTransferFundRequest.getTransPurposeId())) {
            throw new ValidationException("TRANSACTION PURPOSE ID IS REQUIRED");
        }

        if (isNullOrEmpty(cardTransferFundRequest.getBankId())) {
            throw new ValidationException("BANK ID IS REQUIRED");
        }

        if (isNullOrEmpty(cardTransferFundRequest.getMpin())) {
            throw new ValidationException("MPIN IS REQUIRED");
        }



        if (isNullOrEmpty(cardTransferFundRequest.getExpiry())) {
            throw new ValidationException("EXPIRY DATE IS REQUIRED");
        }

        if (isNullOrEmpty(cardTransferFundRequest.getCvc())) {
            throw new ValidationException("CVC IS REQUIRED");
        }


    }

    public static void validateCardLinkingRequest(CardLinkingRequest cardLinkingRequest, Request request) {
    }

    public static void validateGetCardLinkingRequest(GetCardLinkingRequest getCardLinkingRequest, Request request) {
    }

    public static void validateSetDefaultRequest(SetDefaultRequest setDefaultRequest, Request request) {
    }

    public static void validateCardBalanceInquiryRequest(CardBalanceInquiryRequest cardBalanceInquiryRequest, Request request) {
    }

    public static void validateCardToCardRequest(CardToCardRequest cardToCardRequest, Request request) {
    }

    public static void validateFetchBillRequest(FetchBillRequest fetchBillRequest, Request request) {
    }

    public static void validatePayBillRequest(PayBillRequest payBillRequest, Request request) {
    }

    public static void validatecashInAgentRequest(CashInAgentRequest cashInAgentRequest, Request request) {
    }

    public static void validatecashOutAgentRequest(CashOutAgentRequest cashOutAgentRequest, Request request) {

    }

    public static void validateCardToCardCpRequest(CardToCardCpRequest cardToCardRequest, Request request) {
    }

    public static void validateCardBalanceInquiryCpRequest(CardBalanceInquiryCpRequest cardBalanceInquiryRequest, Request request) {
    }
    public static void validatePayBillCpRequest(PayBillCpRequest payBillRequest, Request request) {
    }
}
