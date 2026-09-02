package com.wallet.transaction.switching.ibft.incoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The ISO 8583 message the switch gateway hands over for a 1LINK-originated transaction, and the
 * shape it expects back.
 *
 * Field names mirror the gateway's {@code BasePDU} so the same JSON serialises in both directions.
 * Unknown properties are ignored, so extra data elements added later do not break this contract.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomingSwitchMessage {

    /** Carries the MTI, e.g. 0200 title fetch request or 0220 IBFT advice. */
    private Header header = new Header();

    /** DE-02 */
    private String pan;
    /** DE-03 */
    private String processingCode;
    /** DE-04 */
    private String transactionAmount;
    /** DE-07 */
    private String transactionDate;
    /** DE-11 */
    private String stan;
    /** DE-12 */
    private String transactionLocalTime;
    /** DE-13 */
    private String transactionLocalDate;
    /** DE-15 */
    private String settlementDate;
    /** DE-18 */
    private String merchantType;
    /** DE-22 */
    private String pointOfServiceEntryMode;
    /** DE-24 */
    private String networkIdentifier;
    /** DE-32 */
    private String acquirerIdentification;
    /** DE-33 */
    private String forwardingInsIdenCode;
    /** DE-37 */
    private String rrn;
    /** DE-38 */
    private String authIdResponse;
    /** DE-39 */
    private String responseCode;
    /** DE-41 */
    private String cardAcceptorTerminalIdentification;
    /** DE-42 */
    private String cardAcceptorIdentificationCode;
    /** DE-43 */
    private String cardAcceptorNameAndLocation;
    /** DE-48 */
    private String purposeOfPayment;
    /** DE-49 */
    private String transactionCurrencyCode;
    /** DE-102 - sender account at the remitting bank */
    private String accountNo1;
    /** DE-103 - beneficiary account, a DFS customer on this path */
    private String accountNo2;
    /** DE-120 */
    private String recordData;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String messageType;
    }
}
