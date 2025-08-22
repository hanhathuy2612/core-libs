package com.hnh.enterprise.core.open_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Open api configuration multiple server
 */
@Getter
@Setter
@ConfigurationProperties("app.open-api")
public class OpenApiProperties {
    /**
     * Servers
     */
    @JsonProperty("servers")
    private List<String> servers;
}
