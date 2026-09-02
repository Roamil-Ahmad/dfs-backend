package com.dfs.agentapp.controller;


import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.dto.common.Response;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.CustomDataNotFoundException;
import com.dfs.agentapp.util.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelperClass {

    static Logger LOG = LoggerFactory.getLogger(HelperClass.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public Random rand = new Random();

    // Sleuth instruments the auto-configured WebClient.Builder, so the traceId
    // propagates over outgoing calls. Injected here because subclasses are Spring beans.
    @Autowired
    private WebClient.Builder webClientBuilder;

    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus,
                                                                               String responseCode, String responseMessage, Object payload,String endpoint) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responseCode);
        map.put("messages", responseMessage);
        map.put("data", payload);

        return ResponseEntity.status(httpStatus).body(map);
    }
    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus,
                                                                               HashMap<String, Object> resp) {

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
        WebClient.RequestBodySpec bodySpec = (WebClient.RequestBodySpec)webClient.post().uri((rec$) -> {
            return ((UriBuilder)rec$).build(new Object[0]);
        });
        Iterator var6 = headerMap.entrySet().iterator();

        while(var6.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var6.next();
            bodySpec.header((String)entry.getKey(), new String[]{(String)entry.getValue()});
        }

        WebClient.ResponseSpec responseSpec = bodySpec.body(BodyInserters.fromValue(requestBody)).retrieve();

        ResponseEntity response;
        try {
            response = (ResponseEntity)responseSpec.toEntity(String.class).block();
        } catch (HttpClientErrorException.BadRequest var9) {
            response = new ResponseEntity(var9.getResponseBodyAsString(), var9.getStatusCode());
        } catch (Exception var10) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }

        if (response != null && response.getBody() != null) {
            return (String)response.getBody();
        } else {
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

    public Object getResponseFromGetAPI(String url) {
        WebClient webClient = webClientBuilder.clone().build();

        try {
            return webClient.get().uri(url).retrieve().bodyToMono(Object.class).block();
        } catch (Exception ex) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
    }

    public String autoGenerate(String type, int length) {
        String response;
        switch (type) {
            case "Trace":
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                response = response.substring(response.length() - 6);
                response = new String(shuffleArray(response.toCharArray()));
                break;

            case "TransactionID":
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                response = response.substring(response.length() - 15);
                response = new String(shuffleArray(response.toCharArray()));
                break;

            case "RandomNumber":
                String ticks = String.valueOf(System.currentTimeMillis());
                String extractedNumbers = ticks.substring(ticks.length() - 4);
                String ranNumber = String.valueOf(rand.nextInt()).substring(1, length - 4);
                return ranNumber + extractedNumbers;

            case "Numeric":
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date())
                        + convertDateToStringInFormat("ddMMyyyyHHmmss", new Date());
                response = response.substring(response.length() - length);
                response = new String(shuffleArray(response.toCharArray()));
                break;

            case "AlphaNumeric":
                response = "A1B2C3D4E5F6G7H8I9J10K11L12M13N1";
                response = response.substring(response.length() - length);
                response = new String(shuffleArray(response.toCharArray()));
                break;

            case "Nonce":
                return String.valueOf(UUID.randomUUID());

            case "RRN":
                if (length == 0) {
                    response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                } else if (length == 13) {
                    response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date()).substring(0, 12);
                } else {
                    response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                }
                response = new String(shuffleArray(response.toCharArray()));
                break;

            case "RequestId":
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                response = new String(shuffleArray(response.toCharArray()));
                break;

            case "TelcoDateTime":
                response = convertDateToStringInFormat("yyyy-MM-dd'T'HH:mm:ss", new Date());
                break;

            case "DateTime":
                response = convertDateToStringInFormat("yyyyMMddHHmmss", new Date());
                break;

            case "TransactionDateTime":
                response = convertDateToStringInFormat("MMddHHmmss", new Date());
                break;

            case "Date":
                response = convertDateToStringInFormat("MMdd", new Date());
                break;

            case "LongTime":
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                break;

            default:
                response = convertDateToStringInFormat("HHmmss", new Date());
                break;
        }
        return response;
    }


    private char[] shuffleArray(char[] array) {
        List<Character> characters = new ArrayList<>();
        for (char c : array) {
            characters.add(c);
        }
        Collections.shuffle(characters);
        char[] shuffledArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            shuffledArray[i] = characters.get(i);
        }
        return shuffledArray;
    }

    public static String convertDateToStringInFormat(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getRandomTicketId() {
        // Short random 10-digit numeric ID
        long randomNumber = 1_000_000_000L + (long) (Math.random() * 9_000_000_000L);
        return String.valueOf(randomNumber);
    }
}
