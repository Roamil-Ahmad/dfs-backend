package com.wallet.transaction.switching.ibft.incoming;

import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.switching.common.MwChannelCredentials;
import com.wallet.transaction.switching.common.ProcedureResult;
import com.wallet.transaction.switching.common.SwitchingConstants;
import com.wallet.transaction.switching.ibft.IbftProcedureRequest;
import com.wallet.transaction.switching.ibft.IbftProcedureService;
import com.wallet.transaction.util.GenericResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Incoming IBFT: another bank is sending funds to a DFS customer through 1LINK.
 *
 * DFS is the issuer on this path, so the message arrives from the switch, is answered by
 * {@code PKG_MW.INCOMING_IBFT} and the ISO 8583 response goes straight back down the same link.
 *
 * As on the outgoing side, the credit is entirely the database's work. This class prepares the
 * procedure parameters, calls it, and turns the outcome into a DE-39 response code.
 */
@Service
public class IncomingIbftService {

    private static final Logger log = LoggerFactory.getLogger(IncomingIbftService.class);

    /** DE-120 layout, 1LINK spec 9.62.1.1. */
    private static final int RD_TO_ACCOUNT_TITLE_OFFSET = 20;
    private static final int RD_TO_ACCOUNT_TITLE_LENGTH = 30;
    private static final int RD_SOURCE_IMD_OFFSET = 50;
    private static final int RD_IMD_LENGTH = 11;
    private static final int RD_DESTINATION_IMD_OFFSET = 61;
    private static final int RD_IDENTIFIER_OFFSET = 72;

    @Autowired
    private IbftProcedureService ibftProcedureService;

    @Autowired
    private CommonService commonService;

    /** PKG_MW's TBL_MW_CHANNEL triple; see MwChannelCredentials for why it is all three. */
    @Autowired
    private MwChannelCredentials mwCredentials;

    /**
     * Account Title Inquiry arriving from the switch - the remitting bank is asking DFS to confirm
     * the beneficiary before it sends funds.
     *
     * Returns the standard transaction-layer envelope built by {@code CommonService.getResponse},
     * the same shape the app transaction APIs use. The ISO 8583 message the gateway has to send
     * back down the link is the {@code data} member; DE-39 stays on it.
     */
    public HashMap<String, Object> titleFetch(IncomingSwitchMessage message) {
        log.info("Incoming title fetch | STAN:{} | RRN:{} | beneficiary:{}",
                message.getStan(), message.getRrn(), message.getAccountNo2());
        return envelope(process(message, false, "title fetch"));
    }

    /**
     * IBFT Advice arriving from the switch - the funds are being credited to a DFS customer.
     *
     * Returns the same envelope as {@link #titleFetch(IncomingSwitchMessage)}.
     */
    public HashMap<String, Object> advice(IncomingSwitchMessage message) {
        log.info("Incoming IBFT advice | STAN:{} | RRN:{} | beneficiary:{} | amount:{}",
                message.getStan(), message.getRrn(), message.getAccountNo2(), message.getTransactionAmount());
        return envelope(process(message, true, "IBFT advice"));
    }

