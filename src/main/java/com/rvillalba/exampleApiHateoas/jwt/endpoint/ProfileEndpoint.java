package com.rvillalba.exampleApiHateoas.jwt.endpoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rvillalba.exampleApiHateoas.jwt.JwtAuthenticationToken;
import com.rvillalba.exampleApiHateoas.jwt.model.UserResponse;

@RestController
public class ProfileEndpoint {
    @RequestMapping(value = "/api/me", method = RequestMethod.GET)
    public @ResponseBody UserResponse get(JwtAuthenticationToken token) {
        return (UserResponse) token.getPrincipal();
    }
}
