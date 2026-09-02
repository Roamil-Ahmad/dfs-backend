package com.dfs.app.config;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * Request flow logging for controller methods.
 *
 * - Every request : "Entering: X" and "Exiting: X responsecode=Y" (concise, no payload).
 * - POST requests : the request body is logged at INFO, MASKED (PII/financial fields) and truncated.
 * - The POST RESPONSE body is logged by {@link RequestResponseLoggingFilter} instead, because the
 *   filter captures the real HTTP response — including error/exception responses (e.g. 406) that this
 *   aspect's @AfterReturning never sees.
 * - GET responses are never logged.
 * - Errors : method + message (no payload).
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.dfs.app.controller..*(..))")
    public void applicationMethods() {
    }

    @Before("applicationMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        logger.info("Entering: {}", method);
        if (isPost()) {
            logger.info("Request [{}]: {}", method, LogSanitizer.maskAndTrim(argsToJson(joinPoint.getArgs())));
        }
    }

    @AfterReturning(value = "applicationMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().getName();
        Object body = (result instanceof ResponseEntity) ? ((ResponseEntity<?>) result).getBody() : result;
        logger.info("Exiting: {} responsecode={}", method, responseCode(body));
    }

    @AfterThrowing(value = "applicationMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in {}: {}", joinPoint.getSignature().getName(), exception.getMessage());
    }

    private boolean isPost() {
        return "POST".equalsIgnoreCase(httpMethod());
    }

    private String httpMethod() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attrs != null ? attrs.getRequest().getMethod() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Object responseCode(Object body) {
        if (body instanceof Map) {
            Object rc = ((Map<?, ?>) body).get("responsecode");
            if (rc != null) {
                return rc;
            }
        }
        return "?";
    }

    // Serialize args one-by-one (skip servlet objects, fall back to toString) so a non-serializable
    // arg (e.g. HttpServletRequest) never breaks logging.
    private String argsToJson(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            if (arg == null || arg instanceof ServletRequest || arg instanceof ServletResponse) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(toJson(arg));
        }
        return sb.toString();
    }

    private String toJson(Object value) {
        if (value == null) {
            return "null";
        }
        // Uploaded files are described, never serialised. Jackson would call getBytes() on each
        // one, reading the whole file back into memory and writing customer identity documents
        // into the log. Truncation afterwards would not help: the cost is already paid by then.
        String fileSummary = describeFiles(value);
        if (fileSummary != null) {
            return fileSummary;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    /** @return a description when the value is an uploaded file or a list of them, else null. */
    private String describeFiles(Object value) {
        if (value instanceof MultipartFile) {
            return describeFile((MultipartFile) value);
        }
        if (value instanceof Collection<?>) {
            Collection<?> values = (Collection<?>) value;
            if (!values.isEmpty() && values.stream().allMatch(MultipartFile.class::isInstance)) {
                return values.stream()
                        .map(item -> describeFile((MultipartFile) item))
                        .collect(Collectors.joining(", ", "[", "]"));
            }
        }
        return null;
    }

    private String describeFile(MultipartFile file) {
        return "{fileName=" + file.getOriginalFilename()
                + ", contentType=" + file.getContentType()
                + ", bytes=" + file.getSize() + "}";
    }
}
