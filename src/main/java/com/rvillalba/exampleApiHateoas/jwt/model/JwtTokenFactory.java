package com.rvillalba.exampleApiHateoas.jwt.model;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rvillalba.exampleApiHateoas.jwt.JwtSettings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenFactory {
    private final JwtSettings settings;

    @Autowired
    public JwtTokenFactory(JwtSettings settings) {
        this.settings = settings;
    }

    public AccessJwtToken createAccessJwtToken(UserResponse userResponse) {
        if (StringUtils.isBlank(userResponse.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }
        Claims claims = Jwts.claims().setSubject(userResponse.getUsername());
        claims.put("settings", userResponse.getSettings());
        DateTime currentTime = new DateTime();
        String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer()).setIssuedAt(currentTime.toDate())
                .setExpiration(currentTime.plusMinutes(settings.getTokenExpirationTime()).toDate())
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();
        return new AccessJwtToken(token, null);
    }

    public JwtToken createRefreshToken(UserResponse userResponse) {
        if (StringUtils.isBlank(userResponse.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }
        DateTime currentTime = new DateTime();
        Claims claims = Jwts.claims().setSubject(userResponse.getUsername());
        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
        String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer()).setId(UUID.randomUUID().toString())
                .setIssuedAt(currentTime.toDate()).setExpiration(currentTime.plusMinutes(settings.getRefreshTokenExpTime()).toDate())
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();
        return new AccessJwtToken(token, claims);
    }
}
