package com.drumhub.common.exception;

/** Thrown when the authenticated user lacks permission for the requested action. Maps to HTTP 403. */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
