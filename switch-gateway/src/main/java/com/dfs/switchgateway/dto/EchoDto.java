package com.dfs.switchgateway.dto;

import com.dfs.switchgateway.utils.ISO8583Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.dfs.switchgateway.dto.enums.ISO8583FieldEnum.*;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto

Class Name: EchoRequest

Date and Time:7/10/2023 4:39 PM

Version:1.0
*/
public class EchoDto {
    private String mti;
    private String transactionDateTime;
    private String stan;
    private String networkManagementCode;
    private String networkIdentifier;

    public String getMti() {
        return mti;
    }

    public void setMti( String mti ) {
        this.mti = mti;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime( String transactionDateTime ) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan( String stan ) {
        this.stan = stan;
    }

    public String getNetworkManagementCode() {
        return networkManagementCode;
    }

    public void setNetworkManagementCode( String networkManagementCode ) {
        this.networkManagementCode = networkManagementCode;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier( String networkIdentifier ) {
        this.networkIdentifier = networkIdentifier;
    }
}
