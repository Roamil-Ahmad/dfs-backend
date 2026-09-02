package com.dfs.app.config;

import com.dfs.app.util.AESencryption;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AESDecryptionFilter extends OncePerRequestFilter {

    private final AESencryption aesEncryption = new AESencryption(); // Use your existing class

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {


        // Bypass decryption filter for /encrypt endpoint
        if (request.getRequestURI().equals("/encrypt")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Read the encrypted body
//        String encryptedBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//
//        String decryptedBody;
//        try {
//            decryptedBody = aesEncryption.decrypt(encryptedBody); // Use your decrypt method
//        } catch (Exception e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Encrypted Data");
//            return;
//        }
//
//        // Wrap the request with decrypted body
//        HttpServletRequest wrappedRequest = new DecryptedHttpServletRequest(request, decryptedBody);

        filterChain.doFilter(request, response);
    }

    static class DecryptedHttpServletRequest extends HttpServletRequestWrapper {
        private final String body;

        public DecryptedHttpServletRequest(HttpServletRequest request, String body) {
            super(request);
            this.body = body;
        }

        @Override
        public ServletInputStream getInputStream() {
            InputStream stream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return stream.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(javax.servlet.ReadListener listener) {
                }
            };
        }
    }
}
