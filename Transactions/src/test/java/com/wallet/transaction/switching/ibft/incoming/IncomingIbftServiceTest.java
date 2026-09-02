package com.wallet.transaction.switching.ibft.incoming;

import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.switching.common.MwChannelCredentials;
import com.wallet.transaction.switching.common.ProcedureResult;
import com.wallet.transaction.switching.common.SwitchingConstants;
import com.wallet.transaction.switching.ibft.IbftProcedureRequest;
import com.wallet.transaction.switching.ibft.IbftProcedureService;
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

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncomingIbftServiceTest {

    @Mock
    private IbftProcedureService ibftProcedureService;
    @Mock
    private CommonService commonService;
    @Mock
    private MwChannelCredentials mwCredentials;

    @InjectMocks
    private IncomingIbftService service;

    @BeforeEach
    void setUp() {
        // Lenient: the tests that never reach the procedure must not fail for an unused stub.
        lenient().when(mwCredentials.getClientSecret()).thenReturn("mw-secret");
        lenient().when(mwCredentials.getUserId()).thenReturn(100L);

        // Same envelope CommonServiceImpl.getResponse builds, minus the TBL_MESSAGE lookup.
        when(commonService.getResponse(anyString(), any())).thenAnswer(invocation -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("responsecode", invocation.getArgument(0));
            map.put("messages", null);
            map.put("data", invocation.getArgument(1));
            return map;
        });
    }

    /** The API returns the standard envelope; the message for the switch is the data member. */
    private IncomingSwitchMessage data(HashMap<String, Object> envelope) {
        assertThat(envelope).containsKeys("responsecode", "messages", "data");
        return (IncomingSwitchMessage) envelope.get("data");
    }

    private String responseCode(HashMap<String, Object> envelope) {
        return (String) envelope.get("responsecode");
    }

    /**
     * DE-120 laid out per 1LINK spec 9.62.1.1:
     * to account (20) + title (30) + source IMD (11) + destination IMD (11) + identifier (1).
     */
    private String recordData() {
        return pad("0000003001234567", 20)
                + pad("ZEESHAN AHMED", 30)
                + pad("22116600000", 11)
                + pad("99998200000", 11)
                + "C";
    }

    private String pad(String value, int length) {
        return String.format("%-" + length + "s", value);
    }

    private IncomingSwitchMessage message() {
        IncomingSwitchMessage message = new IncomingSwitchMessage();
        message.getHeader().setMessageType("0220");
        message.setStan("123657");
        message.setRrn("512149123658");
        message.setPan("1604983035847496");
        message.setProcessingCode("480000");
        message.setTransactionAmount("000000150000");
        message.setTransactionCurrencyCode("586");
        message.setAccountNo1("03035847496");
        message.setAccountNo2("PK65KHYB0001002000908196");
        message.setAcquirerIdentification("22116600000");
        message.setMerchantType("0003");
        message.setPointOfServiceEntryMode("002");
        message.setTransactionLocalDate("0113");
        message.setTransactionLocalTime("120531");
        message.setRecordData(recordData());
        return message;
    }

    @Test
    @DisplayName("a posted advice is answered with DE-39 00")
    void adviceSuccessAnswersProcessedOk() {
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(1);
        procedure.setErrorResponse("000");
        procedure.setAuthIdResponse("528605");
        procedure.setTransHeadId(4242L);
        when(ibftProcedureService.incomingIbft(any())).thenReturn(procedure);

        HashMap<String, Object> envelope = service.advice(message());
        IncomingSwitchMessage response = data(envelope);

        assertThat(responseCode(envelope)).isEqualTo(GenericResponseCode.SUCCESS.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo(SwitchingConstants.RC_PROCESSED_OK);
        assertThat(response.getAuthIdResponse()).isEqualTo("528605");
        assertThat(response.getStan()).isEqualTo("123657");
        assertThat(response.getRrn()).isEqualTo("512149123658");
    }

    @Test
    @DisplayName("the core's own response code is preferred when the procedure declines")
    void adviceUsesCoreResponseCodeOnDecline() {
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(1);
        procedure.setErrorResponse("04");
        procedure.setResponseDescription("LOW BALANCE");
        when(ibftProcedureService.incomingIbft(any())).thenReturn(procedure);

        HashMap<String, Object> envelope = service.advice(message());
        IncomingSwitchMessage response = data(envelope);

        assertThat(responseCode(envelope)).isEqualTo(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo("04");
    }

    @Test
    @DisplayName("a broken procedure is answered with DE-39 46, not with a fabricated success")
    void adviceFallsBackToUnableToProcess() {
        // Today's reality: the PKG_MW body is invalid, so nothing comes back.
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(null);
        procedure.setErrorResponse(null);
        procedure.setResponseDescription("ORA-04063: package body \"DFS.PKG_MW\" has errors");
        when(ibftProcedureService.incomingIbft(any())).thenReturn(procedure);

        HashMap<String, Object> envelope = service.advice(message());
        IncomingSwitchMessage response = data(envelope);

        assertThat(responseCode(envelope)).isEqualTo(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        assertThat(response.getResponseCode()).isEqualTo(SwitchingConstants.RC_UNABLE_TO_PROCESS);
    }

    @Test
    @DisplayName("DE-120 sub-fields are read at the offsets the 1LINK spec defines")
    void procedureParametersAreReadFromRecordDataAtSpecOffsets() {
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(1);
        procedure.setErrorResponse("000");
        when(ibftProcedureService.incomingIbft(any())).thenReturn(procedure);

        service.advice(message());

        ArgumentCaptor<IbftProcedureRequest> captor = ArgumentCaptor.forClass(IbftProcedureRequest.class);
        verify(ibftProcedureService).incomingIbft(captor.capture());
        IbftProcedureRequest sent = captor.getValue();

        assertThat(sent.getSourceImd()).isEqualTo("22116600000");
        assertThat(sent.getDestinationImd()).isEqualTo("99998200000");
        assertThat(sent.getIdentifier()).isEqualTo("C");
        assertThat(sent.getUdf1()).isEqualTo("ZEESHAN AHMED");
        assertThat(sent.getToAccountNumber()).isEqualTo("PK65KHYB0001002000908196");
        assertThat(sent.getFromAccountNumber()).isEqualTo("03035847496");
        assertThat(sent.getStan()).isEqualTo("123657");
        assertThat(sent.getRrn()).isEqualTo("512149123658");
    }

    @Test
    @DisplayName("a missing DE-120 does not blow up the incoming path")
    void missingRecordDataIsTolerated() {
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(1);
        procedure.setErrorResponse("000");
        when(ibftProcedureService.incomingIbft(any())).thenReturn(procedure);

        IncomingSwitchMessage input = message();
        input.setRecordData(null);

        HashMap<String, Object> envelope = service.titleFetch(input);
        IncomingSwitchMessage response = data(envelope);

        assertThat(response.getResponseCode()).isEqualTo(SwitchingConstants.RC_PROCESSED_OK);
        ArgumentCaptor<IbftProcedureRequest> captor = ArgumentCaptor.forClass(IbftProcedureRequest.class);
        verify(ibftProcedureService).incomingIbft(captor.capture());
        assertThat(captor.getValue().getSourceImd()).isEmpty();
        // With no identifier present the credit leg is assumed, which is what an incoming IBFT is.
        assertThat(captor.getValue().getIdentifier()).isEqualTo(SwitchingConstants.IDENTIFIER_CREDIT);
    }

    @Test
    @DisplayName("DE-120 rewritten by the procedure is returned to the switch")
    void procedureMayRewriteRecordData() {
        ProcedureResult procedure = new ProcedureResult();
        procedure.setResponseStatus(1);
        procedure.setErrorResponse("000");
        procedure.setRecordData("REWRITTEN BY THE PROCEDURE");
        when(ibftProcedureService.incomingIbft(any())).thenReturn(procedure);

        HashMap<String, Object> envelope = service.advice(message());
        IncomingSwitchMessage response = data(envelope);

        assertThat(response.getRecordData()).isEqualTo("REWRITTEN BY THE PROCEDURE");
    }
}
