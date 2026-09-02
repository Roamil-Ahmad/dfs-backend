package com.wallet.transaction.switching.billpayment;

import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.model.TblCustomer;
import com.wallet.transaction.repo.TblCustomerRepo;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.switching.common.MwChannelCredentials;
import com.wallet.transaction.switching.common.ProcedureResult;
import com.wallet.transaction.switching.common.SwitchGatewayToggle;
import com.wallet.transaction.switching.common.SwitchReferenceGenerator;
import com.wallet.transaction.switching.common.SwitchTransactionType;
import com.wallet.transaction.switching.common.SwitchingConstants;
import com.wallet.transaction.switching.common.client.GatewayBillRequest;
import com.wallet.transaction.switching.common.client.GatewayBillResponse;
import com.wallet.transaction.switching.common.client.GatewayResponse;
import com.wallet.transaction.switching.common.client.SwitchGatewayClient;
import com.wallet.transaction.switching.common.model.TblTransactionMock;
import com.wallet.transaction.switching.common.service.TransactionMockService;
import com.wallet.transaction.util.AESencryption;
import com.wallet.transaction.util.GenericResponseCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillPaymentServiceTest {

    private static final String COMPANY = "KEL";
    private static final String CONSUMER = "0400123456789";
    private static final String FROM_ACCOUNT = "03001234567";
    /** The plain national ID the app sends. */
    private static final String NID = "3520112345671";
    /** What TBL_CUSTOMER.NID_NO actually holds: the AES form of the above. */
    private static final String ENCRYPTED_NID = "Kx8Qm1p2R3s4T5u6V7w8Yg==";

    @Mock
    private TransactionMockService transactionMockService;
    @Mock
    private BillPaymentProcedureService billPaymentProcedureService;
    @Mock
    private SwitchGatewayClient switchGatewayClient;
    @Mock
    private SwitchReferenceGenerator referenceGenerator;
    @Mock
    private CommonService commonService;
    @Mock
    private TblCustomerRepo tblCustomerRepo;
    @Mock
    private AESencryption aesEncryption;
    @Mock
    private MwChannelCredentials mwCredentials;
    @Mock
    private SwitchGatewayToggle switchGatewayToggle;

    // Spied, not just mocked: getFormattedAmount calls PKG_MW.GET_AMOUNT_FORMATTED through the
    // EntityManager, which a unit test has no way to satisfy.
    @Spy
    @InjectMocks
    private BillPaymentService service;

    private Request envelope;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "dfsBin", "958214");
        ReflectionTestUtils.setField(service, "merchantType", "6012");
        ReflectionTestUtils.setField(service, "pointOfEntry", "002");
        ReflectionTestUtils.setField(service, "networkIdentifier", "001");
        ReflectionTestUtils.setField(service, "currencyCode", "586");
        ReflectionTestUtils.setField(service, "cardAcceptorTerminalId", "00000001");
        ReflectionTestUtils.setField(service, "cardAcceptorIdentificationCode", "000000000000001");
        ReflectionTestUtils.setField(service, "cardAcceptorNameAndLocation", "DFS ISLAMABAD PK");
        ReflectionTestUtils.setField(service, "acquirerInstitutionCode", "999982");

        // Infrastructure the service leans on but that no single test is about. Lenient so the
        // tests that stop before reaching them are not failed for an unused stub.
        lenient().when(switchGatewayToggle.isEnabled()).thenReturn(true);
        lenient().when(mwCredentials.getClientSecret()).thenReturn("mw-secret");
        lenient().when(mwCredentials.getUserId()).thenReturn(100L);
        lenient().when(aesEncryption.encryptwith256(NID)).thenReturn(ENCRYPTED_NID);
        lenient().when(tblCustomerRepo.findByAccountNoAndNidNo(FROM_ACCOUNT, ENCRYPTED_NID))
                .thenReturn(customer());

        lenient().when(commonService.getResponseWithOutDB(anyString(), any(), any()))
                .thenAnswer(invocation -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("responsecode", invocation.getArgument(0));
                    map.put("messages", invocation.getArgument(1));
                    map.put("data", invocation.getArgument(2));
                    return map;
                });

        // Same envelope CommonServiceImpl.getResponse builds, minus the TBL_MESSAGE lookup.
        lenient().when(commonService.getResponse(anyString(), any())).thenAnswer(invocation -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("responsecode", invocation.getArgument(0));
            map.put("messages", null);
            map.put("data", invocation.getArgument(1));
            return map;
        });

        envelope = new Request();
        envelope.setChannel("MOBILE");
        envelope.setImieNo("TESTIMEI");
    }

    private TblCustomer customer() {
        TblCustomer customer = new TblCustomer();
        customer.setNidNo(ENCRYPTED_NID);
        return customer;
    }

    @SuppressWarnings("unchecked")
    private <T> T data(HashMap<String, Object> envelopeMap) {
        assertThat(envelopeMap).containsKeys("responsecode", "messages", "data");
        return (T) envelopeMap.get("data");
    }

    private String responseCode(HashMap<String, Object> envelopeMap) {
        return (String) envelopeMap.get("responsecode");
    }

    private BillPaymentRequest validRequest() {
        BillPaymentRequest request = new BillPaymentRequest();
        request.setFromAccountNo(FROM_ACCOUNT);
        request.setFromAccountNid(NID);
        request.setUtilityCompanyCode(COMPANY);
        request.setConsumerNo(CONSUMER);
        request.setAmount("1500.00");
        request.setTransactionReference("REF-1");
        return request;
    }

    /** A procedure result the core would call approved: it ran to completion AND returned 000. */
    private ProcedureResult approved() {
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(1);
        procedure.setErrorResponse("000");
        return procedure;
    }

    private TblTransactionMock mockBill(String paid) {
        TblTransactionMock record = new TblTransactionMock();
        record.setTransactionMockId(BigDecimal.ONE);
        record.setTransactionType("BILL_PAYMENT");
        record.setUtilityCompanyCode(COMPANY);
        record.setUtilityCompanyName("K-ELECTRIC");
        record.setUtilityConsumerId(CONSUMER);
        record.setBillAmount(new BigDecimal("1500.00"));
        record.setBillDueDate("20260901");
        record.setBillPaid(paid);
        return record;
    }

    private void stubReferences() {
        when(referenceGenerator.nextStan()).thenReturn("123657");
        when(referenceGenerator.rrnFor("123657")).thenReturn("512149123657");
    }

    private GatewayResponse<GatewayBillResponse> gatewayOk() {
        GatewayBillResponse data = new GatewayBillResponse();
        data.setResponseCode("00");
        data.setAuthIdResp("528605");
        GatewayResponse<GatewayBillResponse> gateway = new GatewayResponse<>();
        gateway.setData(data);
        return gateway;
    }

    // ------------------------------------------------------------------ bill inquiry

    @Test
    @DisplayName("the inquiry looks the bill up by company code and consumer number")
    void inquiryLooksTheBillUp() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();
        when(switchGatewayClient.billInquiry(any())).thenReturn(gatewayOk());

        HashMap<String, Object> envelopeMap = service.billInquiry(validRequest(), envelope);
        BillInquiryResponse response = data(envelopeMap);

        verify(transactionMockService).findBill(
                eq(SwitchTransactionType.BILL_PAYMENT), eq(COMPANY), eq(CONSUMER));
        assertThat(responseCode(envelopeMap)).isEqualTo(GenericResponseCode.SUCCESS.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo("00");
        assertThat(response.getUtilityCompanyName()).isEqualTo("K-ELECTRIC");
        assertThat(response.getBillAmount()).isEqualTo("1500.00");
        assertThat(response.getBillDueDate()).isEqualTo("20260901");
    }

    @Test
    @DisplayName("an unregistered bill is declined with DE-39 68, and the switch is never called")
    void inquiryWithoutMockRecordIsRejected() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(null);

        HashMap<String, Object> envelopeMap = service.billInquiry(validRequest(), envelope);
        BillInquiryResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
        assertThat(response.getUtilityCompanyName()).isNull();
        verify(switchGatewayClient, never()).billInquiry(any());
    }

    @Test
    @DisplayName("the inquiry reports a link failure when the gateway does not answer")
    void inquiryWithoutGatewayAnswer() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();
        when(switchGatewayClient.billInquiry(any())).thenReturn(null);

        HashMap<String, Object> envelopeMap = service.billInquiry(validRequest(), envelope);
        BillInquiryResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo(SwitchingConstants.RC_HOST_LINK_DOWN);
    }

    @Test
    @DisplayName("the inquiry never calls the debit procedure")
    void inquiryMovesNoMoney() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();
        when(switchGatewayClient.billInquiry(any())).thenReturn(gatewayOk());

        service.billInquiry(validRequest(), envelope);

        verify(billPaymentProcedureService, never()).billPayment(any());
    }

    // ------------------------------------------------------------------ bill payment

    @Test
    @DisplayName("the payment calls the procedure then the switch, and maps both answers")
    void paymentHappyPath() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();

        ProcedureResult procedure = approved();
        when(billPaymentProcedureService.billPayment(any())).thenReturn(procedure);
        when(switchGatewayClient.billPayment(any())).thenReturn(gatewayOk());

        HashMap<String, Object> envelopeMap = service.billPayment(validRequest(), envelope);
        BillPaymentResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap)).isEqualTo(GenericResponseCode.SUCCESS.getResponseCode());
        assertThat(response.getSwitchResponseCode()).isEqualTo("00");
        assertThat(response.getCoreResponseCode()).isEqualTo("000");
        assertThat(response.getAuthIdResponse()).isEqualTo("528605");
        assertThat(response.getStan()).isEqualTo("123657");
        assertThat(response.getRrn()).isEqualTo("512149123657");
        assertThat(response.getUtilityCompanyName()).isEqualTo("K-ELECTRIC");
    }

    @Test
    @DisplayName("an already-paid bill is refused before anything is debited")
    void alreadyPaidBillIsRefused() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("Y"));

        HashMap<String, Object> envelopeMap = service.billPayment(validRequest(), envelope);
        BillPaymentResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.RECORD_ALREADY_EXISTS.getResponseCode());
        assertThat(response.getSwitchResponseCode()).isEqualTo(SwitchingConstants.RC_DUPLICATE_TRANSACTION);
        verify(billPaymentProcedureService, never()).billPayment(any());
        verify(switchGatewayClient, never()).billPayment(any());
    }

    @Test
    @DisplayName("the switch is never told about a payment the core did not post")
    void paymentDoesNotAdviseWhenProcedureFails() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();

        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(null);
        procedure.setResponseDescription("ORA-06550: wrong number or types of arguments");
        when(billPaymentProcedureService.billPayment(any())).thenReturn(procedure);

        HashMap<String, Object> envelopeMap = service.billPayment(validRequest(), envelope);
        BillPaymentResponse response = data(envelopeMap);

        verify(switchGatewayClient, never()).billPayment(any());
        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        assertThat(response.getSwitchResponseCode()).isEqualTo(SwitchingConstants.RC_UNABLE_TO_PROCESS);
        assertThat(response.getResponseDescription()).contains("ORA-06550");
    }

    @Test
    @DisplayName("the procedure is given the MW credentials, the stored NID and the bill's identity")
    void procedureParametersComeFromTheAccountAndTheBill() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();
        // The procedure is handed minor units, produced by PKG_MW.GET_AMOUNT_FORMATTED.
        doReturn("150000").when(service).getFormattedAmount("1500.00", "C");
        when(billPaymentProcedureService.billPayment(any())).thenReturn(approved());
        when(switchGatewayClient.billPayment(any())).thenReturn(gatewayOk());

        service.billPayment(validRequest(), envelope);

        ArgumentCaptor<BillPaymentProcedureRequest> captor =
                ArgumentCaptor.forClass(BillPaymentProcedureRequest.class);
        verify(billPaymentProcedureService).billPayment(captor.capture());
        BillPaymentProcedureRequest sent = captor.getValue();

        // PKG_MW authenticates on all three of these together. The channel code now comes from
        // the request envelope rather than configuration.
        assertThat(sent.getClientSecret()).isEqualTo("mw-secret");
        assertThat(sent.getChannelCode()).isEqualTo(envelope.getChannel());
        assertThat(sent.getUserId()).isEqualTo(100L);

        // The relationship id is the stored ciphertext, not the plain number the app sent.
        assertThat(sent.getRelationshipId()).isEqualTo(ENCRYPTED_NID);
        assertThat(sent.getRelationshipId()).isNotEqualTo(NID);

        assertThat(sent.getFromAccountNumber()).isEqualTo(FROM_ACCOUNT);
        assertThat(sent.getUtilityCompanyId()).isEqualTo(COMPANY);
        assertThat(sent.getUtilityConsumerNumber()).isEqualTo(CONSUMER);
        assertThat(sent.getTransactionAmount()).isEqualTo("150000");
        assertThat(sent.getTransactionCurrency()).isEqualTo("586");
        assertThat(sent.getAcquiringInstitutionCode()).isEqualTo("999982");
        assertThat(sent.getStan()).isEqualTo("123657");
        assertThat(sent.getRrn()).isEqualTo("512149123657");
        assertThat(sent.getUdf1()).isEqualTo("REF-1");
        // The procedure prices the transaction; a fee invented here would override its own.
        assertThat(sent.getTransactionFee()).isNull();
    }

    @Test
    @DisplayName("the switch message carries a 16-digit PAN and the bill resolved from the database")
    void gatewayMessageIsShapedCorrectly() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();
        when(switchGatewayClient.billInquiry(any())).thenReturn(gatewayOk());

        service.billInquiry(validRequest(), envelope);

        ArgumentCaptor<GatewayBillRequest> captor = ArgumentCaptor.forClass(GatewayBillRequest.class);
        verify(switchGatewayClient).billInquiry(captor.capture());
        GatewayBillRequest sent = captor.getValue();

        assertThat(sent.getPan()).hasSize(16);
        assertThat(sent.getPan()).isEqualTo("9582143001234567");
        assertThat(sent.getCardAcceptorNameAndLocation()).hasSize(40);
        assertThat(sent.getMerchantType()).isEqualTo("6012");
        assertThat(sent.getUtilityCompanyCode()).isEqualTo(COMPANY);
        assertThat(sent.getUtilityCompanyName()).isEqualTo("K-ELECTRIC");
        assertThat(sent.getConsumerNo()).isEqualTo(CONSUMER);
        assertThat(sent.getBillAmount()).isEqualTo("1500.00");
        // An inquiry asks what is due, so it sends no amount of its own.
        assertThat(sent.getTransactionAmount()).isNull();
    }

    @Test
    @DisplayName("the payer is identified to the core by the encrypted national ID, not by a Java lookup")
    void payerIsIdentifiedByEncryptedNid() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();
        when(billPaymentProcedureService.billPayment(any())).thenReturn(approved());
        when(switchGatewayClient.billPayment(any())).thenReturn(gatewayOk());

        service.billPayment(validRequest(), envelope);

        ArgumentCaptor<BillPaymentProcedureRequest> captor =
                ArgumentCaptor.forClass(BillPaymentProcedureRequest.class);
        verify(billPaymentProcedureService).billPayment(captor.capture());
        // Java no longer gates the payer: PKG_MW resolves it through VALIDATE_PAN_CNIC and owns
        // that decision, so the customer table is never consulted here.
        verify(tblCustomerRepo, never()).findByAccountNoAndNidNo(anyString(), anyString());
        // The stored ciphertext form, never the plain number the app sent.
        assertThat(captor.getValue().getRelationshipId()).isEqualTo(ENCRYPTED_NID);
        assertThat(captor.getValue().getRelationshipId()).isNotEqualTo(NID);
    }

    @Test
    @DisplayName("with the SWITCH flag off the debit still runs but the switch is not called")
    void switchDisabledSkipsTheGatewayButStillDebits() {
        when(transactionMockService.findBill(SwitchTransactionType.BILL_PAYMENT, COMPANY, CONSUMER))
                .thenReturn(mockBill("N"));
        stubReferences();
        when(switchGatewayToggle.isEnabled()).thenReturn(false);
        when(billPaymentProcedureService.billPayment(any())).thenReturn(approved());

        HashMap<String, Object> envelopeMap = service.billPayment(validRequest(), envelope);
        BillPaymentResponse response = data(envelopeMap);

        verify(billPaymentProcedureService).billPayment(any());
        verify(switchGatewayClient, never()).billPayment(any());
        assertThat(responseCode(envelopeMap)).isEqualTo(GenericResponseCode.SUCCESS.getResponseCode());
        assertThat(response.getSwitchResponseCode()).isEqualTo(SwitchingConstants.RC_PROCESSED_OK);
    }
}
