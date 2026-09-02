package com.wallet.transaction.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JWTSecurity {

    private byte[] key;

    private void setKey() {
        MessageDigest sha;
        try {
            String myKey = JwtConstants.AES_SECRET_KEY;  // Use your secret key from your Constants
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");  // SHA-1 or SHA-256 based on your needs
            key = sha.digest(key);
            key = Arrays.copyOf(key, 32);  // Use a 32-byte key for HS256
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createJWTWithClaims(String subject, Map<String, Object> claims, int time) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        setKey();  // Set the key

        // Use the key and algorithm to generate a signing key
        Key signingKey = new SecretKeySpec(key, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(JwtConstants.ISSUERS)
                .setClaims(claims)
                .signWith(signingKey, signatureAlgorithm);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, time);
        builder.setExpiration(calendar.getTime());

        return "Bearer "+builder.compact();
    }

    public Claims parseJWT(String jwtToken) throws JwtException {
        try {
            setKey();
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);
            return jws.getBody();
        } catch (JwtException e) {
            // Handle invalid JWT
            throw new JwtException("Invalid JWT token", e);
        }
    }

    public String extractSubject(String token) {
        Claims claims = parseJWT(token);
        return claims.getSubject();
    }

    public String extractUserId(String token) {
        Claims claims = parseJWT(token);
        return claims.get(JwtConstants.USER_ID, String.class);  // Or the key used for userId
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseJWT(token);
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;  // In case of parsing issues
        }
    }
}
