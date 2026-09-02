package com.dfs.switchgateway.soketClient;

import com.dfs.switchgateway.dto.BasePDU;
import com.dfs.switchgateway.soketClient.Service.NetworkInfoBean;
import com.dfs.switchgateway.soketClient.codecs.TransactionCodecFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Holds the ISO 8583 socket to the switch.
 *
 * The endpoint comes from configuration, not from the database: the TBL_SWITCH_* configuration
 * schema of the previous architecture does not exist in DFS, and for the mock deployment every
 * service runs on the same host.
 *
 * The reconnect filter installed here is transport-level only - it re-establishes a dropped TCP
 * session. It is not a transaction retry: a transaction is never replayed by this service.
 */
@Component("TransactionClient")
public class TransactionClient {

    private static final Logger log = LoggerFactory.getLogger(TransactionClient.class);

    @Value("${switch.host}")
    private String host;

    @Value("${switch.port}")
    private int port;

    @Value("${switch.connect-timeout-ms}")
    private long connectTimeoutMs;

    @Value("${switch.reconnect-delay-ms}")
    private int reconnectDelayMs;

    @Autowired
    private TransactionClientHandler transactionClientHandler;

    @Autowired
    private NetworkInfoBean networkInfoBean;

    /** Read by the response pool handler when it reports a timed-out link. */
    public String REMOTE_IP;
    public int REMOTE_PORT;

    private IoSession session;
    private NioSocketConnector connector;
    private SocketAddress remoteSocket;

    @PostConstruct
    public void init() {
        try {
            REMOTE_IP = host;
            REMOTE_PORT = port;
            this.remoteSocket = new InetSocketAddress(REMOTE_IP, REMOTE_PORT);

            this.connector = new NioSocketConnector();
            this.connector.setConnectTimeoutMillis(connectTimeoutMs);

            DefaultIoFilterChainBuilder chain = this.connector.getFilterChain();
            chain.addLast("codec", new ProtocolCodecFilter(new TransactionCodecFactory()));
            chain.addLast("executorReceived", new ExecutorFilter(IoEventType.MESSAGE_RECEIVED));
            chain.addLast("executorSent", new ExecutorFilter(IoEventType.MESSAGE_SENT));
            chain.addLast("executorWrite", new ExecutorFilter(IoEventType.WRITE));
            chain.addLast("executorException", new ExecutorFilter(IoEventType.EXCEPTION_CAUGHT));
            chain.addLast("reconnect", new ReconnectionFilter(reconnectDelayMs, this.connector,
                    this.transactionClientHandler, this.remoteSocket, null, this, networkInfoBean));

            this.connector.setHandler(this.transactionClientHandler);

            log.info("Switch link configured | {}:{}", REMOTE_IP, REMOTE_PORT);
            connectSocket();
        } catch (Exception e) {
            log.error("Unable to initialise the switch link | {}:{}", REMOTE_IP, REMOTE_PORT, e);
        }
    }

    public void connectSocket() {
        try {
            if (this.connector == null) {
                log.warn("Connect requested before the connector was built");
                return;
            }
            networkInfoBean.setConnecting(true);
            ConnectFuture future = this.connector.connect(this.remoteSocket);
            future.awaitUninterruptibly();
            this.session = future.getSession();
            networkInfoBean.setConnecting(false);
            networkInfoBean.setConnected(this.session != null && this.session.isConnected());
            log.info("Switch link connected | {}:{} | connected:{}",
                    REMOTE_IP, REMOTE_PORT, networkInfoBean.isConnected());
        } catch (Exception e) {
            networkInfoBean.setConnecting(false);
            networkInfoBean.setConnected(false);
            log.error("Switch link connect failed | {}:{}", REMOTE_IP, REMOTE_PORT, e);
        }
    }

    @PreDestroy
    public void closeSocket() {
        try {
            if (this.session != null) {
                this.session.closeNow();
                this.session = null;
            }
            networkInfoBean.setConnected(false);
            log.info("Switch link closed | {}:{}", REMOTE_IP, REMOTE_PORT);
        } catch (Exception e) {
            log.error("Error closing the switch link", e);
        }
    }

    /** Writes an already-encoded ISO 8583 message onto the switch session. */
    public void sendMessage(BasePDU pdu) {
        if (this.session == null || !this.session.isConnected()) {
            log.error("Cannot send, switch link is down | MTI:{} | STAN:{} | RRN:{}",
                    pdu.getHeader() != null ? pdu.getHeader().getMessageType() : "?",
                    pdu.getStan(), pdu.getRrn());
            return;
        }
        this.session.write(pdu);
        log.info("Sent to switch | MTI:{} | DE-03:{} | STAN:{} | RRN:{}",
                pdu.getHeader() != null ? pdu.getHeader().getMessageType() : "?",
                pdu.getProcessingCode(), pdu.getStan(), pdu.getRrn());
    }

    /** TCP reachability probe used when a response times out. */
    public boolean ping(String ip, int port) {
        try (Socket probe = new Socket(ip, port)) {
            log.info("Switch reachable | {}:{}", ip, port);
            return probe.isConnected();
        } catch (IOException e) {
            log.warn("Switch not reachable | {}:{} | {}", ip, port, e.getMessage());
            return false;
        }
    }

    public IoSession getSession() {
        return session;
    }

    public NioSocketConnector getConnector() {
        return connector;
    }
}
