package com.dfs.switchsimulator.dto.response;


import org.apache.commons.lang3.*;
import org.jpos.iso.*;
import org.jpos.iso.packager.*;
import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.utils.ISO8583Utils;

import static org.apache.commons.lang3.StringUtils.*;
import static com.dfs.switchsimulator.enums.ISO8583FieldEnum.*;

public class IbftTitleFetchResponse extends BasePdu {

    private static final long serialVersionUID = 1895396333666913997L;

    public void build() {
        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);
            msg.setMTI("0210");
            msg.set(PAN.getValue(),trimToEmpty(getPan()));//2
            msg.set(PROCESSING_CODE.getValue(),trimToEmpty(getProcessingCode()));//3
            msg.set(TRANSACTION_AMOUNT.getValue(),trimToEmpty(getTransactionAmount()));//4
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));//7
            msg.set(SYSTEMS_TRACE_NO.getValue(),trimToEmpty(getStan()));//11
            msg.set(TIME_LOCAL_TRANSACTION.getValue(),trimToEmpty(getTransactionLocalTime()));//12
            msg.set(DATE_LOCAL_TRANSACTION.getValue(),trimToEmpty(getTransactionLocalDate()));//13
            msg.set(SETTLEMENT_DATE.getValue(),trimToEmpty(getSettlementDate()));//15
            msg.set(MERCHANT_TYPE.getValue(),trimToEmpty(getMerchantType()));//18
            msg.set(POS_ENTRY_MODE.getValue(),trimToEmpty(getPointOfServiceEntryMode()));//22
            msg.set(NETWORK_IDENTIFIER.getValue(),trimToEmpty(getNetworkIdentifier()));//24
            msg.set(ACQUIRER_IDENTIFICATION.getValue(),trimToEmpty(getAcquirerIdentification()));//32
            msg.set(FORWARDING_INST_IC.getValue(),trimToEmpty(getForwardingInsIdenCode()));//33
            msg.set(RRN.getValue(),trimToEmpty(getRrn()));//37
            msg.set(AUTH_ID_RESPONSE.getValue(),trimToEmpty(getAuthIdResponse()));//38
            msg.set(RESPONSE_CODE.getValue(), getResponseCode());//39
            msg.set(TERMINAL_ID.getValue(),getCardAcceptorTerminalIdentification());//41
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(),getCardAcceptorNameAndLocation());//43
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(),getTransactionCurrencyCode());//49
            msg.set(ACCOUNT_NO_1.getValue(), getAccountNo1());
            msg.set(ACCOUNT_NO_2.getValue(),getAccountNo2());//103
            msg.set(RECORD_DATA.getValue(),getRecordData());//120


            com.dfs.switchsimulator.common.Utils.logIsoMessage("BUILT IbftTitleFetchResponse -> BANK", msg);


            body = msg.pack();
            new Utils().logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));
    }

}
