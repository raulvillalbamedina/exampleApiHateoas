package com.rvillalba.exampleApiHateoas.jwt.endpoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rvillalba.exampleApiHateoas.configuration.WebSecurityConfig;
import com.rvillalba.exampleApiHateoas.jwt.JwtSettings;
import com.rvillalba.exampleApiHateoas.jwt.exception.InvalidJwtToken;
import com.rvillalba.exampleApiHateoas.jwt.extractor.TokenExtractor;
import com.rvillalba.exampleApiHateoas.jwt.model.JwtToken;
import com.rvillalba.exampleApiHateoas.jwt.model.JwtTokenFactory;
import com.rvillalba.exampleApiHateoas.jwt.model.RawAccessJwtToken;
import com.rvillalba.exampleApiHateoas.jwt.model.RefreshToken;
import com.rvillalba.exampleApiHateoas.jwt.model.UserResponse;
import com.rvillalba.exampleApiHateoas.jwt.verifier.TokenVerifier;

@RestController
public class RefreshTokenEndpoint {
    @Autowired
    private JwtTokenFactory tokenFactory;
    @Autowired
    private JwtSettings jwtSettings;
    @Autowired
    private TokenVerifier tokenVerifier;
    @Autowired
    @Qualifier("jwtHeaderTokenExtractor")
    private TokenExtractor tokenExtractor;

    @RequestMapping(value = "/api/auth/token", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));
        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());
        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }
        String login = refreshToken.getSubject();
        // TODO ADD DATABASE SECURITY, OR LDAP.
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(login);
        return tokenFactory.createAccessJwtToken(userResponse);
    }
}
