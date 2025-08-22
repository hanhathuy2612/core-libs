package com.hnh.enterprise.core.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.Comparator;
import java.util.List;

/**
 * Chain of Responsibility implementation for JWT decoding.
 * This decoder tries different strategies in priority order until one succeeds.
 */
@Slf4j
public record ChainJwtDecoder(List<JwtDecoderStrategy> strategies) implements JwtDecoder {
    
    public ChainJwtDecoder(List<JwtDecoderStrategy> strategies) {
        this.strategies = strategies.stream()
                .sorted(Comparator.comparing(JwtDecoderStrategy::getPriority))
                .toList();
        
        log.info("Initialized ChainJwtDecoder with {} strategies: {}",
                this.strategies.size(),
                this.strategies.stream()
                        .map(s -> s.getClass().getSimpleName() + "(priority:" + s.getPriority() + ")")
                        .toList());
    }
    
    @Override
    public Jwt decode(String token) throws JwtException {
        log.debug("Attempting to decode JWT token with {} strategies", strategies.size());
        
        JwtException lastException = null;
        
        for (JwtDecoderStrategy strategy : strategies) {
            if (strategy.canHandle(token)) {
                try {
                    log.debug("Trying strategy: {}", strategy.getClass().getSimpleName());
                    Jwt jwt = strategy.decode(token);
                    log.debug("Successfully decoded token with strategy: {}", strategy.getClass().getSimpleName());
                    return jwt;
                } catch (JwtException e) {
                    log.debug("Strategy {} failed to decode token: {}",
                            strategy.getClass().getSimpleName(), e.getMessage());
                    lastException = e;
                    // Continue to next strategy
                }
            } else {
                log.debug("Strategy {} cannot handle this token", strategy.getClass().getSimpleName());
            }
        }
        
        // If we get here, no strategy was able to decode the token
        String errorMessage = "No JWT decoder strategy was able to decode the token";
        if (lastException != null) {
            log.error("{}, last error: {}", errorMessage, lastException.getMessage());
            throw new JwtException(errorMessage, lastException);
        } else {
            log.error(errorMessage);
            throw new JwtException(errorMessage);
        }
    }
}
