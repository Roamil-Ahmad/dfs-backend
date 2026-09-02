package com.dfs.app.config;

import com.dfs.app.model.*;
import com.dfs.app.repo.*;
import com.dfs.app.service.CommonService;
import com.dfs.app.util.AESencryption;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.concurrent.Executor;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Autowired
    private TblRequestRepo tblRequestRepo;
    @Autowired
    private TblResponseRepo tblResponseRepo;
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
    @Autowired
    private CommonService commonService;
    // Dedicated bounded pool so audit DB writes run OFF the request thread (no audit loss — see AsyncConfig).
    @Autowired
    @Qualifier("auditExecutor")
    private Executor auditExecutor;
    // Reused, thread-safe JSON mapper (avoid constructing one per request/method).
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Skip audit logging for OpenAPI docs and Swagger UI assets so they
    // don't flood the request/response audit tables (this filter persists a row per request).
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs")
                || uri.contains("webjars");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Wrap the request and response to capture their bodies
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Capture everything async-safe IN THE REQUEST THREAD (servlet request/wrappers are not
            // safe to touch from another thread, and the response buffer is cleared by copyBodyToResponse()).
            AuditData audit = new AuditData(
                    request.getPathInfo() != null ? request.getPathInfo() : request.getRequestURI(),
                    request.getRequestURI(),
                    request.getMethod(),
                    getRequestBody(wrappedRequest),
                    getResponseBody(wrappedResponse));

            // Flush the response to the client FIRST — the user no longer waits for the audit DB writes.
            wrappedResponse.copyBodyToResponse();

            // Log the REAL response for POST APIs (incl. error/exception responses like 406, which the
            // controller-method aspect can't see). Masked + truncated.
            if ("POST".equalsIgnoreCase(audit.method)) {
                log.info("Response [{}]: {}", audit.endpoint, LogSanitizer.maskAndTrim(audit.responseBody));
            }

            // Persist the audit OFF the request thread. CallerRunsPolicy ensures no audit row is lost.
            auditExecutor.execute(() -> persistAudit(audit));
        }
    }

    // Runs on the audit executor thread — must never throw (the response has already been sent).
    private void persistAudit(AuditData audit) {
        try {
            TblRequest tblRequest = createTblRequest(audit.endpoint);

            if ("POST".equalsIgnoreCase(audit.method)) {
                handlePostRequest(tblRequest, audit);
            } else if ("GET".equalsIgnoreCase(audit.method)) {
                tblRequest.setCreateuser(BigDecimal.ONE);
            }

            TblRequest savedRequest = tblRequestRepo.save(tblRequest);

            TblResponse tblResponse = createTblResponse(savedRequest, audit.responseBody);
            tblResponseRepo.save(tblResponse);
        } catch (Exception ex) {
            log.error("Async audit persistence failed for {}: {}", audit.endpoint, ex.getMessage());
        }
    }

    // Immutable carrier of the request/response data extracted in the request thread.
    private static final class AuditData {
        final String endpoint;
        final String requestUri;
        final String method;
        final String requestBody;
        final String responseBody;

        AuditData(String endpoint, String requestUri, String method, String requestBody, String responseBody) {
            this.endpoint = endpoint;
            this.requestUri = requestUri;
            this.method = method;
            this.requestBody = requestBody;
            this.responseBody = responseBody;
        }
    }

    // Create TblRequest with common properties
    private TblRequest createTblRequest(String endpoint) {
        TblRequest tblRequest = new TblRequest();
        tblRequest.setEndPoint(endpoint);
        tblRequest.setCreatedate(new Date());
        return tblRequest;
    }

    // Handle POST request-specific logic
    private void handlePostRequest(TblRequest tblRequest, AuditData audit) {
        String requestBody = audit.requestBody;
        String uri = audit.requestUri;
        if (!uri.contains("uploadDocument")
                && !uri.contains("upgradeAccount")
                && !uri.contains("getContactList") && !uri.contains("bioversys") && !uri.contains("customerKyc") && !uri.contains("submitComplaint")) {
            tblRequest.setJsonData(requestBody);
        } else {
            tblRequest.setJsonData("");
        }
        String mobileNumber = extractMobileNumberBasedOnEndpoint(tblRequest.getEndPoint(), requestBody);
        if (mobileNumber != null) {
            TblAppUser tblAppUser = null;
            if (uri.contains("/mobileRegistration") ||
                    uri.contains("/deviceRegistration") ||
                    uri.contains("/customerKyc")) {
                tblAppUser = tblAppUserRepo.getAppUserByMobileNo(aeSencryption.encryptwith256(mobileNumber));
            } else if (uri.contains("login")) {
                tblAppUser = tblAppUserRepo.findByUsername(aeSencryption.encryptwith256(mobileNumber));
            } else {
                tblAppUser = tblAppUserRepo.findByAccountNo(mobileNumber);
            }

            tblRequest.setCreateuser(tblAppUser != null ? BigDecimal.valueOf(tblAppUser.getAppUserId()) : BigDecimal.ONE);
            saveUserActivityLogs(tblAppUser, tblRequest);
        } else {
            tblRequest.setCreateuser(BigDecimal.ONE);
        }
    }

    // Extract mobile number or username based on the endpoint
    private String extractMobileNumberBasedOnEndpoint(String endpoint, String requestBody) {
        if (endpoint.contains("login")) {
            return extractUsername(requestBody);
        } else {
            return extractMobileNumber(requestBody);
        }
    }

    // Create TblResponse with extracted response details
    private TblResponse createTblResponse(TblRequest tblRequest, String responseBody) throws JsonProcessingException {
        TblResponse tblResponse = new TblResponse();
        tblResponse.setTblRequest(tblRequest);
        tblResponse.setCreateuser(tblRequest.getCreateuser());
        tblResponse.setAdditionalData(extractMessage(responseBody));
        tblResponse.setResponseJson(objectMapper.writeValueAsString(responseBody));
        tblResponse.setResponseCode(extractResponseCode(responseBody));
        return tblResponse;
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
            JsonNode rootNode = objectMapper.readTree(requestBody);

            // Check if "payload" exists and is an object
            if (rootNode.has("payload") && rootNode.get("payload").isObject()) {
                JsonNode payloadNode = rootNode.get("payload");

                // Check if "username" exists
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
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Check if "responsecode" exists in the response body
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
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Check if "messages" exists in the response body
            if (rootNode.has("messages")) {
                return rootNode.get("messages").asText();
            }
        } catch (JsonProcessingException e) {
            return null;
        }
        return null; // Return null if message is not found
    }

}
