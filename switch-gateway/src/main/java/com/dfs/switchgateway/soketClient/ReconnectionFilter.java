package com.dfs.switchgateway.soketClient;

import com.dfs.switchgateway.soketClient.Service.NetworkInfoBean;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * Transport-level auto reconnect: when the TCP session to the switch drops, re-establish it.
 *
 * This is link recovery only. It never replays a transaction - there is no transaction retry and
 * no store-and-forward anywhere in this service.
 */
public class ReconnectionFilter extends IoFilterAdapter {
    private static Logger logger = LoggerFactory.getLogger(ReconnectionFilter.class.getSimpleName());
    private int reconnectDelay;
    private IoConnector connector;
    private IoHandler handler;
    private SocketAddress remoteAddress;
    private SocketAddress localAddress;
    private TransactionClient manager;
    private NetworkInfoBean info;

    /*
     * @param delay     delay in mili-seconds between 2 reconnections
     * @param connector the IoConnector for reconnect
     */
    public ReconnectionFilter( int delay, IoConnector connector, IoHandler handler, SocketAddress remoteAddress, SocketAddress localAddress,
                               TransactionClient manager, NetworkInfoBean info ) {
        this.reconnectDelay = delay;
        this.connector = connector;
        this.handler = handler;
        this.remoteAddress = remoteAddress;
        this.localAddress = localAddress;
        this.manager = manager;
        this.info = info;
    }

    public ReconnectionFilter( int reconnectDelay, NioSocketConnector connector, TransactionClientHandler clientHandler,
                               SocketAddress remoteSocket, SocketAddress localAddress, TransactionClient manager,
                               NetworkInfoBean networkInfoBean ) {
        this.reconnectDelay = reconnectDelay;
        this.connector = connector;
        this.handler = clientHandler;
        this.remoteAddress = remoteSocket;
        this.localAddress = localAddress;
        this.manager = manager;
        this.info = networkInfoBean;
    }

    public void sessionClosed( NextFilter nextFilter, IoSession session ) {
        // fire a connection retrying Thread
        logger.info("Socket Connection Terminated By Server");
        nextFilter.sessionClosed(session);
        manager.connectSocket();
    }
}
