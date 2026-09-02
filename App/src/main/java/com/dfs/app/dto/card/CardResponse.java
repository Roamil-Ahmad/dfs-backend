package com.dfs.app.dto.card;

import java.util.List;

public class CardResponse {
    private int responseCode;
    private String responseMessage;
    private List<CardTypeResponse> responseBody;

    // Getters and setters
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<CardTypeResponse> getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(List<CardTypeResponse> responseBody) {
        this.responseBody = responseBody;
    }
}
