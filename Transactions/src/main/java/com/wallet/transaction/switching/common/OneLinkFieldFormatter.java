package com.wallet.transaction.switching.common;

/**
 * Shapes DFS values into the fixed widths 1LINK expects on the wire.
 *
 * The switch rejects a message whose data elements are the wrong length, so the conversion has to
 * happen once, here, rather than being repeated per transaction type.
 */
public final class OneLinkFieldFormatter {

    /** DE-02, "Primary Account Number". 1LINK expects 16 digits. */
    public static final int PAN_LENGTH = 16;

    /**
     * DE-104 purpose of payment as the gateway validates it: the 4-digit product category from
     * 1LINK Appendix-A followed by 40 characters of free text.
     */
    public static final int PURPOSE_OF_PAYMENT_LENGTH = 44;

    /** Length of the account part of a PAN once the 6-digit BIN is in front of it. */
    private static final int PAN_ACCOUNT_LENGTH = 10;

    private OneLinkFieldFormatter() {
    }

    /**
     * Builds DE-02 for a wallet customer: the DFS BIN followed by the last digits of the account.
     *
     * A DFS wallet has no card, but 1LINK still identifies the sender by a 16-digit PAN, so the
     * account is expressed through the institution's own BIN. A Pakistani mobile account number is
     * 11 digits and its leading zero is not significant, which is exactly the 10 digits that follow
     * a 6-digit BIN.
     *
     * @return 16 digits, or null when there is no account to build one from
     */
    public static String pan(String bin, String accountNumber) {
        String digits = digitsOnly(accountNumber);
        if (digits.isEmpty()) {
            return null;
        }
        String institution = digitsOnly(bin);
        if (digits.length() > PAN_ACCOUNT_LENGTH) {
            // Drop the leading zero of an 11-digit mobile number, or the branch part of a longer one.
            digits = digits.substring(digits.length() - PAN_ACCOUNT_LENGTH);
        } else {
            digits = leftPad(digits, PAN_ACCOUNT_LENGTH, '0');
        }
        String pan = institution + digits;
        return pan.length() > PAN_LENGTH ? pan.substring(0, PAN_LENGTH) : rightPad(pan, PAN_LENGTH, ' ');
    }

    /**
     * Expands the product category the application sends into the 44-character field.
     * "0401" becomes "0401" plus 40 spaces.
     */
    public static String purposeOfPayment(String value) {
        return fixedWidth(value, PURPOSE_OF_PAYMENT_LENGTH);
    }

    /** Right pads with spaces, or truncates, so the value is exactly {@code length} characters. */
    public static String fixedWidth(String value, int length) {
        String text = value == null ? "" : value.trim();
        return text.length() > length ? text.substring(0, length) : rightPad(text, length, ' ');
    }

    /** Truncates to at most {@code length} characters without padding. */
    public static String atMost(String value, int length) {
        String text = value == null ? "" : value.trim();
        return text.length() > length ? text.substring(0, length) : text;
    }

    private static String digitsOnly(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder digits = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (Character.isDigit(c)) {
                digits.append(c);
            }
        }
        return digits.toString();
    }

    private static String leftPad(String value, int length, char pad) {
        StringBuilder builder = new StringBuilder();
        for (int i = value.length(); i < length; i++) {
            builder.append(pad);
        }
        return builder + value;
    }

    private static String rightPad(String value, int length, char pad) {
        StringBuilder builder = new StringBuilder(value);
        while (builder.length() < length) {
            builder.append(pad);
        }
        return builder.toString();
    }
}
