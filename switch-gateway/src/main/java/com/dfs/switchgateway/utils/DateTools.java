package com.dfs.switchgateway.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.dfs.switchgateway.dto.BasePDU;
import com.dfs.switchgateway.dto.TSDtos.ResponseDto;
import com.dfs.switchgateway.dto.TSDtos.responseDto.AdviceResponseDto;
import com.dfs.switchgateway.dto.TSDtos.responseDto.MBAApisResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {

    // cannot create, the class has static methods only
    private DateTools() {
    }


    public static String dateToString( Date date, String dateFormat ) {
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

    public static String nextDatetoString( Date date, String dateFormat ) {
        if (date == null)
            return null;

        date = new Date(date.getTime() + (1000 * 60 * 60 * 24));
        String formatedDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            formatedDate = simpleDateFormat.format(date);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Date format is not correct: " + formatedDate);
        }
        return formatedDate;
    }

    public static String dateExpiration( Date date, String dateFormat ) {
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


    public static String currentDateToString( String dateFormat ) {
        return dateToString(new Date(), dateFormat);
    }


    public static Date stringToDate( String dateString, String dateFormat ) throws ParseException {
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

    public static Date truncToSec( Date date ) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        Date newDate = c.getTime();
        return newDate;
    }

    public static ResponseDto<MBAApisResponse> convertJsonToTitleFetchResponse( String responseBody ) {
        ObjectMapper om = new ObjectMapper();
        ResponseDto<MBAApisResponse> mbaApisResponseResponseDto = new ResponseDto<MBAApisResponse>();
        try {
            MBAApisResponse mbaApisResponseResponse = om.readValue(responseBody, MBAApisResponse.class);
            mbaApisResponseResponseDto.setData(mbaApisResponseResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mbaApisResponseResponseDto;
    }
//public static ResponseDto<MBAApisResponse> convertJsonToTitleFetchResponse(String responseBody) {
//    ObjectMapper om = new ObjectMapper();
//    ResponseDto<MBAApisResponse> mbaApisResponseResponseDto = new ResponseDto<>();
//    try {
//        TypeReference<ResponseDto<MBAApisResponse>> typeReference = new TypeReference<ResponseDto<MBAApisResponse>>() {};
//        
//        mbaApisResponseResponseDto = om.readValue(responseBody, typeReference);
//        
//    } catch (JsonProcessingException e) {
//        MBAApisResponse mbaApisResponse=new Gson().fromJson(responseBody.toString(),MBAApisResponse.class);
//        e.printStackTrace();
//    }
//    return mbaApisResponseResponseDto;
//
//    ResponseDto<MBAApisResponse> mbaApisResponseResponseDto = null;
//    try {
//        Gson gson = new Gson();
//        TypeToken<ResponseDto<MBAApisResponse>> typeToken = new TypeToken<ResponseDto<MBAApisResponse>>() {};
//
//         mbaApisResponseResponseDto = gson.fromJson(responseBody, typeToken.getType());
//
//        return mbaApisResponseResponseDto;
//
//    }
//    catch (Exception e) {
//        e.printStackTrace();
//    }
//    return mbaApisResponseResponseDto;
//}

    public static ResponseDto<AdviceResponseDto> convertJsonToAdviceResponse( String responseBody ) {
        ObjectMapper om = new ObjectMapper();
//        ResponseDto<AdviceResponseDto> adviceResponseDtoResponseDto = new ResponseDto<AdviceResponseDto>();
     ResponseDto<AdviceResponseDto> ResponseDto = new ResponseDto<AdviceResponseDto>();
       AdviceResponseDto adviceResponseDtoResponseDto = new AdviceResponseDto();
        try {

            adviceResponseDtoResponseDto = om.readValue(responseBody, AdviceResponseDto.class);
            ResponseDto.setData(adviceResponseDtoResponseDto);
            //adviceResponseDtoResponseDto = om.readValue(responseBody, ResponseDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseDto;
    }

}