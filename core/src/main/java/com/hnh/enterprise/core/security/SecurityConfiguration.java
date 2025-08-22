package com.hnh.enterprise.core.security;

import com.hnh.enterprise.core.security.jwt.SecurityJwtConfiguration;
import com.hnh.enterprise.core.security.properties.SecurityProperties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableConfigurationProperties({SecurityProperties.class})
@EnableMethodSecurity(securedEnabled = true)
@Import({SecurityJwtConfiguration.class})
public class SecurityConfiguration {
    @Bean
    public SecurityService securityService(SecurityProperties securityProperties) {
        return new SecurityService(securityProperties);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityService securityService) throws Exception {
        return securityService.filterChain(http);
    }
    
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditAwareImpl();
    }
    
    @Bean
    @ConditionalOnProperty(value = "app.security.cors.enabled", havingValue = "true", matchIfMissing = true)
    public CorsFilter corsFilter(SecurityService securityService) {
        return securityService.corsFilter();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
