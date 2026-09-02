package com.dfs.switchgateway.dto;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto

Class Name: BaseHeader

Date and Time:5/26/2023 10:57 AM

Version:1.0
*/


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

