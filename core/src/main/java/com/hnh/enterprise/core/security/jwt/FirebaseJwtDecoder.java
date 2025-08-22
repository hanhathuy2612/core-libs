package com.hnh.enterprise.core.security.jwt;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.hnh.enterprise.core.entity.Authority;
import com.hnh.enterprise.core.entity.User;
import com.hnh.enterprise.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FirebaseJwtDecoder {
    private final UserService userService;

    private static final String HEADER_ALG = "alg";
    private static final String HEADER_TYP = "typ";
    private static final String HEADER_SUB = "sub";
    private static final String HEADER_FIREBASE_ID = "firebase_id";
    private static final String HEADER_EMAIL = "email";
    private static final String HEADER_NAME = "name";
    private static final String HEADER_AVATAR_URL = "avatar_url";
    private static final String HEADER_ISS = "iss";
    private static final String HEADER_AUD = "aud";
    private static final String HEADER_EXP = "exp";
    private static final String HEADER_IAT = "iat";
    private static final String HEADER_AUTH_TIME = "auth_time";
    private static final String HEADER_FIREBASE = "firebase";

    public Jwt decode(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
        User user = createUserIfNotExisting(firebaseToken);
        // Convert Firebase token to Spring Security Jwt
        Map<String, Object> headers = new HashMap<>();
        headers.put(HEADER_ALG, "RS256");
        headers.put(HEADER_TYP, "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put(HEADER_SUB, user.getEmail());
        claims.put(HEADER_FIREBASE_ID, firebaseToken.getUid());
        claims.put(HEADER_EMAIL, firebaseToken.getEmail());
        claims.put(HEADER_NAME, firebaseToken.getName());
        claims.put(HEADER_AVATAR_URL, firebaseToken.getPicture());
        claims.put(HEADER_ISS, firebaseToken.getIssuer());

        // Add a permission list (very important for @Secured to work)
        claims.put("authorities", user.getAuthorities().stream()
                .map(Authority::getName) // e.g. "ROLE_GENEALOGY_USER"
                .toList());

        // Use getClaims() for other fields
        Map<String, Object> originalClaims = firebaseToken.getClaims();
        claims.put(HEADER_AUD, originalClaims.get(HEADER_AUD));

        // Handle timestamps properly
        Object expValue = originalClaims.get(HEADER_EXP);
        if (expValue instanceof Number exp) {
            claims.put(HEADER_EXP, Instant.ofEpochSecond(exp.longValue()));
        }

        Object iatValue = originalClaims.get(HEADER_IAT);
        if (iatValue instanceof Number iat) {
            claims.put(HEADER_IAT, Instant.ofEpochSecond(iat.longValue()));
        }

        claims.put(HEADER_AUTH_TIME, originalClaims.get(HEADER_AUTH_TIME));
        claims.put(HEADER_FIREBASE, true); // Mark as Firebase token

        return Jwt.withTokenValue(token)
                .headers(h -> h.putAll(headers))
                .claims(c -> c.putAll(claims))
                .build();
    }

    private User createUserIfNotExisting(FirebaseToken firebaseToken) {
        return userService.createOrFindFirebaseUser(firebaseToken.getEmail(), firebaseToken.getName(),
                firebaseToken.getPicture());
    }
}
