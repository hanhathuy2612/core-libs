package com.hnh.enterprise.core.security;

import com.hnh.enterprise.core.security.properties.SecurityProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
    public SecurityMetersService securityMetersService(MeterRegistry registry) {
        return new SecurityMetersService(registry);
    }

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public SecurityService securityService(SecurityProperties securityProperties) {
        return new SecurityService(securityProperties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityService securityService)
            throws Exception {
        return securityService.filterChain(http);
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
