package com.hnh.enterprise.core.security.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * SecurityProperties
 * example:
 * app:
 * security:
 * permit-all-matchers:
 * - /api/v1/public/**
 * - /api/v1/auth/**
 * - /api/v1/users/**
 * - /api/v1/roles/**
 * - /api/v1/permissions/**
 * cors:
 * allowed-origins:
 * - http://localhost:3000
 * - http://localhost:8080
 * allowed-methods:
 * - GET
 * - POST
 * - PUT
 * - DELETE
 * allowed-headers:
 * - Content-Type
 * - Authorization
 * - X-Requested-With
 * - X-Auth-Token
 * - X-Auth-Refresh-Token
 * - X-Auth-Access-Token
 * - X-Auth-Refresh-Token
 * jwt:
 * secret: 1234567890
 * expiration: 3600000
 * refresh-expiration: 86400000
 * basic:
 * enabled: true
 * username: admin
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
    @JsonProperty("permit-all-matchers")
    private List<String> permitAllMatchers = new ArrayList<>();

    @JsonProperty("cors")
    private Cors cors;

    @JsonProperty("jwt")
    private Jwt jwt;

    @Getter
    @Setter
    public static class Cors {
        @JsonProperty("allowed-origins")
        private List<String> allowedOrigins = new ArrayList<>();

        @JsonProperty("allowed-methods")
        private List<String> allowedMethods = new ArrayList<>();

        @JsonProperty("allowed-headers")
        private List<String> allowedHeaders = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Jwt {
        @JsonProperty("base64-secret")
        private String base64Secret;

        private long tokenValidityInSeconds;

        private long tokenValidityInSecondsForRememberMe;

        private long refreshTokenValidityInSeconds;
    }
}
