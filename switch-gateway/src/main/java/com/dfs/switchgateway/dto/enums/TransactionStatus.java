package com.dfs.switchgateway.dto.enums;

/**
 * TransactionStatus enum, hold value for transaction status which are being stored into mongodb.
 * <ul>
 * <li>PROCESSING - initial status after sending</li>
 * <li>RECEIVED - response is received from phoenix, but transaction still running.</li>
 * <li>COMPLETED - transaction is completed.</li>
 * <li>REJECTED - rejected at phoenix </li>
 * <li>TIMEOUT - when it will timed out by phoenix</li>
 * <li>REVERSAL_PROCESSING - when a transaction's reversal started</li>
 * <li>REVERSAL_COMPLETED - if reversal is completed.</li>
 * <li>REVERSING - when request sent to phoenix for reversal.</li>
 * <li>REVERSED - when reversal is successful on external system</li>
 * </ul>
 */
public enum TransactionStatus {

    PROCESSING(1001),
    RECEIVED(1002),
    COMPLETED(1003),
    REJECTED(1004),
    TIMEOUT(1005),
    REVERSAL_PROCESSING(5001),
    REVERSAL_UNDELIVERED(5003),
    REVERSED(5002),
    REVERSAL_FAILED(5004);

    TransactionStatus( Integer value ) {
        this.value = value;
    }

    private Integer value;

    public Integer getValue() {
        return value;
    }
}
