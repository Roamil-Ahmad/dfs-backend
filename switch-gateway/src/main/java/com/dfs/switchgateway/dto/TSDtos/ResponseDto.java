package com.dfs.switchgateway.dto.TSDtos;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto.TSDtos

Class Name: ResponseDto

Date and Time:5/29/2023 5:43 PM

Version:1.0
*/

public class ResponseDto<T> {
    private String code;
    private String message;
    private T data;
    private boolean isQueue;
    private String responseCode;
    private String queueMessage;

    public String getCode() {
        return code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData( T data ) {
        this.data = data;
    }

    public boolean isQueue() {
        return isQueue;
    }

    public void setQueue( boolean queue ) {
        isQueue = queue;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode( String responseCode ) {
        this.responseCode = responseCode;
    }

    public String getQueueMessage() {
        return queueMessage;
    }

    public void setQueueMessage( String queueMessage ) {
        this.queueMessage = queueMessage;
    }
}
