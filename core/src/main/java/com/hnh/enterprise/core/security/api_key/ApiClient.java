package com.hnh.enterprise.core.security.api_key;

import java.util.Collection;

public record ApiClient(
        String id, String name, Collection<String> roles
) {
}
