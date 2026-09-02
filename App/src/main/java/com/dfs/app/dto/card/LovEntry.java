package com.dfs.app.dto.card;

public class LovEntry {
    private String code;
    private String descr;

    public LovEntry() {
    }

    public LovEntry(String code, String descr) {
        this.code = code;
        this.descr = descr;
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
