package com.hnh.enterprise.core.security;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({SecurityConfiguration.class})
public @interface EnableSecurity {
}
