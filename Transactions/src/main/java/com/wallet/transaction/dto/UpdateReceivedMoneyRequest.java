/*
Author Name: romail.ahmed

Project Name: transactions

Package Name: com.wallet.transaction.dto

Class Name: RequestMoneyRequest

Date and Time:1/11/2025 10:23 PM

Version:1.0
*/
package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateReceivedMoneyRequest {
    private String nidNo;
    private String accountNo;
    private String requestMoneyId;
    private String status;
    private String amount;
    private String appUserId;
    private String mpin;
    private String accountTitle;
    private String mobileNumber;
}
