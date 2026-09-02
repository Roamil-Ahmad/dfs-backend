package com.dfs.switchsimulator.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The bill sub-fields have to survive the round trip through DE-120 unchanged, because the biller's
 * answer is what the customer is shown before they pay.
 */
class BillRecordDataLayoutTest {

    @Test
    @DisplayName("the record data is exactly 91 characters")
    void recordDataHasTheFixedLength() {
        String recordData = BillRecordDataLayout.build("KEL", "K-ELECTRIC", "0400123456789",
                "1500.00", "20260901", BillRecordDataLayout.STATUS_UNPAID);

        assertThat(recordData).hasSize(BillRecordDataLayout.BILL_LENGTH);
    }

    @Test
    @DisplayName("every sub-field reads back exactly as it was written")
    void subFieldsRoundTrip() {
        String recordData = BillRecordDataLayout.build("KEL", "K-ELECTRIC", "0400123456789",
                "1500.00", "20260901", BillRecordDataLayout.STATUS_UNPAID);

        assertThat(BillRecordDataLayout.companyCode(recordData)).isEqualTo("KEL");
        assertThat(BillRecordDataLayout.companyName(recordData)).isEqualTo("K-ELECTRIC");
        assertThat(BillRecordDataLayout.consumerNo(recordData)).isEqualTo("0400123456789");
        assertThat(BillRecordDataLayout.billAmount(recordData)).isEqualTo("1500.00");
        assertThat(BillRecordDataLayout.dueDate(recordData)).isEqualTo("20260901");
        assertThat(BillRecordDataLayout.status(recordData)).isEqualTo("U");
    }

    @Test
    @DisplayName("the amount travels as 12 zero-padded minor units")
    void amountIsMinorUnits() {
        String recordData = BillRecordDataLayout.build("KEL", "K-ELECTRIC", "1", "1500.00",
                "20260901", BillRecordDataLayout.STATUS_UNPAID);

        assertThat(recordData.substring(70, 82)).isEqualTo("000000150000");
    }

    @Test
    @DisplayName("an amount without decimals is still read back with them")
    void wholeAmountRoundTrips() {
        String recordData = BillRecordDataLayout.build("KEL", "K-ELECTRIC", "1", "1500",
                "20260901", BillRecordDataLayout.STATUS_UNPAID);

        assertThat(recordData.substring(70, 82)).isEqualTo("000000150000");
        assertThat(BillRecordDataLayout.billAmount(recordData)).isEqualTo("1500.00");
    }

    @Test
    @DisplayName("a single decimal place is padded rather than shifting the amount by ten")
    void singleDecimalIsPadded() {
        String recordData = BillRecordDataLayout.build("KEL", "K-ELECTRIC", "1", "1500.5",
                "20260901", BillRecordDataLayout.STATUS_UNPAID);

        assertThat(BillRecordDataLayout.billAmount(recordData)).isEqualTo("1500.50");
    }

    @Test
    @DisplayName("an over-long company name is truncated rather than shifting later sub-fields")
    void longValuesDoNotShiftTheLayout() {
        String recordData = BillRecordDataLayout.build("KEL", "A".repeat(60), "0400123456789",
                "1500.00", "20260901", BillRecordDataLayout.STATUS_PAID);

        assertThat(recordData).hasSize(BillRecordDataLayout.BILL_LENGTH);
        assertThat(BillRecordDataLayout.consumerNo(recordData)).isEqualTo("0400123456789");
        assertThat(BillRecordDataLayout.status(recordData)).isEqualTo("P");
    }

    @Test
    @DisplayName("missing values produce a well-formed record rather than a short one")
    void nullsAreTolerated() {
        String recordData = BillRecordDataLayout.build(null, null, null, null, null, null);

        assertThat(recordData).hasSize(BillRecordDataLayout.BILL_LENGTH);
        assertThat(BillRecordDataLayout.companyCode(recordData)).isEmpty();
        assertThat(BillRecordDataLayout.billAmount(recordData)).isEqualTo("0.00");
    }

    @Test
    @DisplayName("reading a truncated or absent record does not blow up")
    void readingIsBoundsSafe() {
        assertThat(BillRecordDataLayout.companyCode(null)).isEmpty();
        assertThat(BillRecordDataLayout.status("KEL")).isEmpty();
        assertThat(BillRecordDataLayout.billAmount("short")).isEmpty();
    }
}
