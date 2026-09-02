package com.dfs.gateway.controller;

import com.dfs.gateway.dto.EncryptedRequest;
import com.dfs.gateway.dto.EncryptedResponse;
import com.dfs.gateway.service.GatewayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Catch-all gateway controller delegating all traffic under {@code /gateway/**} to {@link GatewayService}.
 */
@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;
    private final ObjectMapper objectMapper;

    /**
     * Handles GET requests to any gateway path.
     */
    @GetMapping("/**")
    public ResponseEntity<EncryptedResponse> getGateway(
            HttpServletRequest request,
            @RequestBody(required = false) byte[] body) throws IOException {
        return handle(request, HttpMethod.GET, body);
    }

    /**
     * Handles POST requests to any gateway path.
     */
    @PostMapping("/**")
    public ResponseEntity<EncryptedResponse> postGateway(
            HttpServletRequest request,
            @RequestBody(required = false) byte[] body) throws IOException {
        return handle(request, HttpMethod.POST, body);
    }

    /**
     * Handles PUT requests to any gateway path.
     */
    @PutMapping("/**")
    public ResponseEntity<EncryptedResponse> putGateway(
            HttpServletRequest request,
            @RequestBody(required = false) byte[] body) throws IOException {
        return handle(request, HttpMethod.PUT, body);
    }

    /**
     * Handles PATCH requests to any gateway path.
     */
    @PatchMapping("/**")
    public ResponseEntity<EncryptedResponse> patchGateway(
            HttpServletRequest request,
            @RequestBody(required = false) byte[] body) throws IOException {
        return handle(request, HttpMethod.PATCH, body);
    }

    /**
     * Handles DELETE requests to any gateway path.
     */
    @DeleteMapping("/**")
    public ResponseEntity<EncryptedResponse> deleteGateway(
            HttpServletRequest request,
            @RequestBody(required = false) byte[] body) throws IOException {
        return handle(request, HttpMethod.DELETE, body);
    }

    private ResponseEntity<EncryptedResponse> handle(
            HttpServletRequest request,
            HttpMethod method,
            byte[] body) throws IOException {

        byte[] rawBody = body != null ? body : new byte[0];
        EncryptedRequest envelope = resolveEnvelope(request, method, rawBody);
        // Route on the path within the application, excluding the servlet context path
        // (e.g. "/dfs-crypto-gateway") so route prefixes like "/gateway" still match.
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        String path = (contextPath != null && !contextPath.isEmpty() && requestUri.startsWith(contextPath))
                ? requestUri.substring(contextPath.length())
                : requestUri;

        EncryptedResponse response = gatewayService.process(
                method,
                path,
                request.getHeaderNames(),
                request,
                envelope,
                rawBody);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private EncryptedRequest resolveEnvelope(
            HttpServletRequest request,
            HttpMethod method,
            byte[] rawBody) throws IOException {

        String queryData = request.getParameter("data");
        String queryIv = request.getParameter("iv");
        String queryTimestamp = request.getParameter("timestamp");
        if (queryData != null || queryIv != null || queryTimestamp != null) {
            return EncryptedRequest.builder()
                    .data(queryData)
                    .iv(queryIv)
                    .timestamp(queryTimestamp)
                    .build();
        }

        if (rawBody.length == 0) {
            return null;
        }

        String bodyText = new String(rawBody, StandardCharsets.UTF_8).trim();
        if (bodyText.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.readValue(bodyText, EncryptedRequest.class);
        } catch (Exception ex) {
            if (method == HttpMethod.GET) {
                return null;
            }
            throw new IllegalArgumentException("Request body must be a valid EncryptedRequest JSON envelope");
        }
    }
}
