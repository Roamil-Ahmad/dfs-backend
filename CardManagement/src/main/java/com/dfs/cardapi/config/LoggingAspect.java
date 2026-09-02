/*
Author Name: romail.ahmed

Project Name: booking

Package Name: com.booking.config

Class Name: LoggingAspect

Date and Time:1/6/2025 4:43 PM

Version:1.0
*/
package com.dfs.cardapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Define a pointcut for all methods in your application
    @Pointcut("execution(* com.dfs.cardapi.controller..*(..))")
    public void applicationMethods() {
        // Pointcut expression
    }

    @Before("applicationMethods()")
    public void logBefore(JoinPoint joinPoint) {
        // Convert arguments to readable JSON if possible
        Object[] args = joinPoint.getArgs();
        String readableArgs = Arrays.stream(args)
                .map(this::convertToJson)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        // Log method entry
        logger.info("Entering method: {} with arguments: [{}]",
                joinPoint.getSignature().getName(),
                readableArgs);
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

    // After Returning Advice
    @AfterReturning(value = "applicationMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Object body = ((ResponseEntity<?>) result).getBody();
        logger.info("Exiting method: {} with result: {}",
                joinPoint.getSignature().getName(),
                body);
    }

    // After Throwing Advice
    @AfterThrowing(value = "applicationMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {} with message: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }
}
