/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.dto

Class Name: FundTransferRequest

Date and Time:1/4/2025 3:26 PM

Version:1.0
*/
package com.wallet.transaction.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FundTransferResponce {
    private String nidNo;
    private String accountNo;
    private String amount;
    private String transPurpose;
    private String mPin;
    private String narration;
    private String accountTitle;
    private String beneficiaryName;
    private String beneficiaryMobile;
    private String beneficiaryEmail;
    private String accountType;
    private String appUserId;
}
