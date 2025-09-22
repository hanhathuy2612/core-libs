package com.hnh.enterprise.core.rest.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class LoginResponse implements Serializable {

    private String token;
    private String refreshToken;
}
