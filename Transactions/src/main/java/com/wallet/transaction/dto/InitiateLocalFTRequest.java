/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.dto.common

Class Name: InitiateLocalFTRequest

Date and Time:1/3/2025 11:00 PM

Version:1.0
*/
package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InitiateLocalFTRequest {
    private String mobileNumber;
    private String nidNo;
    private String accountNo;
    private String amount;
    private String accountType;
    private String type;

}
