package com.hnh.enterprise.core.security.jwt;

import java.util.Base64;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import com.hnh.enterprise.core.security.SecurityMetersService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Strategy for decoding regular JWT tokens (non-Firebase).
 * This handles the standard JWT tokens used by the application.
 */
@Slf4j
@Service
public class RegularJwtDecoderStrategy implements JwtDecoderStrategy {
    
    private final SecurityMetersService metersService;
    private final RegularJwtDecoder regularJwtDecoder;
    
    public RegularJwtDecoderStrategy(
            SecurityMetersService metersService,
            RegularJwtDecoder regularJwtDecoder) {
        this.metersService = metersService;
        this.regularJwtDecoder = regularJwtDecoder;
    }
    
    @Override
    public boolean canHandle(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            
            return !payloadJson.contains("firebase");
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            return regularJwtDecoder.decode(token);
        } catch (Exception e) {
            trackJwtError(e);
            throw e;
        }
    }
    
    @Override
    public int getPriority() {
        return 1000; // Lowest priority - acts as fallback
    }
    
    private void trackJwtError(Exception e) {
        String message = e.getMessage();
        if (message.contains("Invalid signature")) {
        } else if (message.contains("Jwt expired at")) {
            metersService.trackTokenExpired();
        } else if (message.contains("Invalid JWT serialization") ||
                message.contains("Malformed token") ||
                message.contains("Invalid unsecured/JWS/JWE")) {
            metersService.trackTokenMalformed();
        } else {
            log.error("Unknown JWT error: {}", message);
        }
    }
}
