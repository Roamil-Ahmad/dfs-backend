package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankLovResponse {
    private String name;
    private String code;
    private String bin;
    private String imd;
    private String ibanShort;
    private int minAccountLength;
    private int maxAccountLength;
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImd() {
        return imd;
    }

    public void setImd(String imd) {
        this.imd = imd;
    }

    public String getIbanShort() {
        return ibanShort;
    }

    public void setIbanShort(String ibanShort) {
        this.ibanShort = ibanShort;
    }
}
