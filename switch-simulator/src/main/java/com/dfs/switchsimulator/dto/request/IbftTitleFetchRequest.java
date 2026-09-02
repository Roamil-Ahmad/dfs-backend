package com.dfs.switchsimulator.dto.request;



import org.apache.commons.lang3.*;
import org.jpos.iso.*;
import org.jpos.iso.packager.*;
import org.slf4j.*;
import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.utils.ISO8583Utils;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static com.dfs.switchsimulator.common.Utils.logISOMessage;
import static com.dfs.switchsimulator.enums.ISO8583FieldEnum.*;


public class IbftTitleFetchRequest extends BasePdu {

    private static final long serialVersionUID = 4288733817973675911L;
    private static Logger logger = LoggerFactory.getLogger(IbftTitleFetchRequest.class.getSimpleName());

    public IbftTitleFetchRequest() {
        if (getHeader() == null) {
            this.setHeader(new BaseHeader());
        }
    }

    public void build() {

        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();
            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);
            msg.setMTI(getHeader().getMessageType());
            msg.set(PAN.getValue(), trimToEmpty(getPan()));//2
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));//3
            msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));//4
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));//7
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));//11
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));//12
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));//13
            msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));//15
            msg.set(MERCHANT_TYPE.getValue(), trimToEmpty((getMerchantType())));//18
            msg.set(POS_ENTRY_MODE.getValue(), trimToEmpty(getPointOfServiceEntryMode()));//22
            msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));//24
            msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));//32
            msg.set(RRN.getValue(), trimToEmpty(getRrn()));//37
            msg.set(TERMINAL_ID.getValue(), trimToEmpty(getCardAcceptorTerminalIdentification()));//41
            msg.set(CARD_ACCEPTOR_IDENTIFICATION_CODE.getValue(), trimToEmpty(getCardAcceptorIdentificationCode()));//42
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), getCardAcceptorNameAndLocation());//43
            msg.set(Additional_Data.getValue(), getPurposeOfPayment());//48
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(), getTransactionCurrencyCode());//49
            msg.set(ACCOUNT_NO_1.getValue(), trimToEmpty(getAccountNo1()));//102
            msg.set(ACCOUNT_NO_2.getValue(), trimToEmpty(getAccountNo2()));//103
            msg.set(RECORD_DATA.getValue(), getRecordData());//120
            com.dfs.switchsimulator.common.Utils.logIsoMessage("BUILT IbftTitleFetchRequest -> BANK", msg);
            body = msg.pack();
            logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.setRawPdu(ArrayUtils.addAll(header, body));

    }
}
