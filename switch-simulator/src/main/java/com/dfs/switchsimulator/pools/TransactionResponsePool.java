package com.dfs.switchsimulator.pools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.dfs.switchsimulator.common.BasePdu;
import com.dfs.switchsimulator.dto.PDUWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("TransactionResponsePool")
public class TransactionResponsePool {
    private Map<String, PDUWrapper> responsePool = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(TransactionResponsePool.class.getSimpleName());

    public void put( PDUWrapper pduWrapper ) {
        if (pduWrapper != null) {
            responsePool.put(pduWrapper.getRRNKey(), pduWrapper);
            logger.info("INSERTING RESPONSE IN RESPONSE POOL WITH RRN: " + pduWrapper.getRRNKey());
            logger.info("RESPONSE POOL SIZE: " + responsePool.size());
        }
    }

    public BasePdu get( String RRNkey ) {  // each transaction RRNkey () will be uniq and mandaory
        logger.info("Searching Response Pool with RRN: " + RRNkey);
        BasePdu basePDU = null;
        if (RRNkey != null) {
            PDUWrapper pduWrapper = this.responsePool.get(RRNkey);
            if (pduWrapper != null) {
                basePDU = pduWrapper.getBasePDU();
            }
        } else {
            logger.info("INVALID RRN / STAN KEY PROVIDED");
            throw new IllegalArgumentException("INVALID ARGUMENT PROVIDED.");
        }
        return basePDU;
    }

    public boolean remove( String RRNkey ) {
        boolean status = false;
        if (RRNkey != null) {
            PDUWrapper pduWrapper = this.responsePool.remove(RRNkey);
            if (pduWrapper != null) {
                status = true;
                logger.info("RRN REMOVED FROM POOL: " + RRNkey);
            }
        }
        return status;
    }

}