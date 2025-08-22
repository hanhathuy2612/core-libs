package com.hnh.enterprise.core.open_api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

public record OpenApiConfigService(OpenApiProperties openApiProperties) {
    
    public OpenAPI openAPI() {
        if (openApiProperties.getServers() == null) {
            return new OpenAPI();
        }
        return new OpenAPI().servers(
                openApiProperties.getServers().stream().map(serverUrl -> new Server().url(serverUrl)).toList()
        );
    }
}
