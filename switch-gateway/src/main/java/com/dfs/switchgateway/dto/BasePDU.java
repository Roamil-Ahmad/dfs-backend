package com.dfs.switchgateway.dto;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto

Class Name: BasedPDU

Date and Time:5/26/2023 10:54 AM

Version:1.0
*/

import com.dfs.switchgateway.dto.TSDtos.responseDto.AdviceResponseDto;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.BitSet;

public class BasePDU implements Serializable {
    private Logger logger = LoggerFactory.getLogger(BasePDU.class.getSimpleName());

    private static final long serialVersionUID = 7105234427190145032L;

    private String stan;
    private String mti;
    private String pan;
    private String processingCode;
    private String transactionAmount;
    private String settlementAmount;
    private String transactionDate;
    private String transactionTime;
    private String settlementConversionRate;
    private String transactionLocalTime;
    private String transactionLocalDate;
    private String settlementDate;
    private String conversionDate;
    private String networkIdentifier;
    private String transactionFee;
    private String settlementFee;
    private String acquirerIdentification;
    private String forwardingInsIdenCode;
    private String rrn;
    private String terminalId;
    private String transactionCurrencyCode;
    private String settlementCurrencyCode;
    private String pin;
    private String privateData;
    private String settlementProcessingFee;
    private String cardAcceptorName;
    private String accountNo1;
    private String accountNo2;
    private String authIdResponse;
    private String responseCode;
    private String originalDataElement;
    private String additionalResponseData;
    private String additionalAmount;
    private String networkManagementCode;
    private String messageSecurityCode;
    private String merchantType;
    private String companyCode;
    private String customerMobile;
    private String billAmount;
    private String billStatus;
    private String customerMobileNumber;
    private String dueDate;
    private String consumerNumber;
    private String productId;
    private String dateExpiration;
    private String pointOfServiceEntryMode;
    private String institutionIdentificationCode;
    private String track2Data;
    private String cardAcceptorTerminalIdentification;
    private String cardAcceptorIdentificationCode;
    private String cardAcceptorNameAndLocation;
    private String track1Data;
    private String additionalDataprivate;
    private String pinData;
    private String privateEmvData;
    private String recordData;
    private String purposeOfPayment;
    private byte[] rawPdu;
    private BaseHeader header;

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getRecordData() {
        return recordData;
    }

