package com.gabrielflores.myfortune.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
public class AuthTokenResponse {

    private String accessToken;

    private String refreshToken;

    private long expiresIn;
}
