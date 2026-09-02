/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.dto

Class Name: FundTransferRequest

Date and Time:1/4/2025 3:26 PM

Version:1.0
*/
package com.wallet.transaction.dto;

import com.wallet.transaction.model.LkpTransPurpose;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FundTransferResponce {
    private String responseDescr;
    private String authIdResponse;
    private String responseCode;
    private String transDate;
}
