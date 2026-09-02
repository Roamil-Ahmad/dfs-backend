package com.mfs.pricingprofile.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfs.pricingprofile.dto.Response;
import com.mfs.pricingprofile.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class AbstractApi {

    // Injected (Sleuth-instrumented) RestTemplate so the traceId propagates over
    // outgoing calls. Configured with 60s connect/read timeouts in HttpClientConfig.
    @Autowired
    protected RestTemplate restTemplate;

    //this method is used to convert json to string
    public String convertObjecttoJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getResponseFromPostAPI(Map<String, String> headerMap, Map postParam, String url) throws HttpClientErrorException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.setAll(headerMap);
        HttpEntity<?> request = new HttpEntity<>(postParam, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
        String entityResponse = (String) response.getBody();
        HttpStatus entitystatus = response.getStatusCode();
        if (entitystatus.value() == 200) {
            if (!entityResponse.isEmpty()) {
                return entityResponse;
            } else {
                return Constants.UNHANDLE_EXCEPTION;
            }
        } else {
            return entityResponse;
        }
    }

    public String getCurrentMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length >= 3) {
            return stackTrace[2].getMethodName();
        } else {
            return Constants.UNKNOWN_METHOD;
        }
    }

    public ResponseEntity<HashMap<String, Object>> getResponseFormatWithRespCode(HttpStatus status, String message, Object data, int respCode) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", respCode);
        map.put("messages", message);
        map.put("data", data);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    public ResponseEntity<Response> getResponseFormat(HttpStatus status, String message, Object data) {

        String responsestatus;
        if (status.equals(HttpStatus.OK) || status.value() == 200) {
            responsestatus = "1";
        } else {
            responsestatus = "0";
        }

        Response response = new Response();
        response.setResponseCode(responsestatus);
        response.setMessage(message);
        response.setPayload(data);
        return ResponseEntity.status(status).body(response);
    }

}
