package com.dfs.gateway.service;

import com.dfs.gateway.crypto.AesGcmCryptoService;
import com.dfs.gateway.dto.EncryptedRequest;
import com.dfs.gateway.dto.EncryptedResponse;
import com.dfs.gateway.exception.DownstreamException;
import com.dfs.gateway.exception.GatewayErrorCodes;
import com.dfs.gateway.filter.ReplayAttackGuard;
import com.dfs.gateway.routing.RouteRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.propagation.Propagator;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Orchestrates replay protection, decryption, routing, downstream forwarding, and response encryption.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayService {

    private static final Set<String> BLOCKED_HEADERS = Set.of(
            "host", "content-length", "transfer-encoding", "connection"
    );

    private final ReplayAttackGuard replayAttackGuard;
    private final AesGcmCryptoService aesGcmCryptoService;
    private final RouteRegistry routeRegistry;
    private final CloseableHttpClient httpClient;
    private final Tracer tracer;
    private final Propagator propagator;

    /**
     * Processes an incoming gateway request end-to-end.
     *
     * @param method      HTTP method
     * @param path        servlet path including {@code /gateway} prefix
     * @param headerNames     incoming request headers
     * @param envelope    encrypted envelope (may be partial for GET)
     * @param rawBody     raw request body bytes
     * @return encrypted response envelope
     */
    public EncryptedResponse process(
            HttpMethod method,
            String path,
            Enumeration<String> headerNames,
            javax.servlet.http.HttpServletRequest request,
            EncryptedRequest envelope,
            byte[] rawBody) {

        boolean encryptedEnvelopePresent = envelope != null
                && envelope.getData() != null && !envelope.getData().isBlank()
                && envelope.getIv() != null && !envelope.getIv().isBlank()
                && envelope.getTimestamp() != null && !envelope.getTimestamp().isBlank();

        String plainBody = "";
        if (encryptedEnvelopePresent) {
            replayAttackGuard.validate(envelope.getTimestamp());
            plainBody = aesGcmCryptoService.decrypt(envelope.getData(), envelope.getIv());
        } else if (method == HttpMethod.GET) {
            log.debug("Forwarding GET without encrypted envelope for path {}", path);
        } else if (rawBody != null && rawBody.length > 0) {
            throw new IllegalArgumentException("Encrypted envelope fields data, iv, and timestamp are required");
        }

        RouteRegistry.ResolvedRoute resolvedRoute = routeRegistry.resolve(path);
        log.debug("Resolved downstream URL {} for path {}", resolvedRoute.getDownstreamUrl(), path);

        String downstreamResponse = forward(
                method.name(),
                resolvedRoute.getDownstreamUrl(),
                headerNames,
                request,
                plainBody,
                encryptedEnvelopePresent || (rawBody != null && rawBody.length > 0));

        return aesGcmCryptoService.encrypt(downstreamResponse);
    }

    private String forward(
            String method,
            String downstreamUrl,
            Enumeration<String> headerNames,
            javax.servlet.http.HttpServletRequest request,
            String plainBody,
            boolean sendBody) {

        ClassicHttpRequest httpRequest = new BasicClassicHttpRequest(method, downstreamUrl);
        copyHeaders(headerNames, request, httpRequest);
        injectTraceContext(httpRequest);

        if (sendBody && plainBody != null && !plainBody.isBlank()) {
            httpRequest.setEntity(new StringEntity(plainBody, ContentType.APPLICATION_JSON));
        }

        try {
            return httpClient.execute(httpRequest, this::handleResponse);
        } catch (DownstreamException ex) {
            throw ex;
        } catch (SocketTimeoutException ex) {
            throw new DownstreamException(
                    GatewayErrorCodes.DOWNSTREAM_TIMEOUT,
                    "Downstream service timed out",
                    ex);
        } catch (Exception ex) {
            if (ex.getCause() instanceof SocketTimeoutException) {
                throw new DownstreamException(
                        GatewayErrorCodes.DOWNSTREAM_TIMEOUT,
                        "Downstream service timed out",
                        (SocketTimeoutException) ex.getCause());
            }
            throw new DownstreamException(
                    GatewayErrorCodes.DOWNSTREAM_ERROR,
                    "Failed to call downstream service: " + ex.getMessage(),
                    ex);
        }
    }

    /**
     * Propagates the current trace context onto the outgoing downstream request.
     * Apache HttpClient5 is not auto-instrumented by Sleuth, so trace headers
     * (B3 by default) are injected manually to keep the {@code traceId} flowing
     * end-to-end across services.
     */
    private void injectTraceContext(ClassicHttpRequest httpRequest) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan == null) {
            return;
        }
        propagator.inject(
                currentSpan.context(),
                httpRequest,
                (req, key, value) -> req.setHeader(key, value));
    }

    private String handleResponse(ClassicHttpResponse response) {
        int status = response.getCode();
        String body = extractBody(response);
        log.debug("Downstream responded with status {}", status);
        if (status >= 400) {
            throw new DownstreamException(
                    GatewayErrorCodes.DOWNSTREAM_ERROR,
                    "Downstream service returned HTTP " + status + ": " + body,
                    status);
        }
        return body == null || body.isBlank() ? "{}" : body;
    }

    private String extractBody(ClassicHttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return "";
            }
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new DownstreamException(
                    GatewayErrorCodes.DOWNSTREAM_ERROR,
                    "Failed to read downstream response body",
                    ex);
        }
    }

    private void copyHeaders(
            Enumeration<String> headerNames,
            javax.servlet.http.HttpServletRequest request,
            ClassicHttpRequest httpRequest) {
        Set<String> added = new HashSet<>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (name == null || BLOCKED_HEADERS.contains(name.toLowerCase(Locale.ROOT))) {
                continue;
            }
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                String value = values.nextElement();
                String canonical = name.toLowerCase(Locale.ROOT);
                if (!added.contains(canonical)) {
                    httpRequest.addHeader(name, value);
                    added.add(canonical);
                }
            }
        }
        if (!added.contains("content-type") && httpRequest.getEntity() != null) {
            httpRequest.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        }
    }
}
