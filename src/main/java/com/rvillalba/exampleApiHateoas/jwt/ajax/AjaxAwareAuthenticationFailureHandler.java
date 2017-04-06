package com.rvillalba.exampleApiHateoas.jwt.ajax;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvillalba.exampleApiHateoas.jwt.exception.AuthMethodNotSupportedException;
import com.rvillalba.exampleApiHateoas.jwt.exception.JwtExpiredTokenException;
import com.rvillalba.exampleApiHateoas.jwt.model.ErrorCode;
import com.rvillalba.exampleApiHateoas.jwt.model.ErrorResponse;

@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;

    @Autowired
    public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (e instanceof BadCredentialsException) {
            mapper.writeValue(response.getWriter(),
                    new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password", ErrorCode.AUTHENTICATION, new Date()));
        } else if (e instanceof JwtExpiredTokenException) {
            mapper.writeValue(response.getWriter(),
                    new ErrorResponse(HttpStatus.UNAUTHORIZED, "Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, new Date()));
        } else if (e instanceof AuthMethodNotSupportedException) {
            mapper.writeValue(response.getWriter(), new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), ErrorCode.AUTHENTICATION, new Date()));
        }
        mapper.writeValue(response.getWriter(),
                new ErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed", ErrorCode.AUTHENTICATION, new Date()));
    }
}
