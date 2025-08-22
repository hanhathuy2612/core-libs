package com.hnh.enterprise.core.open_api;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({OpenApiConfiguration.class})
public @interface EnableOpenApi {
}
