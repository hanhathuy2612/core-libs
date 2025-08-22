package com.hnh.enterprise.core.logging;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableLogging
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({LoggingConfiguration.class})
public @interface EnableLogging {
}
