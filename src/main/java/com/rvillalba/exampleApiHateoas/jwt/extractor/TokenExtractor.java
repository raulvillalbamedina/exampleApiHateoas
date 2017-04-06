package com.rvillalba.exampleApiHateoas.jwt.extractor;

public interface TokenExtractor {
    public String extract(String payload);
}
