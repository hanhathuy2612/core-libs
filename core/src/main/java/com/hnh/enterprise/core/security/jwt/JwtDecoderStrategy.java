package com.hnh.enterprise.core.security.jwt;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

/**
 * Strategy interface for JWT token decoders.
 * Each implementation can handle a specific type of JWT token.
 */
public interface JwtDecoderStrategy {

    /**
     * Check if this strategy can handle the given token.
     *
     * @param token the JWT token string
     * @return true if this strategy can decode the token, false otherwise
     */
    boolean canHandle(String token);

    /**
     * Decode the JWT token.
     *
     * @param token the JWT token string
     * @return the decoded JWT
     * @throws JwtException if the token cannot be decoded
     */
    Jwt decode(String token) throws JwtException;

    /**
     * Get the priority of this strategy. Lower numbers have higher priority.
     * This determines the order in which strategies are tried.
     *
     * @return the priority value
     */
    default int getPriority() {
        return 100;
    }
}
