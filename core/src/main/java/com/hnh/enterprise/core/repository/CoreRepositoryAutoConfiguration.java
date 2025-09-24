package com.hnh.enterprise.core.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableConfigurationProperties(RepositoryProperties.class)
@ConditionalOnProperty(prefix = "app.repository", name = "enabled", havingValue = "true")
@EntityScan(basePackages = "com.hnh.enterprise.core.entity")
@EnableJpaRepositories(basePackages = "com.hnh.enterprise.core.repository", considerNestedRepositories = true)
public class CoreRepositoryAutoConfiguration {
}
