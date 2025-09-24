package com.hnh.enterprise.core.logging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LoggingConfiguration
 */
@Configuration
@ConditionalOnProperty(prefix = "app.logging", name = "enabled", havingValue = "true")
public class LoggingConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
