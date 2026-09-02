package com.dfs.switchsimulator.dto;

/*
Author Name: abdul.fatah

Project Name: 1LinkSimulator

Package Name: com.dfs.switchsimulator.dto

Class Name: PDUWrapper

Date and Time:10/2/2023 5:12 PM

Version:1.0
*/

import com.dfs.switchsimulator.common.BasePdu;

import java.util.Date;

/**
 * <p>
 * <code>PDUWrapper</code> is an utility class, which will hold response PDU and
 * response arrival time, and RRN / Stan key. Which is used to uniquely identify
 * transaction requests in DC and external system as well.
 * </p>
 */
public class PDUWrapper {
    private BasePdu basePDU;
    private Date poolTimeIn;
    private String RRNKey;
    private String stan;

    public BasePdu getBasePDU() {
        return basePDU;
    }

    public void setBasePDU( BasePdu basePDU ) {
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

    public void setRRNKey( String RRNKey ) {
        this.RRNKey = RRNKey;
    }

    public String getStan() {
        return stan;
    }

    public void setStan( String stan ) {
        this.stan = stan;
    }
}