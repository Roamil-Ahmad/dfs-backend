package com.wallet.transaction.switching.ibft;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Input parameters of {@code PKG_MW.OUTGOING_IBFT} and {@code PKG_MW.INCOMING_IBFT}.
 *
 * Both procedures take the same 32 IN / IN OUT parameters in the same order, with two differences
 * that the caller handles: positions 18 and 19 are swapped between them, and {@code P_IDENTIFIER}
 * is VARCHAR2 on the outgoing side and CHAR on the incoming side.
 *
 * Field names mirror the procedure parameter names so the mapping stays obvious when the database
 * team publishes the corrected package body.
 */
@Data
@NoArgsConstructor
public class IbftProcedureRequest {

    /** 1 - P_CLIENT_SECRET */
    private String clientSecret;
    /** 2 - P_CHANNEL_CODE */
    private String channelCode;
    /** 3 - P_USER_ID */
    private Long userId;
    /**
     * 4 - P_RELATIONSHIPID. Either "PAN=EXPIRY" or a bare national ID; the procedure branches on
     * whether an "=" is present. Outgoing sends the NID and the procedure matches it against
     * TBL_CUSTOMER.NID_NO to identify the payer. Incoming only files it in TBL_MW_REQUEST.
     */
    private String relationshipId;
    /** 5 - P_TRANSMISSIONDATE, DE-07 date part */
    private String transmissionDate;
    /** 6 - P_TRANSMISSIONTIME, DE-07 time part */
    private String transmissionTime;
    /** 7 - P_STAN, DE-11 */
    private String stan;
    /** 8 - P_RRN, DE-37 */
    private String rrn;
    /** 9 - P_DATELOCALTRAN, DE-13 MMDD */
    private String dateLocalTran;
    /** 10 - P_TIMELOCALTRAN, DE-12 HHmmss */
    private String timeLocalTran;
    /** 11 - P_ACQINSTCODE, DE-32 */
    private String acquiringInstitutionCode;
    /** 12 - P_MERCHANTTYPE, DE-18 */
    private String merchantType;
    /** 13 - P_POSENTRYMODE, DE-22 */
    private String posEntryMode;
    /** 14 - P_FROMACCOUNTNUMBER, DE-102 */
    private String fromAccountNumber;
    /** 15 - P_FROMACCOUNTTYPE */
    private String fromAccountType;
    /** 16 - P_FROMACCOUNTCURRENCY */
    private String fromAccountCurrency;
    /** 17 - P_TOACCOUNTNUMBER, DE-103 */
    private String toAccountNumber;
    /** 18/19 - P_CARDACCEPTORNAMELOCATION, DE-43 */
    private String cardAcceptorNameLocation;
    /** 18/19 - P_CARDACCEPTORTERMINALID, DE-41 */
    private String cardAcceptorTerminalId;
    /** 20 - P_TRANSACTIONAMOUNT, DE-04 */
    private String transactionAmount;
    /** 21 - P_TRANSACTIONCURRENCY, DE-49 */
    private String transactionCurrency;
    /** 22 - P_TRANSACTIONPURPOSE, DE-48 */
    private String transactionPurpose;
    /** 23 - P_SOURCEIMD, DE-120 sub-field 3 */
    private String sourceImd;
    /** 24 - P_DESTINATIONIMD, DE-120 sub-field 4 */
    private String destinationImd;
    /** 25 - P_IDENTIFIER, DE-120 sub-field 5, D or C */
    private String identifier;
    /** 26 - P_RECORDDATA, DE-120, IN OUT */
    private String recordData;
    /** 27 - P_TRANSACTIONFEE, DE-28 */
    private String transactionFee;
    /** 28 - P_UDF1, IN OUT */
    private String udf1;
    /** 29 - P_UDF2 */
    private String udf2;
    /** 30 - P_UDF3 */
    private String udf3;
    /** 31 - P_UDF4 */
    private String udf4;
    /** 32 - P_UDF5 */
    private String udf5;
}
