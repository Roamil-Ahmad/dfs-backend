package com.dfs.switchsimulator.dto.request;



import org.apache.commons.lang3.*;
import org.jpos.iso.*;
import org.jpos.iso.packager.*;
import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.utils.ISO8583Utils;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static com.dfs.switchsimulator.enums.ISO8583FieldEnum.*;

public class EchoRequest extends BasePdu {

	private static final long serialVersionUID = -6176881409492538220L;

	public EchoRequest() {
		if (getHeader() == null) {
			setHeader(new BaseHeader());
		}
	}

	public void build() {

		byte[] header = null;
		byte[] body = null;
		try {
			getHeader().setMessageType("0800");
			header = getHeader().build().getBytes();

			GenericPackager packager = ISO8583Utils.getISOPackager();
			ISOMsg msg = packager.createISOMsg();
			msg.setPackager(packager);
			msg.setMTI(getHeader().getMessageType());
			msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
			msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
			msg.set(NETWORK_MANAGEMENT_CODE.getValue(), trimToEmpty(getNetworkManagementCode()));

			com.dfs.switchsimulator.common.Utils.logIsoMessage("BUILT EchoRequest -> BANK", msg);

			body = msg.pack();
			Utils.logISOMessage(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		super.setRawPdu(ArrayUtils.addAll(header, body));
	}

}
