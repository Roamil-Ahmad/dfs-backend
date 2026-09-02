/*
Author Name: romail.ahmed

Project Name: transactions

Package Name: com.wallet.transaction.dto

Class Name: RequetMoneyRequest

Date and Time:1/11/2025 8:06 PM

Version:1.0
*/
package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TitleFetchRequetMoneyResponse {
    private String accountTitle;
    private String accountNo;
    private String requesteAccountId;
}
