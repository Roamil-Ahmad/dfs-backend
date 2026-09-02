/*
Author Name: romail.ahmed

Project Name: Transaction

Package Name: com.emo.dto.transactionsMw

Class Name: Payload

Date and Time:12/28/2024 12:29 AM

Version:1.0
*/
package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MpinPayload {

    private String mobileNumber;
    private String mpin;
}
