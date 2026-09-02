package com.dfs.switchgateway.soketClient;

import com.dfs.switchgateway.dto.BasePDU;
import com.dfs.switchgateway.dto.PDUWrapper;
import com.dfs.switchgateway.dto.enums.MessageTypeEnum;
import com.dfs.switchgateway.dto.build.IBFTAdviceResponseBuild;
import com.dfs.switchgateway.dto.build.IBFTTitleFetchResponseBuild;
import com.dfs.switchgateway.dto.enums.TransactionCodeEnum;
import com.dfs.switchgateway.dto.parser.ISO8583MessageParser;
import com.dfs.switchgateway.service.TransactionLayerClient;
import com.dfs.switchgateway.soketClient.Service.NetworkInfoBean;
import com.dfs.switchgateway.validations.TransactionResponsePool;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * ISO 8583 session handler for the link towards the switch.
 *
 * Two directions cross this handler:
 *
 * <ul>
 *   <li><b>Outgoing responses</b> - 0210 / 0230 / 0810 arriving as answers to messages this
 *       gateway sent. They are parked in {@link TransactionResponsePool}, keyed by RRN (STAN for
 *       network management), where the calling thread is blocking for them.</li>
 *   <li><b>Incoming requests</b> - 0200 / 0220 pushed by the switch because another bank is
 *       sending funds to DFS. They are handed to the transaction layer over HTTP and its answer is
 *       written straight back onto the socket.</li>
 * </ul>
 *
 * The handler carries no business logic: it decides nothing about accounts, balances, duplicates or
 * postings. There is no store-and-forward and no retry - a failure is logged and answered.
 */
@Component("TransactionClientHandler")
public class TransactionClientHandler extends IoHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(TransactionClientHandler.class);

    @Autowired
    private TransactionResponsePool responsePool;

    @Autowired
    private NetworkInfoBean networkInfoBean;

    @Autowired
    private TransactionLayerClient transactionLayerClient;

    @Value("${switch.idle-time-seconds}")
    private int idleTimeSeconds;

    @Override
    public void sessionOpened(IoSession session) {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, idleTimeSeconds);
        SocketSessionConfig config = (SocketSessionConfig) session.getConfig();
        config.setTcpNoDelay(true);
        config.setKeepAlive(true);
        config.setReuseAddress(true);
        networkInfoBean.setConnected(true);
        networkInfoBean.setIoSession(session);
        log.info("Switch session opened | sessionId:{}", session.getId());
    }

    @Override
    public void sessionClosed(IoSession session) {
        networkInfoBean.setConnected(false);
        log.warn("Switch session closed | sessionId:{} | bytesRead:{}", session.getId(), session.getReadBytes());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        log.debug("Switch session idle | sessionId:{} | status:{}", session.getId(), status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        log.error("Switch session error | sessionId:{}", session.getId(), cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        CompletableFuture.runAsync(() -> handle(session, message));
    }

    private void handle(IoSession session, Object message) {
        BasePDU pdu;
        try {
            pdu = ISO8583MessageParser.parse((byte[]) message);
        } catch (Exception e) {
            log.error("Unable to parse ISO 8583 message from the switch", e);
            return;
        }
        if (pdu == null || pdu.getHeader() == null) {
            log.error("Unrecognised ISO 8583 message from the switch, discarded");
            return;
        }

        String mti = pdu.getHeader().getMessageType();
        String processingCode = pdu.getProcessingCode();
        log.info("Received from switch | MTI:{} | DE-03:{} | STAN:{} | RRN:{} | DE-39:{}",
                mti, processingCode, pdu.getStan(), pdu.getRrn(), pdu.getResponseCode());

        if (MessageTypeEnum.MT_0810.getValue().equals(mti)) {
            park(pdu, pdu.getStan());
        } else if (MessageTypeEnum.MT_0210.getValue().equals(mti)
                || MessageTypeEnum.MT_0230.getValue().equals(mti)) {
            park(pdu, pdu.getRrn());
        } else if (MessageTypeEnum.MT_0200.getValue().equals(mti)
                || MessageTypeEnum.MT_0220.getValue().equals(mti)) {
            forwardIncoming(session, pdu, mti, processingCode);
        } else {
            log.warn("No route for MTI {} received from the switch | STAN:{}", mti, pdu.getStan());
        }
    }

    /** Parks an answer so the blocked caller in the response pool handler can pick it up. */
    private void park(BasePDU pdu, String key) {
        if (StringUtils.isBlank(key)) {
            log.error("Answer from the switch has no correlation key, discarded | MTI:{}",
                    pdu.getHeader().getMessageType());
            return;
        }
        PDUWrapper wrapper = new PDUWrapper();
        wrapper.setBasePDU(pdu);
        wrapper.setRRNKey(key);
        wrapper.setPoolTimeIn(new Date());
        responsePool.put(wrapper);
    }

    /**
     * Hands a switch-originated request to the transaction layer and writes its answer back.
     * The transaction layer owns the decision and the posting; this method only carries the message.
     */
    private void forwardIncoming(IoSession session, BasePDU pdu, String mti, String processingCode) {
        try {
            BasePDU answer;
            if (TransactionCodeEnum.IBFT_TITLE_FETCH.getValue().equals(processingCode)) {
                log.info("Incoming title fetch from switch | STAN:{} | RRN:{}", pdu.getStan(), pdu.getRrn());
                answer = transactionLayerClient.incomingTitleFetch(pdu);
            } else if (TransactionCodeEnum.IBFT_ADVICE.getValue().equals(processingCode)) {
                log.info("Incoming IBFT advice from switch | STAN:{} | RRN:{}", pdu.getStan(), pdu.getRrn());
                answer = transactionLayerClient.incomingAdvice(pdu);
            } else {
                log.warn("Incoming MTI {} with unsupported DE-03 {} | STAN:{}", mti, processingCode, pdu.getStan());
                return;
            }

            if (answer == null) {
                log.error("Transaction layer returned nothing for incoming {} | STAN:{} | RRN:{}",
                        processingCode, pdu.getStan(), pdu.getRrn());
                return;
            }
            BasePDU outbound = toResponseMessage(answer, mti);
            session.write(outbound);
            log.info("Answered incoming {} | STAN:{} | RRN:{} | DE-39:{}",
                    processingCode, answer.getStan(), answer.getRrn(), answer.getResponseCode());
        } catch (Exception e) {
            log.error("Failed to process incoming message | MTI:{} | STAN:{} | RRN:{}",
                    mti, pdu.getStan(), pdu.getRrn(), e);
        }
    }

    /**
     * Wraps the transaction layer's answer in the matching ISO 8583 packager and encodes it.
     * 1LINK spec section 10.1: a request is answered with its MTI plus 0x10, so 0200 -> 0210 and
     * 0220 -> 0230.
     */
    private BasePDU toResponseMessage(BasePDU answer, String requestMti) {
        if (MessageTypeEnum.MT_0200.getValue().equals(requestMti)) {
            IBFTTitleFetchResponseBuild response = new IBFTTitleFetchResponseBuild();
            BeanUtils.copyProperties(answer, response);
            response.getHeader().setMessageType(MessageTypeEnum.MT_0210.getValue());
            response.build();
            return response;
        }
        IBFTAdviceResponseBuild response = new IBFTAdviceResponseBuild();
        BeanUtils.copyProperties(answer, response);
        response.getHeader().setMessageType(MessageTypeEnum.MT_0230.getValue());
        response.build();
        return response;
    }
}
