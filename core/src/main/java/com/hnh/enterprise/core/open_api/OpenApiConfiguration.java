package com.hnh.enterprise.core.open_api;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OpenApiProperties.class)
@ConditionalOnProperty(prefix = "app.open-api", name = "enabled", havingValue = "true")
public class OpenApiConfiguration {
    @Bean
    public OpenApiConfigService openApiConfigService(OpenApiProperties properties) {
        return new OpenApiConfigService(properties);
    }

    @Bean
    public OpenAPI openAPI(OpenApiConfigService openApiConfigService) {
        return openApiConfigService.openAPI();
    }
}
