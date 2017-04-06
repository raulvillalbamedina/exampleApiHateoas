package com.rvillalba.exampleApiHateoas.jwt;

import java.util.List;

import org.apache.coyote.http2.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.rvillalba.exampleApiHateoas.jwt.model.RawAccessJwtToken;
import com.rvillalba.exampleApiHateoas.jwt.model.UserResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        List<Setting> settings = jwsClaims.getBody().get("settings", List.class);
        String subject = jwsClaims.getBody().getSubject();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(subject);
        userResponse.setSettings(settings);
        return new JwtAuthenticationToken(userResponse, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
