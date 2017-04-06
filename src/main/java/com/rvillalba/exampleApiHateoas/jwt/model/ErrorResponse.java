package com.rvillalba.exampleApiHateoas.jwt.model;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final ErrorCode errorCode;
    private final Date timestamp;
}
