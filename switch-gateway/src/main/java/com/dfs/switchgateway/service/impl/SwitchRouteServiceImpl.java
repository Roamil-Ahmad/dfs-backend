package com.dfs.switchgateway.service.impl;

import com.dfs.switchgateway.dto.BasePDU;
import com.dfs.switchgateway.dto.EchoDto;
import com.dfs.switchgateway.dto.IbftAdviceRqst;
import com.dfs.switchgateway.dto.TSDtos.BillRqst;
import com.dfs.switchgateway.dto.TSDtos.OTIBFTTitleFetchRequest;
import com.dfs.switchgateway.dto.TSDtos.ResponseDto;
import com.dfs.switchgateway.dto.TSDtos.responseDto.BillResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.EchoResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.IBFTAdviceResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.IBFTTitleFetchResponse;
import com.dfs.switchgateway.dto.build.BillRequestBuild;
import com.dfs.switchgateway.dto.build.IBFTTitleFetchRequestBuild;
import com.dfs.switchgateway.dto.build.OTIBFTAdviceRequestBuild;
import com.dfs.switchgateway.dto.build.OneLinkEchoRequest;
import com.dfs.switchgateway.dto.enums.MessageTypeEnum;
import com.dfs.switchgateway.dto.enums.TransactionCodeEnum;
import com.dfs.switchgateway.service.SwitchRouteService;
import com.dfs.switchgateway.soketClient.Service.NetworkInfoBean;
import com.dfs.switchgateway.soketClient.TransactionClient;
import com.dfs.switchgateway.utils.BillRecordDataLayout;
import com.dfs.switchgateway.utils.Constant;
import com.dfs.switchgateway.utils.DateTools;
import com.dfs.switchgateway.utils.ISO8583Utils;
import com.dfs.switchgateway.utils.RecordDataLayout;
import com.dfs.switchgateway.utils.ResponseCodes;
import com.dfs.switchgateway.validations.CSTransactionRequestValidator;
import com.dfs.switchgateway.validations.TransactionResponsePoolHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Outgoing route towards the switch.
 *
 * The whole responsibility is: validate the ISO 8583 shape of what arrived, build the message,
 * send it, wait for the correlated answer, map it back. No transaction decision is taken here - no
 * balance check, no beneficiary validation, no posting, no duplicate ruling. Those belong to the
 * transaction layer and to the database procedures.
 *
 * Message formats follow "1LINK ISO8583 Message Format - Data Element Definitions v7.0":
 * section 11.13 Title Fetch (0200/0210), section 11.10 IBFT Advice (0220/0230),
 * section 11.27 Network Management (0800/0810) and section 9.62.1.1 for DE-120.
 */
@Service
public class SwitchRouteServiceImpl implements SwitchRouteService {

    private static final Logger log = LoggerFactory.getLogger(SwitchRouteServiceImpl.class);

    private static final String IDENTIFIER_CREDIT = "C";

    @Autowired
    private TransactionClient transactionClient;

    @Autowired
    private TransactionResponsePoolHandler responsePool;

    @Autowired
    private NetworkInfoBean networkInfoBean;

    @Autowired
    private CSTransactionRequestValidator validator;

    @Value("${onelink.acquirer-institution-code}")
    private String acquirerInstitutionCode;

    @Value("${onelink.sender-country-code}")
    private String senderCountryCode;

    @Value("${onelink.originator-details}")
    private String originatorDetails;

    /**
     * DE-03 for the two Utility Bill Payment messages. Kept in configuration rather than in
     * {@code TransactionCodeEnum} because the UBP section of the 1LINK spec is not in this
     * repository; see {@link com.dfs.switchgateway.utils.BillRecordDataLayout}.
     */
    @Value("${onelink.processing-code.bill-inquiry}")
    private String billInquiryProcessingCode;

    @Value("${onelink.processing-code.bill-payment}")
    private String billPaymentProcessingCode;

    // ------------------------------------------------------------------ network management

