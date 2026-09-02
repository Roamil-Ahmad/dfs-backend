package com.dfs.thirdparties.util;

public enum GenericResponseCode {
    JSON_PARSE_EXCEPTION("501","Json Not Valid"),
    VALIDATION_ERROR("111","Validation Error"),
    MESSAGE_NOT_FOUND_AGAINST_CODE("112","Message Not Found Against Code : "),
    CUSTOM_MESSAGE("113",""),
    OTP_TYPE_NOT_FOUND("401","OTP TYPE NOT FOUND"),
    USER_NOT_FOUND("402","USER_NOT_FOUND"),
    OTP_ALREADY_SENT("114","OTP ALREADY SENT"),
    SUCCESS("000","SUCCESS"),
    FAILED_TO_GENERATE_OTP("115","FAILED TO GENERATE OTP"),
    OTP_NOT_FOUND("403","OTP NOT FOUND"),
    OTP_LIMIT_EXCEEDS("116","Otp Limit Exceeds"),
    OTP_EXPIRED("117","OTP EXPIRED"),
    OTP_ALREADY_VERIFIED("118","OTP ALREADY VERIFIED"),
    WRONG_OTP("119","Wrong Otp Please Enter Correct "),
    FAILED_TO_VERIFY_OTP("120","Failed to Verify Otp"),
    TECHNICAL_ISSUE("151","Technical Issue"),
    FAILED_TO_CREATE_SESSION("152","Failed to Create Session Id Try Again Later "),
    DEVICE_NOT_VERIFIED("121","Device Not Verified"),
    DEVICE_AUTHENTICATION_FAILED("404","Device Authentication Failed"),
    BAD_REQUEST("405","Request Not Valid"),
    INVALID_TOKEN("406","Invalid Token"),
    CONFIRM_PIN_NOT_MATCHED("122","Confirm Pin Not Matched"),
    DEVICE_INVALID("123","Device Verification Failed Please Contact Call Center for Device Verification"),
    INVALID_USERNAME_OR_PASSWORD("124","INVALID USERNAME OR PASSWORD"),
    ACCOUNT_NOT_FOUND("125","Account Not Found"),
    SIGN_UP_FIRST("126","No Device Registerd Against This Username"),
    WRONG_MPIN("127","Wrong Mpin"),
    ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_NUMBER("128","Account Already Exists Against This Number"),
    ACCOUNT_LEVEL_NOT_FOUNT("129","Account Level Not Found"),
    FILE_IS_INFECTED("130","File Is Infected"),
    DOCUMENT_TYPE_NOT_FOUND("131","Document Type Not Found"),
    FAILED_TO_UPLOAD_DOC("132","Failed to Upload Document"),
    RECORD_NOT_FOUND("222","Record Not Found"),
    INVALID_AUTH_TYPE("444","INVALID AUTH TYPE"),
    SESSION_EXPIRED("445","Session Expired"),
    DAY_MUST_BE_WITHIN_THIRTY_DAYS("133","DAY MUST BE WITHIN 180  DAYS"),
    INVALID_QR_TYPE("446","INVALID QR TYPE");


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
