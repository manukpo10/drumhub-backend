package com.drumhub.common.web;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {

    /**
     * Convenience factory for errors without a field-level details list.
     */
    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(Instant.now(), status, error, message, path, null);
    }

    /**
     * Convenience factory that includes a details list (e.g. validation field errors).
     */
    public static ApiError of(int status, String error, String message, String path, List<String> details) {
        return new ApiError(Instant.now(), status, error, message, path, details);
    }
}
