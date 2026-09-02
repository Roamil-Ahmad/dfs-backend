package com.dfs.switchsimulator.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * DE-120 "Record Data" layout for IBFT and Account Title Inquiry.
 *
 * Field order and widths are taken verbatim from "1LINK ISO8583 Message Format - Data Element
 * Definitions v7.0", section 9.62.1.1 (Usage: IBFT). The advice layout is 324 characters:
 *
 * <pre>
 *   off  len  sub-field                                      spec ref
 *     0   20  To Account                                     9.62.1.1 #1
 *    20   30  To Account Title                               9.62.1.1 #2
 *    50   11  Source IMD       (bank of the "From Account")  9.62.1.1 #3
 *    61   11  Destination IMD  (bank of the "To Account")    9.62.1.1 #4
 *    72    1  Identifier       D = debit leg, C = credit leg 9.62.1.1 #5
 *    73   20  Bank Name        (of the "To" account)         9.62.1.1 #6
 *    93   25  Branch Name      (of the "To" account)         9.62.1.1 #7
 *   118   30  Sender Name                                    9.62.1.1 #8
 *   148   24  Sender IBAN / mobile account number            9.62.1.1 #9
 *   172   24  Beneficiary IBAN / mobile account number       9.62.1.1 #10
 *   196   30  RTP ID                                         9.62.1.1 #11
 *   226   24  Reference No / bill account id                 9.62.1.1 #12
 *   250   30  Sender Id      (CNIC / NICOP / passport)       9.62.1.1 #13
 *   280   15  Beneficiary Id (CNIC / NICOP / passport / NTN) 9.62.1.1 #14
 *   295    4  Sender Country Code (remitting institution)    9.62.1.1 #15
 *   299   25  Originator Details  (remitting institution)    9.62.1.1 #16
 *   ---------
 *   324
 * </pre>
 *
 * The Account Title Inquiry request carries only the first five sub-fields, i.e. 73 characters
 * (1LINK spec section 11.13 read together with 9.62.1.1).
 */
public final class RecordDataLayout {

    public static final int ADVICE_LENGTH = 324;
    public static final int TITLE_FETCH_LENGTH = 73;

    private static final int OFF_TO_ACCOUNT = 0;
    private static final int LEN_TO_ACCOUNT = 20;
    private static final int OFF_TO_ACCOUNT_TITLE = 20;
    private static final int LEN_TO_ACCOUNT_TITLE = 30;
    private static final int OFF_SOURCE_IMD = 50;
    private static final int LEN_IMD = 11;
    private static final int OFF_DESTINATION_IMD = 61;
    private static final int OFF_IDENTIFIER = 72;
    private static final int LEN_IDENTIFIER = 1;
    private static final int LEN_BANK_NAME = 20;
    private static final int LEN_BRANCH_NAME = 25;
    private static final int LEN_SENDER_NAME = 30;
    private static final int LEN_IBAN = 24;
    private static final int LEN_RTP_ID = 30;
    private static final int LEN_REFERENCE_NO = 24;
    private static final int LEN_SENDER_ID = 30;
    private static final int OFF_BENEFICIARY_ID = 280;
    private static final int LEN_BENEFICIARY_ID = 15;
    private static final int LEN_COUNTRY_CODE = 4;
    private static final int LEN_ORIGINATOR_DETAILS = 25;

    /** A Pakistani IBAN is 24 characters: PK + 2 check digits + 4 bank code + 16 account digits. */
    private static final int IBAN_LENGTH = 24;
    private static final int IBAN_ACCOUNT_PART_LENGTH = 16;

    private RecordDataLayout() {
    }

    /**
     * Reduces an account identifier to the 20-character "To Account" sub-field. A full IBAN is
     * narrowed to its 16-digit account part; anything shorter is used as-is. Both are space padded.
     */
    public static String toAccountField(String accountNumber) {
        String value = StringUtils.defaultString(accountNumber).trim();
        if (value.length() >= IBAN_LENGTH) {
            value = value.substring(value.length() - IBAN_ACCOUNT_PART_LENGTH);
        }
        return StringUtils.rightPad(value, LEN_TO_ACCOUNT, " ");
    }

