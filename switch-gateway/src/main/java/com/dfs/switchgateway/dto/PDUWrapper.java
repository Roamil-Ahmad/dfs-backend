package com.dfs.switchgateway.dto;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto

Class Name: PDUWrapper

Date and Time:5/26/2023 10:59 AM

Version:1.0
*/

import java.util.Date;

/**
 * <p>
 * <code>PDUWrapper</code> is an utility class, which will hold response PDU and
 * response arrival time, and RRN / Stan key. Which is used to uniquely identify
 * transaction requests in DC and external system as well.
 * </p>
 */
public class PDUWrapper {
    private BasePDU basePDU;
    private Date poolTimeIn;
    private String RRNKey;
    private String stan;

    public BasePDU getBasePDU() {
        return basePDU;
    }

    public void setBasePDU( BasePDU basePDU ) {
        this.basePDU = basePDU;
    }

    public Date getPoolTimeIn() {
        return poolTimeIn;
    }

    public void setPoolTimeIn( Date poolTimeIn ) {
        this.poolTimeIn = poolTimeIn;
    }

    public String getRRNKey() {
        return RRNKey;
    }

    public void setRRNKey( String rRNKey ) {
        RRNKey = rRNKey;
    }

    public String getStan() {
        return stan;
    }

    public void setStan( String stan ) {
        this.stan = stan;
    }
}