package com.workflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"deprecation"})
@EnableScheduling
@RestController
public class AbstractApi {

    Logger LOG = LoggerFactory.getLogger(AbstractApi.class);

    private static final String[] IP_HEADER_CANDIDATES = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};
    @SuppressWarnings("unused")
    private MessageSource messageSource;
    @Autowired
    private Environment environment;
    // Injected (Sleuth-instrumented) bean — replaces `new RestTemplate()` so the traceId propagates over outgoing calls.
    @Autowired
    private RestTemplate restTemplate;

    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
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

    public HashMap<String, String> getResponseForBukAccount(String message, String data) {

        HashMap<String, String> map = new HashMap<>();
        map.put("messages", message);
        map.put("status", data);
        return map;
    }

    public ResponseEntity<HashMap<String, Object>> getSingleCustomizeResponseFormat(HttpStatus status, String message,
                                                                                    Object data, String key) {
        int responsestatus;
        if (status.equals(HttpStatus.OK) || status.value() == 200) {
            responsestatus = 1;
        } else {
            responsestatus = 0;
        }

        HashMap<String, Object> datamap = new HashMap<>();
        datamap.put(key, data);

        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responsestatus);
        map.put("messages", message);
        map.put("data", datamap);
        return ResponseEntity.status(status).body(map);
    }

    public ResponseEntity<HashMap<String, Object>> getMultipleCustomizeResponseFormat(HttpStatus status, String message,
                                                                                      HashMap<String, Object> datamap) {
        int responsestatus;
        if (status.equals(HttpStatus.OK) || status.value() == 200) {
            responsestatus = 1;
        } else {
            responsestatus = 0;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responsestatus);
        map.put("messages", message);
        map.put("data", datamap);
        return ResponseEntity.status(status).body(map);
    }

    public String getResponseFromPostAPI(Map<String, String> headerMap, Map<String, Object> postParam, String url) {

        try {

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

            headers.setAll(headerMap);

            HttpEntity<?> request = new HttpEntity<>(postParam, headers);
            ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);

            String entityResponse = (String) response.getBody();
            HttpStatus entitystatus = (HttpStatus) response.getStatusCode();
            if (entitystatus.value() == 200) {
                System.out.println(entityResponse);
                if (!entityResponse.isEmpty()) {
                    return entityResponse;
                } else {
                    return null;
                }
            } else {
                return entityResponse;
            }

        } catch (RestClientException e) {
            // process exception
            if (e instanceof HttpStatusCodeException) {
                String errorResponse = ((HttpStatusCodeException) e).getResponseBodyAsString();
                System.out.println(errorResponse);
                return errorResponse;
            }
        } catch (Exception e) {
            String e5 = e.getMessage();

            return e5;
        }
        return null;

    }

    public String getResponseFromGetAPI(String url) {
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(url, String.class);
            String entityResponse = (String) response.getBody();
            HttpStatus entitystatus = (HttpStatus) response.getStatusCode();
            if (entitystatus.value() == 200) {
                System.out.println(entityResponse);
                if (!entityResponse.isEmpty()) {
                    return entityResponse;
                } else {

                    return null;
                }
            } else {
                return entityResponse;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;

    }

    public String getrequestHeadervalue(HttpServletRequest request, String key) {
        return (String) request.getHeader(key);
    }

    public ResponseEntity<HashMap<String, Object>> getResponseFormatWithHTTPStatus(HttpStatus status, String message,
                                                                                   Object data) {

        int responsestatus;
        if (status.equals(HttpStatus.OK) || status.value() == 200) {
            responsestatus = 1;
        } else {
            responsestatus = 0;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responsestatus);
        map.put("messages", message);
        map.put("data", data);
        return ResponseEntity.status(status).body(map);
    }

    //this method is used to convert json to string
    public String convertObjecttoJson(Object object) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter();  // Pretty printing
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCurrentMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length >= 3) {
            return stackTrace[2].getMethodName();
        } else {
            return "Unknown Method";
        }
    }

    public <T> boolean isNullOrEmpty(T input) {
        return input == null || input instanceof String && ((String)input).isEmpty() || input instanceof List && ((List)input).isEmpty() || input instanceof Map && ((Map)input).isEmpty();
    }
}
