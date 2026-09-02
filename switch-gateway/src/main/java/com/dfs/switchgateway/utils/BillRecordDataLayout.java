package com.dfs.switchgateway.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * DE-120 "Record Data" layout for Utility Bill Payment.
 *
 * <p><b>Provisional.</b> The IBFT layout in {@link RecordDataLayout} is taken verbatim from
 * "1LINK ISO8583 Message Format - Data Element Definitions v7.0" section 9.62.1.1. The UBP section
 * of that document is not in this repository, so the widths below are the DFS working layout: they
 * are fixed width, self-consistent between the gateway and the simulator, and isolated in this one
 * class precisely so they can be corrected in a single place once the UBP section is available.
 * Confirm them with 1LINK before any live certification.</p>
 *
 * <pre>
 *   off  len  sub-field
 *     0   20  Utility company code
 *    20   30  Utility company name
 *    50   20  Consumer number
 *    70   12  Bill amount, minor units, zero padded
 *    82    8  Due date, yyyyMMdd
 *    90    1  Bill status: U unpaid, P paid
 *   ---------
 *    91
 * </pre>
 */
public final class BillRecordDataLayout {

    public static final int BILL_LENGTH = 91;

    public static final String STATUS_UNPAID = "U";
    public static final String STATUS_PAID = "P";

    private static final int OFF_COMPANY_CODE = 0;
    private static final int LEN_COMPANY_CODE = 20;
    private static final int OFF_COMPANY_NAME = 20;
    private static final int LEN_COMPANY_NAME = 30;
    private static final int OFF_CONSUMER_NO = 50;
    private static final int LEN_CONSUMER_NO = 20;
    private static final int OFF_BILL_AMOUNT = 70;
    private static final int LEN_BILL_AMOUNT = 12;
    private static final int OFF_DUE_DATE = 82;
    private static final int LEN_DUE_DATE = 8;
    private static final int OFF_STATUS = 90;
    private static final int LEN_STATUS = 1;

    private BillRecordDataLayout() {
    }

    /** Builds the 91-character record data of a bill inquiry or bill payment message. */
    public static String build(String companyCode, String companyName, String consumerNo,
                               String billAmount, String dueDate, String status) {
        // Each sub-field is padded AND truncated: an over-long value must never push the ones
        // after it out of position, because the receiver reads them by offset.
        return fit(companyCode, LEN_COMPANY_CODE)
                + fit(companyName, LEN_COMPANY_NAME)
                + fit(consumerNo, LEN_CONSUMER_NO)
                + amountField(billAmount)
                + fit(dueDate, LEN_DUE_DATE)
                + fit(status, LEN_STATUS);
    }

    /** Exactly {@code length} characters: space padded when short, cut when long. */
    private static String fit(String value, int length) {
        String text = trim(value);
        return text.length() > length ? text.substring(0, length) : StringUtils.rightPad(text, length, " ");
    }

    public static String companyCode(String recordData) {
        return read(recordData, OFF_COMPANY_CODE, LEN_COMPANY_CODE);
    }

    public static String companyName(String recordData) {
        return read(recordData, OFF_COMPANY_NAME, LEN_COMPANY_NAME);
    }

    public static String consumerNo(String recordData) {
        return read(recordData, OFF_CONSUMER_NO, LEN_CONSUMER_NO);
    }

    /** The amount sub-field, converted back to major units, e.g. "000000150000" to "1500.00". */
    public static String billAmount(String recordData) {
        String raw = read(recordData, OFF_BILL_AMOUNT, LEN_BILL_AMOUNT);
        if (raw.isEmpty() || !raw.chars().allMatch(Character::isDigit)) {
            return "";
        }
        String digits = StringUtils.leftPad(raw, 3, "0");
        String whole = digits.substring(0, digits.length() - 2).replaceFirst("^0+(?=\\d)", "");
        return whole + "." + digits.substring(digits.length() - 2);
    }

    public static String dueDate(String recordData) {
        return read(recordData, OFF_DUE_DATE, LEN_DUE_DATE);
    }

    public static String status(String recordData) {
        return read(recordData, OFF_STATUS, LEN_STATUS);
    }

    /** Major units to a 12-digit minor-unit field: "1500.00" becomes "000000150000". */
    private static String amountField(String amount) {
        String value = trim(amount);
        if (value.isEmpty()) {
            return StringUtils.repeat('0', LEN_BILL_AMOUNT);
        }
        String digitsOnly = value.replace(".", "");
        if (!value.contains(".")) {
            digitsOnly = digitsOnly + "00";
        } else {
            int decimals = value.length() - value.indexOf('.') - 1;
            if (decimals == 1) {
                digitsOnly = digitsOnly + "0";
            } else if (decimals > 2) {
                digitsOnly = digitsOnly.substring(0, digitsOnly.length() - (decimals - 2));
            }
        }
        if (!digitsOnly.chars().allMatch(Character::isDigit)) {
            return StringUtils.repeat('0', LEN_BILL_AMOUNT);
        }
        return StringUtils.leftPad(digitsOnly, LEN_BILL_AMOUNT, "0");
    }

    /** Null-safe, bounds-safe fixed-width read of a sub-field. */
    private static String read(String recordData, int offset, int length) {
        if (recordData == null || recordData.length() <= offset) {
            return "";
        }
        int end = Math.min(recordData.length(), offset + length);
        return recordData.substring(offset, end).trim();
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
