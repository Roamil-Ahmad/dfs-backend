package com.wallet.transaction.dto;

public class CardToCardCpRequest {

    private String accountNo;
    private String amount;
    private String fromPan;
    private String cvc;
    private String expiry;
    private String pin;
    private String toPan;
    private String nickName;
    private String fromCardTitle;
    private String toCardTitle;
    private String track2data;
    private String otpPin;
    private String rrn;
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getFromPan() {
        return fromPan;
    }

    public void setFromPan(String fromPan) {
        this.fromPan = fromPan;
    }

    public String getToPan() {
        return toPan;
    }

    public void setToPan(String toPan) {
        this.toPan = toPan;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFromCardTitle() {
        return fromCardTitle;
    }

    public void setFromCardTitle(String fromCardTitle) {
        this.fromCardTitle = fromCardTitle;
    }

    public String getToCardTitle() {
        return toCardTitle;
    }

    public void setToCardTitle(String toCardTitle) {
        this.toCardTitle = toCardTitle;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTrack2data() {
        return track2data;
    }

    public void setTrack2data(String track2data) {
        this.track2data = track2data;
    }

    public String getOtpPin() {
        return otpPin;
    }

    public void setOtpPin(String otpPin) {
        this.otpPin = otpPin;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}
