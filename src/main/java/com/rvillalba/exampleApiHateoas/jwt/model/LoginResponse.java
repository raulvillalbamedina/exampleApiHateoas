package com.rvillalba.exampleApiHateoas.jwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
}
