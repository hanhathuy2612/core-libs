package com.hnh.enterprise.core.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TokenDTO implements Serializable {
    private String token;
    private String refreshToken;
}
