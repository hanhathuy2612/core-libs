package com.hnh.enterprise.core.rest.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class LoginRequest implements Serializable {
    private String username;
    private String password;
    private Boolean rememberMe;
}
