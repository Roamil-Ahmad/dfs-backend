package com.dfs.switchgateway.config;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.config

Class Name: CustomResponseErrorHandler

Date and Time:7/17/2023 10:13 AM

Version:1.0
*/

import com.dfs.switchgateway.validations.CommunicationServiceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError( ClientHttpResponse response ) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError( ClientHttpResponse response ) throws IOException {
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Handle 404 Not Found error
            throw new CommunicationServiceValidationException("Resource not found");
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            // Handle 401 Unauthorized error
            throw new CommunicationServiceValidationException("Unauthorized request");
        } else {
            // Handle other types of errors
            throw new CommunicationServiceValidationException("An error occurred: " + response.getStatusCode());
        }
    }
}
