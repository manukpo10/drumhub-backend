package com.drumhub.common.exception;

/** Thrown when the request conflicts with current server state (e.g. duplicate resource). Maps to HTTP 409. */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
