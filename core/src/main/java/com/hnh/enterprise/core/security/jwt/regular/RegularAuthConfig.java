package com.hnh.enterprise.core.security.jwt.regular;

import com.hnh.enterprise.core.security.properties.SecurityProperties;
import com.hnh.enterprise.core.service.UserService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.hnh.enterprise.core.security.constant.Constant.AUTHORITIES_CLAIM;
import static com.hnh.enterprise.core.security.constant.Constant.JWT_ALGORITHM;

@Configuration
@ConditionalOnProperty(value = "app.security.enabled", havingValue = "true")
public class RegularAuthConfig {
    private final SecurityProperties securityProperties;
    private final UserService userService;

    public RegularAuthConfig(SecurityProperties securityProperties, UserService userService) {
        this.securityProperties = securityProperties;
        this.userService = userService;
    }

    @Bean(value = "regularAuthenticationManager")
    public AuthenticationManager authenticationManager() {
        JwtDecoder decoder = regularJwtDecoder();

        // Provider that uses the decoder and then maps roles
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
        provider.setJwtAuthenticationConverter(regularJwtAuthConverter());
        return new ProviderManager(provider);
    }

    @Bean
    public JwtDecoder regularJwtDecoder() {
        return new RegularJwtDecoder(securityProperties, userService);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> regularJwtAuthConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(securityProperties.getJwt().getBase64Secret()).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public String issuer() {
        return securityProperties.getJwt().getIssuer();
    }
}
