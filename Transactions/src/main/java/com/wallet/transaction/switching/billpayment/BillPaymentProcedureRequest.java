package com.wallet.transaction.switching.billpayment;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Parameters for {@code PKG_MW.BILL_PAYMENT}, in the order the procedure declares them:
 * 28 IN parameters (24 is IN OUT) followed by 8 OUT.
 *
 * <pre>
 *    1 P_CLIENT_SECRET             IN     VARCHAR2
 *    2 P_CHANNEL_CODE              IN     VARCHAR2
 *    3 P_USER_ID                   IN     NUMBER
 *    4 P_RELATIONSHIPID            IN     VARCHAR2
 *    5 P_TRANSMISSIONDATE          IN     VARCHAR2
 *    6 P_TRANSMISSIONTIME          IN     VARCHAR2
 *    7 P_STAN                      IN     VARCHAR2
 *    8 P_RRN                       IN     VARCHAR2
 *    9 P_DATELOCALTRAN             IN     VARCHAR2
 *   10 P_TIMELOCALTRAN             IN     VARCHAR2
 *   11 P_ACQINSTCODE               IN     VARCHAR2
 *   12 P_MERCHANTTYPE              IN     VARCHAR2
 *   13 P_POSENTRYMODE              IN     VARCHAR2
 *   14 P_CARDACCEPTORNAMELOCATION  IN     VARCHAR2
 *   15 P_CARDACCEPTORTERMINALID    IN     VARCHAR2
 *   16 P_FROMACCOUNTNUMBER         IN     VARCHAR2
 *   17 P_FROMACCOUNTTYPE           IN     VARCHAR2
 *   18 P_FROMACCOUNTCURRENCY       IN     VARCHAR2
 *   19 P_TRANSACTIONAMOUNT         IN     VARCHAR2
 *   20 P_TRANSACTIONCURRENCY       IN     VARCHAR2
 *   21 P_UTILITYCOMPANYID          IN     VARCHAR2
 *   22 P_UTILITYCONSUMERNUMBER     IN     VARCHAR2
 *   23 P_TRANSACTIONFEE            IN     VARCHAR2
 *   24 P_UDF1                      IN OUT VARCHAR2
 *   25 P_UDF2                      IN     VARCHAR2
 *   26 P_UDF3                      IN     VARCHAR2
 *   27 P_UDF4                      IN     VARCHAR2
 *   28 P_UDF5                      IN     VARCHAR2
 *   29 P_AUTHIDRESPONSE            OUT    VARCHAR2
 *   30 P_ERRORRESPONSE             OUT    VARCHAR2
 *   31 P_AVAILABLEBALANCE          OUT    VARCHAR2
 *   32 P_ACTUALBALANCE             OUT    VARCHAR2
 *   33 P_RESPONSEDESCR             OUT    VARCHAR2
 *   34 P_RESPONSESTATUS            OUT    NUMBER
 *   35 P_CHECKPOINT                OUT    VARCHAR2
 *   36 P_TRANS_HEAD_ID             OUT    NUMBER
 * </pre>
 *
 * <p>The first thirteen positions match PKG_MW's other procedures; from 14 onward the layout is
 * this procedure's own, so nothing here may be assumed from the IBFT request.</p>
 */
@Data
@NoArgsConstructor
public class BillPaymentProcedureRequest {

    /** 1 - P_CLIENT_SECRET, from TBL_MW_CHANNEL. */
    private String clientSecret;
    /** 2 - P_CHANNEL_CODE, the numeric TBL_MW_CHANNEL code. */
    private String channelCode;
    /** 3 - P_USER_ID, the MW service account that owns the channel row. */
    private Long userId;
    /**
     * 4 - P_RELATIONSHIPID. Either "PAN=EXPIRY" or the national ID as stored in
     * TBL_CUSTOMER.NID_NO, which is ciphertext. The procedure branches on whether an "=" is
     * present and resolves the paying customer from it.
     */
    private String relationshipId;
    /** 5 - P_TRANSMISSIONDATE, DE-07 date part. */
    private String transmissionDate;
    /** 6 - P_TRANSMISSIONTIME, DE-07 time part. */
    private String transmissionTime;
    /** 7 - P_STAN, DE-11. */
    private String stan;
    /** 8 - P_RRN, DE-37. */
    private String rrn;
    /** 9 - P_DATELOCALTRAN, DE-13. */
    private String dateLocalTran;
    /** 10 - P_TIMELOCALTRAN, DE-12. */
    private String timeLocalTran;
    /** 11 - P_ACQINSTCODE, DE-32. */
    private String acquiringInstitutionCode;
    /** 12 - P_MERCHANTTYPE, DE-18. */
    private String merchantType;
    /** 13 - P_POSENTRYMODE, DE-22. */
    private String posEntryMode;
    /** 14 - P_CARDACCEPTORNAMELOCATION, DE-43. */
    private String cardAcceptorNameLocation;
    /** 15 - P_CARDACCEPTORTERMINALID, DE-41. */
    private String cardAcceptorTerminalId;
    /** 16 - P_FROMACCOUNTNUMBER, the customer's own account. */
    private String fromAccountNumber;
    /** 17 - P_FROMACCOUNTTYPE. */
    private String fromAccountType;
    /** 18 - P_FROMACCOUNTCURRENCY. */
    private String fromAccountCurrency;
    /** 19 - P_TRANSACTIONAMOUNT, DE-04. */
    private String transactionAmount;
    /** 20 - P_TRANSACTIONCURRENCY, DE-49. */
    private String transactionCurrency;
    /** 21 - P_UTILITYCOMPANYID, the biller. */
    private String utilityCompanyId;
    /** 22 - P_UTILITYCONSUMERNUMBER, the consumer reference on the bill. */
    private String utilityConsumerNumber;
    /** 23 - P_TRANSACTIONFEE, DE-28. */
    private String transactionFee;
    /** 24 - P_UDF1, IN OUT user defined field. */
    private String udf1;
    /** 25 - P_UDF2. */
    private String udf2;
    /** 26 - P_UDF3. */
    private String udf3;
    /** 27 - P_UDF4. */
    private String udf4;
    /** 28 - P_UDF5. */
    private String udf5;
}
