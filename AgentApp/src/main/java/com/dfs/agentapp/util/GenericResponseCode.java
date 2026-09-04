package com.dfs.agentapp.util;

public enum GenericResponseCode {
    JSON_PARSE_EXCEPTION("501", "Json Not Valid"),
    VALIDATION_ERROR("111", "Validation Error"),
    MESSAGE_NOT_FOUND_AGAINST_CODE("112", "Message Not Found Against Code : "),
    CUSTOM_MESSAGE("113", ""),
    OTP_TYPE_NOT_FOUND("401", "OTP TYPE NOT FOUND"),
    USER_NOT_FOUND("402", "USER_NOT_FOUND"),
    OTP_ALREADY_SENT("114", "OTP ALREADY SENT"),
    SUCCESS("000", "SUCCESS"),
    FAILED_TO_GENERATE_OTP("115", "FAILED TO GENERATE OTP"),
    OTP_NOT_FOUND("403", "OTP NOT FOUND"),
    OTP_LIMIT_EXCEEDS("116", "Otp Limit Exceeds"),
    OTP_EXPIRED("117", "OTP EXPIRED"),
    OTP_ALREADY_VERIFIED("118", "OTP ALREADY VERIFIED"),
    WRONG_OTP("119", "Wrong Otp Please Enter Correct "),
    FAILED_TO_VERIFY_OTP("120", "Failed to Verify Otp"),

    MPIN_CAHANGE_LIMIT_EXCEEDED("155", "MPIN Change Limit Exceeded"),
    FRIEND_INVITE_ALREADY_SEND("156", "Friend invite already sent"),
    TECHNICAL_ISSUE("151", "Technical Issue"),
    FAILED_TO_CREATE_SESSION("152", "Failed to Create Session Id Try Again Later "),
    DEVICE_NOT_VERIFIED("121", "Device Not Verified"),
    DEVICE_AUTHENTICATION_FAILED("404", "Device Authentication Failed"),
    BAD_REQUEST("405", "Request Not Valid"),
    INVALID_TOKEN("406", "Invalid Token"),
    CONFIRM_PIN_NOT_MATCHED("122", "Confirm Pin Not Matched"),
    DEVICE_INVALID("123", "Device Verification Failed Please Contact Call Center for Device Verification"),
    INVALID_PASSWORD("124", "INVALID PASSWORD"),
    ACCOUNT_NOT_FOUND("125", "Account Not Found"),
    ACCOUNT_ALEADY_UPDATED("135", "Account Already Updated"),
    SIGN_UP_FIRST("126", "No Device Registerd Against This Username"),
    WRONG_MPIN("127", "Wrong Mpin"),
    ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_NUMBER("128", "Account Already Exists Against This Number"),
    ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_CNIC_OR_MOBILE("149", "Account Already Exists Against This Cnic or Mobile"),

    ACCOUNT_LEVEL_NOT_FOUND("129", "Account Level Not Found"),
    FILE_IS_INFECTED("130", "File Is Infected"),
    DOCUMENT_TYPE_NOT_FOUND("131", "Document Type Not Found"),
    FAILED_TO_UPLOAD_DOC("132", "Failed to Upload Document"),
    RECORD_NOT_FOUND("409", "Record Not Found"),
    INVALID_AUTH_TYPE("444", "INVALID AUTH TYPE"),
    SESSION_EXPIRED("445", "Session Expired"),
    DAY_MUST_BE_WITHIN_THIRTY_DAYS("133", "DAY MUST BE WITHIN 180  DAYS"),
    INVALID_QR_TYPE("446", "INVALID QR TYPE"),
    PROVINCE_NOT_FOUND("447", "Province Not Found"),
    SOURCE_OF_INCOME_NOT_FOUND("448", "Source of Income Not Found"),
    OCCUPATION_NOT_FOUND("449", "Occupation Not Found"),
    PURPOSE_OF_ACCOUNT_NOT_FOUND("450", "Purpose of Account Not Found"),
    NID_FRONT_NOT_FOUND("451", "NidNo Front Not Found"),
    NID_BACK_NOT_FOUND("452", "NidNo Back Not Found"),
    PROOF_OF_INCOME_NOT_FOUND("453", "Source of Income Not Found"),
    SELFIE_IMAGE_NOT_FOUND("454", "Selfie Image Not Found"),
    PROOF_OF_ADDRESS_NOT_FOUND("455", "Proof Of Address"),

    INVALID_STEP("456", "INVALID STEP"),
    ACCOUNT_PARKED_FOR_APPROVAL("001", "Account Parked For Approval"),

    ACCOUNT_ALREADY_PARKED_FOR_APPROVAL("002", "ACCOUNT ALREADY PARK FOR APPROVAL"),

    INVALID_USERNAME("157", "INVALID USERNAME"),
    USER_IS_LOCKED("158", "Your account is blocked. Remaining hours: %d hour(s) %d minute(s)"),

    BLACKLISTED("150", "BLACKLISTED IN OFAC"),

    DISTRICT_NOT_FOUND("457", "District Not Found"),
    ACCOUNT_UPDATED_SUCESSFULLY("003", "Account Updated Successfully"),

    EMAIL_ALREADY_EXISTS("146", "Email Already Exists"),
    DEBIT_CARD_EXIST("160", "Debit Card Already Exists"),
    SAME_MPIN("159", "Current and new MPIN cannot be same"),
    RECORD_ALREADY_EXISTS("147", "RECORD ALREADY EXISTS"),
    RECORD_NOT_SAVED("141", "RECORD NOT SAVED"),
    LICENSE_NOT_FOUND("161", "License Not Found"),
    BUSINESS_TYPE_NOT_FOUND("162", "Business Type Not Found"),
    PARENT_AGENT_IS_CHILD_AGENT("163", "A child agent cannot be assigned as a parent agent.");

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
