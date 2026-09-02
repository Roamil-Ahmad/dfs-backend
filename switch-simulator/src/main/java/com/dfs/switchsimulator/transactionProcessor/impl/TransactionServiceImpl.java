package com.dfs.switchsimulator.transactionProcessor.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.dfs.switchsimulator.common.BaseHeader;
import com.dfs.switchsimulator.common.BasePdu;
import com.dfs.switchsimulator.common.DateTools;
import com.dfs.switchsimulator.config.SwitchMockServer;
import com.dfs.switchsimulator.dto.request.IbftAdviceRequest;
import com.dfs.switchsimulator.dto.request.IbftTitleFetchRequest;
import com.dfs.switchsimulator.dto.response.BillResponse;
import com.dfs.switchsimulator.dto.response.EchoResponse;
import com.dfs.switchsimulator.dto.response.IbftAdviceResponse;
import com.dfs.switchsimulator.dto.response.IbftTitleFetchResponse;
import com.dfs.switchsimulator.enums.DATEENUM;
import com.dfs.switchsimulator.enums.ProcessingCodeEnum;
import com.dfs.switchsimulator.pools.TransactionResponsePoolHandler;
import com.dfs.switchsimulator.transactionProcessor.TransactionService;
import com.dfs.switchsimulator.utils.BillRecordDataLayout;
import com.dfs.switchsimulator.utils.RecordDataLayout;
import com.dfs.switchsimulator.utils.TransactionEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

/**
 * Mock behaviour of the external payment switch (1LINK).
 *
 * The simulator is deliberately free of DFS business logic: it does not debit or credit anything,
 * does not read the DFS database and does not invent customer data. Its whole job is to answer an
 * ISO 8583 message with a well-formed ISO 8583 message.
 *
 * Mock rule for both IBFT flows: echo the transaction fields back to the caller and answer with the
 * configured response code. In particular DE-120 (record data) is echoed, so the beneficiary title
 * that the Transaction Layer resolved from TBL_TRANSACTION_MOCK travels out and back unchanged
 * instead of being fabricated here.
 *
 * Message formats follow "1LINK ISO8583 Message Format - Data Element Definitions v7.0":
 * section 11.13 (Title Fetch, 0200/0210), section 11.10 (IBFT Advice, 0220/0230) and
 * section 9.62.1.1 (DE-120 record data layout).
 */
