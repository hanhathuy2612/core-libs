package com.hnh.enterprise.core.security.api_key;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private final String headerName;
    private final ApiKeyService apiKeyService; // Ideally, inject this via constructor

    public ApiKeyAuthFilter(String headerName, ApiKeyService apiKeyService) {
        this.headerName = headerName;
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String key = extractKey(request);
            if (key != null && !key.isBlank()) {
                apiKeyService.authenticate(key).ifPresent(client -> {
                    var authorities = client.roles().stream()
                            .map(SimpleGrantedAuthority::new).toList();
                    var auth = new ApiKeyAuthenticationToken(client.id(), authorities);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(auth);
                    SecurityContextHolder.setContext(context);
                });
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractKey(HttpServletRequest req) {
        String key = req.getHeader(headerName); // e.g., X-API-Key
        if (key == null) {
            // Optional: support Authorization: ApiKey <key>
            String auth = req.getHeader("Authorization");
            if (auth != null && auth.startsWith("ApiKey ")) {
                key = auth.substring("ApiKey ".length()).trim();
            }
        }
        if (key == null) {
            key = req.getParameter("apiKey"); // fallback (discouraged)
        }
        return key;
    }
}
