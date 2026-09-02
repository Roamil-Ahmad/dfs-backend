package com.dfs.switchgateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The envelope every DFS transaction-layer API answers with.
 *
 * <pre>
 *   { "responsecode": "000", "messages": "SUCCESS", "data": { ...the ISO 8583 message... } }
 * </pre>
 *
 * The gateway only needs {@code data} - the message to send back down the link, DE-39 included.
 * {@code responsecode} is the DFS generic code and is kept for logging.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionLayerResponse {

    private String responsecode;
    private String messages;
    private BasePDU data;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public BasePDU getData() {
        return data;
    }

    public void setData(BasePDU data) {
        this.data = data;
    }
}