@Service("transactionServiceImpl")
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /** MTI values, 1LINK spec section 10.1. */
    private static final String MTI_TITLE_FETCH_REQUEST = "0200";
    private static final String MTI_TITLE_FETCH_RESPONSE = "0210";
    private static final String MTI_ADVICE_REQUEST = "0220";
    private static final String MTI_ADVICE_RESPONSE = "0230";
    private static final String MTI_NETWORK_RESPONSE = "0810";

    /** DE-39 response codes, 1LINK spec section 9.30. */
    private static final String RC_PROCESSED_OK = "00";

    /** DE-120 identifier, 1LINK spec 9.62.1.1 sub-field 5: C = credit leg of the transfer. */
    private static final String IDENTIFIER_CREDIT = "C";

    @Autowired
    private TransactionResponsePoolHandler responsePool;

    @Autowired
    private SwitchMockServer switchMockServer;

    @Value("${switch.simulator.acquirer-institution-code}")
    private String acquirerInstitutionCode;

    @Value("${switch.simulator.forwarding-institution-code}")
    private String forwardingInstitutionCode;

    @Value("${switch.simulator.card-acceptor-name}")
    private String cardAcceptorName;

    @Value("${switch.simulator.card-acceptor-city}")
    private String cardAcceptorCity;

    @Value("${switch.simulator.country-code}")
    private String countryCode;

    @Value("${switch.simulator.currency-code}")
    private String currencyCode;

    @Value("${switch.simulator.default-response-code}")
    private String defaultResponseCode;

    // ------------------------------------------------------------------ network management

    @Override
    public EchoResponse generateEchoResponse(BasePdu pdu) {
        return networkManagementResponse(pdu);
    }

    @Override
    public EchoResponse signInResponse(BasePdu pdu) {
        return networkManagementResponse(pdu);
    }

    @Override
    public EchoResponse signOffResponse(BasePdu pdu) {
        return networkManagementResponse(pdu);
    }

    private EchoResponse networkManagementResponse(BasePdu pdu) {
        EchoResponse echoResponse = new EchoResponse();
        echoResponse.getHeader().setMessageType(MTI_NETWORK_RESPONSE);
        echoResponse.setStan(pdu.getStan());
        echoResponse.setTransactionDate(pdu.getTransactionDate());
        echoResponse.setNetworkManagementCode(pdu.getNetworkManagementCode());
        echoResponse.setNetworkIdentifier(pdu.getNetworkIdentifier());
        echoResponse.setResponseCode(RC_PROCESSED_OK);
        return echoResponse;
    }

    // ------------------------------------------------------- outgoing (DFS acquires, switch answers)

    /**
     * Answers a 0200 / processing code 620000 Account Title Inquiry with a 0210.
     * DE-120 is echoed, so whatever beneficiary title the caller supplied comes back unchanged.
     */
    @Override
    public IbftTitleFetchResponse generateTileFetchResponse(BasePdu basePdu) {
        if (basePdu.getAccountNo2() == null) {
            logger.warn("Title fetch rejected: DE-103 (beneficiary account) is absent | STAN:{}", basePdu.getStan());
            return null;
        }

        BaseHeader baseHeader = new BaseHeader();
        baseHeader.setMessageType(MTI_TITLE_FETCH_RESPONSE);

        IbftTitleFetchResponse response = new IbftTitleFetchResponse();
        response.setHeader(baseHeader);
        copyCommonFields(basePdu, response);
        response.setSettlementDate(DateTools.nextDatetoString(new Date(), DATEENUM.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        response.setForwardingInsIdenCode(forwardingInstitutionCode);
        response.setAuthIdResponse(basePdu.getStan());
        response.setCardAcceptorNameAndLocation(cardAcceptorNameAndLocation());
        response.setAccountNo2(basePdu.getAccountNo2());
        response.setResponseCode(defaultResponseCode);
        response.setRecordData(RecordDataLayout.padAdvice(basePdu.getRecordData()));

        logger.info("Title fetch answered | STAN:{} | RRN:{} | DE-39:{}",
                response.getStan(), response.getRrn(), response.getResponseCode());
        return response;
    }

    /**
     * Answers a 0220 / processing code 480000 IBFT Advice with a 0230.
     * No account is credited here; posting is the responsibility of the receiving institution.
     */
    @Override
    public IbftAdviceResponse generateIbftAdviceResponse(BasePdu basePdu) {
        BaseHeader baseHeader = new BaseHeader();
        baseHeader.setMessageType(MTI_ADVICE_RESPONSE);

        IbftAdviceResponse response = new IbftAdviceResponse();
        response.setHeader(baseHeader);
        copyCommonFields(basePdu, response);
        response.setSettlementDate(DateTools.nextDatetoString(new Date(), DATEENUM.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        response.setForwardingInsIdenCode(forwardingInstitutionCode);
        response.setPurposeOfPayment(basePdu.getAdditionalDataprivate());
        response.setCardAcceptorName(basePdu.getCardAcceptorName());
        response.setAuthIdResponse(basePdu.getAuthIdResponse());
        response.setCardAcceptorNameAndLocation(basePdu.getCardAcceptorName());
        response.setCardAcceptorIdentificationCode(basePdu.getCardAcceptorIdentificationCode());
        response.setAccountNo1(StringUtils.rightPad(StringUtils.defaultString(basePdu.getAccountNo1()), 28, " "));
        response.setAccountNo2(StringUtils.rightPad(StringUtils.defaultString(basePdu.getAccountNo2()), 28, " "));
        response.setResponseCode(defaultResponseCode);
        response.setRecordData(RecordDataLayout.padAdvice(basePdu.getRecordData()));

        logger.info("IBFT advice answered | STAN:{} | RRN:{} | DE-39:{}",
                response.getStan(), response.getRrn(), response.getResponseCode());
        return response;
    }

    // ------------------------------------------------------------------ utility bill payment

    /**
     * Answers a Utility Bill Inquiry or Bill Payment 0200 with a 0210.
     *
     * The mock rule is the same as for IBFT: echo what was sent and answer with the configured
     * response code. The bill itself - company, consumer, amount, due date - comes back exactly as
     * the transaction layer resolved it from TBL_TRANSACTION_MOCK, so nothing is invented here. The
     * one field the simulator sets is the status sub-field, since only a payment settles the bill.
     */
    @Override
    public BillResponse generateBillResponse(BasePdu basePdu, boolean isPayment) {
        if (basePdu.getAccountNo2() == null) {
            logger.warn("Bill message rejected: DE-103 (consumer number) is absent | STAN:{}", basePdu.getStan());
            return null;
        }

        BaseHeader baseHeader = new BaseHeader();
        baseHeader.setMessageType(MTI_TITLE_FETCH_RESPONSE);

        BillResponse response = new BillResponse();
        response.setHeader(baseHeader);
        copyCommonFields(basePdu, response);
        response.setSettlementDate(DateTools.nextDatetoString(new Date(), DATEENUM.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        response.setForwardingInsIdenCode(forwardingInstitutionCode);
        response.setAuthIdResponse(basePdu.getStan());
        response.setCardAcceptorNameAndLocation(cardAcceptorNameAndLocation());
        response.setAccountNo1(StringUtils.defaultString(basePdu.getAccountNo1()));
        response.setAccountNo2(basePdu.getAccountNo2());
        response.setResponseCode(defaultResponseCode);

        String recordData = basePdu.getRecordData();
        response.setRecordData(BillRecordDataLayout.build(
                BillRecordDataLayout.companyCode(recordData),
                BillRecordDataLayout.companyName(recordData),
                BillRecordDataLayout.consumerNo(recordData),
                BillRecordDataLayout.billAmount(recordData),
                BillRecordDataLayout.dueDate(recordData),
                isPayment ? BillRecordDataLayout.STATUS_PAID : BillRecordDataLayout.STATUS_UNPAID));

        logger.info("Bill {} answered | STAN:{} | RRN:{} | company:{} | DE-39:{}",
                isPayment ? "payment" : "inquiry", response.getStan(), response.getRrn(),
                BillRecordDataLayout.companyCode(recordData), response.getResponseCode());
        return response;
    }

    private void copyCommonFields(BasePdu source, BasePdu target) {
        target.setPan(source.getPan());
        target.setProcessingCode(source.getProcessingCode());
        target.setTransactionAmount(source.getTransactionAmount());
        target.setTransactionDate(source.getTransactionDate());
        target.setStan(source.getStan());
        target.setRrn(source.getRrn());
        target.setTransactionCurrencyCode(source.getTransactionCurrencyCode());
        target.setTransactionLocalTime(source.getTransactionLocalTime());
        target.setTransactionLocalDate(source.getTransactionLocalDate());
        target.setMerchantType(source.getMerchantType());
        target.setPointOfServiceEntryMode(source.getPointOfServiceEntryMode());
        target.setNetworkIdentifier(source.getNetworkIdentifier());
        target.setAcquirerIdentification(source.getAcquirerIdentification());
        target.setCardAcceptorTerminalIdentification(source.getCardAcceptorTerminalIdentification());
    }

    /** DE-43 per 1LINK spec 9.33: name (25) + city (13) + country code (2), upper case, space padded. */
    private String cardAcceptorNameAndLocation() {
        return StringUtils.rightPad(StringUtils.upperCase(cardAcceptorName), 25, " ")
                + StringUtils.rightPad(StringUtils.upperCase(cardAcceptorCity), 13, " ")
                + StringUtils.upperCase(countryCode);
    }

    // ------------------------------------------------- incoming (switch acquires, DFS is the issuer)

    /**
     * Originates a 1LINK-to-DFS Account Title Inquiry: builds a 0200, pushes it down the ISO 8583
     * session held open by the gateway and waits for the matching 0210 in the response pool.
     */
    @Override
    public IbftTitleFetchRequest generateIncomingTileFetchRequest(BasePdu basePdu) throws ParseException {
        if (basePdu.getAccountNo2() == null) {
            logger.warn("Incoming title fetch rejected: DE-103 (beneficiary account) is absent");
            return null;
        }

        BaseHeader baseHeader = new BaseHeader();
        baseHeader.setMessageType(MTI_TITLE_FETCH_REQUEST);

        IbftTitleFetchRequest request = new IbftTitleFetchRequest();
        request.setHeader(baseHeader);
        request.setProcessingCode(String.valueOf(ProcessingCodeEnum.TITLE_FETCH.getValue()));
        applyIncomingCommonFields(basePdu, request);
        request.setAdditionalResponseData(basePdu.getPurposeOfPayment());
        request.setPurposeOfPayment(basePdu.getPurposeOfPayment());
        request.setTransactionTime(basePdu.getTransactionTime());
        request.setCardAcceptorIdentificationCode(basePdu.getCardAcceptorIdentificationCode());
        request.setAccountNo1(basePdu.getAccountNo1());
        request.setAccountNo2(RecordDataLayout.toAccountField(basePdu.getAccountNo2()));

        request.setRecordData(RecordDataLayout.buildTitleFetchRecordData(
                basePdu.getAccountNo2(),
                StringUtils.rightPad(StringUtils.defaultString(basePdu.getAcquirerIdentification()), 11, "0"),
                StringUtils.rightPad(StringUtils.defaultString(basePdu.getToBankImd()), 11, "0"),
                IDENTIFIER_CREDIT));

        logger.debug("Incoming title fetch built: {}", gson.toJson(request));
        request.build();

        BasePdu answer = dispatchAndAwait(request, request.getRrn());
        if (answer == null) {
            return null;
        }
        request.setRecordData(answer.getRecordData());
        request.setResponseCode(answer.getResponseCode());
        request.setBeneficaryId(RecordDataLayout.beneficiaryId(answer.getRecordData()));
        request.setToBankImd(RecordDataLayout.destinationImd(answer.getRecordData()));
        return request;
    }

    /**
     * Originates a 1LINK-to-DFS IBFT Advice: builds a 0220, pushes it down the ISO 8583 session and
     * waits for the matching 0230.
     */
    @Override
    public IbftAdviceRequest generateIncomingIbftAdviceRequest(BasePdu basePdu) throws ParseException {
        if (basePdu.getAccountNo2() == null) {
            logger.warn("Incoming IBFT advice rejected: DE-103 (beneficiary account) is absent");
            return null;
        }

        BaseHeader baseHeader = new BaseHeader();
        baseHeader.setMessageType(MTI_ADVICE_REQUEST);

        IbftAdviceRequest request = new IbftAdviceRequest();
        request.setHeader(baseHeader);
        request.setProcessingCode(String.valueOf(ProcessingCodeEnum.IBFT_ADVICE.getValue()));
        applyIncomingCommonFields(basePdu, request);
        request.setAcquirerIdentification(basePdu.getAcquirerIdentification());
        request.setDateExpiration(basePdu.getDateExpiration());
        request.setPurposeOfPayment(basePdu.getPurposeOfPayment());
        request.setCardAcceptorIdentificationCode(basePdu.getCardAcceptorIdentificationCode());
        request.setTransactionCurrencyCode(currencyCode);
        request.setSettlementCurrencyCode(currencyCode);
        request.setAccountNo1(StringUtils.rightPad(StringUtils.defaultString(basePdu.getAccountNo1()), 28, " "));
        request.setAccountNo2(RecordDataLayout.toAccountField(basePdu.getAccountNo2()));

        request.setRecordData(RecordDataLayout.buildAdviceRecordData(
                basePdu.getAccountNo2(),
                "",
                StringUtils.rightPad(StringUtils.defaultString(basePdu.getAcquirerIdentification()), 11, "0"),
                StringUtils.rightPad(StringUtils.defaultString(basePdu.getToBankImd()), 11, "0"),
                IDENTIFIER_CREDIT,
                "",
                "",
                StringUtils.defaultString(basePdu.getSenderName()),
                StringUtils.defaultString(basePdu.getAccountNo1()),
                StringUtils.defaultString(basePdu.getAccountNo2()),
                StringUtils.defaultString(basePdu.getSenderId()),
                StringUtils.defaultString(basePdu.getBeneficaryId()),
                countryCode,
                ""));

        request.assemble();

        BasePdu answer = dispatchAndAwait(request, request.getRrn());
        if (answer == null) {
            return null;
        }
        request.setResponseCode(answer.getResponseCode());
        request.setRecordData(answer.getRecordData());
        return request;
    }

    private void applyIncomingCommonFields(BasePdu source, BasePdu target) {
        Date now = DateTools.truncToSec(new Date());
        target.setPan(source.getPan());
        target.setTransactionAmount(source.getTransactionAmount());
        target.setTransactionDate(DateTools.dateToString(now, TransactionEnum.TRANSACTION_DATE_FORMAT.getValue()));
        target.setTransactionDateTime(DateTools.dateToString(now, TransactionEnum.TRANSACTION_DATE_FORMAT.getValue()));
        target.setTransactionLocalTime(source.getTransactionLocalTime());
        target.setTransactionLocalDate(source.getTransactionLocalDate());
        target.setDateExpiration(DateTools.dateExpiration(now, "YYMM"));
        target.setSettlementDate(DateTools.nextDatetoString(now, TransactionEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        target.setMerchantType(source.getMerchantType());
        target.setStan(source.getStan());
        target.setRrn(source.getRrn());
        target.setPointOfServiceEntryMode(source.getPointOfServiceEntryMode());
        target.setNetworkIdentifier(source.getNetworkIdentifier());
        target.setAcquirerIdentification(StringUtils.rightPad(StringUtils.defaultString(source.getAcquirerIdentification()), 11, "0"));
        target.setTransactionCurrencyCode(source.getTransactionCurrencyCode());
        target.setForwardingInsIdenCode(forwardingInstitutionCode);
        target.setAuthIdResponse(source.getStan());
        target.setCardAcceptorTerminalIdentification(source.getCardAcceptorTerminalIdentification());
        target.setCardAcceptorNameAndLocation(cardAcceptorNameAndLocation());
    }

    /** Writes the message to every open gateway session and blocks for the correlated answer. */
    private BasePdu dispatchAndAwait(BasePdu message, String rrn) {
        boolean sent = switchMockServer.sendDirectMessages(message);
        if (!sent) {
            logger.warn("No gateway session attached to the simulator, message not delivered | RRN:{}", rrn);
            return null;
        }
        BasePdu answer = responsePool.checkResponseInPool(rrn);
        if (answer == null) {
            logger.warn("No answer correlated within the pool window | RRN:{}", rrn);
        }
        return answer;
    }

    public void dropSession() {
        switchMockServer.closeSessions();
    }
}
