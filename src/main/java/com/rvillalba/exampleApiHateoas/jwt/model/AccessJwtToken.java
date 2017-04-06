package com.rvillalba.exampleApiHateoas.jwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class AccessJwtToken implements JwtToken {
    private final String token;
    @JsonIgnore
    private Claims claims;
}
