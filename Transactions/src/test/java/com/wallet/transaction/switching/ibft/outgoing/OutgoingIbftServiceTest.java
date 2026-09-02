package com.wallet.transaction.switching.ibft.outgoing;

import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.model.LkpBank;
import com.wallet.transaction.model.TblAccount;
import com.wallet.transaction.model.TblCustomer;
import com.wallet.transaction.repo.LkpBankRepo;
import com.wallet.transaction.repo.TblAccountRepo;
import com.wallet.transaction.repo.TblCustomerRepo;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.switching.common.MwChannelCredentials;
import com.wallet.transaction.switching.common.ProcedureResult;
import com.wallet.transaction.switching.common.SwitchGatewayToggle;
import com.wallet.transaction.switching.common.SwitchReferenceGenerator;
import com.wallet.transaction.switching.common.SwitchTransactionType;
import com.wallet.transaction.switching.common.SwitchingConstants;
import com.wallet.transaction.switching.common.client.GatewayAdviceRequest;
import com.wallet.transaction.switching.common.client.GatewayAdviceResponse;
import com.wallet.transaction.switching.common.client.GatewayResponse;
import com.wallet.transaction.switching.common.client.GatewayTitleFetchRequest;
import com.wallet.transaction.switching.common.client.GatewayTitleFetchResponse;
import com.wallet.transaction.switching.common.client.SwitchGatewayClient;
import com.wallet.transaction.switching.common.model.TblTransactionMock;
import com.wallet.transaction.switching.common.service.TransactionMockService;
import com.wallet.transaction.switching.ibft.IbftProcedureRequest;
import com.wallet.transaction.switching.ibft.IbftProcedureService;
import com.wallet.transaction.util.AESencryption;
import com.wallet.transaction.util.GenericResponseCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutgoingIbftServiceTest {

    private static final String BENEFICIARY = "PK65KHYB0001002000908196";
    private static final String FROM_ACCOUNT = "03001234567";
    private static final String BANK_IMD = "221166";
    /** The plain national ID the app sends. */
    private static final String NID = "3520112345671";
    /** What TBL_CUSTOMER.NID_NO actually holds: the AES form of the above. */
    private static final String ENCRYPTED_NID = "Kx8Qm1p2R3s4T5u6V7w8Yg==";

    @Mock
    private TransactionMockService transactionMockService;
    @Mock
    private IbftProcedureService ibftProcedureService;
    @Mock
    private SwitchGatewayClient switchGatewayClient;
    @Mock
    private SwitchReferenceGenerator referenceGenerator;
    @Mock
    private CommonService commonService;
    @Mock
    private TblAccountRepo tblAccountRepo;
    @Mock
    private LkpBankRepo lkpBankRepo;
    @Mock
    private TblCustomerRepo tblCustomerRepo;
    @Mock
    private AESencryption aesEncryption;
    @Mock
    private MwChannelCredentials mwCredentials;
    @Mock
    private SwitchGatewayToggle switchGatewayToggle;

    @InjectMocks
    private OutgoingIbftService service;

    private Request envelope;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "acquirerInstitutionCode", "999982");
        ReflectionTestUtils.setField(service, "merchantType", "0003");
        ReflectionTestUtils.setField(service, "pointOfEntry", "002");
        ReflectionTestUtils.setField(service, "networkIdentifier", "001");
        ReflectionTestUtils.setField(service, "currencyCode", "586");
        ReflectionTestUtils.setField(service, "cardAcceptorTerminalId", "00000001");
        ReflectionTestUtils.setField(service, "cardAcceptorIdentificationCode", "000000000000001");
        ReflectionTestUtils.setField(service, "cardAcceptorNameAndLocation", "DFS ISLAMABAD PK");
        ReflectionTestUtils.setField(service, "dfsBin", "958214");

        // Infrastructure the service leans on but that no single test is about. Lenient so the
        // tests that stop before reaching them are not failed for an unused stub.
        lenient().when(switchGatewayToggle.isEnabled()).thenReturn(true);
        lenient().when(mwCredentials.getClientSecret()).thenReturn("mw-secret");
        lenient().when(mwCredentials.getUserId()).thenReturn(100L);
        lenient().when(lkpBankRepo.findByBankImdAndIsActive(BANK_IMD, "Y")).thenReturn(bank());
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

    /** The APIs return the standard envelope, so every assertion goes through data. */
    @SuppressWarnings("unchecked")
    private <T> T data(HashMap<String, Object> envelopeMap) {
        assertThat(envelopeMap).containsKeys("responsecode", "messages", "data");
        return (T) envelopeMap.get("data");
    }

    private String responseCode(HashMap<String, Object> envelopeMap) {
        return (String) envelopeMap.get("responsecode");
    }

    private LkpBank bank() {
        LkpBank bank = new LkpBank();
        bank.setBankImd(BANK_IMD);
        bank.setBankName("KHUSHHALI BANK");
        return bank;
    }

    private TblCustomer customer() {
        TblCustomer customer = new TblCustomer();
        customer.setNidNo(ENCRYPTED_NID);
        return customer;
    }

    private OutgoingIbftRequest validRequest() {
        OutgoingIbftRequest request = new OutgoingIbftRequest();
        request.setFromAccountNo(FROM_ACCOUNT);
        request.setFromAccountNid(NID);
        request.setBeneficiaryAccountNo(BENEFICIARY);
        request.setBeneficiaryBankImd(BANK_IMD);
        request.setAmount("1500.00");
        request.setPurposeOfPayment("0401 HOME REMITTANCE");
        request.setTransactionReference("REF-1");
        return request;
    }

    private TblTransactionMock mockRecord() {
        TblTransactionMock record = new TblTransactionMock();
        record.setTransactionMockId(BigDecimal.ONE);
        record.setBeneficiaryAccountNo(BENEFICIARY);
        record.setBeneficiaryAccountTitle("ZEESHAN AHMED");
        record.setBeneficiaryBankName("KHUSHHALI BANK");
        record.setImdNo("221166");
        record.setTransactionType("IBFT");
        return record;
    }

    /** A result the core would call approved: it ran to completion AND returned 000. */
    private ProcedureResult approved() {
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(1);
        procedure.setErrorResponse("000");
        return procedure;
    }

    private void stubReferences() {
        when(referenceGenerator.nextStan()).thenReturn("123657");
        when(referenceGenerator.rrnFor("123657")).thenReturn("512149123657");
    }

    // ------------------------------------------------------------------ title fetch

    @Test
    @DisplayName("title fetch looks the beneficiary up with TRANSACTION_TYPE = IBFT and the requested account")
    void titleFetchUsesIbftTypeAndBeneficiaryAccount() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();
        GatewayTitleFetchResponse data = new GatewayTitleFetchResponse();
        data.setResponseCode("00");
        data.setBeneficiaryId("4250105101451");
        GatewayResponse<GatewayTitleFetchResponse> gateway = new GatewayResponse<>();
        gateway.setData(data);
        when(switchGatewayClient.titleFetch(any())).thenReturn(gateway);

        HashMap<String, Object> envelopeMap = service.titleFetch(validRequest(), envelope);
        OutgoingIbftTitleFetchResponse response = data(envelopeMap);

        verify(transactionMockService).find(eq(SwitchTransactionType.IBFT), eq(BENEFICIARY));
        assertThat(responseCode(envelopeMap)).isEqualTo(GenericResponseCode.SUCCESS.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo("00");
        assertThat(response.getBeneficiaryAccountTitle()).isEqualTo("ZEESHAN AHMED");
        assertThat(response.getBeneficiaryBankImd()).isEqualTo("221166");
        assertThat(response.getBeneficiaryId()).isEqualTo("4250105101451");
    }

    @Test
    @DisplayName("title fetch takes the title from the database, never from the switch answer")
    void titleFetchPrefersDatabaseTitle() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();
        GatewayTitleFetchResponse data = new GatewayTitleFetchResponse();
        data.setResponseCode("00");
        data.setAccountTitle("SOMETHING ELSE FROM THE SWITCH");
        GatewayResponse<GatewayTitleFetchResponse> gateway = new GatewayResponse<>();
        gateway.setData(data);
        when(switchGatewayClient.titleFetch(any())).thenReturn(gateway);

        OutgoingIbftTitleFetchResponse response = data(service.titleFetch(validRequest(), envelope));

        assertThat(response.getBeneficiaryAccountTitle()).isEqualTo("ZEESHAN AHMED");
    }

    @Test
    @DisplayName("title fetch stops with DE-39 68 when the beneficiary has no mock record")
    void titleFetchWithoutMockRecordIsRejected() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(null);

        HashMap<String, Object> envelopeMap = service.titleFetch(validRequest(), envelope);
        OutgoingIbftTitleFetchResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
        verify(switchGatewayClient, never()).titleFetch(any());
    }

    @Test
    @DisplayName("title fetch reports a link failure when the gateway does not answer")
    void titleFetchWithoutGatewayAnswer() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();
        when(switchGatewayClient.titleFetch(any())).thenReturn(null);

        HashMap<String, Object> envelopeMap = service.titleFetch(validRequest(), envelope);
        OutgoingIbftTitleFetchResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo(SwitchingConstants.RC_HOST_LINK_DOWN);
    }

    // ------------------------------------------------------------------ transfer

    @Test
    @DisplayName("transfer calls the procedure then the switch, and maps both answers")
    void transferHappyPath() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();

        ProcedureResult procedure = approved();
        procedure.setAuthIdResponse("528605");
        procedure.setTransHeadId(9001L);
        when(ibftProcedureService.outgoingIbft(any())).thenReturn(procedure);

        GatewayAdviceResponse data = new GatewayAdviceResponse();
        data.setResponseCode("00");
        GatewayResponse<GatewayAdviceResponse> gateway = new GatewayResponse<>();
        gateway.setData(data);
        when(switchGatewayClient.advice(any())).thenReturn(gateway);

        HashMap<String, Object> envelopeMap = service.transfer(validRequest(), envelope, BigDecimal.valueOf(42));
        OutgoingIbftResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap)).isEqualTo(GenericResponseCode.SUCCESS.getResponseCode());
        assertThat(response.getSwitchResponseCode()).isEqualTo("00");
        assertThat(response.getCoreResponseCode()).isEqualTo("000");
        assertThat(response.getTransactionId()).isEqualTo(9001L);
        assertThat(response.getAuthIdResponse()).isEqualTo("528605");
        assertThat(response.getStan()).isEqualTo("123657");
        assertThat(response.getRrn()).isEqualTo("512149123657");
    }

    @Test
    @DisplayName("transfer passes the beneficiary IMD and title from the database to the procedure")
    void transferFeedsDatabaseValuesIntoTheProcedure() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();
        ProcedureResult procedure = approved();
        when(ibftProcedureService.outgoingIbft(any())).thenReturn(procedure);
        GatewayAdviceResponse data = new GatewayAdviceResponse();
        data.setResponseCode("00");
        GatewayResponse<GatewayAdviceResponse> gateway = new GatewayResponse<>();
        gateway.setData(data);
        when(switchGatewayClient.advice(any())).thenReturn(gateway);

        service.transfer(validRequest(), envelope, BigDecimal.valueOf(42));

        ArgumentCaptor<IbftProcedureRequest> captor = ArgumentCaptor.forClass(IbftProcedureRequest.class);
        verify(ibftProcedureService).outgoingIbft(captor.capture());
        IbftProcedureRequest sent = captor.getValue();
        assertThat(sent.getDestinationImd()).isEqualTo("221166");
        assertThat(sent.getToAccountNumber()).isEqualTo(BENEFICIARY);
        assertThat(sent.getFromAccountNumber()).isEqualTo(FROM_ACCOUNT);
        assertThat(sent.getIdentifier()).isEqualTo(SwitchingConstants.IDENTIFIER_DEBIT);
        assertThat(sent.getStan()).isEqualTo("123657");

        ArgumentCaptor<GatewayAdviceRequest> adviceCaptor = ArgumentCaptor.forClass(GatewayAdviceRequest.class);
        verify(switchGatewayClient).advice(adviceCaptor.capture());
        assertThat(adviceCaptor.getValue().getAccountTitle()).isEqualTo("ZEESHAN AHMED");
        assertThat(adviceCaptor.getValue().getAccountBankName()).isEqualTo("KHUSHHALI BANK");
    }

    @Test
    @DisplayName("transfer never advises the switch when the procedure did not post")
    void transferDoesNotAdviseWhenProcedureFails() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();

        // What the broken PKG_MW body produces today: no status, only a database message.
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(null);
        procedure.setResponseDescription("ORA-04063: package body \"DFS.PKG_MW\" has errors");
        when(ibftProcedureService.outgoingIbft(any())).thenReturn(procedure);

        HashMap<String, Object> envelopeMap = service.transfer(validRequest(), envelope, BigDecimal.valueOf(42));
        OutgoingIbftResponse response = data(envelopeMap);

        verify(switchGatewayClient, never()).advice(any());
        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        assertThat(response.getSwitchResponseCode()).isEqualTo(SwitchingConstants.RC_UNABLE_TO_PROCESS);
        assertThat(response.getResponseDescription()).contains("ORA-04063");
    }

    @Test
    @DisplayName("transfer stops before the procedure when the beneficiary has no mock record")
    void transferWithoutMockRecordIsRejected() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(null);

        HashMap<String, Object> envelopeMap = service.transfer(validRequest(), envelope, BigDecimal.valueOf(42));
        OutgoingIbftResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.INVALID_ACCOUNT_INFO.getResponseCode());
        assertThat(response.getSwitchResponseCode()).isEqualTo(SwitchingConstants.RC_INVALID_TO_ACCOUNT);
        verify(ibftProcedureService, never()).outgoingIbft(any());
        verify(switchGatewayClient, never()).advice(any());
    }

    @Test
    @DisplayName("transfer reports a link failure when the gateway does not answer the advice")
    void transferWithoutGatewayAnswer() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();
        ProcedureResult procedure = approved();
        when(ibftProcedureService.outgoingIbft(any())).thenReturn(procedure);
        when(switchGatewayClient.advice(any())).thenReturn(null);

        HashMap<String, Object> envelopeMap = service.transfer(validRequest(), envelope, BigDecimal.valueOf(42));
        OutgoingIbftResponse response = data(envelopeMap);

        assertThat(responseCode(envelopeMap))
                .isEqualTo(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        assertThat(response.getSwitchResponseCode()).isEqualTo(SwitchingConstants.RC_HOST_LINK_DOWN);
    }

    @Test
    @DisplayName("the title fetch message carries the technical 1LINK fields from configuration")
    void titleFetchMessageCarriesConfiguredTechnicalFields() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();
        GatewayTitleFetchResponse data = new GatewayTitleFetchResponse();
        data.setResponseCode("00");
        GatewayResponse<GatewayTitleFetchResponse> gateway = new GatewayResponse<>();
        gateway.setData(data);
        when(switchGatewayClient.titleFetch(any())).thenReturn(gateway);

        service.titleFetch(validRequest(), envelope);

        ArgumentCaptor<GatewayTitleFetchRequest> captor = ArgumentCaptor.forClass(GatewayTitleFetchRequest.class);
        verify(switchGatewayClient).titleFetch(captor.capture());
        GatewayTitleFetchRequest sent = captor.getValue();
        assertThat(sent.getMerchantType()).isEqualTo("0003");
        assertThat(sent.getNetworkIdentifier()).isEqualTo("001");
        assertThat(sent.getCurrencyCode()).isEqualTo("586");
        assertThat(sent.getPointOfEntry()).isEqualTo("002");
        assertThat(sent.getToBankImd()).isEqualTo("221166");
    }

    @Test
    @DisplayName("the switch is sent a 16-digit PAN and a 44-character purpose, not the raw request values")
    void outgoingMessageIsShapedToTheWidthsTheSwitchRequires() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();
        GatewayTitleFetchResponse data = new GatewayTitleFetchResponse();
        data.setResponseCode("00");
        GatewayResponse<GatewayTitleFetchResponse> gateway = new GatewayResponse<>();
        gateway.setData(data);
        when(switchGatewayClient.titleFetch(any())).thenReturn(gateway);

        service.titleFetch(validRequest(), envelope);

        ArgumentCaptor<GatewayTitleFetchRequest> captor = ArgumentCaptor.forClass(GatewayTitleFetchRequest.class);
        verify(switchGatewayClient).titleFetch(captor.capture());
        GatewayTitleFetchRequest sent = captor.getValue();

        // The regression: the wallet account number used to go out as DE-02 and the gateway
        // rejected it with "Invalid PAN : Length Should be 16".
        assertThat(sent.getPan()).hasSize(16);
        assertThat(sent.getPan()).isEqualTo("9582143001234567");
        assertThat(sent.getPurposeOfPayment()).hasSize(44);
        assertThat(sent.getCardAcceptorNameAndLocation()).hasSize(40);
        assertThat(sent.getAccountNo1()).isEqualTo(FROM_ACCOUNT);
    }

    @Test
    @DisplayName("the advice fills the DE-120 sub-fields the switch validates, and invents none of them")
    void adviceCarriesRecordDataSubFields() {
        when(transactionMockService.find(SwitchTransactionType.IBFT, BENEFICIARY)).thenReturn(mockRecord());
        stubReferences();
        TblAccount sender = new TblAccount();
        sender.setAccountTitle("MUHAMMAD BIN AFTAB");
        when(tblAccountRepo.findByAccountNo(FROM_ACCOUNT)).thenReturn(sender);

        ProcedureResult procedure = approved();
        when(ibftProcedureService.outgoingIbft(any())).thenReturn(procedure);
        GatewayAdviceResponse data = new GatewayAdviceResponse();
        data.setResponseCode("00");
        GatewayResponse<GatewayAdviceResponse> gateway = new GatewayResponse<>();
        gateway.setData(data);
        when(switchGatewayClient.advice(any())).thenReturn(gateway);

        service.transfer(validRequest(), envelope, BigDecimal.valueOf(42));

        ArgumentCaptor<GatewayAdviceRequest> captor = ArgumentCaptor.forClass(GatewayAdviceRequest.class);
        verify(switchGatewayClient).advice(captor.capture());
        GatewayAdviceRequest sent = captor.getValue();

        assertThat(sent.getPan()).hasSize(16);
        assertThat(sent.getPurposeOfPayment()).hasSize(44);
        assertThat(sent.getSenderName()).isEqualTo("MUHAMMAD BIN AFTAB");
        assertThat(sent.getAccountTitle()).isEqualTo("ZEESHAN AHMED");
        assertThat(sent.getAccountBankName()).isEqualTo("KHUSHHALI BANK");
        // Neither is in TBL_TRANSACTION_MOCK or the request, so neither is made up.
        assertThat(sent.getAccountBranchName()).isEmpty();
        assertThat(sent.getSenderId()).isEmpty();
    }
}
