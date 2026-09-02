package com.dfs.thirdparties.commons;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class OtpGenerator {

    private static final String DIGITS = "0123456789";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHANUMERIC =  UPPERCASE_LETTERS + LOWERCASE_LETTERS+DIGITS;
    private static final Random random = new SecureRandom();

    public static String generateOtp(String pattern) {
        StringBuilder sb = new StringBuilder();

        for (char c : pattern.toCharArray()) {
            switch (c) {
                case 'd':
                    sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
                    break;
                case 'a':
                    sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
                    break;
                case 'l':
                    sb.append(LOWERCASE_LETTERS.charAt(random.nextInt(LOWERCASE_LETTERS.length())));
                    break;
                case 'u':
                    sb.append(UPPERCASE_LETTERS.charAt(random.nextInt(UPPERCASE_LETTERS.length())));
                    break;
                default:
                    throw new IllegalArgumentException("INVALID_OTP_TYPE_EXPRESSION");
            }
        }

        return sb.toString();
    }
}
