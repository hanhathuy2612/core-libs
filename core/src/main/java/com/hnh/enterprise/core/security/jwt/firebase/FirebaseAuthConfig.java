package com.hnh.enterprise.core.security.jwt.firebase;

import com.hnh.enterprise.core.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "app.security.oauth2", name = "firebase.enabled", havingValue = "true")
public class FirebaseAuthConfig {
    private final SecurityProperties securityProperties;

    public FirebaseAuthConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean(value = "firebaseAuthenticationManager")
    public AuthenticationManager authenticationManager() {
        JwtDecoder decoder = firebaseDecoder();
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
        provider.setJwtAuthenticationConverter(firebaseAuthConverter());
        return new ProviderManager(provider);
    }

    @Bean
    public JwtDecoder firebaseDecoder() {
        return JwtDecoders.fromIssuerLocation(securityProperties.getOauth2().getFirebase().getIssuerUri());
    }

    @Bean
    public JwtAuthenticationConverter firebaseAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(firebaseJwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> firebaseJwtGrantedAuthoritiesConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            return authorities;
        };
    }

    public String issuer() {
        return securityProperties.getOauth2().getFirebase().getIssuerUri();
    }
}
