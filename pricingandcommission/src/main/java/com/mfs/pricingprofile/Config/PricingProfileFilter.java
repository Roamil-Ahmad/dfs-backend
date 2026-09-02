/*
Author Name:muhammad.kashif

Project Name: limits

Package Name:com.mfs.limits.Config

Class Name: LimitsFilter

Date and Time:5/12/2023 4:10 PM

Version:1.0

*/
package com.mfs.pricingprofile.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfs.pricingprofile.model.TblAppUserActivityLog;
import com.mfs.pricingprofile.model.TblAppUserLoginHistory;
import com.mfs.pricingprofile.model.TblTransDoc;
import com.mfs.pricingprofile.repo.TblAppUserActivityLogRepo;
import com.mfs.pricingprofile.repo.TblAppUserLoginHistoryRepo;
import com.mfs.pricingprofile.repo.TblTransDocRepo;
import com.mfs.pricingprofile.utils.Constants;
import com.mfs.pricingprofile.utils.JwtConstants;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PricingProfileFilter extends OncePerRequestFilter {
    @Autowired
    private TblTransDocRepo tblTransDocRepo;
    @Autowired
    private TblAppUserLoginHistoryRepo tblAppUserLoginHistoryRepo;
    @Autowired
    private TblAppUserActivityLogRepo tblAppUserActivityLogRepo;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        setupCORSHeaders(response);

        final String contextPath = request.getRequestURL().toString();
        if (contextPath.contains("fee")) {
            if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
                filterChain.doFilter(request, response);
            } else {
                checkToken(wrappedRequest, wrappedResponse, filterChain, request, response);
            }
        } else {
            setTokenFailResponse(response);
        }
        wrappedResponse.copyBodyToResponse(); // Copy the response back to the client
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

    private void setupCORSHeaders(HttpServletResponse httpResponse) {
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_ORIGIN, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_METHODS, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_HEADERS, Constants.S_1);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_ALLOW_CREDENTIALS, Constants.S_1_TRUE);
        httpResponse.setHeader(Constants.ACCESS_CONTROL_MAX_AGE, Constants.MAX_AGE);
    }

    private boolean isValidToken(String headerValue) {
        return (headerValue != null && headerValue.startsWith(Constants.BEARER));
    }

    private void setTokenFailResponse(HttpServletResponse httpResponse) throws IOException {
        if (!httpResponse.isCommitted()) { // Ensure response is not already committed
            httpResponse.reset(); // Reset any previous content
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");

            HashMap<String, Object> map = new HashMap<>();
            map.put("responseCode", "401"); // Ensure proper string format
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