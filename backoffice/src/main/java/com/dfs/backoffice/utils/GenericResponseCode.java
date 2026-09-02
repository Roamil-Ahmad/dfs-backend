package com.dfs.backoffice.utils;

public enum GenericResponseCode {
    JSON_PARSE_EXCEPTION("501", "Json Not Valid"),
    VALIDATION_ERROR("111", "Validation Error"),
    GENERAL_PROCESSING_ERROR("119", "Facing Some Technical Issue. Try Again Later"),
    OTP_TYPE_NOT_FOUND("401", "OTP TYPE NOT FOUND"),
    USER_NOT_FOUND("402", "USER_NOT_FOUND"),
    SUCCESS("000", "SUCCESS"),
    FAILED_TO_GENERATE_OTP("115", "FAILED TO GENERATE OTP"),
    OTP_NOT_FOUND("403", "OTP NOT FOUND"),
    OTP_LIMIT_EXCEEDS("116", "Otp Limit Exceeds"),
    OTP_EXPIRED("117", "OTP EXPIRED"),
    OTP_ALREADY_VERIFIED("118", "OTP ALREADY VERIFIED"),
    WRONG_OTP("119", "Wrong Otp Please Enter Correct "),
    FAILED_TO_VERIFY_OTP("120", "Failed to Verify Otp"),
    TECHNICAL_ISSUE("119", "We Are Facing Some Technical Issue. Please Try Again Later"),
    FAILED_TO_CREATE_SESSION("120", "Failed to Create Session Id Try Again Later "),
    INVALID_USERNAME_OR_PASSWORD("124", "INVALID USERNAME OR PASSWORD"),
    ACCOUNT_NOT_FOUND("1031", "Account Not Found"),
    CUSTOMER_NOT_FOUND("1030", "Customer not Found."),
    RECORD_NOT_FOUND("407", "RECORD NOT FOUND"),
    RECORD_FOUND("138", "RECORD FOUND"),
    RECORD_UPDATED("139", "RECORD UPDATED"),
    RECORD_NOT_UPDATED("408", "RECORD NOT UPDATED"),
    RECORD_NOT_SAVED("141", "RECORD NOT SAVED"),
    RECORD_ALREADY_EXIST("147", "RECORD ALREADY EXISTS"),
    AGENT_CNIC_MOBILE_ALREADY_REG("140", "Agent Cnic/Mobile No. Already Registered"),
    AGENT_BLACKLISTED("142", "Agent is Blacklisted"),
    USER_BLACKLISTED("143", "User is Blacklisted"),
    USER_NID_REGISTERED("144", "User NidNo Already Registered"),
    USER_NAME_EXIST("145", "User Name Already Exists"),
    EMAIL_ALREADY_EXIST("145", "Email Already Exists"),
    INVALID_AUTH_TYPE("444", "INVALID AUTH TYPE"),
    SESSION_EXPIRED("445", "Session Expired"),
    INTERNAL_ERROR("1027", "Internal Error."),
    PROVINCE_NOT_FOUND("447", "Province Not Found"),
    DISTRICT_NOT_FOUND("457","District Not Found" ),
    DAY_MUST_BE_WITHIN_THIRTY_DAYS("133", "DAY MUST BE WITHIN THIRTY DAYS");

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
