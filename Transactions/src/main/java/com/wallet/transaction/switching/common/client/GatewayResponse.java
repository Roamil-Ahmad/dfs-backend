package com.wallet.transaction.switching.common.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Envelope returned by the switch gateway.
 *
 * Mirrors the gateway's own {@code ResponseDto}: {@code code} is the gateway's HTTP-ish outcome
 * and {@code responseCode} is the ISO 8583 DE-39 the switch answered with.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewayResponse<T> {

    private String code;
    private String message;
    private String responseCode;
    private T data;
}
