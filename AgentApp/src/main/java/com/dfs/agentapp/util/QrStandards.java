package com.dfs.agentapp.util;

public enum QrStandards {
    PAYLOAD_FORMAT_INDICATOR_PTP("00","02","02"),
    PAYLOAD_FORMAT_INDICATOR_PTM("00","02","01"),
    POINT_OF_INITIATION_STATIC("01","02","12"),
    POINT_OF_INITIATION_DYNAMIC("01","02","11"),
    PERSON_02("02","02","00"),
    PERSON_IBAN("04","24","PK72...."),
    PERSON_AMOUNT("05","..","..."),
    PERSON_DUE_DATE_TIME("07","12","011220252359"),
    AMOUNT_AFTER_DUE_DATE("08","..","..."),
    PERSON_CRC("10","04","12B3"),
    CEDITOR_INSTITUTION_BIC("01","08","HABBPKKA"),
    MERCHANT_IBAN("02","24",""),
    MERCHANT_CATEGORY_CODE("52","04","5999"),
    CURRENCY("53","03","586"),
    COUNTRY_CODE("58","02","PK"),
    MERCHANT_NAME("59","..","..."),
    MERCHANT_CITY("60","..","..."),
    ADDITIONAL_62("62","..","..."),
    ADD_62_BILL_NUMBER("01","..","...."),
    ADD_62_MOBILE_NUMBER("02","11","03240000000"),
    ADD_62_STORE_NAME("03","..","..."),
    ADD_62_REFERENCE_LABEL("05","..","..."),
    ADD_62_TERMINAL_LABLE("07","12","456789098765"),
    ADD_62_DUE_DATE("50","08","01122024"),
    ADD_62_AMOUNT_AFTER_DUE_DATE("51","..","250"),
    CRC("63","04","000C");
    private final String code;
    private final String length;
    private final String value;

    QrStandards(String code, String length, String value) {
        this.code = code;
        this.length = length;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getLength() {
        return length;
    }

    public String getValue() {
        return value;
    }
}
