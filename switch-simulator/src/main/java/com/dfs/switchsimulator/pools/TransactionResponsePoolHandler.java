package com.dfs.switchsimulator.pools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dfs.switchsimulator.common.BasePdu;
import com.dfs.switchsimulator.utils.ConfigReader;

@Component("TransactionResponsePoolHandler")
public class TransactionResponsePoolHandler {
    private static Logger logger = LoggerFactory.getLogger(TransactionResponsePoolHandler.class);

    @Autowired
    private TransactionResponsePool transactionResponsePool;

    String txTimeoutString;
    String sleepTimeString;
    String debug;
    Integer txTimeout;
    Integer sleepTime;

    public TransactionResponsePoolHandler() {
        System.out.println();
    }

    private void loadApplicationConfig() {
        txTimeoutString = "120000";
        sleepTimeString = "500";
        debug = ConfigReader.getInstance().getProperty("debug", "false");
        txTimeout = Integer.parseInt(txTimeoutString);
        sleepTime = Integer.parseInt(sleepTimeString);
    }

    public BasePdu checkResponseInPool( String rrn ) {
        loadApplicationConfig();
        Long startTime = System.currentTimeMillis();
        while (true) {
            long timeElapsed = System.currentTimeMillis() - startTime.longValue();

            if (timeElapsed >= txTimeout) {
                logger.info("***** TRANSACTION REQUEST TIMED OUT  *****");
                logger.info("Response Code 404 for RRN: " + rrn);
                return null;
            }

            BasePdu basePDU = transactionResponsePool.get(rrn);
            if (basePDU != null) {
                transactionResponsePool.remove(rrn);
                return basePDU;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.info("Exception", e);
                logger.info(e.getMessage());
            }
        }
    }
}
