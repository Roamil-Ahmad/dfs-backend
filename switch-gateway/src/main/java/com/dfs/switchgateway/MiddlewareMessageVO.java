package com.dfs.switchgateway;

import java.io.Serializable;
import java.util.Date;

public class MiddlewareMessageVO implements Serializable {

    private static final long serialVersionUID = 5824473488070381027L;

    private String rrnKey;
    private String retrievalReferenceNumber;
    private String microbankTransactionCode;
    private Date transmissionTime;
    private Date requestTime;
    private String transDateTime;

    private String responseCode;
    private String stan;
    private String PAN;
    private String transactionAmount;
    private String settlemetAmount;

    private String transactionFee;
    private String settlemetFee;
    private String settlemetProcessingFee;

    private String accountTitle;
    private String accountBalance;
    private String acountType;
    private String amountType;
    private String pointOfEntry;
    private String currencyCode;
    private String balanceType;

    private String accountNo1;
    private String accountNo2;

    private String accquirerInstitutionIdentificationCode;

    private String cardAcceptorIdentificationCode;

    private String cnicNo;
    private String consumerNo;
    private String consumerName;
    private String compnayCode;
    private Date billDueDate;
    private String billAggregator;
    private String billStatus;
    private String billingMonth;
    private String amountDueDate;
    private String amountAfterDueDate;
    private String billCategoryId;
    private String ubpSTAN;

    private String terminalId;


    private Long parentTransactionId;
    private String reversalSTAN;
    private String reversalRequestTime;

    //Request by Kashif
    private Date timeLocalTransaction;
    private Date dateLocalTransaction;

    private String bankIMD;
    private String companyCode;
    private String consumerNumber;
    private String purposeOfPayment;
    private String senderName;
    private String senderIban;
    private String accountBankName;
    private String benificieryIban;
    private String accountBranchName;
    private String senderBankImd;
    private String beneficiaryBankImd;
    private String crdr;
    private String agentId;
    private String cardAcceptorNameAndLocation;
    private String transType;

    private String authIdResp;

    private String networkIdentifer;

    private String merchantType;

    private String rrn;

    private String timeLclTransaction;
    private String dateLclTransaction;

    private String senderId;

    private String branchName;
    private String beneficaryId;
    private String senderCountryCode;
    private String originatorDetails;

    public String getOriginatorDetails() {
        return originatorDetails;
    }

    public void setOriginatorDetails(String originatorDetails) {
        this.originatorDetails = originatorDetails;
    }

    public String getSenderCountryCode() {
        return senderCountryCode;
    }

    public void setSenderCountryCode(String senderCountryCode) {
        this.senderCountryCode = senderCountryCode;
    }

    public String getBeneficaryId() {
        return beneficaryId;
    }

