package com.dfs.backoffice.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Pakistan (DFS) IBAN generation.
 *
 * Format: "PK" + checkDigit + "DFSS" + paddedAccountNumber  = 24 characters
 *   PK(2) + check(2) + DFSS(4) + account(16) = 24
 * The account number is left-padded with "00000" so an 11-digit mobile number
 * yields the 16-character account segment the 24-char IBAN requires.
 * The country code contributes to the check digit only.
 */
public class IbanGenerator {

    public static String generateIban(String accountNumber) {
        String bankCode = convertCharactersToAscii("DFSS");
        String countryCode = convertCharactersToAscii(convertCharactersToAscii("PK"));
        String paddedAccountNumber = "00000" + accountNumber;
        String checkDigit = genrateCheckDigit(bankCode + paddedAccountNumber + countryCode + "00");
        String iban = "PK" + checkDigit + "DFSS" + paddedAccountNumber;
        return iban;
    }

    public static String convertCharactersToAscii(String x) {

        byte[] bytes = x.getBytes(StandardCharsets.US_ASCII);
        List<Integer> result = new ArrayList<>();   // convert bytes to ascii
        for (byte aByte : bytes) {
            int ascii = (int) aByte;                // byte -> int
            result.add(ascii);
        }
        String finalResult = result.toString().replace("[", "");
        finalResult = finalResult.replace("]", "");
        finalResult = finalResult.replace(",", "");
        finalResult = finalResult.replace(" ", "");
        return finalResult;

    }

    public static String genrateCheckDigit(String x) {
        String result;
        long sum = 0;
        for (int i = 0; i < x.length(); i++) {
            sum = sum + Long.valueOf(x.charAt(i));
        }
        sum = sum % 97;
        sum = 98 - sum;
        if (sum < 10) {
            result = "0" + sum;
        } else {
            result = String.valueOf(sum);
        }
        return result;
    }
}
