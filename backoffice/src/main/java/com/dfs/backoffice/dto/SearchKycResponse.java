package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SearchKycResponse {
    private BigDecimal accountUpgradeId;
    private BigDecimal statusId;
    private BigDecimal accountId;
    private BigDecimal accountLevelId;
    private String accountNo;
    private String accountTitle;
    private String accountLevelDescr;
    private String status;
    private String sourceOfIncome;
    private String province;
    private String occupation;
    private String accountPurpose;
    private String nidFront;
    private String nidBack;
    private String selfie;
    private String fingerprint;
    private String proofOfAddress;
    private String comments;
    private String upgradeRequestDate;
    private String checkerName;
    private String checkerComments;
    private String checkDate;
}
