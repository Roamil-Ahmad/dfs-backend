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

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class InitiateLocalFTResponce {
    private String authIdResponse;
    private String accountTitle;
    private String accountNo;
    private String responseDescr;
    private String responseCode;
    private BigDecimal fee;
    private String availableBalance;
    private String actualBalance;
    private GenerateOtpResponse generateOtpResponse;
    List<LovResponse> transPurposes;

}
