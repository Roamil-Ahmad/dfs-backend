package com.dfs.switchsimulator.dto.response;


import org.apache.commons.lang3.*;
import org.jpos.iso.*;
import org.jpos.iso.packager.*;
import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.utils.ISO8583Utils;

import static org.apache.commons.lang3.StringUtils.*;
import static com.dfs.switchsimulator.enums.ISO8583FieldEnum.*;

public class EchoResponse extends BasePdu {
    private static final long serialVersionUID = -1352911653475030358L;

    public EchoResponse() {
        if (getHeader() == null) {
            setHeader(new BaseHeader());
        }
    }

    public void build() {

        byte[] header = null;
        byte[] body = null;
        try {
            getHeader().setMessageType(getHeader().getMessageType());
            header = getHeader().build().getBytes();

            GenericPackager packager = ISO8583Utils.getISOPackager();
            ISOMsg msg = packager.createISOMsg();
            msg.setPackager(packager);
            msg.setMTI(getHeader().getMessageType());
            msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
            msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
            msg.set(NETWORK_MANAGEMENT_CODE.getValue(), trimToEmpty(getNetworkManagementCode()));
            msg.set(RESPONSE_CODE.getValue(), getResponseCode());

            com.dfs.switchsimulator.common.Utils.logIsoMessage("BUILT EchoResponse -> BANK", msg);

            body = msg.pack();
            new Utils().logISOMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.setRawPdu(ArrayUtils.addAll(header, body));
    }

}
