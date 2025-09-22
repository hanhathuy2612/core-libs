package com.hnh.enterprise.core.security.jwt;

import com.hnh.enterprise.core.entity.Authority;
import com.hnh.enterprise.core.entity.User;
import com.hnh.enterprise.core.security.properties.SecurityProperties;
import com.hnh.enterprise.core.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.hnh.enterprise.core.security.constant.Constant.JWT_ALGORITHM;

@Component
public class RegularJwtDecoder {
    private final UserService userService;
    private final NimbusJwtDecoder nimbusJwtDecoder;

    private static final String HEADER_AUTHORITIES = "auth";

    public RegularJwtDecoder(SecurityProperties securityProperties, UserService userService) {
        this.nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(getSecretKey(securityProperties.getJwt().getBase64Secret()))
                .macAlgorithm(JWT_ALGORITHM)
                .build();
        this.userService = userService;
    }

    public Jwt decode(String token) throws JwtException {
        Jwt jwt = nimbusJwtDecoder.decode(token);

        Map<String, Object> claims = new HashMap<>(jwt.getClaims());

        if (!claims.containsKey(HEADER_AUTHORITIES)) {
            String email = jwt.getSubject();
            User user = userService.getUserWithAuthoritiesByLogin(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

            claims.put(HEADER_AUTHORITIES, user.getAuthorities().stream()
                    .map(Authority::getName)
                    .toList());
        }

        return new Jwt(
                jwt.getTokenValue(),
                jwt.getIssuedAt(),
                jwt.getExpiresAt(),
                jwt.getHeaders(),
                claims);
    }

    private SecretKey getSecretKey(String jwtKey) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtKey);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}