    /**
     * Wraps the answer in the standard envelope. The DFS generic code reflects the DE-39 the switch
     * will receive, so a caller reading only the envelope still sees the right outcome.
     */
    private HashMap<String, Object> envelope(IncomingSwitchMessage response) {
        return commonService.getResponse(
                SwitchingConstants.RC_PROCESSED_OK.equals(response.getResponseCode())
                        ? GenericResponseCode.SUCCESS.getResponseCode()
                        : GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), response);
    }

    private IncomingSwitchMessage process(IncomingSwitchMessage message, boolean isAdvice, String description) {
        ProcedureResult procedure = ibftProcedureService.incomingIbft(buildProcedureRequest(message));

        IncomingSwitchMessage response = message;
        response.setAuthIdResponse(procedure.getAuthIdResponse());

        if (procedure.getRecordData() != null && !procedure.getRecordData().isBlank()) {
            // The procedure may rewrite DE-120, for instance to fill in the beneficiary title.
            response.setRecordData(procedure.getRecordData());
        }

        if (procedure.isApproved()) {
            response.setResponseCode(SwitchingConstants.RC_PROCESSED_OK);
            log.info("Incoming {} accepted | STAN:{} | RRN:{} | transHeadId:{}",
                    description, message.getStan(), message.getRrn(), procedure.getTransHeadId());
        } else {
            // A code the core supplied is preferred; otherwise report an inability to process.
            String code = procedure.getErrorResponse() != null && !procedure.getErrorResponse().isBlank()
                    ? procedure.getErrorResponse()
                    : SwitchingConstants.RC_UNABLE_TO_PROCESS;
            response.setResponseCode(code);
            log.error("Incoming {} declined, PKG_MW.INCOMING_IBFT did not succeed | STAN:{} | RRN:{} | status:{} | descr:{}",
                    description, message.getStan(), message.getRrn(),
                    procedure.getResponseStatus(), procedure.getResponseDescription());
        }
        return response;
    }

    private IbftProcedureRequest buildProcedureRequest(IncomingSwitchMessage message) {
        Date now = new Date();
        String recordData = message.getRecordData();

        IbftProcedureRequest procedure = new IbftProcedureRequest();
        procedure.setClientSecret(mwCredentials.getClientSecret());
        procedure.setChannelCode("MOB");
        procedure.setUserId(mwCredentials.getUserId());
        // Incoming keeps the beneficiary account here. INCOMING_IBFT never resolves anyone from
        // this value - it only writes it to TBL_MW_REQUEST.RELATIONSHIPID as a record of the
        // request - and the payer is at another bank, so there is no DFS national ID to send.
        procedure.setRelationshipId(message.getAccountNo2());
        procedure.setTransmissionDate(new SimpleDateFormat("yyyyMMdd").format(now));
        procedure.setTransmissionTime(new SimpleDateFormat("HHmmss").format(now));
        procedure.setStan(message.getStan());
        procedure.setRrn(message.getRrn());
        procedure.setDateLocalTran(message.getTransactionLocalDate());
        procedure.setTimeLocalTran(message.getTransactionLocalTime());
        procedure.setAcquiringInstitutionCode(message.getAcquirerIdentification());
        procedure.setMerchantType(message.getMerchantType());
        procedure.setPosEntryMode(message.getPointOfServiceEntryMode());
        procedure.setFromAccountNumber(message.getAccountNo1());
        procedure.setFromAccountType(null);
        procedure.setFromAccountCurrency(message.getTransactionCurrencyCode());
        procedure.setToAccountNumber(message.getAccountNo2());
        procedure.setCardAcceptorNameLocation(message.getCardAcceptorNameAndLocation());
        procedure.setCardAcceptorTerminalId(message.getCardAcceptorTerminalIdentification());
        procedure.setTransactionAmount(message.getTransactionAmount());
        procedure.setTransactionCurrency(message.getTransactionCurrencyCode());
        procedure.setTransactionPurpose(message.getPurposeOfPayment());
        procedure.setSourceImd(slice(recordData, RD_SOURCE_IMD_OFFSET, RD_IMD_LENGTH));
        procedure.setDestinationImd(slice(recordData, RD_DESTINATION_IMD_OFFSET, RD_IMD_LENGTH));
        // The DFS customer is being credited on this path.
        String identifier = slice(recordData, RD_IDENTIFIER_OFFSET, 1);
        procedure.setIdentifier(identifier.isEmpty() ? SwitchingConstants.IDENTIFIER_CREDIT : identifier);
        procedure.setRecordData(recordData);
        procedure.setTransactionFee(null);
        procedure.setUdf1(slice(recordData, RD_TO_ACCOUNT_TITLE_OFFSET, RD_TO_ACCOUNT_TITLE_LENGTH));
        return procedure;
    }

    /** Null-safe, bounds-safe fixed-width read of a DE-120 sub-field. */
    private String slice(String recordData, int offset, int length) {
        if (recordData == null || recordData.length() <= offset) {
            return "";
        }
        int end = Math.min(recordData.length(), offset + length);
        return recordData.substring(offset, end).trim();
    }
}
