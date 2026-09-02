package com.dfs.switchgateway.validations;

import com.dfs.switchgateway.dto.BasePDU;
import com.dfs.switchgateway.dto.IbftAdviceRqst;
import com.dfs.switchgateway.dto.TSDtos.BillRqst;
import com.dfs.switchgateway.dto.TSDtos.IBFTRequest;
import com.dfs.switchgateway.dto.TSDtos.OTIBFTTitleFetchRequest;
import com.dfs.switchgateway.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class CSTransactionRequestValidator {

    private static Logger logger = LoggerFactory.getLogger(CSTransactionRequestValidator.class.getSimpleName());

    public void validateIBFTTitleFetchRequest( OTIBFTTitleFetchRequest basePDU ) {
        if (ObjectUtils.isEmpty(basePDU.getPan()) || basePDU.getPan().length() != 16) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid PAN : Length Should be 16 : " + basePDU.getPan());
        }
        if (ObjectUtils.isEmpty(basePDU.getTransactionAmount()) || basePDU.getTransactionAmount().length() > 12) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Transacation Amount : Length Should be less than or equal  12 : " + basePDU.getTransactionAmount());
        }
        if (ObjectUtils.isEmpty(basePDU.getTransactionDateTime()) || basePDU.getTransactionDateTime().length() != 14) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Transaction Date Time : Length Should be 14 : " + basePDU.getTransactionDateTime());
        } else {
            try {
                Date date = new SimpleDateFormat("ddMMyyyyHHmmss").parse(basePDU.getTransactionDateTime());
            } catch (ParseException e) {
                throw new CommunicationServiceValidationException("Validation Failed TransactionDate Check Date Format (ddMMyyyyHHmmss)" + " [FAILED] TransactionDate: " + basePDU.getTransactionDateTime());
            }
        }

        if (ObjectUtils.isEmpty(basePDU.getMerchantType()) || basePDU.getMerchantType().length() != 4) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Merchant Type : Length Should be 4 : " + basePDU.getMerchantType());
        }
        if (ObjectUtils.isEmpty(basePDU.getPointOfEntry()) || basePDU.getPointOfEntry().length() != 3) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Point-of-Service Entry :  Length Should be 3 : " + basePDU.getPointOfEntry());
        }
        if (ObjectUtils.isEmpty(basePDU.getNetworkIdentifier()) || basePDU.getNetworkIdentifier().length() != 3) {
            logger.info("[FAILED] Invalid Network Institution Id :  Length Should be 3 " + basePDU.getNetworkIdentifier());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Network Institution Id :  Length Should be 3 : " + basePDU.getNetworkIdentifier());
        }


        if (ObjectUtils.isEmpty(basePDU.getCardAcceptorTerminalId()) || basePDU.getCardAcceptorTerminalId().length() != 8) {
            logger.info("[FAILED] Invalid Card Acceptor Terminal.. : Length Should be 8 " + basePDU.getCardAcceptorTerminalId());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Card Acceptor Terminal.. : Length Should be 8 : " + basePDU.getCardAcceptorTerminalId());
        }
        if (ObjectUtils.isEmpty(basePDU.getCardAcceptorIdentificationCode()) || basePDU.getCardAcceptorIdentificationCode().length() != 15) {
            logger.info("[FAILED] Invalid Card Acceptor Identification Code : Length Should be 15" + basePDU.getCardAcceptorIdentificationCode());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Card Acceptor Identification Code : Length Should be 15 : " + basePDU.getCardAcceptorIdentificationCode());
        }

        if (ObjectUtils.isEmpty(basePDU.getCardAcceptorNameAndLocation()) || basePDU.getCardAcceptorNameAndLocation().length() != 40) {
            logger.info("[FAILED] From Card Acceptor Name and Location : Length Should be 40" + basePDU.getCardAcceptorNameAndLocation());
            throw new CommunicationServiceValidationException("[FAILED] From Card Acceptor Name and Location : Length Should be 40 : " + basePDU.getCardAcceptorNameAndLocation());
        }
        if (ObjectUtils.isEmpty(basePDU.getCurrencyCode()) || basePDU.getCurrencyCode().length() != 3) {
            logger.info("[FAILED] Invalid Currency Code :  Length Should be 3 " + basePDU.getCurrencyCode());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Currency Code :  Length Should be 3 : " + basePDU.getCurrencyCode());
        }
        if (ObjectUtils.isEmpty(basePDU.getAccountNo2()) || basePDU.getAccountNo2().length() > 24) {
            logger.info("[FAILED] Invalid Account Number 2  :  Length Should be <=24 " + basePDU.getAccountNo2());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Account Number 2  :  Length Should be <=24 : " + basePDU.getAccountNo2());
        }
        if (ObjectUtils.isEmpty(basePDU.getAccountNo1()) || basePDU.getAccountNo1().length() > 24) {
            logger.info("[FAILED] Invalid Account Number 1  :  Length Should be <=24 " + basePDU.getAccountNo1());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Account Number 1  :  Length Should be <=24 : " + basePDU.getAccountNo1());
        }

        if (ObjectUtils.isEmpty(basePDU.getPurposeOfPayment()) || basePDU.getPurposeOfPayment().length() != 44) {
            logger.info("[FAILED] Invalid Purpose Of Payment :  Length Should be 44" + basePDU.getPurposeOfPayment());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Purpose Of Payment :  Length Should be 44 : " + basePDU.getPurposeOfPayment());
        }
        if (ObjectUtils.isEmpty(basePDU.getToBankImd()) || basePDU.getToBankImd().length() != 6) {
            logger.info("[FAILED] Invalid To Bank Imd :  Length Should be 6" + basePDU.getToBankImd());
            throw new CommunicationServiceValidationException("[FAILED] Invalid To Bank Imd :  Length Should be 6 : " + basePDU.getToBankImd());
        }
        if (ObjectUtils.isEmpty(basePDU.getStan()) || basePDU.getStan().length() != 6) {
            logger.info("[FAILED] Invalid Stan :  Length Should be 6" + basePDU.getStan());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Stan :  Length Should be 6 : " + basePDU.getStan());

        }
        if (ObjectUtils.isEmpty(basePDU.getRrn()) || basePDU.getRrn().length() != 12) {
            logger.info("[FAILED] Invalid RRN :  Length Should be 12" + basePDU.getRrn());
            throw new CommunicationServiceValidationException("[FAILED] Invalid RRN :  Length Should be 12 : " + basePDU.getRrn());
        }

    }


    /**
     * Shape check for a Utility Bill Inquiry or Bill Payment.
     *
     * The same widths the IBFT messages use apply here, since they are properties of the data
     * elements rather than of the transaction. An amount is only required on a payment: an inquiry
     * is asking what is due, so it has nothing to send.
     */
    public void validateBillRequest( BillRqst request, boolean isPayment ) {
        if (ObjectUtils.isEmpty(request.getPan()) || request.getPan().length() != 16) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid PAN : Length Should be 16 : " + request.getPan());
        }
        if (ObjectUtils.isEmpty(request.getTransactionDateTime()) || request.getTransactionDateTime().length() != 14) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Transaction Date Time : Length Should be 14 : " + request.getTransactionDateTime());
        } else {
            try {
                new SimpleDateFormat("ddMMyyyyHHmmss").parse(request.getTransactionDateTime());
            } catch (ParseException e) {
                throw new CommunicationServiceValidationException("Validation Failed TransactionDate Check Date Format (ddMMyyyyHHmmss)" + " [FAILED] TransactionDate: " + request.getTransactionDateTime());
            }
        }
        if (ObjectUtils.isEmpty(request.getMerchantType()) || request.getMerchantType().length() != 4) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Merchant Type : Length Should be 4 : " + request.getMerchantType());
        }
        if (ObjectUtils.isEmpty(request.getPointOfEntry()) || request.getPointOfEntry().length() != 3) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Point-of-Service Entry :  Length Should be 3 : " + request.getPointOfEntry());
        }
        if (ObjectUtils.isEmpty(request.getNetworkIdentifier()) || request.getNetworkIdentifier().length() != 3) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Network Institution Id :  Length Should be 3 : " + request.getNetworkIdentifier());
        }
        if (ObjectUtils.isEmpty(request.getCardAcceptorTerminalId()) || request.getCardAcceptorTerminalId().length() != 8) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Card Acceptor Terminal.. : Length Should be 8 : " + request.getCardAcceptorTerminalId());
        }
        if (ObjectUtils.isEmpty(request.getCardAcceptorIdentificationCode()) || request.getCardAcceptorIdentificationCode().length() != 15) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Card Acceptor Identification Code : Length Should be 15 : " + request.getCardAcceptorIdentificationCode());
        }
        if (ObjectUtils.isEmpty(request.getCardAcceptorNameAndLocation()) || request.getCardAcceptorNameAndLocation().length() != 40) {
            throw new CommunicationServiceValidationException("[FAILED] From Card Acceptor Name and Location : Length Should be 40 : " + request.getCardAcceptorNameAndLocation());
        }
        if (ObjectUtils.isEmpty(request.getCurrencyCode()) || request.getCurrencyCode().length() != 3) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Currency Code :  Length Should be 3 : " + request.getCurrencyCode());
        }
        if (ObjectUtils.isEmpty(request.getAccountNo1()) || request.getAccountNo1().length() > 24) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Account Number 1  :  Length Should be <=24 : " + request.getAccountNo1());
        }
        if (ObjectUtils.isEmpty(request.getUtilityCompanyCode()) || request.getUtilityCompanyCode().length() > 20) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Utility Company Code :  Length Should be <=20 : " + request.getUtilityCompanyCode());
        }
        if (ObjectUtils.isEmpty(request.getConsumerNo()) || request.getConsumerNo().length() > 20) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Consumer No :  Length Should be <=20 : " + request.getConsumerNo());
        }
        if (request.getUtilityCompanyName() != null && request.getUtilityCompanyName().length() > 30) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Utility Company Name :  Length Should be <=30 : " + request.getUtilityCompanyName());
        }
        if (isPayment && (ObjectUtils.isEmpty(request.getTransactionAmount()) || request.getTransactionAmount().length() > 12)) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Transacation Amount : Length Should be less than or equal  12 : " + request.getTransactionAmount());
        }
        if (ObjectUtils.isEmpty(request.getStan()) || request.getStan().length() != 6) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid Stan :  Length Should be 6 : " + request.getStan());
        }
        if (ObjectUtils.isEmpty(request.getRrn()) || request.getRrn().length() != 12) {
            throw new CommunicationServiceValidationException("[FAILED] Invalid RRN :  Length Should be 12 : " + request.getRrn());
        }
    }

    public void validateIBFTAdviceRequest( IbftAdviceRqst ibftAdviceRqst ) {
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getPan()) || ibftAdviceRqst.getPan().length() != 16) {
            logger.info("[FAILED] Invalid PAN : Length Should be 16 " + ibftAdviceRqst.getPan());
            throw new CommunicationServiceValidationException("[FAILED] Invalid PAN : Length Should be 16 : " + ibftAdviceRqst.getPan());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getTransactionAmount()) || ibftAdviceRqst.getTransactionAmount().length() > 12) {
            logger.info("[FAILED] Invalid Transacation Amount : Length Should be less than or equal  12 " + ibftAdviceRqst.getTransactionAmount());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Transacation Amount : Length Should be less than or equal  12 : " + ibftAdviceRqst.getTransactionAmount());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getTransactionDateTime()) || ibftAdviceRqst.getTransactionDateTime().length() != 14) {
            logger.info("[FAILED] Invalid Transaction Date Time : Length Should be 14 " + ibftAdviceRqst.getTransactionDateTime());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Transaction Date Time : Length Should be 14 : " + ibftAdviceRqst.getTransactionDateTime());
        } else {
            try {
                Date date = new SimpleDateFormat("ddMMyyyyHHmmss").parse(ibftAdviceRqst.getTransactionDateTime());
            } catch (ParseException e) {
                logger.info("[FAILED] From Transaction Date Validation: " + ibftAdviceRqst.getTransactionDateTime());
                throw new CommunicationServiceValidationException("Validation Failed TransactionDate Check Date Format (ddMMyyyyHHmmss)" + " [FAILED] TransactionDate: " + ibftAdviceRqst.getTransactionDateTime());
            }
        }

        if (ObjectUtils.isEmpty(ibftAdviceRqst.getMerchantType()) || ibftAdviceRqst.getMerchantType().length() != 4) {
            logger.info("[FAILED] Invalid Merchant Type : Length Should be 4 " + ibftAdviceRqst.getMerchantType());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Merchant Type : Length Should be 4 : " + ibftAdviceRqst.getMerchantType());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getPointOfEntry()) || ibftAdviceRqst.getPointOfEntry().length() != 3) {
            logger.info("[FAILED] Invalid Point-of-Service Entry :  Length Should be 3 " + ibftAdviceRqst.getPointOfEntry());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Point-of-Service Entry :  Length Should be 3 : " + ibftAdviceRqst.getPointOfEntry());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getNetworkIdentifier()) || ibftAdviceRqst.getNetworkIdentifier().length() != 3) {
            logger.info("[FAILED] Invalid Network Institution Id :  Length Should be 3 " + ibftAdviceRqst.getNetworkIdentifier());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Network Institution Id :  Length Should be 3 : " + ibftAdviceRqst.getNetworkIdentifier());
        }


        if (ObjectUtils.isEmpty(ibftAdviceRqst.getCardAcceptorTerminalId()) || ibftAdviceRqst.getCardAcceptorTerminalId().length() != 8) {
            logger.info("[FAILED] Invalid Card Acceptor Terminal.. : Length Should be 8 " + ibftAdviceRqst.getCardAcceptorTerminalId());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Card Acceptor Terminal.. : Length Should be 8 : " + ibftAdviceRqst.getCardAcceptorTerminalId());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getCardAcceptorIdentificationCode()) || ibftAdviceRqst.getCardAcceptorIdentificationCode().length() != 15) {
            logger.info("[FAILED] Invalid Card Acceptor Identification Code : Length Should be 15" + ibftAdviceRqst.getCardAcceptorIdentificationCode());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Card Acceptor Identification Code : Length Should be 15 : " + ibftAdviceRqst.getCardAcceptorIdentificationCode());
        }

        if (ObjectUtils.isEmpty(ibftAdviceRqst.getCardAcceptorNameAndLocation()) || ibftAdviceRqst.getCardAcceptorNameAndLocation().length() != 40) {
            logger.info("[FAILED] From Card Acceptor Name and Location : Length Should be 40" + ibftAdviceRqst.getCardAcceptorNameAndLocation());
            throw new CommunicationServiceValidationException("[FAILED] From Card Acceptor Name and Location : Length Should be 40 : " + ibftAdviceRqst.getCardAcceptorNameAndLocation());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getCurrencyCode()) || ibftAdviceRqst.getCurrencyCode().length() != 3) {
            logger.info("[FAILED] Invalid Currency Code :  Length Should be 3 " + ibftAdviceRqst.getCurrencyCode());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Currency Code :  Length Should be 3 : " + ibftAdviceRqst.getCurrencyCode());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getAccountNo2()) || ibftAdviceRqst.getAccountNo2().length() > 24) {
            logger.info("[FAILED] Invalid Account Number 2  :  Length Should be <=24 " + ibftAdviceRqst.getAccountNo2());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Account Number 2  :  Length Should be <=24 : " + ibftAdviceRqst.getAccountNo2());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getAccountNo1()) || ibftAdviceRqst.getAccountNo1().length() > 24) {
            logger.info("[FAILED] Invalid Account Number 1  :  Length Should be <=24 " + ibftAdviceRqst.getAccountNo1());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Account Number 1  :  Length Should be <=24 : " + ibftAdviceRqst.getAccountNo1());
        }

        if (ObjectUtils.isEmpty(ibftAdviceRqst.getPurposeOfPayment()) || ibftAdviceRqst.getPurposeOfPayment().length() != 44) {
            logger.info("[FAILED] Invalid Purpose Of Payment :  Length Should be 44" + ibftAdviceRqst.getPurposeOfPayment());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Purpose Of Payment :  Length Should be 44 : " + ibftAdviceRqst.getPurposeOfPayment());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getToBankImd()) || ibftAdviceRqst.getToBankImd().length() != 6) {
            logger.info("[FAILED] Invalid Reciever Bank Imd :  Length Should be 6" + ibftAdviceRqst.getToBankImd());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Reciever Bank Imd :  Length Should be 6 : " + ibftAdviceRqst.getToBankImd());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getStan()) || ibftAdviceRqst.getStan().length() != 6) {
            logger.info("[FAILED] Invalid Stan :  Length Should be 6" + ibftAdviceRqst.getStan());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Stan :  Length Should be 6 : " + ibftAdviceRqst.getStan());

        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getRrn()) || ibftAdviceRqst.getRrn().length() != 12) {
            logger.info("[FAILED] Invalid RRN :  Length Should be 12" + ibftAdviceRqst.getRrn());
            throw new CommunicationServiceValidationException("[FAILED] Invalid RRN :  Length Should be 12 : " + ibftAdviceRqst.getRrn());
        }

        if (ObjectUtils.isEmpty(ibftAdviceRqst.getAccountTitle()) || ibftAdviceRqst.getAccountTitle().length() > 30) {
            logger.info("[FAILED] Invalid Account Title :  Length Should be <=30" + ibftAdviceRqst.getAccountTitle());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Account Title :  Length Should be <=30 : " + ibftAdviceRqst.getAccountTitle());
        }
        if (ObjectUtils.isEmpty(ibftAdviceRqst.getSenderName()) || ibftAdviceRqst.getSenderName().length() > 30) {
            logger.info("[FAILED] Invalid Sender Name :  Length Should be <=30" + ibftAdviceRqst.getSenderName());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Sender Name :  Length Should be <=30 : " + ibftAdviceRqst.getSenderName());
        }

        if (ObjectUtils.isEmpty(ibftAdviceRqst.getAccountBankName()) || ibftAdviceRqst.getAccountBankName().length() > 20) {
            logger.info("[FAILED] Invalid Account Bank Name :  Length Should be <=20" + ibftAdviceRqst.getAccountBankName());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Account Bank Name :  Length Should be <=20 : " + ibftAdviceRqst.getAccountBankName());
        }
        // Branch name and sender id are optional sub-fields of the DE-120 record layout
        // (1LINK 9.62.1.1 #7 and #13): they are fixed width and may be sent blank. Only the width
        // is enforced, so a caller that genuinely has neither is not forced to invent them.
        if (ibftAdviceRqst.getAccountBranchName() != null && ibftAdviceRqst.getAccountBranchName().length() > 25) {
            logger.info("[FAILED] Invalid Account Branch Name :  Length Should be <=25" + ibftAdviceRqst.getAccountBranchName());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Account Branch Name :  Length Should be <=25 : " + ibftAdviceRqst.getAccountBranchName());
        }
        if (ibftAdviceRqst.getSenderId() != null && ibftAdviceRqst.getSenderId().length() > 30) {
            logger.info("[FAILED] Invalid Sender Id :  Length Should be <=30" + ibftAdviceRqst.getSenderId());
            throw new CommunicationServiceValidationException("[FAILED] Invalid Sender Id :  Length Should be <=30 : " + ibftAdviceRqst.getSenderId());
        }
    }
}



