package com.hnh.enterprise.core.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.service")
public class ServiceProperties {
    private Boolean enabled = Boolean.FALSE;
}
