package com.hnh.enterprise.core.security;

import lombok.Getter;

/**
 * Constants for Spring Security authorities.
 */
@Getter
public enum AuthoritiesConstants {
    /**
     * Role for users with full administrative privileges.
     */
    ADMIN("admin"),
    
    /**
     * Role for users with administrative privileges.
     */
    USER("user"),
    
    /**
     * Role for users with read-only access.
     */
    SYSTEM("system"),
    
    /**
     * Role for users with anonymous access.
     */
    ANONYMOUS("anonymous");
    
    private final String value;
    
    AuthoritiesConstants(String value) {
        this.value = value;
    }
    
}
