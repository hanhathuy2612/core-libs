package com.hnh.enterprise.core.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.rest")
public class CoreRestProperties {
    /**
     * Enable rest api
     */
    private boolean enabled = false;
}
