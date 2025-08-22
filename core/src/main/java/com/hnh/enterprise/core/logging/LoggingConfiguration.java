package com.hnh.enterprise.core.logging;

import org.springframework.context.annotation.Bean;

/**
 * LoggingConfiguration
 */
public class LoggingConfiguration {
    
    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
