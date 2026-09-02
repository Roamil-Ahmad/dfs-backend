package com.dfs.switchsimulator.transactionProcessor;

import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.dto.request.IbftAdviceRequest;
import com.dfs.switchsimulator.dto.request.IbftTitleFetchRequest;
import com.dfs.switchsimulator.dto.response.*;

import java.text.ParseException;

public interface TransactionService {

    EchoResponse generateEchoResponse( BasePdu pdu );

    EchoResponse signInResponse( BasePdu pdu );

    EchoResponse signOffResponse( BasePdu pdu );

    IbftTitleFetchResponse generateTileFetchResponse( BasePdu basePdu );

    IbftAdviceResponse generateIbftAdviceResponse( BasePdu basePdu );

    /**
     * Answers a Utility Bill Inquiry or Bill Payment 0200 with a 0210.
     *
     * @param isPayment true for a payment, so the answer reports the bill as settled
     */
    BillResponse generateBillResponse( BasePdu basePdu, boolean isPayment );

    IbftTitleFetchRequest generateIncomingTileFetchRequest(BasePdu basePdu ) throws ParseException;

    IbftAdviceRequest generateIncomingIbftAdviceRequest(BasePdu basePdu ) throws ParseException;

}
