package com.hnh.enterprise.core.security.jwt.keycloak;

import com.hnh.enterprise.core.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "app.security.oauth2", name = "keycloak.enabled", havingValue = "true")
public class KeycloakAuthConfig {
    private final SecurityProperties securityProperties;

    public KeycloakAuthConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean(value = "keycloakAuthenticationManager")
    public AuthenticationManager authenticationManager() {
        JwtDecoder decoder = keycloakDecoder();

        // Provider that uses the decoder and then maps roles
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
        provider.setJwtAuthenticationConverter(keycloakAuthConverter());
        return new ProviderManager(provider);
    }


    @Bean
    public JwtDecoder keycloakDecoder() {
        final String ISSUER = securityProperties.getOauth2().getKeycloak().getIssuerUri();
        return JwtDecoders.fromIssuerLocation(ISSUER);
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> keycloakAuthConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            // 2) client roles (optional, if you want them)
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> client = (Map<String, Object>) resourceAccess.get(securityProperties.getOauth2().getKeycloak().getClientId());
                if (client != null) {
                    @SuppressWarnings("unchecked")
                    List<String> roles = (List<String>) client.getOrDefault("roles", List.of());
                    roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.toUpperCase())));
                }
            }

            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    @Bean
    public String issuer() {
        return securityProperties.getOauth2().getKeycloak().getIssuerUri();
    }
}
