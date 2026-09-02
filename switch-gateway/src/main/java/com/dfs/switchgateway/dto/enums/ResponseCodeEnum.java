package com.dfs.switchgateway.dto.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * An enum that will have all Response Codes from Phoenix Server. According to 7.1.17 Response Code.
 * It also give the flexibility of reverse enum lookup i.e. if you give value(response code) it can
 * return enum.
 */
public enum ResponseCodeEnum {

    PROCESSED_OK("00"),
    LIMIT_EXCEEDED("01"),
    ACCOUNT_NOT_EXIST("02"),
    ACCOUNT_INACTIVE("03"),
    LOW_BALANCE("04"),
    PAN__MISSING("05"),
    IMD_MISSING("06"),
    INCORRECT_CARD_DATA("07"),
    CARD_RECORD_NOT_FOUND("08"),
    FIELD_ERROR("09"),
    DUPLICATE_TRANSACTION("10"),
    BAD_TRANSACTION_CODE("11"),
    CARD_STATUS_ERROR("12"),
    SYSTEM_ERROR("13"),
    WARM_CARD("14"),
    HOT_CARD("15"),
    BAD_CARD_STATUS("16"),
    UNKNOWN_AUTHORIZATION_MODE("17"),
    ERROR_IN_TRANSACTION_DATE("18"),
    ERROR_IN_CURRENCY_CODE("19"),
    TRANSACTION_NOT_ALLOWED_ON_THIS_IMD("20"),
    TRANSACTION_NOT_ALLOWED_ON_THIS_ACCOUNT("21"),
    BAD_CARD_CYCLE_DATE("22"),
    BAD_CARD_CYCLE_LENGTH("23"),
    INCORRECT_PIN("24"),
    SYSTEM_ERROR1("25"),
    SYSTEM_ERROR2("26"),
    SYSTEM_ERROR3("27"),
    NO_ACCOUNT_LINKED("28"),
    SYSTEM_ERROR4("29"),
    ORIGINAL_TRANSACTION_NOT_FOUND("30"),
    SYSTEM_ERROR5("31"),
    SYSTEM_ERROR6("32"),
    SYSTEM_ERROR7("33"),
    ORIGINAL_TRANSACTION_NOT_AUTHORIZED("34"),
    ORIGINAL_TRANSACTION_ALREADY_REVERSED("35"),
    ACQUIRER_REVERSAL("36"),
    SYSTEM_ERROR8("37"),
    TRANSACTION_CODE_MISMATCHED("38"),
    BAD_TRANSACTION_TYPE("39"),
    SYSTEM_ERROR9("40"),
    EXPIRY_DATE_MISMATCHED("41"),
    TRACK_2_DATA_MISMATCHED("45"),
    SYSTEM_ERROR46("46"),
    ERROR_IN_CURRENCY_CONVERSION("47"),
    BAD_AMOUNT("48"),
    SYSTEM_ERROR49("49"),
    HOST_STATUS_UNKNOWN("50"),
    HOST_NOT_PROCESSING("51"),
    HOST_BUSY52("52"),
    HOST_BUSY53("53"),
    HOST_BUSY("54"),
    HOST_LINK_DOWN("55"),
    TRANSACTION_SENT_TO_HOST("56"),
    SYSTEM_ERROR57("57"),
    TRANSACTION_TIMED_OUT("58"),
    TRANSACTION_REJECTED_BY_HOST("59"),
    PIN_RETRIES_EXHAUSTED("60"),
    HSM_NOT_RESPONDING("61"),
    HOST_OFFLINE("62"),
    DESTINATION_NOT_FOUND("63"),
    DESTINATION_NOT_REGISTERED("64"),
    CASH_TRANSACTION_NOT_ALLOWED("65"),
    NO_TRANSACTION_NOT_ALLOWED("66"),
    INVALID_ACCOUNT_STATUS("67"),
    INVALID_TO_ACCOUNT("68"),
    SYSTEM_ERROR69("69"),
    REFUSED_IMD("70"),
    SYSTEM_ERROR71("71"),
    CURRENCY_NOT_ALLOWED("72"),
    SYSTEM_ERROR73("73"),
    TRANSACTION_SOURCE_NOT_ALLOWED("74"),
    UNKNOWN_TRANSACTION_SOURCE("75"),
    MANUAL_ENTRY_NOT_ALLOWED("76"),
    REFER_TO_ISSUER("77"),
    INVALID_MERCHANT("78"),
    HONOR_WITH_ID("79"),
    MESSAGE_FORMAT_ERROR("80"),
    SECURITY_VIOLATION("81"),
    SYSTEM_ERROR82("82"),
    SYSTEM_ERROR83("83"),
    SYSTEM_ERROR84("84"),
    TRANSACTION_FREQUENCY_EXCEEDED("85"),
    INCORRECT_PIN_LENGTH("86"),
    ERROR_IN_CASH_RETRACT("87"),
    FAULTY_DISPENSE("88"),
    SHORT_DISPENSE("89"),
    CUSTOMER_RECORD_NOT_FOUND("90"),
    ISSUER_REVERSAL("91"),
    ACCOUNT_LOCKED("92"),
    PIN_EXPIRED("93"),
    PERMISSION_DENIED("94"),
    TRANSACTION_REJECTED("95"),
    ORIGINAL_TRANSACTION_REJECTED("96"),
    BAD_EXPIRY_DATE("97"),
    ORIGINAL_AMOUNT_INCORRECT("98"),
    ORIGINAL_DATA_ELEMENT_MISMATCH("99");
    // @formatter:on
    /**
     * A Map to hold all Enum values used for reverse lookup.
     */
    private static final Map<String, ResponseCodeEnum> lookup = new HashMap<String, ResponseCodeEnum>();

    /**
     * The static block to populate the Map uses a specialized implementation of Set, java.util.EnumSet,
     * that "probably" (according to the javadocs) has better performance than java.util.HashSet.
     * Java 5.0 also provides java.util.EnumMap, a specialized implementation of
     * Map for enumerations that is more compact than java.util.HashMap.
     */
    static {
        for (ResponseCodeEnum responseCodeEnum : EnumSet.allOf(ResponseCodeEnum.class))
            lookup.put(responseCodeEnum.getValue(), responseCodeEnum);
    }

    ResponseCodeEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    /**
     * The static get(int) method here provides the reverse lookup by simply
     * getting the value from the Map,
     * @return ResponseCodeEnum
     */
    public static ResponseCodeEnum lookup(String code) {
        return lookup.get(code);
    }
}
