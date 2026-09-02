package com.barq.nadra.controller;

import com.barq.nadra.utils.CustomDataNotFoundException;
import com.barq.nadra.utils.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HelperClass {

    static Logger LOG = LoggerFactory.getLogger(HelperClass.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus, String responseCode, String responseMessage, Object payload) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responseCode);
        map.put("messages", responseMessage);
        map.put("data", payload);

        return ResponseEntity.status(httpStatus).body(map);
    }

    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus, HashMap<String, Object> resp) {

        return ResponseEntity.status(httpStatus).body(resp);
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

    public String getResponseFromPostAPI(Map<String, String> headerMap, Object requestBody, String url) {
        WebClient webClient = WebClient.builder().baseUrl(url).build();
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

    public Object getResponseFromGetAPI(String url) {
        WebClient webClient = WebClient.builder().build();

        try {
            return webClient.get().uri(url).retrieve().bodyToMono(Object.class).block();
        } catch (Exception ex) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
    }


    public static <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) || (input instanceof List && ((List<?>) input).isEmpty()) || (input instanceof Map && ((Map<?, ?>) input).isEmpty()) || (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }


    public String getResponseFromPostAPIRestTemplate(Map<String, String> headerMap, Object requestBody, String url) {
        RestTemplate restTemplate = new RestTemplate();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            headers.set(entry.getKey(), entry.getValue());
        }

        // Wrap request body and headers
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response != null && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
            }

        } catch (HttpClientErrorException.BadRequest ex) {
            return ex.getResponseBodyAsString(); // You were returning a ResponseEntity in WebClient; here we return the body directly
        } catch (Exception e) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
    }

    public String financialYearCalculator() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        String financialYear;

        if (today.getMonthValue() >= Month.JULY.getValue()) {
            financialYear = "From 01 July " + year + " to 30 June " + (year + 1);
        } else {
            financialYear = "From 01 July " + (year - 1) + " to 30 June " + year;
        }

        return financialYear;
    }

    public static String getRandomTicketId() {
        // Short random 10-digit numeric ID
        long randomNumber = 1_000_000_000L + (long) (Math.random() * 9_000_000_000L);
        return String.valueOf(randomNumber);
    }

    public static <T> T fromJson(String json, Class<T> tClass) throws JsonProcessingException {
        if (json == null || tClass == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.JSON_PARSE_EXCEPTION.getResponseCode());
        }
        return objectMapper.readValue(json, tClass);
    }

    public Object getResponseFromRestApi(String url, String headerName, String headerValue, Object requestBody) {
        Object responseBody = null;
        WebClient webClient = WebClient.create();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(headerName, headerValue);
        try {
            responseBody = webClient.post()
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseBody;
    }

}
