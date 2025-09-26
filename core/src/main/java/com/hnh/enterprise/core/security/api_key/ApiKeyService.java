package com.hnh.enterprise.core.security.api_key;

import java.util.Optional;

public interface ApiKeyService {
    Optional<ApiClient> authenticate(String apiKey);
}
