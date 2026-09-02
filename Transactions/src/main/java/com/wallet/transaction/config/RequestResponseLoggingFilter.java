package com.wallet.transaction.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.transaction.model.*;
import com.wallet.transaction.repo.TblAppUserActivityLogRepo;
import com.wallet.transaction.repo.TblAppUserLoginHistoryRepo;
import com.wallet.transaction.repo.TblAppUserRepo;
import com.wallet.transaction.repo.TblTransDocRepo;
import com.wallet.transaction.util.AESencryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblTransDocRepo tblTransDocRepo;
    @Autowired
    private TblAppUserActivityLogRepo tblAppUserActivityLogRepo;
    @Autowired
    private TblAppUserLoginHistoryRepo tblAppUserLoginHistoryRepo;
    @Autowired
    private AESencryption aeSencryption;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bypass logging for /api/procedures endpoints
        if (request.getRequestURI().contains("/api/procedures") || request.getRequestURI().contains("/v1/purchase")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Wrap the request and response to capture their bodies
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {

            TblRequest tblRequest = createTblRequest(request);

            if ("POST".equalsIgnoreCase(request.getMethod())) {
                handlePostRequest(tblRequest, request, wrappedRequest);
            } else if ("GET".equalsIgnoreCase(request.getMethod())) {
                tblRequest.setCreateuser(BigDecimal.ONE);
            }

            wrappedResponse.copyBodyToResponse(); // Copy the response back to the client
        }
    }

    // Create TblRequest with common properties
    private TblRequest createTblRequest(HttpServletRequest request) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setEndPoint(request.getPathInfo() != null ? request.getPathInfo() : request.getRequestURI());
        tblRequest.setCreatedate(new Date());
        return tblRequest;
    }

    // Handle POST request-specific logic
    private void handlePostRequest(TblRequest tblRequest, HttpServletRequest request, ContentCachingRequestWrapper wrappedRequest) {
        String requestBody = getRequestBodyBasedOnEndpoint(tblRequest.getEndPoint(), request, wrappedRequest);
        if (!request.getRequestURI().contains("uploadDocument")
                && !request.getRequestURI().contains("upgradeAccount")
                && !request.getRequestURI().contains("bioversys")) {
            tblRequest.setJsonData(requestBody);
        }
        String mobileNumber = extractMobileNumberBasedOnEndpoint(tblRequest.getEndPoint(), requestBody);
        if (mobileNumber != null) {
            TblAppUser tblAppUser = tblAppUserRepo.getAppUserByMobileNo(aeSencryption.encryptwith256(mobileNumber));
            tblRequest.setCreateuser(tblAppUser != null ? BigDecimal.valueOf(tblAppUser.getAppUserId()) : BigDecimal.ONE);
            saveUserActivityLogs(tblAppUser, tblRequest);
        } else {
            tblRequest.setCreateuser(BigDecimal.ONE);
        }
    }

    // Extract request body based on the endpoint
    private String getRequestBodyBasedOnEndpoint(String endpoint, HttpServletRequest request, ContentCachingRequestWrapper wrappedRequest) {

        return getRequestBody(wrappedRequest);

    }

    // Extract mobile number or username based on the endpoint
    private String extractMobileNumberBasedOnEndpoint(String endpoint, String requestBody) {
        if (endpoint.contains("login")) {
            return extractUsername(requestBody);
        } else {
            return extractMobileNumber(requestBody);
        }
    }

    private void saveUserActivityLogs(TblAppUser tblAppUser, TblRequest tblRequest) {
        if (tblAppUser != null) {
            TblTransDoc tblTransDoc = tblTransDocRepo.getTblTransDocByEndPoint(tblRequest.getEndPoint());
            if (tblTransDoc != null) {
                TblAppUserLoginHistory tblAppUserLoginHistory = tblAppUserLoginHistoryRepo.getLatestAppUserByAppUserId(tblAppUser.getAppUserId());
                if (tblAppUserLoginHistory != null) {
                    TblAppUserActivityLog tblAppUserActivityLog = new TblAppUserActivityLog();
                    tblAppUserActivityLog.setTblTransDoc(tblTransDoc);
                    tblAppUserActivityLog.setActivityDate(new Date());
                    tblAppUserActivityLog.setTblAppUserLoginHistory(tblAppUserLoginHistory);
                    tblAppUserActivityLogRepo.saveAndFlush(tblAppUserActivityLog);
                }

            }
        }
    }

    private String extractUsername(String requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(requestBody);

            // Check if "payload" exists and is an object
            if (rootNode.has("payload") && rootNode.get("payload").isObject()) {
                JsonNode payloadNode = rootNode.get("payload");

                // Check if "mobileNumber" exists
                if (payloadNode.has("username")) {
                    return payloadNode.get("username").asText();
                }
            }
        } catch (JsonProcessingException e) {
            return null;
        }
        return null;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        return buf.length > 0 ? new String(buf, StandardCharsets.UTF_8) : null;
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        return buf.length > 0 ? new String(buf, StandardCharsets.UTF_8) : null;
    }

    private String extractMobileNumber(String requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(requestBody);

            // Check if "payload" exists and is an object
            if (rootNode.has("payload") && rootNode.get("payload").isObject()) {
                JsonNode payloadNode = rootNode.get("payload");

                // Check if "mobileNumber" exists
                if (payloadNode.has("mobileNumber")) {
                    return payloadNode.get("mobileNumber").asText();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private String extractResponseCode(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Check if "responseCode" exists in the response body
            if (rootNode.has("responsecode")) {
                return rootNode.get("responsecode").asText();
            }
        } catch (JsonProcessingException e) {
            return null;
        }
        return null; // Return null if responseCode is not found
    }

    private String extractMessage(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Check if "responseCode" exists in the response body
            if (rootNode.has("messages")) {
                return rootNode.get("messages").asText();
            }
        } catch (JsonProcessingException e) {
            return null;
        }
        return null; // Return null if responseCode is not found
    }
//    private String extractDocumentsRequestJson(HttpServletRequest request) {
//        try {
//            Collection<Part> parts = request.getParts();
//            for (Part part : parts) {
//                if ("documentsRequest".equals(part.getName())) {
//                    // Convert the part to a JSON string
//                    try (InputStream inputStream = part.getInputStream()) {
//                        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
//                    }
//                }
//            }
//        } catch (IOException | ServletException e) {
//            return null;
//        }
//        return null;
//    }


}
