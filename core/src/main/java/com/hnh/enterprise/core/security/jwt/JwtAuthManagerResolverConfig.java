package com.hnh.enterprise.core.security.jwt;

import com.hnh.enterprise.core.security.jwt.firebase.FirebaseAuthConfig;
import com.hnh.enterprise.core.security.jwt.keycloak.KeycloakAuthConfig;
import com.hnh.enterprise.core.security.jwt.regular.RegularAuthConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

import java.util.Map;

@Configuration
public class JwtAuthManagerResolverConfig {
    @Bean
    AuthenticationManagerResolver<HttpServletRequest> jwtResolver(KeycloakAuthConfig keycloakAuthConfig, RegularAuthConfig regularAuthConfig, FirebaseAuthConfig firebaseAuthConfig) {
        Map<String, AuthenticationManager> byIssuer = Map.of(
                keycloakAuthConfig.issuer(), keycloakAuthConfig.authenticationManager(),
                regularAuthConfig.issuer(), regularAuthConfig.authenticationManager(),
                firebaseAuthConfig.issuer(), firebaseAuthConfig.authenticationManager()
        );
        return new JwtIssuerAuthenticationManagerResolver(byIssuer::get);
    }
}
