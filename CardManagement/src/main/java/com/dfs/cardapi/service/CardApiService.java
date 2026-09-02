package com.dfs.cardapi.service;

import com.dfs.cardapi.utils.CardApiException;
import com.dfs.cardapi.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
public class CardApiService {

    private static final Logger logger = LoggerFactory.getLogger(CardApiService.class);

    // Injected, Sleuth-instrumented bean (see HttpClientConfig) so traceId propagates downstream.
    @Autowired
    private RestTemplate restTemplate;

    @Value("${card.api.base.url}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token.replace("Bearer ", ""));
        return headers;
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null) {
            request = new LoginRequest();
            request.setUsername("test");
            request.setPassword("Welcome@123");
        }
        logger.info("Login request initiated for user: {}", request.getUsername());
        try {
            ResponseEntity<LoginResponse> response = restTemplate.exchange(
                    baseUrl + "/user/login",
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    LoginResponse.class);
            logger.info("Login response received with status: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Login failed with response: {}", e.getResponseBodyAsString());
            throw new CardApiException("Login failed: " + parseErrorMessage(e.getResponseBodyAsString()));
        }
    }

    public GenericResponse newCardRequest(NewRequest request, String token) {
        logger.info("New card request initiated");
        try {
            HttpEntity<NewRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<GenericResponse> response = restTemplate.exchange(
                    baseUrl + "/card/new-request",
                    HttpMethod.POST,
                    entity,
                    GenericResponse.class);
            logger.info("New card response received: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("New card request failed: {}", e.getResponseBodyAsString());
            return parseErrorGenericResponse(e.getResponseBodyAsString(), e.getRawStatusCode());
        }
    }

    public InquiryResponse inquiry(InquiryRequest request, String token) {
        logger.info("Card inquiry initiated");
        try {
            HttpEntity<InquiryRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<InquiryResponse> response = restTemplate.exchange(
                    baseUrl + "/card/inquiry",
                    HttpMethod.POST,
                    entity,
                    InquiryResponse.class);
            logger.info("Inquiry response received: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Inquiry failed: {}", e.getResponseBodyAsString());
            throw new CardApiException("Inquiry failed: " + parseErrorMessage(e.getResponseBodyAsString()));
        }
    }

    public GenericResponse changePin(ChangePinRequest request, String token) {
        logger.info("Change PIN request initiated");
        try {
            HttpEntity<ChangePinRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<GenericResponse> response = restTemplate.exchange(
                    baseUrl + "/card/change-pin",
                    HttpMethod.POST,
                    entity,
                    GenericResponse.class);
            logger.info("Change PIN response status: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Change PIN failed: {}", e.getResponseBodyAsString());
            return parseErrorGenericResponse(e.getResponseBodyAsString(), e.getRawStatusCode());
        }
    }

    public GenericResponse generatePin(GeneratePinRequest request, String token) {
        logger.info("Generate PIN request initiated, flag: {}", request != null ? request.getFlag() : null);
        try {
            HttpEntity<GeneratePinRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<GenericResponse> response = restTemplate.exchange(
                    baseUrl + "/card/generate-pin",
                    HttpMethod.POST,
                    entity,
                    GenericResponse.class);
            logger.info("Generate PIN response status: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Generate PIN failed: {}", e.getResponseBodyAsString());
            return parseErrorGenericResponse(e.getResponseBodyAsString(), e.getRawStatusCode());
        }
    }

    public GenericResponse updateStatus(UpdateStatusRequest request, String token) {
        logger.info("Update card status request initiated");
        try {
            HttpEntity<UpdateStatusRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<GenericResponse> response = restTemplate.exchange(
                    baseUrl + "/card/update-status",
                    HttpMethod.POST,
                    entity,
                    GenericResponse.class);
            logger.info("Update status response: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Update status failed: {}", e.getResponseBodyAsString());
            return parseErrorGenericResponse(e.getResponseBodyAsString(), e.getRawStatusCode());
        }
    }

    public GenericResponse validate(ValidateRequest request, String token) {
        logger.info("Validate card request initiated");
        try {
            HttpEntity<ValidateRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<GenericResponse> response = restTemplate.exchange(
                    baseUrl + "/card/validate",
                    HttpMethod.POST,
                    entity,
                    GenericResponse.class);
            logger.info("Validate card response: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Validate card failed: {}", e.getResponseBodyAsString());
            return parseErrorGenericResponse(e.getResponseBodyAsString(), e.getRawStatusCode());
        }
    }

    public LimitValidateResponse validateLimit(LimitValidateRequest request, String token) {
        logger.info("Validate card limit request initiated");
        try {
            HttpEntity<LimitValidateRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<LimitValidateResponse> response = restTemplate.exchange(
                    baseUrl + "/card/limit/validate",

                    HttpMethod.POST,
                    entity,
                    LimitValidateResponse.class);
            logger.info("Validate card limit response: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Validate card limit failed: {}", e.getResponseBodyAsString());
            throw new CardApiException("Validate card limit failed: " + parseErrorMessage(e.getResponseBodyAsString()));
        }
    }

    public LimitFetchResponse fetchLimit(LimitFetchRequest request, String token) {
        logger.info("Fetch card limit request initiated");
        try {
            HttpEntity<LimitFetchRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<LimitFetchResponse> response = restTemplate.exchange(
                    baseUrl + "/card/limit/available",
                    HttpMethod.POST,
                    entity,
                    LimitFetchResponse.class);
            logger.info("Fetch card limit response: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Fetch card limit failed: {}", e.getResponseBodyAsString());
            throw new CardApiException("Fetch card limit failed: " + parseErrorMessage(e.getResponseBodyAsString()));
        }
    }

    public SpendingSummaryResponse spendingSummary(SpendingSummaryRequest request, String token) {
        logger.info("Card spending summary request initiated");
        try {
            HttpEntity<SpendingSummaryRequest> entity = new HttpEntity<>(request, createHeaders(token));
            ResponseEntity<SpendingSummaryResponse> response = restTemplate.exchange(
                    baseUrl + "/card/spending-summary",
                    HttpMethod.POST,
                    entity,
                    SpendingSummaryResponse.class);
            logger.info("Card spending summary response: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Card spending summary failed: {}", e.getResponseBodyAsString());
            throw new CardApiException("Card spending summary failed: " + parseErrorMessage(e.getResponseBodyAsString()));
        }
    }

    public LovResponse getLov(String token) {
        logger.info("LOV fetch request initiated");
        try {
            HttpEntity<Void> entity = new HttpEntity<>(createHeaders(token));
            ResponseEntity<LovResponse> response = restTemplate.exchange(
                    baseUrl + "/card/lov/all",
                    HttpMethod.GET,
                    entity,
                    LovResponse.class);
            logger.info("LOV fetch response: {}", response.getStatusCode());
            return handleResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Get LOV failed: {}", e.getResponseBodyAsString());
            throw new CardApiException("Get LOV failed: " + parseErrorMessage(e.getResponseBodyAsString()));
        }
    }

    private <T> T handleResponse(ResponseEntity<T> response) {
        HttpStatus status = response.getStatusCode();
        logger.debug("Handling response with status: {}", status);
        if (status.is2xxSuccessful()) {
            return response.getBody();
        } else if (status == HttpStatus.BAD_REQUEST) {
            throw new CardApiException("Bad Request (400): Check your input.");
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            throw new CardApiException("Internal Server Error (500): Something went wrong on the server.");
        } else {
            throw new CardApiException("Unhandled status code: " + status);
        }
    }

    private String parseErrorMessage(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            String message = root.path("responseMessage").asText("Unknown error");
            JsonNode errors = root.path("responseBody").path("errors");
            if (errors.isArray()) {
                StringBuilder sb = new StringBuilder(message + ": ");
                for (JsonNode node : errors) {
                    sb.append(node.asText()).append(" ");
                }
                return sb.toString().trim();
            }
            return message;
        } catch (Exception e) {
            logger.warn("Failed to parse error message", e);
            return "Could not parse error response.";
        }
    }

    private GenericResponse parseErrorGenericResponse(String json, int statusCode) {
        try {
            JsonNode root = objectMapper.readTree(json);
            GenericResponse response = new GenericResponse();
            response.setResponseCode(root.path("responseCode").asInt(statusCode));
            JsonNode errors = root.path("responseBody").path("errors");
            if (errors.isArray()) {
                StringBuilder sb = new StringBuilder();
                for (JsonNode node : errors) {
                    sb.append(node.asText()).append("; ");
                }
                String errorMessage = sb.toString().trim();
                response.setResponseMessage(errorMessage);
                response.setError(errorMessage);
            }
            logger.debug("Parsed error response: {}", response.getResponseMessage());
            return response;
        } catch (Exception e) {
            logger.error("Error parsing generic response JSON", e);
            GenericResponse fallback = new GenericResponse();
            fallback.setResponseCode(statusCode);
            fallback.setResponseMessage("Unable to parse error response");
            fallback.setError(e.getMessage());
            return fallback;
        }
    }
}
