package com.dfs.switchsimulator.common;

public class BaseHeader {

    private String messageType;

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.toString();
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType( String messageType ) {
        this.messageType = messageType;
    }
}
