package com.wallet.transaction.switching.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The switch rejects a message whose data elements are the wrong length, so these widths are the
 * difference between a transaction going through and being refused before it leaves the gateway.
 */
class OneLinkFieldFormatterTest {

    private static final String DFS_BIN = "958214";

    @Test
    @DisplayName("an 11-digit mobile account becomes a 16-digit PAN behind the DFS BIN")
    void mobileAccountBecomesSixteenDigitPan() {
        String pan = OneLinkFieldFormatter.pan(DFS_BIN, "03006088659");

        assertThat(pan).hasSize(16);
        assertThat(pan).isEqualTo("9582143006088659");
        assertThat(pan).startsWith(DFS_BIN);
    }

    @Test
    @DisplayName("the leading zero of the mobile number is the digit that gets dropped")
    void leadingZeroIsDropped() {
        assertThat(OneLinkFieldFormatter.pan(DFS_BIN, "03035847496")).isEqualTo("9582143035847496");
    }

    @Test
    @DisplayName("a shorter account is left padded rather than shifting the BIN")
    void shortAccountIsLeftPadded() {
        String pan = OneLinkFieldFormatter.pan(DFS_BIN, "12345");

        assertThat(pan).hasSize(16);
        assertThat(pan).isEqualTo("9582140000012345");
    }

    @Test
    @DisplayName("a formatted or spaced account still produces digits only")
    void nonDigitsAreIgnored() {
        assertThat(OneLinkFieldFormatter.pan(DFS_BIN, " 0300-608-8659 ")).isEqualTo("9582143006088659");
    }

    @Test
    @DisplayName("no account means no PAN, rather than a PAN made of padding")
    void missingAccountGivesNull() {
        assertThat(OneLinkFieldFormatter.pan(DFS_BIN, null)).isNull();
        assertThat(OneLinkFieldFormatter.pan(DFS_BIN, "   ")).isNull();
    }

    @Test
    @DisplayName("the purpose of payment is padded to the 44 characters the switch expects")
    void purposeOfPaymentIsPaddedTo44() {
        String purpose = OneLinkFieldFormatter.purposeOfPayment("0401");

        assertThat(purpose).hasSize(44);
        assertThat(purpose).startsWith("0401");
        assertThat(purpose.substring(4)).isBlank();
    }

    @Test
    @DisplayName("an over-long purpose of payment is truncated, not rejected on the wire")
    void purposeOfPaymentIsTruncated() {
        String purpose = OneLinkFieldFormatter.purposeOfPayment("0401 " + "X".repeat(80));

        assertThat(purpose).hasSize(44);
    }

    @Test
    @DisplayName("card acceptor name and location is exactly the 40 characters DE-43 carries")
    void fixedWidthPadsAndTruncates() {
        assertThat(OneLinkFieldFormatter.fixedWidth("DFS", 40)).hasSize(40);
        assertThat(OneLinkFieldFormatter.fixedWidth("X".repeat(60), 40)).hasSize(40);
        assertThat(OneLinkFieldFormatter.fixedWidth(null, 40)).hasSize(40);
    }

    @Test
    @DisplayName("atMost trims to the limit without padding short values")
    void atMostTrimsWithoutPadding() {
        assertThat(OneLinkFieldFormatter.atMost("KHUSHHALI BANK", 20)).isEqualTo("KHUSHHALI BANK");
        assertThat(OneLinkFieldFormatter.atMost("A".repeat(40), 20)).hasSize(20);
        assertThat(OneLinkFieldFormatter.atMost(null, 20)).isEmpty();
    }
}
