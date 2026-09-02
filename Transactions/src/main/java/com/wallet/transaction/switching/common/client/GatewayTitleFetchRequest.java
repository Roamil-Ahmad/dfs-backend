package com.wallet.transaction.switching.common.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Account Title Inquiry request as the switch gateway expects it.
 *
 * Field names follow the "1link Apis Document" IBFT Title Fetch request so the same object shape
 * carries through to the real 1LINK REST interface unchanged.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewayTitleFetchRequest {

    /** DE-02 */
    private String pan;
    /** DE-04, minor units, 12 digits */
    private String transactionAmount;
    /** ddMMyyyyHHmmss */
    private String transactionDateTime;
    /** DE-18 */
    private String merchantType;
    /** DE-22 */
    private String pointOfEntry;
    /** DE-24 */
    private String networkIdentifier;
    /** DE-41 */
    private String cardAcceptorTerminalId;
    /** DE-42 */
    private String cardAcceptorIdentificationCode;
    /** DE-43 */
    private String cardAcceptorNameAndLocation;
    /** DE-48 product category code plus free text */
    private String purposeOfPayment;
    /** DE-49 */
    private String currencyCode;
    /** DE-102 - sender account */
    private String accountNo1;
    /** DE-103 - beneficiary account */
    private String accountNo2;
    /** DE-120 destination IMD */
    private String toBankImd;
    /** DE-11 */
    private String stan;
    /** DE-37 */
    private String rrn;
}
