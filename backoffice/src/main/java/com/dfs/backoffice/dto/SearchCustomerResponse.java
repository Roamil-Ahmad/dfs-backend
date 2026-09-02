package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SearchCustomerResponse {
    private BigDecimal customerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fatherName;
    private String grandfatherName;
    private String nidNo;
    private Date dob;
    private String email;
    private String addressC;
    private String addressM;
    private String addressP;
    private Date createdate;
    private String fullName;
    private String gender;
    private String isActive;
    private String pob;
    private Date nidExpiryDate;
    private Date nidIssueDate;
    private BigDecimal accountId;
    private String accountLevelDescr;
    private String accountStatusDescr;
    private String accountNo;
    private String nationality;
    private String occupationDescr;
    private String segmentDescr;
    private String riskProfile;
    private String mobileNo;
    private String provinceDescr;
    private String districtDescr;
    private String cityDescr;
    private BigDecimal currentBalance;
    private String nidFront;
    private String nidBack;
    private String proofOfAddress;
    private String selfie;
    private String signature;
    private String deviceId;
    private BigDecimal dailyAmtLimitDr;
    private BigDecimal monthlyAmtLimitDr;
    private BigDecimal yearlyAmtLimitDr;
    private BigDecimal dailyAmtLimitCr;
    private BigDecimal monthlyAmtLimitCr;
    private BigDecimal yearlyAmtLimitCr;
    private BigDecimal dailyTransLimitDr;
    private BigDecimal monthlyTransLimitDr;
    private BigDecimal yearlyTransLimitDr;
    private BigDecimal dailyTransLimitCr;
    private BigDecimal monthlyTransLimitCr;
    private BigDecimal yearlyTransLimitCr;
}
