package com.hnh.enterprise.core.security.jwt;

import com.hnh.enterprise.core.security.properties.SecurityProperties;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

import static com.hnh.enterprise.core.security.constant.Constant.JWT_ALGORITHM;


@Configuration
@RequiredArgsConstructor
public class SecurityJwtConfiguration {
    
    private static final Logger LOG = LoggerFactory.getLogger(SecurityJwtConfiguration.class);

    private final SecurityProperties securityProperties;
    
    @Bean
    public JwtDecoder jwtDecoder(List<JwtDecoderStrategy> strategies) {
        LOG.info("Configuring JWT decoder with Chain of Responsibility pattern");
        return new ChainJwtDecoder(strategies);
    }
    
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }
    
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(securityProperties.getJwt().getBase64Secret()).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}
