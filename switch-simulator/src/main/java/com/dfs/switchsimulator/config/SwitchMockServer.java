package com.dfs.switchsimulator.config;

import org.apache.mina.core.filterchain.*;
import org.apache.mina.core.service.*;
import org.apache.mina.core.session.*;
import org.apache.mina.filter.codec.*;
import org.apache.mina.filter.executor.*;
import org.apache.mina.filter.logging.*;
import org.apache.mina.transport.socket.nio.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;
import com.dfs.switchsimulator.common.*;
import com.dfs.switchsimulator.filtersConfig.*;
import com.dfs.switchsimulator.transactionProcessor.*;
import com.dfs.switchsimulator.utils.ConfigReader;

import javax.annotation.*;
import java.io.*;
import java.net.*;
import java.util.TimerTask;

@Component
public class SwitchMockServer extends Utils {
    private Logger logger = LoggerFactory.getLogger(SwitchMockServer.class);
	
    @Value("${switch.simulator.iso-port}")
    private final Integer PORT = null;
    private IoAcceptor acceptor;
    @Autowired
    private SwitchMessageProcessor processor;

    public SwitchMockServer() {
    }

	public static void main(String[] args) {
		SwitchMockServer mockServer = new SwitchMockServer();
		mockServer.start();
		System.out.println("MOCK SERVER GREETS YOU !!!!!!");
	}

    @PostConstruct
    public void start() {
        this.init();
        this.startup();
    }

    public void init() {
        logger.info("Initiating Integration Module Mock Server");
        acceptor = new NioSocketAcceptor();

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        LoggingFilter loggingFilter = new LoggingFilter();
        chain.addLast("logger", loggingFilter);
        chain.addLast("executor2", new ExecutorFilter(new IoEventType[]{IoEventType.MESSAGE_RECEIVED}));
        chain.addLast("executor3", new ExecutorFilter(new IoEventType[]{IoEventType.MESSAGE_SENT}));
        chain.addLast("executor5", new ExecutorFilter(new IoEventType[]{IoEventType.WRITE}));
        chain.addLast("executor6", new ExecutorFilter(new IoEventType[]{IoEventType.EXCEPTION_CAUGHT}));

        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MockCodecFactory()));

        acceptor.setHandler(processor);

        acceptor.getSessionConfig().setReadBufferSize(4096);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3000);
    }

    public void startup() {
        logger.info("Starting up Mock Server");
        try {
            // Starting the Server
            getServerPort();
            acceptor.bind(new InetSocketAddress(PORT));

            logger.info("Server started and listing at {}" , acceptor.getLocalAddress());
        } catch (IOException e) {
            logger.error("Exception during server startup", e);
        }

    }
    
    public boolean sendDirectMessages(Object message) {
         boolean isSent = false;
        for (IoSession clientSession :  acceptor.getManagedSessions().values()) {
                clientSession.write(message);
                isSent = true;
        }
        if(isSent) {
            logger.info("Packet sent to JS Middleware Client");
        }
        else {
            logger.info("JS Middleware Client Not Found");
        }
        return isSent;
    }
    
    public void closeSessions() {
        for (IoSession clientSession :  acceptor.getManagedSessions().values()) {
            clientSession.closeNow();
        }
    }
}