    public void setBeneficaryId(String beneficaryId) {
        this.beneficaryId = beneficaryId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimeLclTransaction() {
        return timeLclTransaction;
    }

    public void setTimeLclTransaction(String timeLclTransaction) {
        this.timeLclTransaction = timeLclTransaction;
    }

    public String getDateLclTransaction() {
        return dateLclTransaction;
    }

    public void setDateLclTransaction(String dateLclTransaction) {
        this.dateLclTransaction = dateLclTransaction;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getNetworkIdentifer() {
        return networkIdentifer;
    }

    public void setNetworkIdentifer(String networkIdentifer) {
        this.networkIdentifer = networkIdentifer;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    private String recieverId;

    public String getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(String transDateTime) {
        this.transDateTime = transDateTime;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getAuthIdResp() {
        return authIdResp;
    }

    public void setAuthIdResp(String authIdResp) {
        this.authIdResp = authIdResp;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getAccquirerInstitutionIdentificationCode() {
        return accquirerInstitutionIdentificationCode;
    }

    public void setAccquirerInstitutionIdentificationCode(String accquirerInstitutionIdentificationCode) {
        this.accquirerInstitutionIdentificationCode = accquirerInstitutionIdentificationCode;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getPointOfEntry() {
        return pointOfEntry;
    }

    public void setPointOfEntry(String pointOfEntry) {
        this.pointOfEntry = pointOfEntry;
    }

    public String getSenderBankImd() {
        return senderBankImd;
    }

    public void setSenderBankImd(String senderBankImd) {
        this.senderBankImd = senderBankImd;
    }

    public String getBeneficiaryBankImd() {
        return beneficiaryBankImd;
    }

    public void setBeneficiaryBankImd(String beneficiaryBankImd) {
        this.beneficiaryBankImd = beneficiaryBankImd;
    }

    public String getCrdr() {
        return crdr;
    }

    public void setCrdr(String crdr) {
        this.crdr = crdr;
    }

    public String getAccountBranchName() {
        return accountBranchName;
    }

    public void setAccountBranchName(String accountBranchName) {
        this.accountBranchName = accountBranchName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public void setSenderIban(String senderIban) {
        this.senderIban = senderIban;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getBenificieryIban() {
        return benificieryIban;
    }

    public void setBenificieryIban(String benificieryIban) {
        this.benificieryIban = benificieryIban;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
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

    public Date getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(Date timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public Date getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(Date dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getSettlemetAmount() {
        return settlemetAmount;
    }

    public void setSettlemetAmount(String settlemetAmount) {
        this.settlemetAmount = settlemetAmount;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(String transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getSettlemetFee() {
        return settlemetFee;
    }

    public void setSettlemetFee(String settlemetFee) {
        this.settlemetFee = settlemetFee;
    }

    public String getAccountNo1() {
        return accountNo1;
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

    public String getCnicNo() {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getCompnayCode() {
        return compnayCode;
    }

    public void setCompnayCode(String compnayCode) {
        this.compnayCode = compnayCode;
    }

    public String getSettlemetProcessingFee() {
        return settlemetProcessingFee;
    }

    public void setSettlemetProcessingFee(String settlemetProcessingFee) {
        this.settlemetProcessingFee = settlemetProcessingFee;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public Date getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(Date billDueDate) {
        this.billDueDate = billDueDate;
    }

    public String getBillAggregator() {
        return billAggregator;
    }

    public void setBillAggregator(String billAggregator) {
        this.billAggregator = billAggregator;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getReversalSTAN() {
        return reversalSTAN;
    }

    public void setReversalSTAN(String reversalSTAN) {
        this.reversalSTAN = reversalSTAN;
    }

    public String getReversalRequestTime() {
        return reversalRequestTime;
    }

    public void setReversalRequestTime(String reversalRequestTime) {
        this.reversalRequestTime = reversalRequestTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public Date getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(Date transmissionTime) {
        this.transmissionTime = transmissionTime;
    }


    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getAmountDueDate() {
        return amountDueDate;
    }

    public void setAmountDueDate(String amountDueDate) {
        this.amountDueDate = amountDueDate;
    }

    public String getAmountAfterDueDate() {
        return amountAfterDueDate;
    }

    public void setAmountAfterDueDate(String amountAfterDueDate) {
        this.amountAfterDueDate = amountAfterDueDate;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getBillCategoryId() {
        return billCategoryId;
    }

    public void setBillCategoryId(String billCategoryId) {
        this.billCategoryId = billCategoryId;
    }

    public Long getParentTransactionId() {
        return parentTransactionId;
    }

    public void setParentTransactionId(Long parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }

    public String getAcountType() {
        return acountType;
    }

    public void setAcountType(String acountType) {
        this.acountType = acountType;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRrnKey() {
        return rrnKey;
    }

    public void setRrnKey(String rrnKey) {
        this.rrnKey = rrnKey;
    }

    public String getUbpSTAN() {
        return ubpSTAN;
    }

    public void setUbpSTAN(String ubpSTAN) {
        this.ubpSTAN = ubpSTAN;
    }


    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public String getCardAcceptorIdentificationCode() {
        return cardAcceptorIdentificationCode;
    }

    public void setCardAcceptorIdentificationCode(String cardAcceptorIdentificationCode) {
        this.cardAcceptorIdentificationCode = cardAcceptorIdentificationCode;
    }




}
