package com.hnh.enterprise.core.service;

import com.hnh.enterprise.core.rest.request.LoginRequest;
import com.hnh.enterprise.core.rest.request.RegisterRequest;
import com.hnh.enterprise.core.rest.response.LoginResponse;
import com.hnh.enterprise.core.rest.response.RegisterResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);

    RegisterResponse register(RegisterRequest request);
}
