package com.wallet.transaction.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateTools {

    // cannot create, the class has static methods only
    private DateTools() {
    }


    public static String dateToString(Date date, String dateFormat) {
        if (date == null)
            return null;

        String formatedDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            formatedDate = simpleDateFormat.format(date);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Date format is not correct: " + formatedDate);
        }
        return formatedDate;
    }

    public static String nextDatetoString(Date date, String dateFormat) {
        if (date == null)
            return null;

        date=new Date(date.getTime() + (1000 * 60 * 60 * 24));
        String formatedDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            formatedDate = simpleDateFormat.format(date);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Date format is not correct: " + formatedDate);
        }
        return formatedDate;
    }

    public static String dateExpiration(Date date, String dateFormat) {
        if (date == null)
            return null;

         date = DateUtils.addMonths(new Date(), 4);
        String formatedDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            formatedDate = simpleDateFormat.format(date);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Date format is not correct: " + formatedDate);
        }
        return formatedDate;
    }


    public static String currentDateToString(String dateFormat) {
        return dateToString(new Date(), dateFormat);
    }


    public static Date stringToDate(String dateString, String dateFormat) throws ParseException {
        if (StringUtils.isEmpty(dateString))
            return null;

        Date parsedDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            parsedDate = simpleDateFormat.parse(dateString);
        } catch (ParseException ex) {
            throw new ParseException("Input is not valid date string: " + dateString, 0);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Date format is not correct: " + dateFormat);
        }
        return parsedDate;
    }
    public static Date truncToSec(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        Date newDate = c.getTime();
        return newDate;
    }


    public static void main(String[] args) {
        System.out.println(DateTools.dateToString(new Date(), "yyyyMMdd"));
    }

    public static String generateStan() {
        String current=currentDateToString("yyyyMMddHHmmssSSS");
        Random random = new Random();
        int startIndex = random.nextInt(current.length() - 6); // Ensure space for 6 digits

        // Get a substring of 6 random digits
        return current.substring(startIndex, startIndex + 6);

    }

}