    @Override
    public ResponseDto<EchoResponse> networkManagement(EchoDto request) {
        ResponseDto<EchoResponse> result = new ResponseDto<>();

        OneLinkEchoRequest message = new OneLinkEchoRequest();
        message.setStan(request.getStan());
        message.setTransactionDate(DateTools.currentDateToString(
                com.dfs.switchgateway.dto.enums.TransactionEnum.TRANSACTION_DATE_FORMAT.getValue()));
        message.setNetworkManagementCode(request.getNetworkManagementCode());
        message.setNetworkIdentifier(request.getNetworkIdentifier());
        message.build();

        log.info("Network management to switch | DE-70:{} | STAN:{}",
                request.getNetworkManagementCode(), request.getStan());
        transactionClient.sendMessage(message);

        // Network management is correlated on STAN, financial traffic on RRN (1LINK spec section 6).
        BasePDU answer = responsePool.awaitResponse(request.getStan());
        if (answer == null) {
            result.setCode(Constant.VERIFICATION_FAILED_CODE);
            result.setResponseCode(ResponseCodes.HOST_LINK_DOWN);
            result.setMessage("No answer from the switch for STAN " + request.getStan());
            return result;
        }

        EchoResponse echo = new EchoResponse();
        echo.setMti(answer.getHeader().getMessageType());
        echo.setNetworkIdentifier(request.getNetworkIdentifier());
        echo.setNetworkManagementCode(answer.getNetworkManagementCode());
        echo.setStan(answer.getStan());
        echo.setTransactionDate(answer.getTransactionDate());
        echo.setResponseCode(answer.getResponseCode());

        result.setData(echo);
        result.setCode(Constant.SUCCESS_CODE);
        result.setResponseCode(answer.getResponseCode());
        result.setMessage("Processed");
        return result;
    }

    // ------------------------------------------------------------------ outgoing title fetch

    @Override
    public ResponseDto<IBFTTitleFetchResponse> titleFetch(OTIBFTTitleFetchRequest request) throws Exception {
        ResponseDto<IBFTTitleFetchResponse> result = new ResponseDto<>();

        if (!networkInfoBean.isConnected()) {
            log.error("Title fetch refused, switch link is down | STAN:{}", request.getStan());
            return linkDown(result);
        }

        validator.validateIBFTTitleFetchRequest(request);

        IBFTTitleFetchRequestBuild message = new IBFTTitleFetchRequestBuild();
        message.getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());
        message.setProcessingCode(TransactionCodeEnum.IBFT_TITLE_FETCH.getValue());
        applyCommonFields(message, request.getPan(), request.getTransactionAmount(), request.getStan(),
                request.getRrn(), request.getMerchantType(), request.getPointOfEntry(),
                request.getNetworkIdentifier(), request.getCurrencyCode());
        message.setTerminalId(request.getCardAcceptorTerminalId());
        message.setCardAcceptorIdentificationCode(request.getCardAcceptorIdentificationCode());
        message.setCardAcceptorNameAndLocation(request.getCardAcceptorNameAndLocation());
        message.setPurposeOfPayment(request.getPurposeOfPayment());
        message.setAccountNo1(request.getAccountNo1());
        message.setAccountNo2(request.getAccountNo2());
        message.setRecordData(RecordDataLayout.buildTitleFetchRecordData(
                request.getAccountNo2(),
                acquirerInstitutionCode,
                request.getToBankImd(),
                IDENTIFIER_CREDIT));
        message.build();

        log.info("Title fetch to switch | STAN:{} | RRN:{} | toBankImd:{}",
                request.getStan(), request.getRrn(), request.getToBankImd());
        transactionClient.sendMessage(message);

        BasePDU answer = responsePool.awaitResponse(request.getRrn());
        if (answer == null) {
            result.setCode(Constant.BAD_REQUEST_CODE);
            result.setResponseCode(ResponseCodes.INTERNAL_ERROR);
            result.setMessage("No answer from the switch for RRN " + request.getRrn());
            result.setData(new IBFTTitleFetchResponse());
            return result;
        }

