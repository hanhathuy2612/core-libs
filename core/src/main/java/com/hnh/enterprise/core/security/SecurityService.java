package com.hnh.enterprise.core.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.hnh.enterprise.core.security.properties.SecurityProperties;

import java.util.List;
import java.util.Objects;

/**
 * SecurityService
 * <p>>
 * This service configures security settings for the application, including CORS, CSRF, session management,
 * and authorization rules. It also provides a method to create a CORS filter based on the provided security properties.
 * <p>
 * It uses the {@link SecurityProperties} to determine the allowed origins, headers, and methods for CORS requests.
 * The security filter chain is built to handle authentication and authorization, including JWT resource server support.
 * <p>
 * The service is designed to be used in a Spring Security context, allowing for flexible security configurations
 * based on the application's requirements.
 *
 * @param securityProperties the security properties containing configuration details such as CORS settings and permit-all matchers.
 */
public record SecurityService(SecurityProperties securityProperties) {
    
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        this.buildAuthorizeHttpRequests(http);
        
        http.exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        
        return http.build();
    }
    
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        if (Objects.nonNull(securityProperties.getCors())) {
            config.setAllowedOriginPatterns(
                    Objects.nonNull(securityProperties.getCors().getAllowedOrigins())
                            ? securityProperties.getCors().getAllowedOrigins()
                            : List.of("*")
            );
            config.setAllowedHeaders(
                    Objects.nonNull(securityProperties.getCors().getAllowedHeaders())
                            ? securityProperties.getCors().getAllowedHeaders()
                            : List.of("*")
            );
            config.setAllowedMethods(
                    Objects.nonNull(securityProperties.getCors().getAllowedMethods())
                            ? securityProperties.getCors().getAllowedMethods()
                            : List.of("*")
            );
        } else {
            config.setAllowedHeaders(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            config.setAllowedOriginPatterns(List.of("*"));
        }
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
    /**
     * @param http http
     * @throws Exception exception
     */
    private void buildAuthorizeHttpRequests(HttpSecurity http) throws Exception {
        if (Objects.nonNull(securityProperties.getPermitAllMatchers())) {
            http.authorizeHttpRequests(
                    authorize -> {
                        for (String pattern : securityProperties.getPermitAllMatchers()) {
                            authorize.requestMatchers(pattern).permitAll();
                        }
                    }
            );
        }
        
        http.authorizeHttpRequests(
                authorize -> authorize.anyRequest().authenticated()
        );
    }
    
}
