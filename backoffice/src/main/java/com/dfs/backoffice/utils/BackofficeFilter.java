package com.dfs.backoffice.utils;

import com.dfs.backoffice.model.TblAppUser;
import com.dfs.backoffice.model.TblAppUserActivityLog;
import com.dfs.backoffice.model.TblAppUserLoginHistory;
import com.dfs.backoffice.model.TblTransDoc;
import com.dfs.backoffice.repo.TblAppUserActivityLogRepo;
import com.dfs.backoffice.repo.TblAppUserLoginHistoryRepo;
import com.dfs.backoffice.repo.TblAppUserRepo;
import com.dfs.backoffice.repo.TblTransDocRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
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
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BackofficeFilter extends OncePerRequestFilter {

    @Autowired
    private TblTransDocRepo tblTransDocRepo;
    @Autowired
    private TblAppUserLoginHistoryRepo tblAppUserLoginHistoryRepo;
    @Autowired
    private TblAppUserActivityLogRepo tblAppUserActivityLogRepo;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Wrap the request and response to capture their bodies
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        setupCORSHeaders(response);
        final String contextPath = request.getRequestURL().toString();
        if (contextPath.contains("/backoffice")) {
            if (request.getMethod().equalsIgnoreCase("OPTIONS")
                    || contextPath.contains("/login")
                    || contextPath.contains("/swagger-ui")
                    // Uploaded KYC images are rendered by the portal in <img> tags, which cannot
                    // carry a Bearer header. Served read-only from a fixed directory.
                    || contextPath.contains("/document/")
                    || contextPath.contains("v3/api-docs")
                    || contextPath.contains("/forgetPassword")
                    || contextPath.contains("/verifyOtpForgotPassword")
                    || contextPath.contains("/generateOtp")) {
                if (contextPath.contains("/login")) {
                    wrappedRequest.getParameterMap();
                    handleLoginRequest(wrappedRequest);
                }
                filterChain.doFilter(wrappedRequest, wrappedResponse);
            } else {
                checkToken(wrappedRequest, wrappedResponse, filterChain, request, response);
            }
        } else {
            setTokenFailResponse(response);
        }
        wrappedResponse.copyBodyToResponse(); // Copy the response back to the client

    }

    private void setupCORSHeaders(HttpServletResponse httpResponse) {
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_METHODS, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_HEADERS, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_CREDENTIALS, Constants.S_1_TRUE);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_MAX_AGE, Constants.MAX_AGE);
    }

    // Handle POST request-specific logic
    private void handleLoginRequest(ContentCachingRequestWrapper wrappedRequest) throws IOException {
        String requestBody = getRequest(wrappedRequest);
        String userName = extractUsernameBasedOnEndpoint(requestBody);
        if (userName != null) {
            TblAppUser tblAppUser = tblAppUserRepo.findByUsername(userName);
            if (tblAppUser != null) {
                saveUserActivityLogs(tblAppUser.getAppUserId(), wrappedRequest.getRequestURI());
            }
        }
    }

    // Extract mobile number or username based on the endpoint
    private String extractUsernameBasedOnEndpoint(String requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> requestMap = objectMapper.readValue(requestBody, HashMap.class);
        return requestMap.get("username");
    }

    private String getRequest(ContentCachingRequestWrapper wrappedRequest) {
        wrappedRequest.getParameterMap(); // Ensure request parameters are cached properly
        byte[] buf = wrappedRequest.getContentAsByteArray();
        return (buf.length > 0) ? new String(buf, StandardCharsets.UTF_8) : "{}"; // Return empty JSON instead of null
    }

    private void checkToken(ContentCachingRequestWrapper wrappedRequest, ContentCachingResponseWrapper wrappedResponse, FilterChain filterChain, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        String headerValue = httpRequest.getHeader("Authorization");
        if (isValidToken(headerValue)) {
            JWTSecurity jwtSecurity = new JWTSecurity();
            String token = headerValue.substring(7);
            Claims claims = jwtSecurity.parseJWT(token);
            if (claims != null && !claims.isEmpty()) {
                wrappedRequest.setAttribute(JwtConstants.APP_USER_ID, claims.get(JwtConstants.APP_USER_ID));
                wrappedRequest.setAttribute(JwtConstants.PASSWORD_UPDATE_FLAG, claims.get(JwtConstants.PASSWORD_UPDATE_FLAG));
                wrappedRequest.setAttribute(JwtConstants.USER_LOGIN_HISTORY_ID, claims.get(JwtConstants.USER_LOGIN_HISTORY_ID));
                saveUserActivityLogs(Long.parseLong(claims.get(JwtConstants.APP_USER_ID).toString()), httpRequest.getRequestURI());
                filterChain.doFilter(wrappedRequest, wrappedResponse);
            } else {
                setTokenFailResponse(httpResponse);
            }
        } else {
            setTokenFailResponse(httpResponse);
        }
    }

    private boolean isValidToken(String headerValue) {
        return (headerValue != null && headerValue.startsWith("Bearer"));
    }

    private void setTokenFailResponse(HttpServletResponse httpResponse) throws IOException {
        if (!httpResponse.isCommitted()) { // Ensure response is not already committed
            httpResponse.reset(); // Reset any previous content
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");

            HashMap<String, Object> map = new HashMap<>();
            map.put("responseCode", "406"); // Ensure proper string format
            map.put("message", "Invalid Token");
            map.put("data", null);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(map);
            httpResponse.getWriter().write(jsonResponse);
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close(); // Ensure stream is closed properly
        }
    }


    private void saveUserActivityLogs(Long appUserId, String endPoint) {
        if (appUserId != null && appUserId > 0) {
            TblTransDoc tblTransDoc = tblTransDocRepo.getTblTransDocByEndPoint(endPoint);
            if (tblTransDoc != null) {
                TblAppUserLoginHistory tblAppUserLoginHistory = tblAppUserLoginHistoryRepo.getLatestAppUserByAppUserId(appUserId);
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
}