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
public class GetReceivedMoneyRequest {
    private String mobileNumber;
    private String nidNo;
}
