package com.hnh.enterprise.core.security.api_key;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InMemoryApiKeyService implements ApiKeyService {

    private final Map<String, ApiClient> keys = Map.of(
            "demo-key-123", new ApiClient("client-1", "Demo Client", List.of("ROLE_API_USER"))
    );

    @Override
    public Optional<ApiClient> authenticate(String apiKey) {
        return Optional.ofNullable(keys.get(apiKey));
    }
}
