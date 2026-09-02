package com.dfs.app.dto;

import java.util.List;

public class ContactlistDTO {

    private List<String> contactList;

    private String walletOrDebitAccounts;
    private String mobileNumber;

    public String getWalletOrDebitAccounts() {
        return walletOrDebitAccounts;
    }

    public void setWalletOrDebitAccounts(String walletOrDebitAccounts) {
        this.walletOrDebitAccounts = walletOrDebitAccounts;
    }

    public List<String> getContactList() {
        return contactList;
    }

    public void setContactList(List<String> contactList) {
        this.contactList = contactList;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
