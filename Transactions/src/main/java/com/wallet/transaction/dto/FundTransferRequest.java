/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.dto

Class Name: FundTransferRequest

Date and Time:1/4/2025 3:26 PM

Version:1.0
*/
package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FundTransferRequest {
    private String mobileNumber;
    private String nidNo;
    private String accountNo;
    private String amount;
    private String transPurposeId;
    private String mpin;
    private String narration;
    private String accountTitle;
    private String beneficiaryName;
    private String beneficiaryMobile;
    private String beneficiaryEmail;
    private String accountType;
    private String appUserId;

    // ---------------------------------------------------------------------
    // Cash-in specifics. PKG_FUNDS_TRANSFER.FT_CASH_IN records who physically
    // handed the money over, which the old AGENT_CASH_IN procedure never asked
    // for, so these are only populated on /v1/internalcashin.
    // ---------------------------------------------------------------------
    // P_CHANNEL_ID is not taken from the payload: the channel is already on the request envelope
    // as a code, and the id is resolved from LKP_CHANNEL.
    // P_DEPOSITOR_MOB is not taken from the payload either: it is accountNo, the account being
    // credited.
    /** P_BVS - biometric verification result for the depositor. */
    private String bvs;
    /** P_CASH_IN_TYPE - single character; the cash-in category. */
    private String cashInType;
    /** P_DEPOSITOR_NID - numeric CNIC of the person depositing the cash. */
    private String depositorNid;
    /** P_DEPOSITOR_NAME */
    private String depositorName;
    /** P_DEPOSITOR_DOB - yyyy-MM-dd. */
    private String depositorDob;
}
