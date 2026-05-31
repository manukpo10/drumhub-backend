package com.drumhub.common.exception;

/** Thrown when authentication is required but missing or invalid. Maps to HTTP 401. */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
