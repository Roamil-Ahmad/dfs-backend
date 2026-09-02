package com.dfs.switchgateway.utils;

/*Author Name: abdul.fatah
Project Name: switch-gateway

Package Name: com.dfs.switchgateway.utils

Interface Name: Constant

Date and Time:5/29/2023 5:46 PM

Version:1.0
*/
public interface Constant {
    /*
     * Verification Status Codes Default
     */
    String SUCCESS_CODE = "200";
    String PROCESSOR_BAD_REQUEST = "503";
    String PROCESSOR_MESSAGE = "Service Unavailable";
    String SERVER_BAD_REQUEST = "500";
    String SERVER_MSG = "1Link server is disconnected!, Please contact with the concern person";
    String BAD_REQUEST_CODE = "400";
    String IBFT_TF_PROCESSING_CODE = "62";
    String IBFT_ADVICE_PROCESSING_CODE = "48";
    Integer ZERO = 0;
    String STAN_000000 = "00000";
    String LOGGING_LEVEL_INFO = "INFO";

    String LOGGING_LEVEL_EXE = "EXE";
    String LOGGING_LEVEL_WARN = "WARN";
    String LOGGING_LEVEL_DEBUG = "DEBUG";
    String SERVICE = "switch-gateway";
    String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.SSS";
    String IS_QUEUE_YES = "Y";
    String LIMIT_EXCEED = "Queue limit exceeded";
    String ADDED_IN_QUEUE = "Object Added in queue!";
    String REMOVED_IN_QUEUE = "Object Removed in queue! due expiry time exceeded";
    String TYPE_INCOMING = "Incoming";
    String TYPE_INCOMING_ADVICE = "Incoming Advice";
    String TYPE_INCOMING_TITLE_FETCH = "Incoming Title-Fetch";
    String TYPE_OUTGOING = "O";
    String QUEUE_OBJECT_RELEASE_AND_REMOVE = "Queue Object processed to [PROCESSOR]!";
    String QUEUE_OBJECT_EXPIRY_TIME = "The object request has time out";
    String FAILED = "failed";
    String SUCCESS = "success";
    String EXPIRY_TIME = "TIMEEXPIRED";
    boolean FALSE = false;
    String TYPE_I = "I";
    String TYPE_O = "O";
    String ISLAMABAD = "ISLAMABAD";
    String GULBURG_SLAMABAD = "GULBURG SLAMABAD ";
    String MISMATCH_FACTORY_CONDITION = "No matching class found for the provided message type.";
    String DUPLICATE_ADVICE = "Advice not processed due to duplicated transaction!";
    /*
     * Verification Status Codes
     */
    String VERIFICATION_FAILED_CODE = "404";
    String VERIFICATION_SUCCESS_CODE = "200";
    String NULL=null;

    /*
     * Verification Status Msg
     */
    String VERIFICATION_SUCCESS_MESSAGE = "DE: fields are verified!";
    String VERIFICATION_FAILED_MESSAGE = "DEs Fields are not retrieved from cache <> something went wrong!";
    String VERIFICATION_FAILED_ERROR = "Verification Failed!";


    /*
     * Transaction Response Messages.
     */
    String ECHO_RES_SUCCESS_MSG = "Response retrieved successfully [STAN]: ";
    String ECHO_RES_FAILED_MSG = "Response is not retrieved [STAN]: ";

