package com.dfs.switchsimulator.dto.request;



import org.apache.commons.lang3.*;
import org.jpos.iso.*;
import org.jpos.iso.packager.*;
import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.utils.ISO8583Utils;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static com.dfs.switchsimulator.enums.ISO8583FieldEnum.*;


public class IbftAdviceRequest extends BasePdu {
    private static final long serialVersionUID = 4288733817973675911L;

    public IbftAdviceRequest() {
        if (getHeader() == null) {
            this.setHeader(new BaseHeader());
        }
    }

    public void assemble() {
        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);
            msg.setMTI(getHeader().getMessageType());
            msg.set(PAN.getValue(), trimToEmpty(getPan()));
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));
            msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));
            msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));
            msg.set(MERCHANT_TYPE.getValue(), trimToEmpty(getMerchantType()));
            msg.set(POS_ENTRY_MODE.getValue(), trimToEmpty(getPointOfServiceEntryMode()));
            msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
            msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));
            msg.set(FORWARDING_INST_IC.getValue(),getForwardingInsIdenCode());
            msg.set(RRN.getValue(), trimToEmpty(getRrn()));
            msg.set(AUTH_ID_RESPONSE.getValue(), trimToEmpty(getAuthIdResponse()));
            msg.set(TERMINAL_ID.getValue(), trimToEmpty(getCardAcceptorTerminalIdentification()));
            msg.set(CARD_ACCEPTOR_IDENTIFICATION_CODE.getValue(),trimToEmpty(getCardAcceptorIdentificationCode()));
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(),trimToEmpty(getCardAcceptorNameAndLocation()));
            msg.set(Additional_Data.getValue(), trimToEmpty(getPurposeOfPayment()));
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(), trimToEmpty(getTransactionCurrencyCode()));
            msg.set(ACCOUNT_NO_1.getValue(), trimToEmpty(getAccountNo1()));
            msg.set(ACCOUNT_NO_2.getValue(), trimToEmpty(getAccountNo2()));
            msg.set(RECORD_DATA.getValue(), (getRecordData()));
            com.dfs.switchsimulator.common.Utils.logIsoMessage("BUILT IbftAdviceRequest -> BANK", msg);
            body = msg.pack();
            Utils.logISOMessage(msg);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.setRawPdu(ArrayUtils.addAll(header, body));

    }
}
