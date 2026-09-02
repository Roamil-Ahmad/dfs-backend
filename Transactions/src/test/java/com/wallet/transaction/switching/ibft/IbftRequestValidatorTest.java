package com.wallet.transaction.switching.ibft;

import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.switching.ibft.outgoing.OutgoingIbftRequest;
import com.wallet.transaction.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IbftRequestValidatorTest {

    private Request envelope;

    @BeforeEach
    void setUp() {
        envelope = new Request();
        envelope.setChannel("MOBILE");
        envelope.setImieNo("TESTIMEI");
    }

    private OutgoingIbftRequest valid() {
        OutgoingIbftRequest request = new OutgoingIbftRequest();
        request.setFromAccountNo("03001234567");
        request.setBeneficiaryAccountNo("PK65KHYB0001002000908196");
        request.setBeneficiaryBankImd("221166");
        request.setFromAccountNid("3520112345671");
        request.setAmount("1500.00");
        request.setPurposeOfPayment("0401 HOME REMITTANCE");
        return request;
    }

    @Test
    @DisplayName("a complete transfer request passes")
    void validTransferPasses() {
        assertThatCode(() -> IbftRequestValidator.validateOutgoingTransfer(valid(), envelope))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("title fetch needs both accounts: the beneficiary to look up and the sender for DE-02")
    void titleFetchNeedsBothAccounts() {
        OutgoingIbftRequest request = new OutgoingIbftRequest();
        request.setFromAccountNo("03006088659");
        request.setBeneficiaryAccountNo("PK65KHYB0001002000908196");
        request.setBeneficiaryBankImd("221166");
        request.setFromAccountNid("3520112345671");
        assertThatCode(() -> IbftRequestValidator.validateOutgoingTitleFetch(request, envelope))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("title fetch without the sending account is rejected: the switch needs a PAN")
    void titleFetchWithoutSenderIsRejected() {
        OutgoingIbftRequest request = new OutgoingIbftRequest();
        request.setBeneficiaryAccountNo("PK65KHYB0001002000908196");
        request.setBeneficiaryBankImd("221166");
        request.setFromAccountNid("3520112345671");
        assertThatThrownBy(() -> IbftRequestValidator.validateOutgoingTitleFetch(request, envelope))
                .hasMessageContaining("From Account No Required");
    }

    @Test
    @DisplayName("a missing beneficiary account is rejected")
    void missingBeneficiaryRejected() {
        OutgoingIbftRequest request = valid();
        request.setBeneficiaryAccountNo("  ");
        assertThatThrownBy(() -> IbftRequestValidator.validateOutgoingTransfer(request, envelope))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Beneficiary Account No");
    }

    @Test
    @DisplayName("a missing source account is rejected")
    void missingFromAccountRejected() {
        OutgoingIbftRequest request = valid();
        request.setFromAccountNo(null);
        assertThatThrownBy(() -> IbftRequestValidator.validateOutgoingTransfer(request, envelope))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("From Account No");
    }

    @Test
    @DisplayName("a non-numeric amount is rejected")
    void nonNumericAmountRejected() {
        OutgoingIbftRequest request = valid();
        request.setAmount("many rupees");
        assertThatThrownBy(() -> IbftRequestValidator.validateOutgoingTransfer(request, envelope))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("INVALID AMOUNT");
    }

    @Test
    @DisplayName("a zero or negative amount is rejected")
    void nonPositiveAmountRejected() {
        OutgoingIbftRequest request = valid();
        request.setAmount("0");
        assertThatThrownBy(() -> IbftRequestValidator.validateOutgoingTransfer(request, envelope))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Greater Than Zero");
    }

    @Test
    @DisplayName("a missing purpose of payment is rejected")
    void missingPurposeRejected() {
        OutgoingIbftRequest request = valid();
        request.setPurposeOfPayment(null);
        assertThatThrownBy(() -> IbftRequestValidator.validateOutgoingTransfer(request, envelope))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Purpose Of Payment");
    }

    @Test
    @DisplayName("a missing IMEI in the envelope is rejected")
    void missingImeiRejected() {
        envelope.setImieNo(null);
        assertThatThrownBy(() -> IbftRequestValidator.validateOutgoingTransfer(valid(), envelope))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("IMEI");
    }
}
