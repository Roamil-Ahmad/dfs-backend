/*Author Name:muhammad.kashif
Project Name: BackOffice
Package Name:com.dfs.backoffice.config
Class Name: LoggingAspect
Date and Time:07/01/2025 4:44 PM
Version:1.0*/
package com.mfs.pricingprofile.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfs.pricingprofile.controller.AbstractApi;
import com.mfs.pricingprofile.dto.Response;
import com.mfs.pricingprofile.utils.Constants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class LoggingAspect extends AbstractApi {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String endpoint = getRequestURI().orElse(Constants.UNKNOWN_ENDPOINT);
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String packageName = joinPoint.getTarget().getClass().getPackageName();
        Object[] args = joinPoint.getArgs();
        String readableArgs = Arrays.stream(args)
                .map(this::convertToJson)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        Response response = new Response();

        // Log method start
        logMethodStart(endpoint, className, methodName, packageName, readableArgs);

        Object result;
        try {
            result = joinPoint.proceed();

            // Extract response and log success
            response = extractResponse(result).orElse(new Response());
            logMethodEnd(endpoint, className, methodName, packageName, readableArgs, response);
        } catch (Exception e) {
            // Log exception
            LOG.error("Exception in method: {}.{} - {}", className, methodName, e.getMessage(), e);
            response.setResponseCode(Constants.GENERAL_PROCESSING_CODE);
            response.setMessage(Constants.GENERAL_PROCESSING_ERROR);
            // Optionally log response with error details
            logMethodEnd(endpoint, className, methodName, packageName, readableArgs, response);
            throw e;
        }

        return result;
    }

    private String convertToJson(Object arg) {
        if (arg == null) {
            return "null";
        }
        try {
            // Convert object to JSON
            return objectMapper.writeValueAsString(arg);
        } catch (Exception e) {
            // Fallback to toString() if JSON conversion fails
            return arg.toString();
        }
    }

    private Optional<String> getRequestURI() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(attr -> ((ServletRequestAttributes) attr).getRequest().getRequestURI());
    }

    private void logMethodStart(String endpoint, String className, String methodName, String packageName, String request) {
        LOG.info(Constants.STARTING_METHOD + "{}.{} - Endpoint: {}, Package: {}, Request: {}",
                className, methodName, endpoint, packageName, request);
    }

    private void logMethodEnd(String endpoint, String className, String methodName, String packageName, String request, Response response) {
        LOG.info(Constants.ENDING_METHOD + " {}.{} - Endpoint: {}, Package: {}, Request: {}, Response: {}",
                className, methodName, endpoint, packageName, request, convertObjecttoJson(response));
    }

    private Optional<Response> extractResponse(Object result) {
        if (result instanceof ResponseEntity<?>) {
            Object body = ((ResponseEntity<?>) result).getBody();
            return Optional.ofNullable(body).filter(Response.class::isInstance).map(Response.class::cast);
        }
        return Optional.empty();
    }
}

