package com.hnh.enterprise.core.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CoreRestProperties.class})
@ConditionalOnProperty(prefix = "app.rest", name = "enabled", havingValue = "true")
@ComponentScan(basePackages = "com.hnh.enterprise.core.rest")
public class RestAutoConfiguration {
}
