package com.dfs.backoffice.utils;


public interface JwtConstants {


    public static final String ALGORITHM = "AES";
    public static final String PADDING_CBC = "AES/CBC/PKCS5PADDING";
    public static final String AES_SECRET_KEY = "ais-567890123456789012345678-256";
    public static final String JWT_SUPRESS_WARNING="java:S3329";
    public static final String IV_PARAM="1234567890123456";
    public static final String ISSUERS="dfs.com";

    // claims Constants
    public static final String USER_ID = "userId";
    public static final String PASSWORD_UPDATE_FLAG = "pwdUpdateFlag";
    public static final String USER_LOGIN_HISTORY_ID = "userLoginHistoryId";
    public static final String APP_USER_ID="appUserId";
    public static final String CUSTOMER_ID="customerId";
    public static final String MOBILE_NUMBER ="mobileNumber" ;
    public static final String UUID = "uuid";
}