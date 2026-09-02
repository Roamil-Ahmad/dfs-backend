package com.dfs.switchsimulator.dto.response;


import org.apache.commons.lang3.*;
import org.jpos.iso.*;
import org.jpos.iso.packager.*;
import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.utils.ISO8583Utils;

import static org.apache.commons.lang3.StringUtils.*;
import static com.dfs.switchsimulator.enums.ISO8583FieldEnum.*;

public class IbftAdviceResponse extends BasePdu {

    public IbftAdviceResponse() {
        setHeader(new BaseHeader());
    }

    private static final long serialVersionUID = 1895396333666913997L;

    public void build() {
        byte[] header = null;
        byte[] body = null;
        try {
            header = getHeader().build().getBytes();
/*
----ISO MESSAGE-----
BitMap : 01111001000111010010000010000000110001110101000001000000000000000000000000000000000000000000000000000011000000000000000010000000
 MTI : 0230
    DE-2 : 2211660000012345
    DE-3 : 480000
    DE-4 : 000000100000
    DE-7 : 0102120007
    DE-11 : 283653
    DE-12 : 120007
    DE-13 : 0102
    DE-15 : 0103
    DE-18 : 0003
    DE-24 : 001
    DE-32 : 99998200000
    DE-33 : 000001
    DE-37 : 021200283653
    DE-38 : 528605
    DE-39 : 00
    DE-41 : 21021212
    DE-43 : 1LINK-UAT                FOREE IBFT   PK
    DE-49 : 586
    DE-102 : 1234567891112
    DE-103 : 00020000011003325
    DE-120 : 00020000011003325   SYED gfgdfdfgfd//gdf  fdgdFAIS9999820000016049800000C                    1LINK PAK TOWER          AMMAR KAZMI                   000000000000000000000000

 */
            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);
            msg.setMTI("0230");
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
            msg.set(ACQUIRER_IDENTIFICATION.getValue(),trimToEmpty(getAcquirerIdentification()));
            msg.set(FORWARDING_INST_IC.getValue(),trimToEmpty(getForwardingInsIdenCode()));
            msg.set(NETWORK_IDENTIFIER.getValue(), trimToEmpty(getNetworkIdentifier()));
            msg.set(RRN.getValue(), trimToEmpty(getRrn()));
            msg.set(AUTH_ID_RESPONSE.getValue(),trimToEmpty(getAuthIdResponse()));
            msg.set(RESPONSE_CODE.getValue(), trimToEmpty(getResponseCode()));
            msg.set(TERMINAL_ID.getValue(),trimToEmpty(getCardAcceptorTerminalIdentification()));
            msg.set(CARD_ACCEPTOR_IDENTIFICATION_CODE.getValue(),trimToEmpty(getCardAcceptorIdentificationCode()));
            msg.set(CARD_ACCEPTOR_NAME_AND_LOCATION.getValue(),trimToEmpty(getCardAcceptorNameAndLocation()));
            msg.set(ADDITIONAL_RESPONSE_DATA_PRIVATE.getValue(),trimToEmpty(getPurposeOfPayment()));
            msg.set(TRANSACTION_CURRENCY_CODE.getValue(), trimToEmpty(getTransactionCurrencyCode()));
            msg.set(ACCOUNT_NO_1.getValue(), getAccountNo1());
            msg.set(ACCOUNT_NO_2.getValue(),getAccountNo2());
            msg.set(RECORD_DATA.getValue(), getRecordData());



            com.dfs.switchsimulator.common.Utils.logIsoMessage("BUILT IbftAdviceResponse -> BANK", msg);



            body = msg.pack();
            Utils.logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));
    }
}