        result.setData(mapTitleFetch(request, answer));
        result.setResponseCode(answer.getResponseCode());
        result.setCode(Constant.SUCCESS_CODE);
        result.setMessage(Constant.SUCCESS);
        return result;
    }

    private IBFTTitleFetchResponse mapTitleFetch(OTIBFTTitleFetchRequest request, BasePDU answer) {
        String recordData = answer.getRecordData();

        IBFTTitleFetchResponse response = new IBFTTitleFetchResponse();
        response.setPan(answer.getPan());
        response.setStan(answer.getStan());
        response.setRrn(answer.getRrn());
        response.setResponseCode(answer.getResponseCode());
        response.setTransactionAmount(answer.getTransactionAmount());
        response.setTransmissionTime(new Date());
        response.setDateLocalTransaction(answer.getTransactionLocalDate());
        response.setTimeLocalTransaction(answer.getTransactionLocalTime());
        response.setMerchantType(answer.getMerchantType());
        response.setNetworkIdentifier(answer.getNetworkIdentifier());
        response.setPointOfEntry(answer.getPointOfServiceEntryMode());
        response.setCardAcceptorNameAndLocation(answer.getCardAcceptorNameAndLocation());
        response.setCardAcceptorTerminalId(request.getCardAcceptorTerminalId());
        response.setPurposeOfPayment(request.getPurposeOfPayment());
        response.setAccountNo1(request.getAccountNo1());
        response.setAccountNo2(request.getAccountNo2());

        // DE-120 sub-fields, 1LINK spec 9.62.1.1.
        response.setAccountTitle(RecordDataLayout.beneficiaryTitle(recordData));
        response.setToBankImd(RecordDataLayout.destinationImd(recordData));
        response.setIdentifier(RecordDataLayout.identifier(recordData));
        response.setBeneficiaryId(RecordDataLayout.beneficiaryId(recordData));
        return response;
    }

    // ------------------------------------------------------------------ outgoing IBFT advice

    @Override
    public ResponseDto<IBFTAdviceResponse> advice(IbftAdviceRqst request) throws Exception {
        ResponseDto<IBFTAdviceResponse> result = new ResponseDto<>();

        if (!networkInfoBean.isConnected()) {
            log.error("IBFT advice refused, switch link is down | STAN:{}", request.getStan());
            IBFTAdviceResponse empty = new IBFTAdviceResponse();
            result.setResponseCode(ResponseCodes.HOST_LINK_DOWN);
            result.setCode(Constant.SERVER_BAD_REQUEST);
            result.setMessage(Constant.SERVER_MSG);
            result.setData(empty);
            return result;
        }

        validator.validateIBFTAdviceRequest(request);

        OTIBFTAdviceRequestBuild message = new OTIBFTAdviceRequestBuild();
        message.getHeader().setMessageType(MessageTypeEnum.MT_0220.getValue());
        message.setProcessingCode(TransactionCodeEnum.IBFT_ADVICE.getValue());
        applyCommonFields(message, request.getPan(), request.getTransactionAmount(), request.getStan(),
                request.getRrn(), request.getMerchantType(), request.getPointOfEntry(),
                request.getNetworkIdentifier(), request.getCurrencyCode());
        message.setAuthIdResponse(request.getAuthIdResp());
        message.setTerminalId(request.getCardAcceptorTerminalId());
        message.setCardAcceptorIdentificationCode(request.getCardAcceptorIdentificationCode());
        message.setCardAcceptorNameAndLocation(request.getCardAcceptorNameAndLocation());
        message.setPurposeOfPayment(request.getPurposeOfPayment());
        message.setAccountNo1(request.getAccountNo1());
        message.setAccountNo2(request.getAccountNo2());
        message.setRecordData(RecordDataLayout.buildAdviceRecordData(
                request.getAccountNo2(),
                request.getAccountTitle(),
                acquirerInstitutionCode,
                request.getToBankImd(),
                IDENTIFIER_CREDIT,
                request.getAccountBankName(),
                request.getAccountBranchName(),
                request.getSenderName(),
                request.getAccountNo1(),
                request.getAccountNo2(),
                request.getSenderId(),
                request.getBeneficiaryId(),
                senderCountryCode,
                originatorDetails));
        message.assemble();

        log.info("IBFT advice to switch | STAN:{} | RRN:{} | toBankImd:{}",
                request.getStan(), request.getRrn(), request.getToBankImd());
        transactionClient.sendMessage(message);

        BasePDU answer = responsePool.awaitResponse(request.getRrn());

        IBFTAdviceResponse response = new IBFTAdviceResponse();
        response.setStan(request.getStan());
        response.setRrn(request.getRrn());
        response.setTransDateTime(request.getTransactionDateTime());
        response.setTransactionAmount(request.getTransactionAmount());
        response.setAccountNo1(request.getAccountNo1());
        response.setAccountNo2(request.getAccountNo2());
        response.setAccountTitle(request.getAccountTitle());
        response.setAccountBankName(request.getAccountBankName());
        response.setAccountBranchName(request.getAccountBranchName());
        response.setToBankImd(request.getToBankImd());
        response.setSenderName(request.getSenderName());
        response.setSenderId(request.getSenderId());
        response.setReceiverId(request.getBeneficiaryId());
        response.setPan(request.getPan());
        response.setPurposeOfPayment(request.getPurposeOfPayment());
        response.setCardAcceptorIdentificationCode(request.getCardAcceptorIdentificationCode());
        response.setCardAcceptorTerminalId(request.getCardAcceptorTerminalId());
        response.setCardAcceptorNameAndLocation(request.getCardAcceptorNameAndLocation());
        response.setAuthIdResp(request.getAuthIdResp());
        response.setNetworkIdentifer(request.getNetworkIdentifier());
        response.setMerchantType(request.getMerchantType());
        response.setIdentifier(IDENTIFIER_CREDIT);
        response.setTransmissionTime(DateTools.currentDateToString("HHmmss"));

        if (answer == null) {
            response.setResponseCode(ResponseCodes.INTERNAL_ERROR);
            result.setCode(Constant.BAD_REQUEST_CODE);
            result.setMessage("No answer from the switch for RRN " + request.getRrn());
        } else {
            response.setResponseCode(answer.getResponseCode());
            result.setCode(Constant.SUCCESS_CODE);
            result.setMessage(Constant.SUCCESS);
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response);
        return result;
    }

    // ------------------------------------------------------------------ utility bill payment

    @Override
    public ResponseDto<BillResponse> billInquiry(BillRqst request) throws Exception {
        return bill(request, billInquiryProcessingCode, false, "bill inquiry");
    }

    @Override
    public ResponseDto<BillResponse> billPayment(BillRqst request) throws Exception {
        return bill(request, billPaymentProcessingCode, true, "bill payment");
    }

    /**
     * Both bill messages are the same 0200 with a different DE-03; a payment additionally carries
     * the amount in DE-04 and the core's authorisation id in DE-38.
     */
    private ResponseDto<BillResponse> bill(BillRqst request, String processingCode, boolean isPayment,
                                           String description) throws Exception {
        ResponseDto<BillResponse> result = new ResponseDto<>();

        if (!networkInfoBean.isConnected()) {
            log.error("Bill message refused, switch link is down | STAN:{}", request.getStan());
            return linkDown(result);
        }

        validator.validateBillRequest(request, isPayment);

        BillRequestBuild message = new BillRequestBuild();
        message.getHeader().setMessageType(MessageTypeEnum.MT_0200.getValue());
        message.setProcessingCode(processingCode);
        applyCommonFields(message, request.getPan(), request.getTransactionAmount(), request.getStan(),
                request.getRrn(), request.getMerchantType(), request.getPointOfEntry(),
                request.getNetworkIdentifier(), request.getCurrencyCode());
        message.setAuthIdResponse(request.getAuthIdResp());
        message.setTerminalId(request.getCardAcceptorTerminalId());
        message.setCardAcceptorIdentificationCode(request.getCardAcceptorIdentificationCode());
        message.setCardAcceptorNameAndLocation(request.getCardAcceptorNameAndLocation());
        message.setAccountNo1(request.getAccountNo1());
        message.setAccountNo2(request.getConsumerNo());
        message.setRecordData(BillRecordDataLayout.build(
                request.getUtilityCompanyCode(),
                request.getUtilityCompanyName(),
                request.getConsumerNo(),
                request.getBillAmount(),
                request.getBillDueDate(),
                isPayment ? BillRecordDataLayout.STATUS_PAID : BillRecordDataLayout.STATUS_UNPAID));
        message.build();

        log.info("{} to switch | STAN:{} | RRN:{} | company:{} | DE-03:{}",
                description, request.getStan(), request.getRrn(), request.getUtilityCompanyCode(),
                processingCode);
        transactionClient.sendMessage(message);

        BasePDU answer = responsePool.awaitResponse(request.getRrn());

        BillResponse response = new BillResponse();
        response.setPan(request.getPan());
        response.setStan(request.getStan());
        response.setRrn(request.getRrn());
        response.setUtilityCompanyCode(request.getUtilityCompanyCode());
        response.setUtilityCompanyName(request.getUtilityCompanyName());
        response.setConsumerNo(request.getConsumerNo());
        response.setBillAmount(request.getBillAmount());
        response.setBillDueDate(request.getBillDueDate());
        response.setAccountNo1(request.getAccountNo1());
        response.setMerchantType(request.getMerchantType());
        response.setNetworkIdentifier(request.getNetworkIdentifier());
        response.setPointOfEntry(request.getPointOfEntry());
        response.setCardAcceptorNameAndLocation(request.getCardAcceptorNameAndLocation());
        response.setCardAcceptorTerminalId(request.getCardAcceptorTerminalId());
        response.setTransactionAmount(request.getTransactionAmount());

        if (answer == null) {
            response.setResponseCode(ResponseCodes.INTERNAL_ERROR);
            result.setCode(Constant.BAD_REQUEST_CODE);
            result.setMessage("No answer from the switch for RRN " + request.getRrn());
        } else {
            String recordData = answer.getRecordData();
            response.setResponseCode(answer.getResponseCode());
            response.setAuthIdResp(answer.getAuthIdResponse());
            response.setDateLocalTransaction(answer.getTransactionLocalDate());
            response.setTimeLocalTransaction(answer.getTransactionLocalTime());
            // The biller's own view of the bill wins over what we sent, when it supplies one.
            if (!BillRecordDataLayout.billAmount(recordData).isEmpty()) {
                response.setBillAmount(BillRecordDataLayout.billAmount(recordData));
            }
            if (!BillRecordDataLayout.companyName(recordData).isEmpty()) {
                response.setUtilityCompanyName(BillRecordDataLayout.companyName(recordData));
            }
            if (!BillRecordDataLayout.dueDate(recordData).isEmpty()) {
                response.setBillDueDate(BillRecordDataLayout.dueDate(recordData));
            }
            response.setBillStatus(BillRecordDataLayout.status(recordData));
            result.setCode(Constant.SUCCESS_CODE);
            result.setMessage(Constant.SUCCESS);
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response);
        return result;
    }

    // ------------------------------------------------------------------ shared field mapping

    /** Fills the data elements every 1LINK financial message carries. */
    private void applyCommonFields(BasePDU message, String pan, String amount, String stan, String rrn,
                                   String merchantType, String pointOfEntry, String networkIdentifier,
                                   String currencyCode) {
        Date now = DateTools.truncToSec(new Date());
        message.setPan(pan);
        message.setTransactionAmount(ISO8583Utils.formatISO8583Amount(amount));
        message.setStan(stan);
        message.setRrn(rrn);
        message.setTransactionDate(DateTools.dateToString(now,
                com.dfs.switchgateway.dto.enums.TransactionEnum.TRANSACTION_DATE_FORMAT.getValue()));
        message.setTransactionLocalTime(DateTools.dateToString(now,
                com.dfs.switchgateway.dto.enums.TransactionEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        message.setTransactionLocalDate(DateTools.dateToString(now,
                com.dfs.switchgateway.dto.enums.TransactionEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        message.setSettlementDate(DateTools.nextDatetoString(now,
                com.dfs.switchgateway.dto.enums.TransactionEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
        message.setMerchantType(merchantType);
        message.setPointOfServiceEntryMode(pointOfEntry);
        message.setNetworkIdentifier(networkIdentifier);
        message.setTransactionCurrencyCode(currencyCode);
        message.setAcquirerIdentification(StringUtils.rightPad(acquirerInstitutionCode, 11, "0"));
    }

    private <T> ResponseDto<T> linkDown(ResponseDto<T> result) {
        result.setCode(Constant.SERVER_BAD_REQUEST);
        result.setResponseCode(ResponseCodes.HOST_LINK_DOWN);
        result.setMessage(Constant.SERVER_MSG);
        return result;
    }
}
