package com.wallet.transaction.dto;

public class WalletToWalletRequest {
    private String clientSecret;
    private String channelCode;
    private Long appUserId;
    private String relationshipId;
    private String transmissionDate;
    private String transmissionTime;
    private String stan;
    private String rrn;
    private String dateLocalTran;
    private String timeLocalTran;
    private String acqInstCode;
    private String merchantType;
    private String posEntryMode;
    private String cardAcceptorNameLocation;
    private String cardAcceptorTerminalId;
    private String fromAccountNumber;
    private String fromAccountType;
    private String fromAccountCurrency;
    private String toAccountNumber;
    private String toAccountType;
    private String toAccountCurrency;
    private String transactionAmount;
    private String transactionCurrency;
    private String transactionFee;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;

    // Getters and setters
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

    public String getChannelCode() { return channelCode; }
    public void setChannelCode(String channelCode) { this.channelCode = channelCode; }

    public Long getAppUserId() { return appUserId; }
    public void setAppUserId(Long appUserId) { this.appUserId = appUserId; }

    public String getRelationshipId() { return relationshipId; }
    public void setRelationshipId(String relationshipId) { this.relationshipId = relationshipId; }

    public String getTransmissionDate() { return transmissionDate; }
    public void setTransmissionDate(String transmissionDate) { this.transmissionDate = transmissionDate; }

    public String getTransmissionTime() { return transmissionTime; }
    public void setTransmissionTime(String transmissionTime) { this.transmissionTime = transmissionTime; }

    public String getStan() { return stan; }
    public void setStan(String stan) { this.stan = stan; }

    public String getRrn() { return rrn; }
    public void setRrn(String rrn) { this.rrn = rrn; }

    public String getDateLocalTran() { return dateLocalTran; }
    public void setDateLocalTran(String dateLocalTran) { this.dateLocalTran = dateLocalTran; }

    public String getTimeLocalTran() { return timeLocalTran; }
    public void setTimeLocalTran(String timeLocalTran) { this.timeLocalTran = timeLocalTran; }

    public String getAcqInstCode() { return acqInstCode; }
    public void setAcqInstCode(String acqInstCode) { this.acqInstCode = acqInstCode; }

    public String getMerchantType() { return merchantType; }
    public void setMerchantType(String merchantType) { this.merchantType = merchantType; }

    public String getPosEntryMode() { return posEntryMode; }
    public void setPosEntryMode(String posEntryMode) { this.posEntryMode = posEntryMode; }

    public String getCardAcceptorNameLocation() { return cardAcceptorNameLocation; }
    public void setCardAcceptorNameLocation(String cardAcceptorNameLocation) { this.cardAcceptorNameLocation = cardAcceptorNameLocation; }

    public String getCardAcceptorTerminalId() { return cardAcceptorTerminalId; }
    public void setCardAcceptorTerminalId(String cardAcceptorTerminalId) { this.cardAcceptorTerminalId = cardAcceptorTerminalId; }

    public String getFromAccountNumber() { return fromAccountNumber; }
    public void setFromAccountNumber(String fromAccountNumber) { this.fromAccountNumber = fromAccountNumber; }

    public String getFromAccountType() { return fromAccountType; }
    public void setFromAccountType(String fromAccountType) { this.fromAccountType = fromAccountType; }

    public String getFromAccountCurrency() { return fromAccountCurrency; }
    public void setFromAccountCurrency(String fromAccountCurrency) { this.fromAccountCurrency = fromAccountCurrency; }

    public String getToAccountNumber() { return toAccountNumber; }
    public void setToAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; }

    public String getToAccountType() { return toAccountType; }
    public void setToAccountType(String toAccountType) { this.toAccountType = toAccountType; }

    public String getToAccountCurrency() { return toAccountCurrency; }
    public void setToAccountCurrency(String toAccountCurrency) { this.toAccountCurrency = toAccountCurrency; }

    public String getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(String transactionAmount) { this.transactionAmount = transactionAmount; }

    public String getTransactionCurrency() { return transactionCurrency; }
    public void setTransactionCurrency(String transactionCurrency) { this.transactionCurrency = transactionCurrency; }

    public String getTransactionFee() { return transactionFee; }
    public void setTransactionFee(String transactionFee) { this.transactionFee = transactionFee; }

    public String getUdf1() { return udf1; }
    public void setUdf1(String udf1) { this.udf1 = udf1; }

    public String getUdf2() { return udf2; }
    public void setUdf2(String udf2) { this.udf2 = udf2; }

    public String getUdf3() { return udf3; }
    public void setUdf3(String udf3) { this.udf3 = udf3; }

    public String getUdf4() { return udf4; }
    public void setUdf4(String udf4) { this.udf4 = udf4; }

    public String getUdf5() { return udf5; }
    public void setUdf5(String udf5) { this.udf5 = udf5; }
}