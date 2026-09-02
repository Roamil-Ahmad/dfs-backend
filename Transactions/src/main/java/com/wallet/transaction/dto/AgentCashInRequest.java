package com.wallet.transaction.dto;

public class AgentCashInRequest {
    private String clientSecret;
    private String channelCode;
    private Long appUserId;
    private String relationshipId;
    private String transmissionDate;
    private String transmissionTime;
    private String stan;
    private String rrn;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String transactionAmount;
    private Float latitude;
    private Float longitude;

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

    public String getFromAccountNumber() { return fromAccountNumber; }
    public void setFromAccountNumber(String fromAccountNumber) { this.fromAccountNumber = fromAccountNumber; }

    public String getToAccountNumber() { return toAccountNumber; }
    public void setToAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; }

    public String getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(String transactionAmount) { this.transactionAmount = transactionAmount; }

    public Float getLatitude() { return latitude; }
    public void setLatitude(Float latitude) { this.latitude = latitude; }

    public Float getLongitude() { return longitude; }
    public void setLongitude(Float longitude) { this.longitude = longitude; }
}