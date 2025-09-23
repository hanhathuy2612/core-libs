package com.hnh.enterprise.core.service.impl;

import java.util.Collections;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.hnh.enterprise.core.constants.Constants;
import com.hnh.enterprise.core.dto.TokenDTO;
import com.hnh.enterprise.core.entity.Authority;
import com.hnh.enterprise.core.entity.User;
import com.hnh.enterprise.core.enumeration.AuthProvider;
import com.hnh.enterprise.core.repository.UserRepository;
import com.hnh.enterprise.core.rest.request.LoginRequest;
import com.hnh.enterprise.core.rest.request.RegisterRequest;
import com.hnh.enterprise.core.rest.response.LoginResponse;
import com.hnh.enterprise.core.rest.response.RegisterResponse;
import com.hnh.enterprise.core.security.Authorities;
import com.hnh.enterprise.core.service.AuthenticationService;
import com.hnh.enterprise.core.service.TokenService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public LoginResponse login(LoginRequest request) {
        TokenDTO token = tokenService.createToken(request.getUsername(), request.getPassword(),
                request.getRememberMe());
        return LoginResponse.builder()
                .token(token.getToken())
                .refreshToken(token.getRefreshToken())
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

    private Set<Authority> getUserAuthorities() {
        return Collections.singleton(new Authority(Authorities.USER.name()));
    }
}
