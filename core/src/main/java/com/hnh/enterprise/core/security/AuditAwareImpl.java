package com.hnh.enterprise.core.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 * This class retrieves the current user's email for auditing purposes.
 */
@Service
public class AuditAwareImpl implements AuditorAware<String> {
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserEmail().orElse(Authorities.SYSTEM.getValue()));
    }
}
