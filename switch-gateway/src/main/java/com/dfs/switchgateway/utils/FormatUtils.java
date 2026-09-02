package com.dfs.switchgateway.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class FormatUtils {

    private static Logger logger = LoggerFactory.getLogger(FormatUtils.class.getSimpleName());

    /**
     * <p>
     * This method take a phoenix currency value which assumes to have always
     * two decimal point value with every currency amount. For example, phoenix
     * 100 means 1.00, 10055 means 100.55 and so on.
     * </p>
     *
     * @return String - A formatted currency value in #0.00 pattern.
     */
    public static String parseCSAmount(String value) {

        try {
            //The check only for when response from mock.
            if (value != null && value.contains(".")) {
                value = parseI8Amount(value);
            }
            if (value != null && value.length() > 2) {
                int length = value.length();
                int decimalPosition = length - 2;
                StringBuilder builder = new StringBuilder(value);
                builder.insert(decimalPosition, ".").toString();
                Double d = Double.parseDouble(builder.toString());
                // value = d.toString();
                DecimalFormat Currency = new DecimalFormat("#0.00");
                String howmuch = Currency.format(d);
                value = howmuch;
            }
        } catch (Exception e) {
            logger.info("Exception", e);
        }
        return value;
    }

    /**
     * <p>
     * An utility method which translate currency related amount from
     * i8Microbank to Phoenix supported protocol currency. Phoenix require two
     * decimal points for every currency amount without decimal point. For
     * example, 200 would be 20000, 200.08 would be 20008 and so on.
     * </p>
     *
     * @return String - Formatted String amount according to Phoenix protocol
     * supported.
     */
    public static String parseI8Amount(String value) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (StringUtils.isNotEmpty(value)) {
                // CHECK IF AMOUNT HAS ALREADY DECIMAL POINT
                if (value.contains(".")) {
                    // SPILIT AMOUNT INTO TWO PARTS, ONE BEFORE DECIMAL & 2ND
                    // AFTER DECIMAL
                    String[] args = StringUtils.split(value, ".");
                    stringBuilder.append(args[0]);
                    // CHECK IF ONE DECIMAL PONT VALUE LIKE 200.5
                    if (args[1].length() == 1) {
                        stringBuilder.append(args[1]);
                        stringBuilder.append("0");
                        // CHECK IF ALREADY TWO DECIMAL PONT VALUE LIKE 200.50
                    } else if (args[1].length() == 2) {
                        stringBuilder.append(args[1]);
                    }
                    // IF THERE IS NO DECIMAL POINT THEN APPEND TWO 00 TO THE
                    // VALUE
                } else {
                    stringBuilder.append(value);
                    stringBuilder.append("00");
                }
            } else {
                throw new RuntimeException("Invalid Transaction Amount Provided. Please verify ");
            }
        } catch (Exception e) {
            logger.info("Exception", e);
        }
        return stringBuilder.toString();
    }
    public static String parseMiddlewareAmount(String value) {

        try {
            //The check only for when response from mock.
            if (value != null && value.contains(".")) {
                value = parseI8Amount(value);
            }
            if (value != null && value.length() > 2) {
                int length = value.length();
                int decimalPosition = length - 2;
                StringBuilder builder = new StringBuilder(value);
                builder.insert(decimalPosition, ".").toString();
                Double d = Double.parseDouble(builder.toString());
                // value = d.toString();
                DecimalFormat Currency = new DecimalFormat("#0.00");
                String howmuch = Currency.format(d);
                value = howmuch;
            }
        } catch (Exception e) {
            logger.info("Exception", e);
        }
        return value;
    }
}
