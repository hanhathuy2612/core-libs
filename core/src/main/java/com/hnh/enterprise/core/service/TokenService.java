package com.hnh.enterprise.core.service;

import com.hnh.enterprise.core.dto.TokenDTO;

public interface TokenService {
    TokenDTO createToken(String username, String password, Boolean rememberMe);
}
