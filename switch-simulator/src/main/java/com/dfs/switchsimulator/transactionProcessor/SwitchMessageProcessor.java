package com.dfs.switchsimulator.transactionProcessor;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.*;
import org.apache.mina.core.service.*;
import org.apache.mina.core.session.*;
import org.apache.mina.transport.socket.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.*;
import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.dto.PDUWrapper;
import com.dfs.switchsimulator.dto.response.*;
import com.dfs.switchsimulator.enums.*;
import com.dfs.switchsimulator.parser.*;
import com.dfs.switchsimulator.pools.TransactionResponsePool;
import com.dfs.switchsimulator.transactionProcessor.impl.TransactionServiceImpl;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

@Component("switchMessageProcessor")
@Lazy
public class SwitchMessageProcessor extends IoHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(SwitchMessageProcessor.class.getSimpleName());
    @Autowired
    TransactionServiceImpl transactionService;
    @Autowired
    private TransactionResponsePool responsePool;

    /**
     * DE-03 for the two Utility Bill Payment messages. Configured rather than hardcoded because
     * the UBP section of the 1LINK spec is not in this repository, and because the gateway must be
     * pointed at the same two values.
     */
    @Value("${switch.simulator.processing-code.bill-inquiry}")
    private String billInquiryProcessingCode;

    @Value("${switch.simulator.processing-code.bill-payment}")
    private String billPaymentProcessingCode;


    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception {
        logger.error("Exception occured at server.");
        logger.error("Exception", cause);
    }

    private void msgProcessor( IoSession session, Object message ) throws InterruptedException {
        // super.messageReceived(session, message);
        logger.debug("Message Received at Middleware Mock Server");
        logger.debug("Message: " + message);

        byte[] packet = (byte[]) message;
        BasePdu pdu = ISO8583MessageParser.parse(packet);

//
        if (pdu == null) {
            logger.error("######## PDU is not recognized ###");
        } else {
            logger.info("######## PDU Sending MiddlewareClient CreditResponse Back ###");

        }

        try {
            BasePdu transactionPdu = processIntegrationTransactions(pdu);  // check MTI request which request is type of recevend over there
            if (transactionPdu != null) {

                session.write(transactionPdu);
            } else {
                // No MBCreditResponse to send
            }
        } catch (Exception e) {
            logger.error("Erro", e);
        }
        System.out.println();

    }

    public BasePdu processIntegrationTransactions( BasePdu pdu ) {
        BasePdu transactionPdu = null;
        String messageType = pdu.getHeader().getMessageType();
        String procCode = pdu.getProcessingCode();
        try {

            if (messageType.equals("0200") && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
                if (procCode.equals("620000")) {
                    IbftTitleFetchResponse ibftTitleFetchResponse = transactionService.generateTileFetchResponse(pdu);
                    if (ibftTitleFetchResponse != null) {
                        ibftTitleFetchResponse.build();
                        transactionPdu = ibftTitleFetchResponse;
                    }
                } else if (procCode.equals(billInquiryProcessingCode) || procCode.equals(billPaymentProcessingCode)) {
                    // Utility Bill Payment: the two messages differ only in DE-03.
                    boolean isPayment = procCode.equals(billPaymentProcessingCode);
                    BillResponse billResponse = transactionService.generateBillResponse(pdu, isPayment);
                    if (billResponse != null) {
                        billResponse.build();
                        transactionPdu = billResponse;
                    }
                }


            }else
            if (messageType.equals("0230") && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
                if (procCode.equals(String.valueOf(ProcessingCodeEnum.IBFT_ADVICE.getValue()))) {

                    PDUWrapper pduWrapper = new PDUWrapper();
                    pduWrapper.setBasePDU(pdu);
                    logger.info("IBFT ADVICE RESPONSE : " + pdu.toString());

                    String transactionKey = pdu.getRrn();
                    pduWrapper.setRRNKey(transactionKey);
                    pduWrapper.setPoolTimeIn(new Date());

                    this.responsePool.put(pduWrapper);
                }
            }
            else if (messageType.equals("0220") && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
                if (procCode.equals(String.valueOf(ProcessingCodeEnum.IBFT_ADVICE.getValue()))) {

                    IbftAdviceResponse ibftAdviceResponse = transactionService.generateIbftAdviceResponse(pdu);
                    if (ibftAdviceResponse != null) {
                        ibftAdviceResponse.build();
                        transactionPdu = ibftAdviceResponse;
                    }

                }
            }
            if (messageType.equals("0210") && StringUtils.isNotEmpty(pdu.getProcessingCode())) {
                if (procCode.equals("620000")
                        || procCode.equals(billInquiryProcessingCode)
                        || procCode.equals(billPaymentProcessingCode)) {
                    PDUWrapper pduWrapper = new PDUWrapper();
                    pduWrapper.setBasePDU(pdu);
                    logger.info("Response : " + pdu.toString());

                    String transactionKey = pdu.getRrn();
                    pduWrapper.setRRNKey(transactionKey);
                    pduWrapper.setPoolTimeIn(new Date());

                    this.responsePool.put(pduWrapper);
                }


            } else if (messageType.equals("0800")) {
                if (pdu.getNetworkManagementCode().equals("001")) {  // sign in
//                if (pdu.getNetworkIdentifier().equals("001")) {  // sign in
                    EchoResponse echoResponse = transactionService.signInResponse(pdu);
                    if (echoResponse != null) {
                        echoResponse.build();
                        transactionPdu = echoResponse;
                    }
//                } else if (pdu.getNetworkIdentifier().equals("002")) {  // sigoff  58 code  for sigup
                } else if (pdu.getNetworkManagementCode().equals("002")) {  // sigoff  58 code  for sigup
                    EchoResponse echoResponse = transactionService.signOffResponse(pdu);
                    if (echoResponse != null) {
                        echoResponse.build();
                        transactionPdu = echoResponse;
                    }
//                } else if (pdu.getNetworkIdentifier().equals("003")) {  // check the heartbeat
                } else if (pdu.getNetworkManagementCode().equals("003")) {  // check the heartbeat
                    EchoResponse echoResponse = transactionService.generateEchoResponse(pdu);
                    if (echoResponse != null) {
                        echoResponse.build();
                        transactionPdu = echoResponse;
                    }
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return transactionPdu;
    }

    @Override
    public void messageReceived( final IoSession session, final Object message ) throws Exception {

//        ExecutorService es = Executors.newCachedThreadPool();
//        final Future<Object> future = es.submit(new Callable<Object>() {
//            public Object call() throws Exception {
//                msgProcessor(session, message);
//                return null;
//            }
//        });

        CompletableFuture.runAsync(() -> {
            try {
                msgProcessor(session, message);
            } catch (Exception e) {
                logger.error("Exception during processing", e);
            }
        });
    }

    //
    @Override
    public void messageSent( IoSession session, Object message ) throws Exception {
        super.messageSent(session, message);
        logger.debug("Sending Message: " + message);
    }

    @Override
    public void sessionClosed( IoSession session ) throws Exception {
        super.sessionClosed(session);
        logger.debug("Session being closed");
    }

    //
    private Map<String, String> acl = new HashMap<String, String>();

    @Override
    public void sessionCreated( IoSession session ) throws Exception {
        // super.sessionCreated(session);
        logger.info("Session being created for Host Server | SessionId: {}", new Object[]{session.getId()});

    }

    @Override
    /**
     * WHEN EVER SESSION (READING, WRITING) IS IDLE, SEND ECHOTEST MESSAGE
     * TO ENSURE THE CONNECTIVITY WITH
     */
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
        logger.debug("Idle Session detected. Sending Echo Message from server.");
//		// Send Echo if session is idle
//        EchoRequest request = new EchoRequest();
//        request.setStan("123456");
//        request.setTransactionDate("1207202310");
//        request.setNetworkManagementCode("003");
//        request.build();
//        logger.info("request: "+request.toString());
//        session.write(request);
    }

    @Override
    public void sessionOpened( IoSession session ) throws Exception {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
        ((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);
        ((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
        ((SocketSessionConfig) session.getConfig()).setReuseAddress(true);
        session.getConfig().setUseReadOperation(true);
    }

    public void prepareResponseHeader( BaseHeader requestHeader, BaseHeader responseHeader ) {
        // responseHeader.setMessageType(MessageTypeEnum.MT_0210.getValue());
        // responseHeader.setPan(requestHeader.getPan());
        // responseHeader.setTransCode(requestHeader.getTransCode());
        // responseHeader.setTransmissionDateTime(requestHeader.getTransmissionDateTime());
    }


    public BasePdu incomingRequest( BasePdu pdu ) throws ParseException {
        return transactionService.generateIncomingTileFetchRequest(pdu);
    }
    public BasePdu incomingIbftAdviceRequest( BasePdu pdu ) throws ParseException {
        return transactionService.generateIncomingIbftAdviceRequest(pdu);
    }
    public void closeallsession( ) {
         transactionService.dropSession();
    }
    
}
