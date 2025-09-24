package com.hnh.enterprise.core.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
@ConditionalOnProperty(prefix = "app.service", name = "enabled", havingValue = "true")
@ComponentScan(basePackages = "com.hnh.enterprise.core.service")
public class CoreServiceAutoConfiguration {
}
