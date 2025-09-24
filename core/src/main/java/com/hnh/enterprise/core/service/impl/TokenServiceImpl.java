package com.hnh.enterprise.core.service.impl;

import static com.hnh.enterprise.core.security.constant.Constant.AUTHORITIES_CLAIM;
import static com.hnh.enterprise.core.security.constant.Constant.JWT_ALGORITHM;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.hnh.enterprise.core.dto.TokenDTO;
import com.hnh.enterprise.core.security.properties.SecurityProperties;
import com.hnh.enterprise.core.service.TokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtEncoder jwtEncoder;
    private final SecurityProperties securityProperties;
    private final AuthenticationManagerBuilder authenticationManager;

    @Override
    public TokenDTO createToken(String username, String password, Boolean rememberMe) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication authentication = authenticationManager.getObject().authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = createToken(authentication, rememberMe);
        String refreshToken = createRefreshToken(username);
        return TokenDTO.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .build();
    }

    private String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(securityProperties.getJwt().getTokenValidityInSecondsForRememberMe(),
                    ChronoUnit.SECONDS);
        } else {
            validity = now.plus(securityProperties.getJwt().getTokenValidityInSeconds(), ChronoUnit.SECONDS);
        }

        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim(AUTHORITIES_CLAIM, authorities);

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }

    private String createRefreshToken(String username) {
        Instant now = Instant.now();
        Instant validity = now.plus(securityProperties.getJwt().getRefreshTokenValidityInSeconds(), ChronoUnit.SECONDS);

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(username)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);
        Jwt jwt = this.jwtEncoder.encode(jwtEncoderParameters);
        return jwt.getTokenValue();
    }
}
