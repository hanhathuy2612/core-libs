package com.hnh.enterprise.core.rest;

import com.hnh.enterprise.core.repository.AuthorityRepository;
import com.hnh.enterprise.core.repository.UserRepository;
import com.hnh.enterprise.core.security.properties.SecurityProperties;
import com.hnh.enterprise.core.service.AuthenticationService;
import com.hnh.enterprise.core.service.UserService;
import com.hnh.enterprise.core.service.impl.AuthenticationServiceImpl;
import com.hnh.enterprise.core.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
@ComponentScan(basePackages = {
        "com.hnh.enterprise.core.rest",
        "com.hnh.enterprise.core.repository",
        "com.hnh.enterprise.core.service",
})
public class AuthAPIConfiguration {
    @Bean
    public AuthenticationService authenticationService(UserRepository userRepository,
                                                       PasswordEncoder passwordEncoder,
                                                       AuthenticationManagerBuilder authenticationManager,
                                                       JwtEncoder jwtEncoder,
                                                       SecurityProperties securityProperties) {
        return new AuthenticationServiceImpl(passwordEncoder, userRepository, authenticationManager, jwtEncoder, securityProperties);
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        return new UserServiceImpl(userRepository, passwordEncoder, authorityRepository);
    }
}
