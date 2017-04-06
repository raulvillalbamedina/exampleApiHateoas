package com.rvillalba.exampleApiHateoas.jwt.verifier;

public interface TokenVerifier {
    public boolean verify(String jti);
}
