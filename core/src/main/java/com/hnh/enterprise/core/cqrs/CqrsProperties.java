package com.hnh.enterprise.core.cqrs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cqrs")
public class CqrsProperties {

    private boolean enabled = false;
}
