package com.hnh.enterprise.core.service.impl;

import com.hnh.enterprise.core.constants.Constants;
import com.hnh.enterprise.core.entity.Authority;
import com.hnh.enterprise.core.entity.User;
import com.hnh.enterprise.core.enumeration.AuthProvider;
import com.hnh.enterprise.core.repository.UserRepository;
import com.hnh.enterprise.core.rest.request.LoginRequest;
import com.hnh.enterprise.core.rest.request.RegisterRequest;
import com.hnh.enterprise.core.rest.response.LoginResponse;
import com.hnh.enterprise.core.rest.response.RegisterResponse;
import com.hnh.enterprise.core.security.Authorities;
import com.hnh.enterprise.core.security.properties.SecurityProperties;
import com.hnh.enterprise.core.service.AuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hnh.enterprise.core.security.constant.Constant.AUTHORITIES_CLAIM;
import static com.hnh.enterprise.core.security.constant.Constant.JWT_ALGORITHM;


public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final SecurityProperties securityProperties;

    public AuthenticationServiceImpl(PasswordEncoder passwordEncoder,
                                     UserRepository userRepository,
                                     AuthenticationManagerBuilder authenticationManager,
                                     JwtEncoder jwtEncoder,
                                     SecurityProperties securityProperties) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.securityProperties = securityProperties;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword());
        Authentication authentication = authenticationManager.getObject().authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = createToken(authentication, request.getRememberMe());
        return LoginResponse.builder()
                .token(jwt)
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        User user = new User();
        user.setLogin(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setActivated(true);
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setAuthProvider(AuthProvider.DEFAULT);
        user.setAuthorities(getUserAuthorities());
        user = userRepository.save(user);
        return RegisterResponse.builder()
                .userId(user.getId())
                .message("User registered successfully")
                .build();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(securityProperties.getJwt().getTokenValidityInSecondsForRememberMe(), ChronoUnit.SECONDS);
        } else {
            validity = now.plus(securityProperties.getJwt().getTokenValidityInSeconds(), ChronoUnit.SECONDS);
        }

        // @formatter:off
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_CLAIM, authorities);

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }

    private Set<Authority> getUserAuthorities() {
        return Collections.singleton(new Authority(Authorities.USER.name()));
    }
}
