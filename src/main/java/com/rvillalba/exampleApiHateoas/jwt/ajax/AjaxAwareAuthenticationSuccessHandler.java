package com.rvillalba.exampleApiHateoas.jwt.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvillalba.exampleApiHateoas.jwt.model.JwtToken;
import com.rvillalba.exampleApiHateoas.jwt.model.JwtTokenFactory;
import com.rvillalba.exampleApiHateoas.jwt.model.LoginResponse;
import com.rvillalba.exampleApiHateoas.jwt.model.UserResponse;

@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper mapper;
    private final JwtTokenFactory tokenFactory;

    @Autowired
    public AjaxAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
        this.mapper = mapper;
        this.tokenFactory = tokenFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserResponse userResponse = (UserResponse) authentication.getPrincipal();
        JwtToken accessToken = tokenFactory.createAccessJwtToken(userResponse);
        JwtToken refreshToken = tokenFactory.createRefreshToken(userResponse);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(accessToken.getToken());
        loginResponse.setRefreshToken(refreshToken.getToken());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), loginResponse);
        clearAuthenticationAttributes(request);
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
