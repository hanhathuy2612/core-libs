package com.hnh.enterprise.core.security;

import com.hnh.enterprise.core.security.properties.SecurityProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties({SecurityProperties.class})
@ConditionalOnProperty(value = "app.security.enabled", havingValue = "true")
@ComponentScan(basePackages = "com.hnh.enterprise.core.security")
public class SecurityConfiguration {
    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityService securityService,
                                                   AuthenticationManagerResolver<HttpServletRequest> jwtResolver)
            throws Exception {
        return securityService.filterChain(http, jwtResolver);
    }

    @Bean
    public CorsFilter corsFilter(SecurityService securityService) {
        return securityService.corsFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
