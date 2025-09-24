package com.hnh.enterprise.core.annotations;


import com.hnh.enterprise.core.logging.EnableLogging;
import com.hnh.enterprise.core.open_api.EnableOpenApi;
import com.hnh.enterprise.core.rest.EnableRest;
import com.hnh.enterprise.core.security.EnableSecurity;

import java.lang.annotation.*;

/**
 * Enable common features for the application.
 *
 * @author hnh
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableSecurity
@EnableLogging
@EnableOpenApi
@EnableRest
public @interface EnableCommon {
}
