package com.hnh.enterprise.core.security;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class SecurityMetersService {
    /**
     * Meter name for tracking invalid tokens.
     * This meter is used to count the number of invalid tokens presented by clients.
     */
    public static final String INVALID_TOKENS_METER_NAME = "security.authentication.invalid-tokens";
    public static final String INVALID_TOKENS_METER_DESCRIPTION =
            "Indicates validation error count of the tokens presented by the clients.";
    public static final String INVALID_TOKENS_METER_BASE_UNIT = "errors";
    public static final String INVALID_TOKENS_METER_CAUSE_DIMENSION = "cause";
    
    private final Counter tokenInvalidSignatureCounter;
    private final Counter tokenExpiredCounter;
    private final Counter tokenMalformedCounter;
    
    public SecurityMetersService(MeterRegistry registry) {
        this.tokenInvalidSignatureCounter = invalidTokensCounterForCauseBuilder("invalid-signature").register(registry);
        this.tokenExpiredCounter = invalidTokensCounterForCauseBuilder("expired").register(registry);
        this.tokenMalformedCounter = invalidTokensCounterForCauseBuilder("malformed").register(registry);
    }
    
    /**
     * Creates a Counter.Builder for invalid tokens with a specific cause.
     * This method is used to create counters for different types of invalid tokens.
     *
     * @param cause the cause of the invalid token (e.g., "invalid-signature", "expired", "malformed")
     * @return a Counter.Builder configured with the specified cause
     */
    private Counter.Builder invalidTokensCounterForCauseBuilder(String cause) {
        return Counter.builder(INVALID_TOKENS_METER_NAME)
                .baseUnit(INVALID_TOKENS_METER_BASE_UNIT)
                .description(INVALID_TOKENS_METER_DESCRIPTION)
                .tag(INVALID_TOKENS_METER_CAUSE_DIMENSION, cause);
    }
    
    /**
     * Tracks the number of invalid tokens due to signature issues.
     * This method increments the counter for tokens that have an invalid signature.
     */
    public void trackTokenInvalidSignature() {
        this.tokenInvalidSignatureCounter.increment();
    }
    
    /**
     * Tracks the number of expired tokens.
     * This method increments the counter for tokens that have expired.
     */
    public void trackTokenExpired() {
        this.tokenExpiredCounter.increment();
    }
    
    /**
     * Tracks the number of malformed tokens.
     * This method increments the counter for tokens that are malformed or cannot be parsed.
     */
    public void trackTokenMalformed() {
        this.tokenMalformedCounter.increment();
    }
}
