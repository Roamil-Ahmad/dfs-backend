package com.wallet.transaction.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.transaction.dto.MpinPayload;
import com.wallet.transaction.dto.MpinRequest;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.dto.common.Response;
import com.wallet.transaction.util.Constants;
import com.wallet.transaction.util.CustomDataNotFoundException;
import com.wallet.transaction.util.GenericResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

public class HelperClass {

    static Logger LOG = LoggerFactory.getLogger(HelperClass.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Sleuth-instrumented clients so the traceId propagates over outgoing REST calls.
    // Injected when HelperClass is used as a base class of a Spring bean.
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder webClientBuilder;

    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus,
                                                                               String responseCode, String responseMessage, Object payload, String endpoint) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responseCode);
        map.put("messages", responseMessage);
        map.put("data", payload);
        return ResponseEntity.status(httpStatus).body(map);
    }

    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus,
                                                                               HashMap<String, Object> resp, String endpoint) {
        return ResponseEntity.status(httpStatus).body(resp);
    }

    public static <T> T fromJson(String json, Class<T> tClass) throws JsonProcessingException {
        if (json == null || tClass == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.JSON_PARSE_EXCEPTION.getResponseCode());
        }
        return objectMapper.readValue(json, tClass);
    }

    public static String convertObjecttoJson(Object object) {

        objectMapper.writerWithDefaultPrettyPrinter();  // Pretty printing
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Response sendRequestAndGetResponse(Request request, String token, String endpoint) throws JsonProcessingException {
        String result = this.getResponseFromPostAPI(this.createHeaderMapBackOffice(token), request, endpoint);
        return fromJson(result, Response.class);
    }

    public String getResponseFromPostAPI(Map<String, String> headerMap, Object requestBody, String url) {
        WebClient webClient = webClientBuilder.clone().baseUrl(url).build();
        WebClient.RequestBodySpec bodySpec = (WebClient.RequestBodySpec) webClient.post().uri((rec$) -> {
            return ((UriBuilder) rec$).build(new Object[0]);
        });
        Iterator var6 = headerMap.entrySet().iterator();

        while (var6.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var6.next();
            bodySpec.header((String) entry.getKey(), new String[]{(String) entry.getValue()});
        }

        WebClient.ResponseSpec responseSpec = bodySpec.body(BodyInserters.fromValue(requestBody)).retrieve();

        ResponseEntity response;
        try {
            response = (ResponseEntity) responseSpec.toEntity(String.class).block();
        } catch (HttpClientErrorException.BadRequest var9) {
            response = new ResponseEntity(var9.getResponseBodyAsString(), var9.getStatusCode());
        } catch (Exception var10) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }

        if (response != null && response.getBody() != null) {
            return (String) response.getBody();
        } else {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
    }

    public String getResponseFromGetAPI(String url) {
        WebClient webClient = webClientBuilder.clone().baseUrl(url).build();
        // Fetch response
        ResponseEntity<String> response;
        try {
            String responseBody = webClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (responseBody != null) {
                return responseBody;
            } else {
                throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
            }
        } catch (Exception ex) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
    }

    public Map<String, String> createHeaderMapBackOffice(String authorization) {
        // Create a new HashMap object to store the headers
        Map<String, String> headerMap = new HashMap<>();
        // Set the "content-type" header to "application/json"
        headerMap.put(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
        // Set the "accept" header to "application/json"
        headerMap.put(Constants.ACCEPT, Constants.APPLICATION_JSON);
        //Set the "Authorization" Token
        headerMap.put(Constants.AUTHORIZATION, authorization);

        // Return the populated headerMap object
        return headerMap;
    }

    public static <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) ||
                (input instanceof List && ((List<?>) input).isEmpty()) ||
                (input instanceof Map && ((Map<?, ?>) input).isEmpty()) ||
                (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }


    public Map<String, String> createHeaderMap(String Token) {
        // Create a new HashMap object to store the headers
        Map<String, String> headerMap = new HashMap<>();

        // Set the "content-type" header to "application/json"
        headerMap.put("content-type", "application/json");

        // Set the "accept" header to "application/json"
        headerMap.put("accept", "application/json");
        headerMap.put("Authorization", Token);

        // Return the populated headerMap object
        return headerMap;
    }


    public String getResponseFromPostAPILms(Map<String, String> headerMap, Object postParam, String url) throws Exception {
        try {
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setAll(headerMap);

            // Create request entity
            HttpEntity<?> requestEntity = new HttpEntity<>(postParam, headers);

            // Send POST request and get response (uses the injected, Sleuth-instrumented RestTemplate)
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // Check if the status code is 200 (OK)
            if (response.getStatusCode() == HttpStatus.OK) {
                String entityResponse = response.getBody();
                return !entityResponse.isEmpty() ? entityResponse : null;
            } else {
                return response.getBody();
            }
        } catch (HttpStatusCodeException e) {
            System.err.println("HTTP Status Code Exception: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new Exception(e.getMessage());
        } catch (RestClientException e) {
            System.err.println("RestClientException: " + e.getMessage());
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    public static String maskNumber(String number) {
        int index = 4;
        StringBuilder maskeNumber = new StringBuilder(number);
        if (number.length() > 7) {
            for (int i = index; i < index + 4; i++) {
                maskeNumber.setCharAt(i, '*');
            }
        }
        return maskeNumber.toString();
    }

    public String amountformat(String amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("en", "PK"));
        String currency = formatter.format(Double.valueOf(amount));
        if (!(currency.contains("."))) {
            currency = currency + ".00";
        }
        String[] splitter = currency.split("\\.");
        splitter[0].length(); // Before Decimal Count
        splitter[1].length(); // After Decimal Count
        if (splitter[1].length() <= 1) {
            currency = currency + "0";
        }
        return currency;
    }


    public String checkAgentMpinValidation(String accountNo, String mPin, String token, String imei, String url) throws Exception {

        // Get the URL from the environment properties
        MpinRequest mpinRequest = new MpinRequest();
        MpinPayload payload = new MpinPayload();
        payload.setMobileNumber(accountNo);
        payload.setMpin(mPin);
        mpinRequest.setImieNo(imei);
        mpinRequest.setPayload(payload);
        // Send a POST request to the specified URL with the request payload
        String result = getResponseFromPostAPILms(createHeaderMap(token), mpinRequest, url);
        return result;


    }
}
