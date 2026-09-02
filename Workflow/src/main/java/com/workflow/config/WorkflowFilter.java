/*
Author Name: romail.ahmed

Project Name: integration

Package Name: com.workflow.integration.config

Class Name: IntegrationFilter

Date and Time:2/15/2023 3:00 PM

Version:1.0
*/
package com.workflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.controller.AbstractApi;
import com.workflow.utils.Constants;
import com.workflow.utils.JWTSecurity;
import com.workflow.utils.JwtConstants;
import io.jsonwebtoken.Claims;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Order(1)
public class WorkflowFilter extends AbstractApi implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        setupCORSHeaders(httpResponse);

        // Let Swagger UI / OpenAPI assets through without a JWT (the UI is otherwise blocked by the token check).
        if (isSwaggerRequest(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            final String contextPath = httpRequest.getRequestURL().toString();
            if (contextPath.contains(Constants.WORKFLOW)) {
                if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
                    chain.doFilter(request, response);
                } else {
                    checkToken(request, response, chain, httpRequest, httpResponse);
                }
            } else {
                setTokenFailResponse(httpResponse);
            }
        } finally {
            MDC.clear();
        }
    }

    private void checkToken(ServletRequest request, ServletResponse response, FilterChain chain, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        String headerValue = httpRequest.getHeader("Authorization");
        if (isValidToken(headerValue)) {
            JWTSecurity jwtSecurity = new JWTSecurity();
            String token = headerValue.substring(7);
            Claims claims = jwtSecurity.parseJWT(token);
            if (claims != null && !claims.isEmpty()) {
                request.setAttribute(JwtConstants.APP_USER_ID, claims.get(JwtConstants.APP_USER_ID));
                chain.doFilter(request, response);
            } else {
                setTokenFailResponse(httpResponse);
            }
        } else {
            setTokenFailResponse(httpResponse);
        }
    }

    private void setupCORSHeaders(HttpServletResponse httpResponse) {
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_METHODS, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_HEADERS, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_CREDENTIALS, Constants.S_1_TRUE);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_MAX_AGE, Constants.MAX_AGE);
    }

    private boolean isSwaggerRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars");
    }

    private boolean isValidToken(String headerValue) {
        return (headerValue != null && headerValue.startsWith(Constants.BEARER));
    }

    private void setTokenFailResponse(HttpServletResponse httpResponse) throws IOException {

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Set the response content type and write the JSON response to the output stream
        httpResponse.setContentType("application/json;charset=UTF-8");
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", 0);
        map.put("messages", "Invalid Token");
        map.put("data", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(map);
        httpResponse.getWriter().write(jsonResponse);
    }

    public <T> boolean isNullOrEmpty(T input) {
        return input == null || input instanceof String && ((String) input).isEmpty() || input instanceof List && ((List) input).isEmpty() || input instanceof Map && ((Map) input).isEmpty();
    }
}
