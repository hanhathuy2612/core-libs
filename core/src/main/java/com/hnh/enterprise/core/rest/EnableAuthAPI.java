package com.hnh.enterprise.core.rest;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({AuthAPIConfiguration.class})
public @interface EnableAuthAPI {
}
