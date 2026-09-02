package com.dfs.switchgateway.validations;

import com.dfs.switchgateway.dto.BasePDU;
import com.dfs.switchgateway.soketClient.TransactionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Correlates an answer coming back from the switch with the thread that sent the request.
 *
 * ISO 8583 over a single socket is not request/response at the transport level, so the sending
 * thread parks here until {@code TransactionClientHandler} drops the matching message into
 * {@link TransactionResponsePool}. Correlation is by RRN (DE-37) for financial messages and by STAN
 * (DE-11) for network management, exactly as the 1LINK transaction key is defined in spec section 6.
 *
 * On timeout the caller is told; the transaction is never replayed from here.
 */
@Component("TransactionResponsePoolHandler")
public class TransactionResponsePoolHandler {

    private static final Logger log = LoggerFactory.getLogger(TransactionResponsePoolHandler.class);

    private static final long POLL_INTERVAL_MS = 50L;

    @Autowired
    private TransactionResponsePool transactionResponsePool;

    @Autowired
    private TransactionClient transactionClient;

    @Value("${switch.response-timeout-ms}")
    private long responseTimeoutMs;

    /**
     * Blocks until the answer identified by {@code correlationKey} appears, or the configured
     * timeout elapses.
     *
     * @return the answer, or {@code null} if none arrived in time
     */
    public BasePDU awaitResponse(String correlationKey) {
        if (correlationKey == null || correlationKey.isBlank()) {
            log.error("Cannot wait for an answer without a correlation key");
            return null;
        }

        long deadline = System.currentTimeMillis() + responseTimeoutMs;
        while (System.currentTimeMillis() < deadline) {
            BasePDU answer = transactionResponsePool.get(correlationKey);
            if (answer != null) {
                transactionResponsePool.remove(correlationKey);
                log.info("Answer correlated | key:{} | DE-39:{}", correlationKey, answer.getResponseCode());
                return answer;
            }
            try {
                Thread.sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while waiting for an answer | key:{}", correlationKey);
                return null;
            }
        }

        log.error("Timed out after {} ms waiting for an answer | key:{} | switch:{}:{} | reachable:{}",
                responseTimeoutMs, correlationKey, transactionClient.REMOTE_IP, transactionClient.REMOTE_PORT,
                transactionClient.ping(transactionClient.REMOTE_IP, transactionClient.REMOTE_PORT));
        return null;
    }
}
