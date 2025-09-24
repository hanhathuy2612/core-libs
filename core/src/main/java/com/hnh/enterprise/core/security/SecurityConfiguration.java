package com.hnh.enterprise.core.security;

import com.hnh.enterprise.core.repository.UserRepository;
import com.hnh.enterprise.core.security.jwt.SecurityJwtConfiguration;
import com.hnh.enterprise.core.security.properties.SecurityProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@Import({SecurityJwtConfiguration.class, JpaAuditConfiguration.class})
@EnableConfigurationProperties({SecurityProperties.class})
@ConditionalOnProperty(value = "app.security.enabled", havingValue = "true")
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

    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        return new UserDetailsServiceImpl(repository);
    }
}
