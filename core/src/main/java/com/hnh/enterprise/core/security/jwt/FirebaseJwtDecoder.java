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
    
    public Jwt decode(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
        User user = createUserIfNotExisting(firebaseToken);
        // Convert Firebase token to Spring Security Jwt
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RS256");
        headers.put("typ", "JWT");
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail());
        claims.put("firebase_id", firebaseToken.getUid());
        claims.put("email", firebaseToken.getEmail());
        claims.put("name", firebaseToken.getName());
        claims.put("avatar_url", firebaseToken.getPicture());
        claims.put("iss", firebaseToken.getIssuer());
        
        // Add a permission list (very important for @Secured to work)
        claims.put("authorities", user.getAuthorities().stream()
                .map(Authority::getName) // e.g. "ROLE_GENEALOGY_USER"
                .toList());
        
        // Use getClaims() for other fields
        Map<String, Object> originalClaims = firebaseToken.getClaims();
        claims.put("aud", originalClaims.get("aud"));
        
        // Handle timestamps properly
        Object expValue = originalClaims.get("exp");
        if (expValue instanceof Number exp) {
            claims.put("exp", Instant.ofEpochSecond(exp.longValue()));
        }
        
        Object iatValue = originalClaims.get("iat");
        if (iatValue instanceof Number iat) {
            claims.put("iat", Instant.ofEpochSecond(iat.longValue()));
        }
        
        claims.put("auth_time", originalClaims.get("auth_time"));
        claims.put("firebase", true); // Mark as Firebase token
        
        return Jwt.withTokenValue(token)
                .headers(h -> h.putAll(headers))
                .claims(c -> c.putAll(claims))
                .build();
    }
    
    private User createUserIfNotExisting(FirebaseToken firebaseToken) {
        return userService.createOrFindFirebaseUser(firebaseToken.getEmail(), firebaseToken.getName(), firebaseToken.getPicture());
    }
}
