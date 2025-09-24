package com.hnh.enterprise.core.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.repository")
public class RepositoryProperties {
    private Boolean enabled = false;
}
