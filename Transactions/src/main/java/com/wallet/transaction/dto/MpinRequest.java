/*
Author Name: romail.ahmed

Project Name: Transaction

Package Name: com.emo.dto.transactionsMw

Class Name: mPinRequest

Date and Time:12/28/2024 12:28 AM

Version:1.0
*/
package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class MpinRequest {

    private String channel;
    private String segment;
    private String imieNo;
    private MpinPayload payload;

}
