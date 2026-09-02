package com.dfs.app.config;

import java.util.regex.Pattern;

/**
 * Masks sensitive (PII / financial / security) field values in a JSON string before it is logged,
 * and truncates to a safe length. Shared by the request-logging aspect and the audit filter.
 */
public final class LogSanitizer {

    private LogSanitizer() {
    }

    private static final int MAX_LEN = 4000;

    private static final Pattern SENSITIVE = Pattern.compile(
            "(\"(?i:password|newPassword|oldPassword|pin|mpin|newMpin|oldMpin|otp|otpCode|cvv|cnic|nidNo|nidNumber|mobileNumber|cardNumber|cardNo|cardPin|accountNo|accountNumber|iban|balance|amount|dob|email|username|token|accessToken|authToken)\"\\s*:\\s*)(\"[^\"]*\"|-?\\d+(?:\\.\\d+)?)");

    public static String maskAndTrim(String json) {
        if (json == null) {
            return "null";
        }
        String masked = SENSITIVE.matcher(json).replaceAll("$1\"***\"");
        return masked.length() > MAX_LEN ? masked.substring(0, MAX_LEN) + "...(truncated)" : masked;
    }
}
