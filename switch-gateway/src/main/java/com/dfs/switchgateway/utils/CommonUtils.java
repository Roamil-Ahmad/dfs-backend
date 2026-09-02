/**
 *
 */
package com.dfs.switchgateway.utils;


//import ch.qos.logback.core.net.server.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.dfs.switchgateway.dto.TSDtos.OTIBFTTitleFetchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CommonUtils {
    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class.getSimpleName());

    public static String getFirstLengthHexByte( int length ) {
        String firstByte = Integer.toHexString(length / 256);
        if (firstByte.length() == 1) {
            firstByte = "0" + firstByte;
        } else if (firstByte.length() < 1) {
            firstByte = "00";
        }
        return firstByte;
    }

    public static String getTransactionDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHH");
        return simpleDateFormat.format(new Date());
    }

    public static String get2ndLengthHexByte( int length ) {
        String secondByte = Integer.toHexString(length % 256);
        if (secondByte.length() == 1) {
            secondByte = "0" + secondByte;
        } else if (secondByte.length() < 1) {
            secondByte = "00";
        }
        return secondByte;
    }

    public static byte[] hexStringToByteArray( String hex ) {

        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex( byte[] bytes ) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String addPadding( String padCharacter, boolean leftPad, int requiredLength, String value ) {
        String input = StringUtils.defaultString(value).trim();
        if (leftPad)
            return StringUtils.leftPad(input, requiredLength, padCharacter);
        else
            return StringUtils.rightPad(input, requiredLength, padCharacter);
    }

    //    public static IBFTTitleFetchRequest objectMapped( String json ) {
//        IBFTTitleFetchRequest titleFetchRequestTarget = new IBFTTitleFetchRequest();
//        ObjectMapper om = new ObjectMapper();
//        try {
//            titleFetchRequestTarget = om.readValue(json, IBFTTitleFetchRequest.class);
//            logger.info("PAN:  " + titleFetchRequestTarget.getPan());
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return titleFetchRequestTarget;
//    }
//
//
//    public static IbftAdviceRqst objectMappedAdviceRqst( String json ) {
//        IbftAdviceRqst adviceRqst = new IbftAdviceRqst();
//        ObjectMapper om = new ObjectMapper();
//        try {
//            adviceRqst = om.readValue(json, IbftAdviceRqst.class);
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return adviceRqst;
//    }
//
//    public static IBFTTitleFetchJSResponse objectMappedJSResponse( String json ) {
//        IBFTTitleFetchJSResponse fetchJSResponse = new IBFTTitleFetchJSResponse();
//        ObjectMapper om = new ObjectMapper();
//        try {
//            fetchJSResponse = om.readValue(json, IBFTTitleFetchJSResponse.class);
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return fetchJSResponse;
//    }
//
    public static OTIBFTTitleFetchRequest objectMappedTitleFetchRequest( String json ) {
        OTIBFTTitleFetchRequest ibfTitleFetchRequest = new OTIBFTTitleFetchRequest();
        ObjectMapper om = new ObjectMapper();
        try {
            ibfTitleFetchRequest = om.readValue(json, OTIBFTTitleFetchRequest.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ibfTitleFetchRequest;
    }

    public static String generateStan() {
        String OTP = "";
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        for (int i = 0; i < 6; i++) {
            OTP += numbers.get(i).toString();
        }
        return OTP;
    }

}