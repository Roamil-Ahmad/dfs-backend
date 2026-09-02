package com.dfs.switchsimulator.dto.response;

import com.dfs.switchsimulator.common.BasePdu;
import com.dfs.switchsimulator.common.Utils;
import com.dfs.switchsimulator.utils.ISO8583Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import static com.dfs.switchsimulator.enums.ISO8583FieldEnum.*;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 * 0210 answer to a Utility Bill Inquiry or Bill Payment.
 *
 * The same response serves both: they differ only in the DE-03 that is echoed back and in whether
 * DE-04 carries an amount.
 */
public class BillResponse extends BasePdu {

    private static final long serialVersionUID = 5583110455213348811L;

    public void build() {
        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);
            msg.setMTI("0210");
            msg.set(PAN.getValue(), trimToEmpty(getPan()));//2
            msg.set(PROCESSING_CODE.getValue(), trimToEmpty(getProcessingCode()));//3
            msg.set(TRANSACTION_AMOUNT.getValue(), trimToEmpty(getTransactionAmount()));//4
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));//7
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));//11
            msg.set(TIME_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalTime()));//12
            msg.set(DATE_LOCAL_TRANSACTION.getValue(), trimToEmpty(getTransactionLocalDate()));//13
            msg.set(SETTLEMENT_DATE.getValue(), trimToEmpty(getSettlementDate()));//15
            msg.set(MERCHANT_TYPE.getValue(), trimToEmpty(getMerchantType()));//18
            msg.set(POS_ENTRY_MODE.getValue(), trimToEmpty(getPointOfServiceEntryMode()));//22
            msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));//24
            msg.set(ACQUIRER_IDENTIFICATION.getValue(), trimToEmpty(getAcquirerIdentification()));//32
            msg.set(FORWARDING_INST_IC.getValue(), trimToEmpty(getForwardingInsIdenCode()));//33
            msg.set(RRN.getValue(), trimToEmpty(getRrn()));//37
            msg.set(AUTH_ID_RESPONSE.getValue(), trimToEmpty(getAuthIdResponse()));//38
            msg.set(RESPONSE_CODE.getValue(), getResponseCode());//39
            msg.set(TERMINAL_ID.getValue(), trimToEmpty(getCardAcceptorTerminalIdentification()));//41
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(), getCardAcceptorNameAndLocation());//43
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(), getTransactionCurrencyCode());//49
            msg.set(ACCOUNT_NO_1.getValue(), trimToEmpty(getAccountNo1()));//102
            msg.set(ACCOUNT_NO_2.getValue(), trimToEmpty(getAccountNo2()));//103
            msg.set(RECORD_DATA.getValue(), getRecordData());//120

            Utils.logIsoMessage("BUILT BillResponse -> BANK", msg);

            body = msg.pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));
    }
}
