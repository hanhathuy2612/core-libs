package com.hnh.enterprise.core.open_api;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(OpenApiProperties.class)
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
