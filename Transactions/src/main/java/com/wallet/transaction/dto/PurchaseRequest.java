package com.wallet.transaction.dto;

public class PurchaseRequest {
    private String amount;
    private String pin;
    private String track2data;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
}