    public void setRecordData(String recordData) {
        this.recordData = recordData;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getPointOfServiceEntryMode() {
        return pointOfServiceEntryMode;
    }

    public void setPointOfServiceEntryMode(String pointOfServiceEntryMode) {
        this.pointOfServiceEntryMode = pointOfServiceEntryMode;
    }

    public String getInstitutionIdentificationCode() {
        return institutionIdentificationCode;
    }

    public void setInstitutionIdentificationCode(String institutionIdentificationCode) {
        this.institutionIdentificationCode = institutionIdentificationCode;
    }

    public String getTrack2Data() {
        return track2Data;
    }

    public void setTrack2Data(String track2Data) {
        this.track2Data = track2Data;
    }

    public String getCardAcceptorTerminalIdentification() {
        return cardAcceptorTerminalIdentification;
    }

    public void setCardAcceptorTerminalIdentification(String cardAcceptorTerminalIdentification) {
        this.cardAcceptorTerminalIdentification = cardAcceptorTerminalIdentification;
    }

    public String getCardAcceptorIdentificationCode() {
        return cardAcceptorIdentificationCode;
    }

    public void setCardAcceptorIdentificationCode(String cardAcceptorIdentificationCode) {
        this.cardAcceptorIdentificationCode = cardAcceptorIdentificationCode;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getTrack1Data() {
        return track1Data;
    }

    public void setTrack1Data(String track1Data) {
        this.track1Data = track1Data;
    }

    public String getAdditionalDataprivate() {
        return additionalDataprivate;
    }

    public void setAdditionalDataprivate(String additionalDataprivate) {
        this.additionalDataprivate = additionalDataprivate;
    }

    public String getPinData() {
        return pinData;
    }

    public void setPinData(String pinData) {
        this.pinData = pinData;
    }

    public String getPrivateEmvData() {
        return privateEmvData;
    }

    public void setPrivateEmvData(String privateEmvData) {
        this.privateEmvData = privateEmvData;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }



    public byte[] getRawPdu() {
        return rawPdu;
    }

    public void setRawPdu(byte[] rawPdu) {
        this.rawPdu = rawPdu;
    }

    public BaseHeader getHeader() {
        return header;
    }

    public void setHeader(BaseHeader header) {
        this.header = header;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }


    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getSettlementConversionRate() {
        return settlementConversionRate;
    }

    public void setSettlementConversionRate(String settlementConversionRate) {
        this.settlementConversionRate = settlementConversionRate;
    }

    public String getTransactionLocalTime() {
        return transactionLocalTime;
    }

    public void setTransactionLocalTime(String transactionLocalTime) {
        this.transactionLocalTime = transactionLocalTime;
    }

    public String getTransactionLocalDate() {
        return transactionLocalDate;
    }

    public void setTransactionLocalDate(String transactionLocalDate) {
        this.transactionLocalDate = transactionLocalDate;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(String conversionDate) {
        this.conversionDate = conversionDate;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier(String networkIdentifier) {
        this.networkIdentifier = networkIdentifier;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(String transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getSettlementFee() {
        return settlementFee;
    }

    public void setSettlementFee(String settlementFee) {
        this.settlementFee = settlementFee;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public String getSettlementCurrencyCode() {
        return settlementCurrencyCode;
    }

    public void setSettlementCurrencyCode(String settlementCurrencyCode) {
        this.settlementCurrencyCode = settlementCurrencyCode;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPrivateData() {
        return privateData;
    }

    public void setPrivateData(String privateData) {
        this.privateData = privateData;
    }

    public String getSettlementProcessingFee() {
        return settlementProcessingFee;
    }

    public void setSettlementProcessingFee(String settlementProcessingFee) {
        this.settlementProcessingFee = settlementProcessingFee;
    }

    public String getCardAcceptorName() {
        return cardAcceptorName;
    }

    public void setCardAcceptorName(String cardAcceptorName) {
        this.cardAcceptorName = cardAcceptorName;
    }

    public String getAccountNo1() {
        return accountNo1;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public void setAccountNo1(String accountNo1) {
        this.accountNo1 = accountNo1;
    }

    public String getAccountNo2() {
        return accountNo2;
    }

    public void setAccountNo2(String accountNo2) {
        this.accountNo2 = accountNo2;
    }

    public String getAuthIdResponse() {
        return authIdResponse;
    }

    public void setAuthIdResponse(String authIdResponse) {
        this.authIdResponse = authIdResponse;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAdditionalResponseData() {
        return additionalResponseData;
    }

    public void setAdditionalResponseData(String additionalResponseData) {
        this.additionalResponseData = additionalResponseData;
    }

    public String getForwardingInsIdenCode() {
        return forwardingInsIdenCode;
    }

    public void setForwardingInsIdenCode(String forwardingInsIdenCode) {
        this.forwardingInsIdenCode = forwardingInsIdenCode;
    }

    public String getAcquirerIdentification() {
        return acquirerIdentification;
    }

    public void setAcquirerIdentification(String acquirerIdentification) {
        this.acquirerIdentification = acquirerIdentification;
    }

    public String getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public String getOriginalDataElement() {
        return originalDataElement;
    }

    public void setOriginalDataElement(String originalDataElement) {
        this.originalDataElement = originalDataElement;
    }

    public String getNetworkManagementCode() {
        return networkManagementCode;
    }

    public void setNetworkManagementCode(String networkManagementCode) {
        this.networkManagementCode = networkManagementCode;
    }

    public String getMessageSecurityCode() {
        return messageSecurityCode;
    }

    public void setMessageSecurityCode(String messageSecurityCode) {
        this.messageSecurityCode = messageSecurityCode;
    }

    protected void logISOMessage(ISOMsg msg) {
        StringBuilder log = new StringBuilder();
        log.append("\n----ISO MESSAGE-----");
        log.append("\n");
        try {
            BitSet bset = (BitSet) msg.getValue(-1);
            log.append("BitMap : " + org.jpos.iso.ISOUtil.bitSet2String(bset));
            log.append("\n");
            log.append(" MTI : " + msg.getMTI());
            log.append("\n");
            for (int i = 0; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    if (i == 54 || i == 102 || i == 103) {
                        log.append("    DE-" + i + " : " + msg.getString(i));

                    } else {
                        log.append("    DE-" + i + " : " + msg.getString(i));
                    }
                    log.append("\n");
                }
            }
        } catch (Exception e) {
            logger.info("Exception", e);
        } finally {
            log.append("----ISO MESSAGE-----");
            log.append("\n");
        }
        logger.info(log.toString());
    }
}
