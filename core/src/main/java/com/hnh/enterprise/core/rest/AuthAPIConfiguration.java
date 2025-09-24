package com.hnh.enterprise.core.rest;

import com.hnh.enterprise.core.repository.AuthorityRepository;
import com.hnh.enterprise.core.repository.UserRepository;
import com.hnh.enterprise.core.security.SecurityConfiguration;
import com.hnh.enterprise.core.security.properties.SecurityProperties;
import com.hnh.enterprise.core.service.AuthenticationService;
import com.hnh.enterprise.core.service.TokenService;
import com.hnh.enterprise.core.service.UserService;
import com.hnh.enterprise.core.service.impl.AuthenticationServiceImpl;
import com.hnh.enterprise.core.service.impl.TokenServiceImpl;
import com.hnh.enterprise.core.service.impl.UserServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
@Import({AuthenticationConfiguration.class, SecurityConfiguration.class, UserRepository.class, AuthorityRepository.class})
public class AuthAPIConfiguration {
    @Bean
    public TokenService tokenService(JwtEncoder jwtEncoder, SecurityProperties securityProperties, AuthenticationManagerBuilder authenticationManager) {
        return new TokenServiceImpl(jwtEncoder, securityProperties, authenticationManager);
    }

    @Bean
    public AuthenticationService authenticationService(UserRepository userRepository,
                                                       PasswordEncoder passwordEncoder,
                                                       TokenService tokenService) {
        return new AuthenticationServiceImpl(passwordEncoder, userRepository, tokenService);
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        return new UserServiceImpl(userRepository, passwordEncoder, authorityRepository);
    }
}
