package com.hnh.enterprise.core.open_api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

public record OpenApiConfigService(OpenApiProperties openApiProperties) {
    
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        if (openApiProperties.getServers() != null) {
            setupServers(openAPI, openApiProperties.getServers());
        }
        
        setupSecuritySchemes(openAPI);
        
        return openAPI;
    }
    
    private void setupServers(OpenAPI openAPI, List<String> servers) {
        List<Server> serverList = servers.stream().map(serverUrl -> new Server().url(serverUrl)).toList();
        openAPI.servers(serverList);
    }
    
    private void setupSecuritySchemes(OpenAPI openAPI) {
        Components components = new Components()
                .addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT"))
                .addSecuritySchemes("apiKey",
                        new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                                .name("X-API-Key"));
        openAPI.components(components);
        
        openAPI.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
