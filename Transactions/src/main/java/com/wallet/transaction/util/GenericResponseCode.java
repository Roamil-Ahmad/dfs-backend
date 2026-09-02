package com.wallet.transaction.util;

public enum GenericResponseCode {
    JSON_PARSE_EXCEPTION("501", "Json Not Valid"),
    VALIDATION_ERROR("111", "Validation Error"),
    MESSAGE_NOT_FOUND_AGAINST_CODE("112", "Message Not Found Against Code : "),
    CUSTOM_MESSAGE("113", ""),
    DEVICE_AUTHENTICATION_FAILED("404", "Device Authentication Failed"),
    BAD_REQUEST("405", "Request Not Valid"),
    INVALID_TOKEN("406", "Invalid Token"),
    INVALID_PIN("124", "Invalid Pin or Password"),
    RECORD_NOT_FOUND("409", "Record Not Found"),
    INVALID_ACCOUNT_INFO("134", "Invalid Account Information Provided"),
    INVALID_AUTH_TYPE("444", "INVALID AUTH TYPE"),
    SUCCESS("000","SUCCESS"),
    TECHNICAL_ISSUE("151","Technical Issue"),
    SESSION_EXPIRED("445", "Session Expired"),
    WRONG_OTP("119","Wrong Otp Please Enter Correct "),
    RECORD_ALREADY_EXISTS("147","Record Already Exists"),

    INVALID_ACCOUNT("1031", "Account Not Found");


    private final String responseCode;
    private final String responseMessage;


    GenericResponseCode(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
