package com.finance.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 * Results in a 404 Not Found HTTP response.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException with the specified error message.
     *
     * @param message The detail message explaining which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
} 