    /*
     * Validation Fields
     */
    String PAN = "Primary Account Number (PAN)";
    String PROCESSING_CODE = "Processing Code";
    String TRANSACTION_AMOUNT = "Amount, Transaction";
    String SETTLEMENT_AMOUNT = "Amount, Settlement";
    String BILLING_AMOUNT = "Amount, Cardholder Billing";
    String TRANSMICCION_DATE_TIME = "Transmission Date and Time";
    String CONVERSATION_RATE_SETTLEMENT = "Conversion Rate, Settlement";
    String STAN = "System Trace Audit Number (STAN)";
    String TIME_LOCAL_TRANSACTION = "Time, Local Transaction";
    String DATE_LOCAL_TRANSACTION = "Date, Local Transaction";
    String DATE_EXPIRATION = "Date, Expiration";
    String DATE_SETTLEMENT = "Date, Settlement";
    String DATE_CONSERSATION = "Date, Conversion";
    String MERCHANT_TYPE = "Merchant Type";
    String POINT_OF_ENTYR_MODE = "Point-of-Service Entry Mode";
    String CARD_SEQUENCE_NUMBER = "Card Sequence Number";
    String NETWORK_INST_IDENTIFIER = "Network Institution Identifier";
    String POINT_OFSERVICE_CONDITION_CODE = "Point of Service Condition Code";
    String POINTOFSERVICE_PIN_CAPTURE_CODE = "Point of Service PIN Capture Code";
    String AMOUNT_TRANS_FEE = "Amount, Transaction Fee";
    String AMOUNT_TRANS_PROCESS_FEE = "Amount, Transaction Processing Fee";
    String ACQ_INST_IDEN_CODE = "Acquiring Institution Identification Code";
    String FORWARD_INST_IDEN_CODE = "Forwarding Institution Identification Code";
    String TRACK_2_DATA = "Track-2 Data";
    String TRACK_3_DATA = "Track-3 Data";
    String RETRIEVAL_REF_NUM = "Retrieval Reference Number";
    String AUTH_IDEN_RESP = "Authorization Identification Response";
    String RESPONSE_CODE = "Response Code";
    String CARD_ACCET_TERMINAL_IDEN = "Card Acceptor Terminal Identification";
    String CARD_ACCET_IDEN_CODE = "Card Acceptor Identification Code";
    String CARD_ACCET_NAME_LOCATION = "Card Acceptor Name and Location";
    String ADD_RESPONSE_DATA = "Additional Response Data";
    String TRACK_1_DATA = "Track-1 Data";
    String ADD_DATA_NATIONAL = "Additional Data (National)";
    String ADD_DATA = "Additional Data";
    String CURR_CODE_TRANS = "Currency Code, Transaction";
    String CURR_CODE_SETTLE = "Currency Code, Settlement";
    String PERSONAL_INDENTIFICATION_PIN_DATA = "Personal Identification Number (PIN) Data";
    String ADD_AMOUNT = "Additional Amounts";
    String ICC_REL_DATA = "ICC Related Data";
    String ISSURE_ADD_DATA_PRIVATE = "Issuer Additional Data - Private";
    String ADD_EMV_INFORMATION = "Additional EMV Information";
    String CARDHOLDER_AUTH_INFO = "Cardholder Authentication Information";
    String NET_MANAG_INFO_CODE = "Network Management Information Code";
    String ORIG_DATA_FORMAT = "Original Data Element";
    String REP_AMMOUNT = "Replacement Amounts";
    String RECE_INST_CODE = "Receiving Institution Code";
    String ACCOUNT_IDEN_1 = "Account Identification-1";
    String ACCOUNT_IDEN_2 = "Account Identification-2";
    String TRANS_DESC = "Transaction Description";
    String RES_FOR_NATION_USE_1 = "Reserved for National Use 1";
    String RES_FOR_NATION_USE_2 = "Reserved for National Use 2";
    String RES_FOR_NATION_USE_3 = "Reserved for National Use 3";
    String RES_FOR_NATION_USE_4 = "Reserved for National Use 4";
    String RES_FOR_NATION_USE_5 = "Reserved for National Use 5";
    String RES_FOR_NATION_USE_6 = "Reserved for National Use 6";
    String RES_FOR_NATION_USE_7 = "Reserved for National Use 7";
    String RES_FOR_NATION_USE_8 = "Reserved for National Use 8";
    String RECORD_DATA = "Record Data";
    String VERIF_DATA = "Verification Data";
    String SUPPORTING_INFORMTION = "Supporting Information";
    String NEW_PIN = "New PIN Block Data";
    String unhandleException = null;
    String UNKNOWN_METHOD = "UnknownMethod";
    String AUTHORIZATION = "Authorization";
    String INTERNAL_SERVER_ERROR = "220";
    String HOST_LINK_DOWN ="55" ;
}
