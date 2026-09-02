/*
Author Name: romail.ahmed

Project Name: configurations

Package Name: com.workflow.configurations.dto

Class Name: Response

Date and Time:3/13/2023 12:34 PM

Version:1.0
*/
package com.dfs.agentapp.dto.workflow;

public class Response {

    private String responseCode;
    private String message;
    private Object payload;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
