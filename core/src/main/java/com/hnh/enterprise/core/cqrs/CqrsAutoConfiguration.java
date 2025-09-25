package com.hnh.enterprise.core.cqrs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CqrsProperties.class)
@ConditionalOnProperty(prefix = "app.cqrs", name = "enabled", havingValue = "true")
@ComponentScan(basePackages = "com.hnh.enterprise.core.cqrs")
public class CqrsAutoConfiguration {
}