    /** Builds the 73-character record data of an Account Title Inquiry request. */
    public static String buildTitleFetchRecordData(String beneficiaryAccount,
                                                   String sourceImd,
                                                   String destinationImd,
                                                   String identifier) {
        String recordData = toAccountField(beneficiaryAccount)
                + StringUtils.rightPad("", LEN_TO_ACCOUNT_TITLE, " ")
                + StringUtils.rightPad(StringUtils.defaultString(sourceImd), LEN_IMD, "0")
                + StringUtils.rightPad(StringUtils.defaultString(destinationImd), LEN_IMD, "0")
                + StringUtils.rightPad(StringUtils.defaultString(identifier), LEN_IDENTIFIER, " ");
        return StringUtils.rightPad(recordData, TITLE_FETCH_LENGTH, " ");
    }

    /** Builds the 324-character record data of an IBFT advice. */
    public static String buildAdviceRecordData(String beneficiaryAccount,
                                               String beneficiaryTitle,
                                               String sourceImd,
                                               String destinationImd,
                                               String identifier,
                                               String bankName,
                                               String branchName,
                                               String senderName,
                                               String senderIban,
                                               String beneficiaryIban,
                                               String senderId,
                                               String beneficiaryId,
                                               String senderCountryCode,
                                               String originatorDetails) {
        String recordData = toAccountField(beneficiaryAccount)
                + StringUtils.rightPad(StringUtils.defaultString(beneficiaryTitle), LEN_TO_ACCOUNT_TITLE, " ")
                + StringUtils.rightPad(StringUtils.defaultString(sourceImd), LEN_IMD, "0")
                + StringUtils.rightPad(StringUtils.defaultString(destinationImd), LEN_IMD, "0")
                + StringUtils.rightPad(StringUtils.defaultString(identifier), LEN_IDENTIFIER, " ")
                + StringUtils.rightPad(StringUtils.defaultString(bankName), LEN_BANK_NAME, " ")
                + StringUtils.rightPad(StringUtils.defaultString(branchName), LEN_BRANCH_NAME, " ")
                + StringUtils.rightPad(StringUtils.defaultString(senderName), LEN_SENDER_NAME, " ")
                + StringUtils.rightPad(StringUtils.defaultString(senderIban), LEN_IBAN, " ")
                + StringUtils.rightPad(StringUtils.defaultString(beneficiaryIban), LEN_IBAN, " ")
                + StringUtils.rightPad("", LEN_RTP_ID, " ")
                + StringUtils.rightPad("", LEN_REFERENCE_NO, " ")
                + StringUtils.rightPad(StringUtils.defaultString(senderId), LEN_SENDER_ID, " ")
                + StringUtils.rightPad(StringUtils.defaultString(beneficiaryId), LEN_BENEFICIARY_ID, " ")
                + StringUtils.rightPad(StringUtils.defaultString(senderCountryCode), LEN_COUNTRY_CODE, " ")
                + StringUtils.rightPad(StringUtils.defaultString(originatorDetails), LEN_ORIGINATOR_DETAILS, " ");
        return StringUtils.rightPad(recordData, ADVICE_LENGTH, " ");
    }

    /** Right-pads any record data to the advice length so the packager sees a fixed-width field. */
    public static String padAdvice(String recordData) {
        return StringUtils.rightPad(StringUtils.defaultString(recordData), ADVICE_LENGTH, " ");
    }

    public static String beneficiaryTitle(String recordData) {
        return slice(recordData, OFF_TO_ACCOUNT_TITLE, LEN_TO_ACCOUNT_TITLE);
    }

    public static String toAccount(String recordData) {
        return slice(recordData, OFF_TO_ACCOUNT, LEN_TO_ACCOUNT);
    }

    public static String sourceImd(String recordData) {
        return slice(recordData, OFF_SOURCE_IMD, LEN_IMD);
    }

    public static String destinationImd(String recordData) {
        return slice(recordData, OFF_DESTINATION_IMD, LEN_IMD);
    }

    public static String identifier(String recordData) {
        return slice(recordData, OFF_IDENTIFIER, LEN_IDENTIFIER);
    }

    public static String beneficiaryId(String recordData) {
        return slice(recordData, OFF_BENEFICIARY_ID, LEN_BENEFICIARY_ID);
    }

    /** Null-safe, bounds-safe fixed-width read; returns an empty string when the field is absent. */
    private static String slice(String recordData, int offset, int length) {
        if (recordData == null || recordData.length() <= offset) {
            return "";
        }
        int end = Math.min(recordData.length(), offset + length);
        return recordData.substring(offset, end).trim();
    }
}
