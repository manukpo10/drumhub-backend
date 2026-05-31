package com.drumhub.common.exception;

/** Thrown when the client sends a malformed or semantically invalid request. Maps to HTTP 400. */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
