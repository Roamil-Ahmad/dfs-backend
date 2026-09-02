package com.wallet.transaction.switching.billpayment;

import com.wallet.transaction.dto.common.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BillPaymentRequestValidatorTest {

    private Request envelope;

    @BeforeEach
    void setUp() {
        envelope = new Request();
        envelope.setChannel("MOBILE");
        envelope.setImieNo("TESTIMEI");
    }

    private BillPaymentRequest valid() {
        BillPaymentRequest request = new BillPaymentRequest();
        request.setFromAccountNo("03006088659");
        request.setFromAccountNid("3520112345671");
        request.setUtilityCompanyCode("KEL");
        request.setConsumerNo("0400123456789");
        request.setAmount("1500.00");
        return request;
    }

    @Test
    @DisplayName("a complete payment passes")
    void validPaymentPasses() {
        assertThatCode(() -> BillPaymentRequestValidator.validateBillPayment(valid(), envelope))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("an inquiry needs no amount: it is asking what is due")
    void inquiryNeedsNoAmount() {
        BillPaymentRequest request = valid();
        request.setAmount(null);
        assertThatCode(() -> BillPaymentRequestValidator.validateBillInquiry(request, envelope))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("the payer is required even on an inquiry, because DE-02 is built from it")
    void inquiryNeedsThePayer() {
        BillPaymentRequest request = valid();
        request.setFromAccountNo(null);
        assertThatThrownBy(() -> BillPaymentRequestValidator.validateBillInquiry(request, envelope))
                .hasMessageContaining("From Account No Required");
    }

    @Test
    @DisplayName("a missing utility company code is rejected")
    void missingCompanyCodeIsRejected() {
        BillPaymentRequest request = valid();
        request.setUtilityCompanyCode("  ");
        assertThatThrownBy(() -> BillPaymentRequestValidator.validateBillInquiry(request, envelope))
                .hasMessageContaining("Utility Company Code Required");
    }

    @Test
    @DisplayName("a missing consumer number is rejected")
    void missingConsumerNoIsRejected() {
        BillPaymentRequest request = valid();
        request.setConsumerNo(null);
        assertThatThrownBy(() -> BillPaymentRequestValidator.validateBillPayment(request, envelope))
                .hasMessageContaining("Consumer No Required");
    }

    @Test
    @DisplayName("a payment with no amount is rejected")
    void paymentNeedsAnAmount() {
        BillPaymentRequest request = valid();
        request.setAmount(null);
        assertThatThrownBy(() -> BillPaymentRequestValidator.validateBillPayment(request, envelope))
                .hasMessageContaining("Amount Required");
    }

    @Test
    @DisplayName("a zero or negative amount is rejected")
    void amountMustBePositive() {
        BillPaymentRequest request = valid();
        request.setAmount("0");
        assertThatThrownBy(() -> BillPaymentRequestValidator.validateBillPayment(request, envelope))
                .hasMessageContaining("Amount Must Be Greater Than Zero");
    }

    @Test
    @DisplayName("a formatted amount is rejected rather than silently reinterpreted")
    void malformedAmountIsRejected() {
        BillPaymentRequest request = valid();
        request.setAmount("1,500.00");
        assertThatThrownBy(() -> BillPaymentRequestValidator.validateBillPayment(request, envelope))
                .hasMessageContaining("INVALID AMOUNT");
    }

    @Test
    @DisplayName("the envelope still has to identify the device and channel")
    void envelopeIsChecked() {
        Request bad = new Request();
        bad.setChannel("MOBILE");
        assertThatThrownBy(() -> BillPaymentRequestValidator.validateBillInquiry(valid(), bad))
                .hasMessageContaining("INVALID IMEI NO");
    }
}
