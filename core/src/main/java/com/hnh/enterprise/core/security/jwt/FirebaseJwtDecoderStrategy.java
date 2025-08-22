package com.hnh.enterprise.core.security.jwt;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

/**
 * Strategy for decoding Firebase JWT tokens.
 * Firebase tokens can be identified by the presence of "kid" field in the header
 * and "alg":"RS256" algorithm.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FirebaseJwtDecoderStrategy implements JwtDecoderStrategy {

    private final FirebaseJwtDecoder firebaseJwtDecoder;

    @Override
    public boolean canHandle(String token) {
        try {
            FirebaseAuth.getInstance().verifyIdToken(token);
            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            return firebaseJwtDecoder.decode(token);
        } catch (FirebaseAuthException e) {
            log.debug("Firebase token decode failed: {}", e.getMessage());
            throw new JwtException("Firebase token decode failed", e);
        }
    }

    @Override
    public int getPriority() {
        return 10; // Higher priority than regular JWT
    }
}
