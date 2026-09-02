package com.dfs.switchsimulator.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Locks the DE-120 layout to "1LINK ISO8583 Message Format - Data Element Definitions v7.0",
 * section 9.62.1.1. If a field width or offset ever drifts, these fail.
 */
class RecordDataLayoutTest {

    @Test
    @DisplayName("advice record data is exactly 324 characters, the sum of the sixteen sub-fields")
    void adviceRecordDataIs324Characters() {
        String recordData = RecordDataLayout.buildAdviceRecordData(
                "PK65KHYB0001002000908196", "ZEESHAN AHMED", "999982", "221166", "C",
                "KHUSHHALI BANK", "MAIN BRANCH", "AMMAR KAZMI",
                "03035847496", "PK65KHYB0001002000908196",
                "3720374772689", "4250105101451", "0586", "DFS PAKISTAN");

        assertThat(recordData).hasSize(324);
        assertThat(20 + 30 + 11 + 11 + 1 + 20 + 25 + 30 + 24 + 24 + 30 + 24 + 30 + 15 + 4 + 25).isEqualTo(324);
    }

    @Test
    @DisplayName("title fetch record data is exactly 73 characters, the first five sub-fields")
    void titleFetchRecordDataIs73Characters() {
        String recordData = RecordDataLayout.buildTitleFetchRecordData(
                "PK65KHYB0001002000908196", "999982", "221166", "C");

        assertThat(recordData).hasSize(73);
        assertThat(20 + 30 + 11 + 11 + 1).isEqualTo(73);
    }

    @Test
    @DisplayName("every sub-field is written at the offset the spec defines and reads back unchanged")
    void subFieldsRoundTripAtSpecOffsets() {
        String recordData = RecordDataLayout.buildAdviceRecordData(
                "PK65KHYB0001002000908196", "ZEESHAN AHMED", "999982", "221166", "C",
                "KHUSHHALI BANK", "MAIN BRANCH", "AMMAR KAZMI",
                "03035847496", "PK65KHYB0001002000908196",
                "3720374772689", "4250105101451", "0586", "DFS PAKISTAN");

        assertThat(RecordDataLayout.beneficiaryTitle(recordData)).isEqualTo("ZEESHAN AHMED");
        assertThat(RecordDataLayout.sourceImd(recordData)).isEqualTo("99998200000");
        assertThat(RecordDataLayout.destinationImd(recordData)).isEqualTo("22116600000");
        assertThat(RecordDataLayout.identifier(recordData)).isEqualTo("C");
        assertThat(RecordDataLayout.beneficiaryId(recordData)).isEqualTo("4250105101451");
    }

    @Test
    @DisplayName("a 24-character IBAN is narrowed to its 16-digit account part for the 20-char field")
    void ibanIsNarrowedToItsAccountPart() {
        String field = RecordDataLayout.toAccountField("PK65KHYB0001002000908196");

        assertThat(field).hasSize(20);
        assertThat(field.trim()).isEqualTo("0001002000908196");
    }

    @Test
    @DisplayName("a short account number is padded, not truncated")
    void shortAccountIsPadded() {
        String field = RecordDataLayout.toAccountField("03035847496");

        assertThat(field).hasSize(20);
        assertThat(field.trim()).isEqualTo("03035847496");
    }

    @Test
    @DisplayName("IMD sub-fields are zero padded to eleven, as 1LINK requires")
    void imdIsZeroPaddedToEleven() {
        String recordData = RecordDataLayout.buildTitleFetchRecordData("03035847496", "999982", "221166", "C");

        assertThat(recordData.substring(50, 61)).isEqualTo("99998200000");
        assertThat(recordData.substring(61, 72)).isEqualTo("22116600000");
    }

    @Test
    @DisplayName("reading a sub-field past the end of a short record returns empty, never an exception")
    void readingPastTheEndIsSafe() {
        assertThat(RecordDataLayout.beneficiaryId("too short")).isEmpty();
        assertThat(RecordDataLayout.beneficiaryTitle(null)).isEmpty();
        assertThat(RecordDataLayout.sourceImd("")).isEmpty();
    }

    @Test
    @DisplayName("null sub-field values become spaces rather than the literal \"null\"")
    void nullValuesBecomeSpaces() {
        String recordData = RecordDataLayout.buildAdviceRecordData(
                "03035847496", null, "999982", "221166", "C",
                null, null, null, null, null, null, null, null, null);

        assertThat(recordData).hasSize(324);
        assertThat(recordData).doesNotContain("null");
    }
}
